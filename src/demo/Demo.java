package demo;

import client.ClientMain;
import server.ServerMain;

public class Demo {

	public static void main(String[] args) throws Exception {
		ServerMain.init();
		ClientMain.init();
	}

}
