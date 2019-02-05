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
package com.mtnfog.ngramdb.api.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mtnfog.ngramdb.model.exceptions.BadRequestException;
import com.mtnfog.ngramdb.model.exceptions.ServiceUnavailableException;
import com.mtnfog.ngramdb.model.exceptions.UnauthorizedException;

@ControllerAdvice
public class RestApiExceptions {

	private static final Logger LOGGER = LogManager.getLogger(RestApiExceptions.class);

	@ResponseBody
	@ExceptionHandler({BadRequestException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleBadRequestException(Exception ex) {
		return new String(ex.getMessage());
	}
	
	@ResponseBody
	@ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleMissingParameterException(Exception ex) {
		final String message = "A required parameter is missing or contains an invalid value.";
		LOGGER.error(message, ex);
		return new String(message);
	}
	
	@ResponseBody
	@ExceptionHandler({UnauthorizedException.class})
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public String handleUnauthorizedException(UnauthorizedException ex) {
	    return new String(ex.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(ServiceUnavailableException.class)
	@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
	public String handleServiceUnavailableException(ServiceUnavailableException ex) {
	    return new String(ex.getMessage());
	}
	
	@ResponseBody
	@ExceptionHandler({Exception.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleUnknownException(Exception ex) {
		LOGGER.error("Unknown exception caught.", ex);
	    return new String("An unknown error has occurred.");
	}
	
}
