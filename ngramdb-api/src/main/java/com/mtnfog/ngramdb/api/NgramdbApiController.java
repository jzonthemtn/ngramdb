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

package com.mtnfog.ngramdb.api;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtnfog.ngramdb.model.api.Status;
import com.mtnfog.ngramdb.model.similarity.SimilarityAlgorithm;
import com.mtnfog.ngramdb.services.IgniteNgramdbService;

@Controller
public class NgramdbApiController {
	
	private static final Logger LOGGER = LogManager.getLogger(NgramdbApiController.class);

	@Autowired
	private IgniteNgramdbService service;
	
	@RequestMapping(value="/api/similarity", method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody double calculateSimilarity(@RequestParam(name="c1") String context1,
			@RequestParam(name="c2") String context2,
			@RequestParam(name="a", defaultValue="cosine") String algorithm) throws Exception {
		
		return service.calculateSimilarity(SimilarityAlgorithm.fromName(algorithm), context1, context2);
		
	}
	
	@RequestMapping(value="/api/count", method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody long count(@RequestParam(name="c", defaultValue="") String context) {
		
		return service.getCount(context);
		
	}
	
	@RequestMapping(value="/api/insert", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody int insert(@RequestParam(name="c") String context,
			@RequestParam(name="n") List<String> ngrams) {
		
		return service.insert(context, ngrams);

	}
	
	@RequestMapping(value="/api/raw", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody int raw(@RequestParam(name="c") String context,
			@RequestParam(name="n", defaultValue="1") int n,
			@RequestBody String raw) {
		
		return service.raw(context, n, raw);
		
	}
	
	@RequestMapping(value="/api/top", method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Long> top(@RequestParam(name="c") String context,
			@RequestParam(name="l", defaultValue="10") int max,
			@RequestParam(name="n", defaultValue="1") int n) {
		
		return service.top(context, max, n);
		
	}
	
	@RequestMapping(value="/api/startswith", method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Long> startsWith(@RequestParam(name="c") String context, 
			@RequestParam(name="sw") String startsWith,
			@RequestParam(name="l", defaultValue="10") int max,
			@RequestParam(name="n", defaultValue="1") int n) {
		
		return service.startsWith(context, startsWith, max, n);

	}
	
	@RequestMapping(value="/api/status", method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody Status status() {
		
		return new Status("Healthy");
		
	}

}
