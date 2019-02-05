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

package com.mtnfog.ngramdb.model.objects;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 * An n-gram.
 * 
 * @author Mountain Fog, Inc.
 *
 */
public class Ngram implements Serializable {

	private static final long serialVersionUID = 4918243654387434112L;

	private Long id;
	private String context;
	private String ngram;
	private int n;

	/**
	 * Creates a new n-gram.
	 * @param context The context of the n-gram.
	 * @param ngram The n-gram.
	 */
	public Ngram(long id, String context, String ngram) {
		
		this.id = id;
		this.context = context;
		this.ngram = ngram.toLowerCase();
		this.n = StringUtils.countMatches(ngram, " ") + 1;
		
	}
	
	@Override
	public String toString() {
		return "Context: " + context + "; n-gram: " + ngram + "; n: " + n;
	}
		
	public String getContext() {
		return context;
	}
	
	public void setContext(String context) {
		this.context = context;
	}

	public String getNgram() {
		return ngram;
	}

	public void setNgram(String ngram) {
		this.ngram = ngram;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	
}
