/*******************************************************************************
 * Copyright 2019 Mountain Fog, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package com.mtnfog.ngramdb.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mtnfog.ngramdb.model.objects.Ngram;
import com.mtnfog.ngramdb.model.services.NgramdbService;
import com.mtnfog.ngramdb.model.similarity.Similarity;
import com.mtnfog.ngramdb.model.similarity.SimilarityAlgorithm;
import com.mtnfog.ngramdb.services.similarity.CosineSimilarity;

import opennlp.tools.ngram.NGramUtils;
import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
 * Implementation of @{link NgramdbService} that uses Apache Ignite to
 * persist and query the n-grams.
 * 
 * @author Mountain Fog, Inc.
 *
 */
@Component
public class IgniteNgramdbService implements NgramdbService {
	
	private static final Logger LOGGER = LogManager.getLogger(IgniteNgramdbService.class);

	private static final String CACHE_NAME = "ngramdb";
	private static final String ID_SEQUENCE_NAME = "ngramdb_id_sequence";
	
	// TODO: Make this a parameter.
	private static final int MAX_VOCABULARY_SIZE = 32000;
	
	@Autowired
    private Ignite ignite;
	
	@Override
	public double calculateSimilarity(SimilarityAlgorithm algorithm, String context1, String context2) throws Exception {
	
		IgniteCache<Long, Ngram> cache = ignite.getOrCreateCache(CACHE_NAME);

		ExecutorService exec = ignite.executorService();

		Map<String, Long> context1Ngrams = new HashMap<String, Long>();
		Map<String, Long> context2Ngrams = new HashMap<String, Long>();

		Future<?> future = exec.submit(new IgniteRunnable() {
			
			private static final long serialVersionUID = 11234L;

			@Override
		    public void run() {

				SqlFieldsQuery query = new SqlFieldsQuery(
		          "select n.ngram, count(n.ngram) as cnt from Ngram n " + 
		            "where n.context = ? " + 
		            "and n.n = " + 1 + " " +
		            "group by n.ngram " + 
		            "order by cnt desc",
		          true
			    );
				
				query.setArgs(context1);
				query.setSchema(CACHE_NAME);
				
				try (QueryCursor<List<?>> cursor = cache.query(query)) {
					
					Iterator<List<?>> i = cursor.iterator();
					
					while(i.hasNext()) {
					
						List<?> l = i.next();
						
						String k = l.get(0).toString();
						long val = Long.valueOf(l.get(1).toString());

						context1Ngrams.put(k, val);
						
					}
					
				}

				query = new SqlFieldsQuery(
		          "select n.ngram, count(n.ngram) as cnt from Ngram n " + 
		            "where n.context = ? " + 
		            "and n.n = " + 1 + " " +
		            "group by n.ngram " + 
		            "order by cnt desc",
		          true
			    );
				
				query.setArgs(context2);
				query.setSchema(CACHE_NAME);
				
				try (QueryCursor<List<?>> cursor = cache.query(query)) {
					
					Iterator<List<?>> i = cursor.iterator();
					
					while(i.hasNext()) {
					
						List<?> l = i.next();
						
						String k = l.get(0).toString();
						long val = Long.valueOf(l.get(1).toString());
										
						context2Ngrams.put(k, val);
						
					}
					
				}
						
		    }
		    
		});
		
		// Wait for completion.
		future.get();
		
		// Combine all of the keys so we know how many there are.
		Collection<String> union = CollectionUtils.union(context1Ngrams.keySet(), context2Ngrams.keySet());
		
		// The vector size is the minimum of either the size of the union or MAX_VOCABULARY_SIZE.
		int vectorSize = Math.min(union.size(), MAX_VOCABULARY_SIZE);
		
		double[] context1Vector = new double[vectorSize];
		Arrays.fill(context1Vector, 0L);
		
		double[] context2Vector = new double[vectorSize];
		Arrays.fill(context2Vector, 0L);
		
		int count = 0;
		
		for(String ngram : union) {
			
			int index;
			
			// Calculate a hash of the ngram that returns a value between 1 and MAX_VOCABULARY_SIZE.
			if(union.size() > MAX_VOCABULARY_SIZE) {
				index = ngram.hashCode();
			} else {
				index = count;
			}
			
			LOGGER.info("{}. {} = {} - {}", index, ngram, context1Ngrams.getOrDefault(ngram, 0L), context2Ngrams.getOrDefault(ngram, 0L));
			
			context1Vector[index] = context1Ngrams.getOrDefault(ngram, 0L);
			context2Vector[index] = context2Ngrams.getOrDefault(ngram, 0L);
			
			count++;
			
		}
		
		LOGGER.trace("Context 1 vector: " + Arrays.toString(context1Vector));
		LOGGER.trace("Context 2 vector: " + Arrays.toString(context2Vector));
		
		Similarity similarity = new CosineSimilarity();
		
		return similarity.compute(context1Vector, context2Vector);
		
	}
	
	@Override
	public int raw(String context, int n, String raw) {
		
		raw = raw.toLowerCase().replaceAll("\\p{Punct}", "");
		
		String[] tokens = WhitespaceTokenizer.INSTANCE.tokenize(raw);
		
		Collection<String[]> ngrams = NGramUtils.getNGrams(tokens, n);
		
		Collection<String> l = new LinkedList<>();
		
		for(String[] ngram : ngrams) {
			
			l.add(StringUtils.join(ngram, " "));
			
		}

		return insert(context, l);
		
	}
	
	@Override
	public long getCount() {
		
		IgniteCache<Long, Ngram> cache = ignite.getOrCreateCache(CACHE_NAME);
		long size = cache.sizeLong(CachePeekMode.ALL);
		
		return size;
		
	}
	
	@Override
	public long getCount(String context) {
		
		IgniteCache<Long, Ngram> cache = ignite.getOrCreateCache(CACHE_NAME);
		
		long count;
		
		if(StringUtils.isEmpty(context)) {
			
			count = cache.sizeLong(CachePeekMode.ALL);
			
		} else {
		
			// TODO: Implement this.
			count = -1;
			
		}
		
		return count;
		
	}

	@Override
	public Map<String, Long> startsWith(String context, String startsWith, int max, int n) {
		
		IgniteCache<Long, Ngram> cache = ignite.getOrCreateCache(CACHE_NAME);
		
		SqlFieldsQuery query = new SqlFieldsQuery(
          "select n.ngram, count(n.ngram) as cnt from Ngram n " + 
            "where n.context = ? " + 
            "and n.ngram like ? " + 
            "and n.n = ? " +
            "group by n.ngram " + 
            "order by cnt desc " + 
            "limit ?",
            false
	    );
      
		query.setArgs(context, startsWith + "%", n, max);
		query.setSchema(CACHE_NAME);
 
		return resultsToMap(cache.query(query).getAll());

	}
	
	@Override
	public Map<String, Long> top(String context, int max, int n) {

		IgniteCache<Long, Ngram> cache = ignite.getOrCreateCache(CACHE_NAME);
		
		SqlFieldsQuery query = new SqlFieldsQuery(
	          "select n.ngram, count(n.ngram) as cnt from Ngram n " + 
	            "where n.context = ? " + 
	        	"and n.n = ? " +
	            "group by n.ngram " + 
	            "order by cnt desc " + 
	            "limit ?",
	            false
	    );

		query.setArgs(context, n, max);
		query.setSchema(CACHE_NAME);

		return resultsToMap(cache.query(query).getAll());
	        
	}

	@Override
	public void deleteNgram(String context, String ngram) {
		
		// TODO: Implement.
		//getCache().remove(formatKey(context, ngram));
		
	}

	@Override
	public int insert(String context, Collection<String> ngrams) {
	
		IgniteCache<Long, Ngram> cache = ignite.getOrCreateCache(CACHE_NAME);
		IgniteAtomicSequence sequence = ignite.atomicSequence(ID_SEQUENCE_NAME, 0, true);
		
		for(String ngram : ngrams) {
			
			Ngram n = new Ngram(sequence.getAndIncrement(), context, ngram);
			
			cache.put(n.getId(), n);
			
			LOGGER.trace("Inserted n-gram: " + n.toString());
			
		}
		
        return ngrams.size();
        
	}
	
    private Map<String, Long> resultsToMap(List<List<?>> r) {
    	
    	Map<String, Long> results = new HashMap<>();
    	
    	if(r != null && !r.isEmpty()) {
    	
            for (Object row : r) {
            	
                if (row instanceof List) {

                    List<?> l = (List<?>)row;
                    
                    String key = l.get(0).toString();
                    long value = Long.valueOf(l.get(1).toString());
                    
                    results.put(key, value);

                }
                
            }
    		
    	}
    	
    	return results;
    	
    }

}
