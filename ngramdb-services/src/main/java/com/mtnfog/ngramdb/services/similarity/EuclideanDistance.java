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
 * Implementation of @{Similarity} that calculates the Euclidean distance.
 * 
 * @author Mountain Fog, Inc.
 *
 */
public class EuclideanDistance implements Similarity {

	@Override
	public double compute(double[] vectorA, double[] vectorB) {
		
		org.apache.commons.math3.ml.distance.EuclideanDistance ed = new org.apache.commons.math3.ml.distance.EuclideanDistance();
		
	    return ed.compute(vectorA, vectorB);
	    
	}
	
}
