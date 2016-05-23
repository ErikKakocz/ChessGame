package server;

import java.io.IOException;

public class PlayerActionListener implements Runnable {

	PlayerConnection connection;
	
	
	
	public PlayerActionListener(PlayerConnection connection) {
		super();
		this.connection = connection;
	}



	public void run() {
		while (true){
			try {
				connection.getIn().read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}



	public PlayerConnection getConnection() {
		return connection;
	}

	public void setConnection(PlayerConnection connection) {
		this.connection = connection;
	}
	
	

}
