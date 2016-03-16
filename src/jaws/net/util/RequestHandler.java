package jaws.net.util;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import jaws.business.http.HTTPRequest;
import jaws.business.http.HTTPResponse;

public class RequestHandler {
	
	private Function<String, Optional<Handler>> handlerGetter;
	
	public RequestHandler(Function<String, Optional<Handler>> handlerGetter) {
		
		this.handlerGetter = handlerGetter;
	}

	public void handle(Connection client) {
		
		try {
			HTTPRequest request = HTTPRequest.parse(client.read());
			HTTPResponse response = new HTTPResponse().httpVersion("HTTP/1.1").statusCode(200).reason("OK")
			                                          .header("Content-Type", "text/html");
			Handler handler = handlerGetter.apply(request.url().substring(request.url().lastIndexOf('.') + 1)).get();
			
			response = handler.handle(request, response, new File("D:\\Projects\\www"));
			//client.write(response.getBytes());
			client.write(response.getOutputStream());
		} catch (IOException | NoSuchElementException e) {
			
			String body = "<h1>500 - Internal Server Error</h1>";
			HTTPResponse response = new HTTPResponse().httpVersion("HTTP/1.1").statusCode(500).reason("Internal Server Error")
			                                          .header("Content-Type", "text/html")
			                                          .header("Content-Length", Integer.toString(body.length()))
			                                          .body(body);
			
			for(int i=0; i<3; i++) {
				
				try {
					client.write(response.getBytes());
					break;
				} catch (IOException e1) {
					continue;
				}
			}
		}
	}
}
