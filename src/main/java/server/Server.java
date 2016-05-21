package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import player.GameAction;
import player.Player;

public class Server {

	static ArrayList<Player> registeredUsers;
	static ArrayList<PlayerConnection> pendingPlayers;
	static ArrayList<PlayerConnection> players;
	static ServerSocket serverSocket;
	static final String filename = "players.json";
	static final Gson gson=new Gson();
	static final Type type=new TypeToken<List<Player>>(){}.getType();
	
	class ConnectionListener implements Runnable{

		public void run() {
			char[] charbuffer=new char[100];
			String uname=null,pass=null;
			GameAction action=null;
			while(true)
			{
				OutputStream outputStream=null;
				InputStream inputStream=null;
				Socket sock;
				InputStreamReader in=null;
				try {
					sock=serverSocket.accept();
					outputStream=sock.getOutputStream();
					inputStream=sock.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(outputStream!=null)
					try {
						outputStream.write(0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if(inputStream!=null)
					try {
						in=new InputStreamReader(inputStream);
						in.read(charbuffer);
						uname=new String(charbuffer);
						in.read(charbuffer);
						pass=new String(charbuffer);
						in.read(charbuffer);
						action=Player.gameActionTranslator(new String(charbuffer));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if(uname!=null&&pass!=null&&action!=null)
					switch(action){
					case LOGIN:{login(uname,pass,in,new OutputStreamWriter(outputStream));break;}
					case REGISTER:{registerUser(uname,pass);break;}
					default:break;
					}
			}
		}
		
		
	}
	
	public Server() throws IOException {
		serverSocket = new ServerSocket(5555,0,InetAddress.getLoopbackAddress());
		players = new ArrayList();
		setupRegisteredUsers();
	}

	private void setupRegisteredUsers() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		File file;
		if ((file = new File(Server.class.getResource(filename).getFile())).exists()) {
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(new FileReader(file)).getAsJsonObject().get("players").getAsJsonArray();
			registeredUsers=gson.fromJson(array, type);

		}

	}
	
	private void registerUser(String name,String password){
		BCrypt bcrypt=new BCrypt();
		registeredUsers.add(new Player(name,bcrypt.hashpw(password, bcrypt.gensalt())));
		
	}
	
	
	private void login(String name,String password,InputStreamReader in,OutputStreamWriter out){
		if(validateLogin(name,password))
			new PlayerConnection(findUser(name),out,in);
	}

	private boolean validateLogin(String name,String password) {
		Player player=findUser(name);
			return validatePassword(password,player.getPassword());
			
		
	}

	private boolean validatePassword(String password,String storedpw) {
		
		return BCrypt.checkpw(password,storedpw);
	}

	private Player findUser(String name) {
		for(Player p:registeredUsers)
			if(p.getName().equals(name))
				return p;
		return null;
	}
	
	private void logout(PlayerConnection p){
		players.remove(p);
	}

	
}
