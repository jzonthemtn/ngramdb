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

package com.mtnfog.ngramdb.model.services;

import java.util.Collection;
import java.util.Map;

import com.mtnfog.ngramdb.model.similarity.SimilarityAlgorithm;

/**
 * Interface for interacting with the n-gram repository.
 * 
 * @author Mountain Fog, Inc.
 *
 */
public interface NgramdbService {
	
	/**
	 * Gets the count of all stored n-grams.
	 * @return The count of all stored n-grams.
	 */
    long getCount();
    
    /**
     * Gets the count of all stored n-grams for a given context.
     * @param context The context.
     * @return The count of all stored n-grams for a given context.
     */
    long getCount(String context);

    /**
     * Delete an n-gram.
     * @param context The context.
     * @param ngram The n-gram.
     */
    void deleteNgram(String context, String ngram);

    /**
     * Insert n-grams.
     * @param context The n-grams' context.
     * @param ngrams A collection of n-grams.
     * @return The count of n-grams inserted.
     */
    int insert(String context, Collection<String> ngrams);
    
    /**
     * Processes the raw text by extracting n-grams and inserting them.
     * Uses Apache OpenNLP to extract the n-grams by splitting on
     * the occurrences of whitespace.
     * 
     * @param context The context to hold the n-grams.
     * @param n The size (n) of the n-grams to extract.
     * @param raw The text to process.
     * @return The count of n-grams extracted and inserted.
     */
    int raw(String context, int n, String raw);
    
    /**
     * Gets the top n-grams for a context.
     * @param context The context.
     * @param max The maximum number of n-grams to return.
     * @param n The size of the n-grams.
     * @return The most occurring n-grams sorted by size.
     */
    Map<String, Long> top(String context, int max, int n);
	
    /**
     * Gets n-grams starting with a given string.
     * @param context The context.
     * @param startsWith The start of the n-grams.
     * @param max The maximum number of n-grams to return.
     * @param n The size of the n-grams.
     * @return N-grams starting with the given string.
     */
    Map<String, Long> startsWith(String context, String startsWith, int max, int n);
    
    /**
     * Calculate the similarity of two contexts.
     * @param algorithm The similarity {@link SimilarityAlgorithm algorithm}.
     * @param context1 The name of the first context.
     * @param context2 The name of the second context
     * @return The similarity of the two contexts.
     * @throws Exception Thrown if the similarity cannot be calculated.
     */
    double calculateSimilarity(SimilarityAlgorithm algorithm, String context1, String context2) throws Exception;
    
}
