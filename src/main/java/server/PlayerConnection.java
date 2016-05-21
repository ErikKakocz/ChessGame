package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import player.Player;

public class PlayerConnection {

	private Player player;
	private OutputStreamWriter out;
	private InputStreamReader in;
	
	public PlayerConnection(Player player, OutputStreamWriter out, InputStreamReader in) {
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

	public OutputStreamWriter getOut() {
		return out;
	}

	public void setOut(OutputStreamWriter out) {
		this.out = out;
	}

	public InputStreamReader getIn() {
		return in;
	}

	public void setIn(InputStreamReader in) {
		this.in = in;
	}
	

}
