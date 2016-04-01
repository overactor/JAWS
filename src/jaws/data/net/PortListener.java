package jaws.data.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import jaws.business.net.RequestProcessor;
import jaws.business.thread.ThreadPool;
import jaws.data.module.ModuleLoader;

public class PortListener {
	
	ThreadPool threadPool;

	public PortListener(int port) {
		
		threadPool = new ThreadPool(5);
		ServerSocket server = null;
		
		try {
			server = new ServerSocket(port);
			while (true) {
				
				Socket socket = server.accept();
				SocketConnection client = new SocketConnection(socket);
				final RequestProcessor handler = new RequestProcessor(ModuleLoader.getHandlerGetter());
				threadPool.execute(() -> handler.handle(client));
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} finally {
			
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void stop() {
		
		threadPool.stop();
	}
}
