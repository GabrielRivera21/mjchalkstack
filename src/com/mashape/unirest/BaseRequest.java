/*
The MIT License

Copyright (c) 2013 Mashape (http://mashape.com)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.mashape.unirest;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Future;

public abstract class BaseRequest {

	protected static final String UTF_8 = "UTF-8";
	
	protected HttpRequest httpRequest;
	
	protected BaseRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
	
	public HttpRequest getHttpRequest() {
		return this.httpRequest;
	}
	
	protected BaseRequest() {
		super();
	}
	
	public HttpResponse<String> asString() throws UnirestException, UnsupportedEncodingException {
		return HttpClientHelper.request(httpRequest, String.class);
	}

	public Future<HttpResponse<String>> asStringAsync() throws UnsupportedEncodingException {
		return HttpClientHelper.requestAsync(httpRequest, String.class, null);
	}
	
	public Future<HttpResponse<String>> asStringAsync(Callback<String> callback) throws UnsupportedEncodingException {
		return HttpClientHelper.requestAsync(httpRequest, String.class, callback);
	}

	public HttpResponse<JsonNode> asJson() throws UnirestException, UnsupportedEncodingException {
		return HttpClientHelper.request(httpRequest, JsonNode.class);
	}

	public Future<HttpResponse<JsonNode>> asJsonAsync() throws UnsupportedEncodingException {
		return HttpClientHelper.requestAsync(httpRequest, JsonNode.class, null);
	}
	
	public Future<HttpResponse<JsonNode>> asJsonAsync(Callback<JsonNode> callback) throws UnsupportedEncodingException {
		return HttpClientHelper.requestAsync(httpRequest, JsonNode.class, callback);
	}

	public HttpResponse<InputStream> asBinary() throws UnirestException, UnsupportedEncodingException {
		return HttpClientHelper.request(httpRequest, InputStream.class);
	}

	public Future<HttpResponse<InputStream>> asBinaryAsync() throws UnsupportedEncodingException {
		return HttpClientHelper.requestAsync(httpRequest, InputStream.class, null);
	}
	
	public Future<HttpResponse<InputStream>> asBinaryAsync(Callback<InputStream> callback) throws UnsupportedEncodingException {
		return HttpClientHelper.requestAsync(httpRequest, InputStream.class, callback);
	}

}
