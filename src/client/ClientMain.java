package client;

public class ClientMain {
	
	public static void main(){
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void init() throws Exception {

		ClientConnectionHandler c = new ClientConnectionHandler();
		c.init();

	}
}
