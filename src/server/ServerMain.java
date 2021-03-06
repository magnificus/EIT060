package server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.security.*;

public class ServerMain {
	
	
	public static void main(String[] args){
		init();
	}

	public static void init() {
		printOutInterestingInfo();
		ServerConnectionHandler.init(null);

	}

	public static void printOutInterestingInfo() {
		try {
			Class.forName("com.sun.net.ssl.internal.ssl.Provider");
		} catch (Exception e) {
			System.out.println("JSSE is NOT installed correctly!");
			System.exit(1);
		}
		System.out.println("JSSE is installed correctly!");
	}

}
