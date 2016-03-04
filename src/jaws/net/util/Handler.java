package jaws.net.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import jaws.business.http.HTTPRequest;
import jaws.business.http.HTTPResponse;

import static trycrash.Try.*;

public class Handler {
	
	private Method method;
	
	private Handler(Method method) {
		
		this.method = method;
	}
	
	public static Optional<Handler> from(Method method) {
		
		if(Arrays.equals(method.getParameterTypes(), new Class<?>[] {HTTPRequest.class, HTTPResponse.class, File.class})
				&& method.getReturnType() == HTTPResponse.class) {
			return Optional.of(new Handler(method));
		}
		return Optional.empty();
	}
	
	public HTTPResponse handle(HTTPRequest request, HTTPResponse response, File webroot) {
		
		return tryCrash(() -> (HTTPResponse) method.invoke(null, request, response, webroot));
		
		/*try {
			return (HTTPResponse) method.invoke(null, request, response, webroot);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Error invoking handler Method", e);
		}*/
	}
}