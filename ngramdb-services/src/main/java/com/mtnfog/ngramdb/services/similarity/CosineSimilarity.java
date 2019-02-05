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

package com.mtnfog.ngramdb.services.similarity;

import com.mtnfog.ngramdb.model.similarity.Similarity;

/**
 * Implementation of @{Similarity} that calculates cosine similarity.
 * 
 * @author Mountain Fog, Inc.
 *
 */
public class CosineSimilarity implements Similarity {

	@Override
	public double compute(double[] vectorA, double[] vectorB) {
		
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    
	    for (int i = 0; i < vectorA.length; i++) {
	    	
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	        
	    }
	    
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	    
	}
	
}
