package jaws.data.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

import jaws.business.thread.Stoppable;
import jaws.context.Context;

import static trycrash.Try.tryCatch;

public class PortListener implements Runnable, Stoppable {

	private ServerSocket server;
	private int port;
	private Consumer<Connection> connectionHandler;

	public PortListener(int port, Consumer<Connection> connectionHandler) {

		this.port = port;
		this.connectionHandler = connectionHandler;
	}

	@Override
	public void run() {

		try {

			server = new ServerSocket(port);
			while (true) {

				Context.logger.debug("waiting to accept a socket", Integer.toString(port));
				Socket socket = server.accept();
				Context.logger.debug("accepted a socket", Integer.toString(port));
				Connection client = new SocketConnection(socket);
				connectionHandler.accept(client);
			}
		} catch (SocketException e) {
			// Expected SocketException when server.close() is called
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {

			if (server != null && !server.isClosed()) {
				stop();
			}
		}
	}

	@Override
	public synchronized void stop() {

		tryCatch(() -> server.close());
	}
}
