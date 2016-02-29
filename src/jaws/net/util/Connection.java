package jaws.net.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

	private Socket socket;
	
	public Connection(Socket socket) {
		
		this.socket = socket;
	}
	
	public String read() throws IOException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		final String nl = System.lineSeparator();
		
		String input = "";
		{
			String line;
			while ((line = in.readLine()) != null) {
				input += line + nl;
			}
		}
		
		return input.substring(0, Math.max(0, input.length() - nl.length()));
	}
	
	public void write(String message) throws IOException {
		
		PrintWriter out = new PrintWriter(socket.getOutputStream());
	}
}
