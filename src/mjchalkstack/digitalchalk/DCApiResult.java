/**
 * Copyright (C) 2012 Infinity Learning Solutions.  All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, is NOT permitted unless the following conditions are
 * met:
 * 1. The redistribution of any kind or in any form must be approved
 *    in writing from an official of Infinity Learning Solutions and a third
 *    party witness.
 * 2. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.  The
 *    redistribution must also be approved in writing from an official
 *    of Infinity Learning Solutions and a third party witness.
 * 3. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ''AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package mjchalkstack.digitalchalk;

import java.util.*;

public class DCApiResult {
	
	private int statusCode;
	
	private String statusLine;
	
	@SuppressWarnings("rawtypes")
	private List<HashMap> results;

	private String error;
	
	private String errorDescription;
	
	private String rawJson;
	
	@SuppressWarnings("rawtypes")
	private LinkedHashMap fieldErrors;
	
	private String locationId;
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getRawJson() {
		return rawJson;
	}

	public void setRawJson(String rawJson) {
		this.rawJson = rawJson;
	}

	@SuppressWarnings("rawtypes")
	public List<HashMap> getResults() {
		return results;
	}

	@SuppressWarnings("rawtypes")
	public void setResults(List<HashMap> results) {
		this.results = results;
	}

	@SuppressWarnings("rawtypes")
	public LinkedHashMap getFieldErrors() {
		return fieldErrors;
	}

	@SuppressWarnings("rawtypes")
	public void setFieldErrors(LinkedHashMap fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public String getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	

}
