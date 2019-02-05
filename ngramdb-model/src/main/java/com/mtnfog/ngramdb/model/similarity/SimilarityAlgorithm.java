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

package com.mtnfog.ngramdb.model.similarity;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Similarity algorithms.
 * 
 * @author Mountain Fog, Inc.
 *
 */
public enum SimilarityAlgorithm {

	COSINE("cosine"),
	EUCLIDEAN("euclidean");
	
	private static final Logger LOGGER = LogManager.getLogger(SimilarityAlgorithm.class);
	
	private String name;
	
	private SimilarityAlgorithm(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static SimilarityAlgorithm fromName(String name) {
		
		if(StringUtils.equalsIgnoreCase(name, "cosine")) {
			
			return COSINE;
			
		} else if(StringUtils.equalsIgnoreCase(name, "euclidean")) {
			
			return EUCLIDEAN;
			
		} else {
			
			LOGGER.warn("Invalid similarity algorithm name '{}'. Defaulting to cosine similarity.", name);
			return COSINE;
			
		}
		
	}
	
}
