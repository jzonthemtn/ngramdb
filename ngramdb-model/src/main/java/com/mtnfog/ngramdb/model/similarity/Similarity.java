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

/**
 * Interface for similarity measures.
 * 
 * @author Mountain Fog, Inc.
 *
 */
public interface Similarity {

	/**
	 * Calculate the similarity of two vectors.
	 * @param vectorA The first vector.
	 * @param vectorB The second vector.
	 * @return A value representing the similarity of the two vectors.
	 */
	double compute(double[] vectorA, double[] vectorB);
	
}
