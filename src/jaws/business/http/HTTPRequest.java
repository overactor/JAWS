package jaws.business.http;

import java.io.BufferedReader;
import java.io.IOException;

import jaws.business.http.util.RequestMethod;

/**
 * A class to represent an HTTP request to the webserver.
 * 
 * @author Rik
 * 
 * @see jaws.business.http.HTTPResponse
 */
public class HTTPRequest extends HTTPObject<HTTPRequest> {

	private RequestMethod requestMethod;
	private String url;
	private String httpVersion;
	
	@Override
	protected String getFirstLine() {
		return requestMethod + " " + url + " " + httpVersion;
	}
	
	/**
	 * Parses a String into an HTTPRequest.
	 * 
	 * @param reader the String to parse.
	 * @return the parsed HTTPRequest.
	 * @throws IOException 
	 */
	public static HTTPRequest parse(BufferedReader reader) throws IOException {
		
		HTTPRequest request = new HTTPRequest();

		String[] firstLineItems = reader.readLine().split(" ");

		request.requestMethod = RequestMethod.valueOf(firstLineItems[0]);
		request.url = firstLineItems[1];
		request.httpVersion = firstLineItems[2];
		
		request = HTTPObject.parseHeadersAndBody(request, reader);

		return request;
	}
	
	/**
	 * Gets the method of the HTTP Request.
	 * 
	 * @return the method of the HTTP Request as an Enum.
	 */
	public RequestMethod method() {
		
		return requestMethod;
	}
	
	/**
	 * Sets the method of the HTTP Request.
	 * 
	 * @param method the method of the HTTP Request as an Enum.
	 * @return the HTTPRequest for method chaining.
	 */
	public HTTPRequest method(RequestMethod method) {
		
		requestMethod = method;
		return this;
	}
	
	/**
	 * Gets the url of the HTTP Request.
	 * 
	 * @return the url as a String.
	 */
	public String url() {
		
		return url;
	}
	
	/**
	 * Sets the url of the HTTP Request.
	 * 
	 * @param url the url as a String.
	 * @return the HTTPRequest for method chaining.
	 */
	public HTTPRequest url(String url) {
		
		this.url = url;
		return this;
	}
	
	/**
	 * Gets the HTTP version of the HTTP Request.
	 * 
	 * @return the HTTP version as a String.
	 */
	public String httpVersion() {
		
		return httpVersion;
	}
	
	/**
	 * Sets the HTTP version of the HTTP Request.
	 * 
	 * @param httpVersion the HTTP version as a String.
	 * @return the HTTPRequest for method chaining.
	 */
	public HTTPRequest httpVersion(String httpVersion) {
		
		this.httpVersion = httpVersion;
		return this;
	}
}
