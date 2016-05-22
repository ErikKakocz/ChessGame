package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import player.Player;

public class PlayerConnection {

	private Player player;
	private DataOutputStream out;
	private DataInputStream in;
	
	public PlayerConnection(Player player, DataOutputStream out, DataInputStream in) {
		super();
		this.player = player;
		this.out = out;
		this.in = in;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public DataInputStream getIn() {
		return in;
	}

	public void setIn(DataInputStream in) {
		this.in = in;
	}



}
