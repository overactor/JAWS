package jaws.business.defaultmodule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;

import jaws.business.http.DefaultHTTPRequest;
import jaws.business.http.DefaultHTTPResponse;
import jaws.module.http.HTTPResponse;
import jaws.module.net.Handle;

public class DefaultHandler {
	
	private static final Map<String, String> mimeTypes = new HashMap<>();
	
	static {
		
		// plain text types
		mimeTypes.put("html", "text/html");
		mimeTypes.put("htm", "text/html");
		mimeTypes.put("xml", "text/xml");
		mimeTypes.put("txt", "text/plain");
		mimeTypes.put("css", "text/css");
		mimeTypes.put("js", "text/javascript");
		
		// image types
		mimeTypes.put("png", "image/png");
		mimeTypes.put("jpg", "image/jpeg");
		mimeTypes.put("jpeg", "image/jpeg");
		mimeTypes.put("bmp", "image/bmp");
		mimeTypes.put("bm", "image/bmp");
		
		// video types
		mimeTypes.put("mp4", "video/mp4");
		mimeTypes.put("avi", "video/avi");
	}
	
	@Handle(extensions = {".*"}, priority = Integer.MIN_VALUE)
	public static HTTPResponse handle(DefaultHTTPRequest request, DefaultHTTPResponse response, File webRoot) throws IOException {
		
		try {
			File file = new File(webRoot, request.url().substring(1));
			// if the requested path is a folder, try to get the 'index.html' file
			if(file.isDirectory() && !new File(file, "index.html").exists()) {
				String body = "";
				String[] fileNames = file.list();
				for(String fileName : fileNames) {
					body += "<a href=\"" + request.url() + "/" + fileName + "\">" + fileName + (new File(file, fileName).isDirectory()?"/":"") + "</a><br>";
				}
				
				response.body(body);
			} else {
				if(file.isDirectory() && new File(file, "index.html").exists()) {
					file = new File(file, "index.html");
				}
				//String content = new String(Files.readAllBytes(file.toPath()));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.body(out);
				Files.copy(file.toPath(), out);
				String fileExtension = request.url().substring(request.url().lastIndexOf('.') + 1);
				if(mimeTypes.containsKey(fileExtension)) {
					response.header("Content-Type", mimeTypes.get(fileExtension));
				}
			}
		}  catch(NoSuchFileException e) {
			response.statusCode(404).reason("Not Found").body("<h1>404 - Not Found</h1>");
		}
		
		//System.out.println(response.body());
		
		return response.header("Content-Length", Integer.toString(response.body().length));
	}
}
