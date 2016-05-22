package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
			String uname=null,pass=null;
			GameAction action=null;
			while(true)
			{
				DataOutputStream outputStream=null;
				DataInputStream inputStream=null;
				Socket sock;
				
				try {
					sock=serverSocket.accept();
					System.out.println(sock==null);
					outputStream=new DataOutputStream(sock.getOutputStream());
					inputStream=new DataInputStream(sock.getInputStream());
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
				System.out.println("anyád");
				if(inputStream!=null)
					try {
						
						
						System.out.println("anyád");
						uname=inputStream.readUTF();
						System.out.println(uname);
						pass=inputStream.readUTF();
						System.out.println(pass);
						action=Player.gameActionTranslator(inputStream.readUTF());
						System.out.println(action);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				System.out.println("anyád");
				if(uname!=null&&pass!=null&&action!=null)
					switch(action){
					case LOGIN:{login(uname,pass,inputStream,new DataOutputStream(outputStream));System.out.println("loginsucces");break;}
					case REGISTER:{registerUser(uname,pass);System.out.println("regsucces");break;}
					default:break;
					}
				try {
					outputStream.write(1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	public Server() throws IOException {
		serverSocket = new ServerSocket(5555,0,InetAddress.getLoopbackAddress());
		setupRegisteredUsers();
		ConnectionListener connectionListener=new ConnectionListener();
		connectionListener.run();
	}

	private void setupRegisteredUsers() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		File file;
		file = new File(getClass().getClassLoader().getResource(filename).getPath());
		System.out.println("file status: "+file==null);
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(new FileReader(file)).getAsJsonObject().get("players").getAsJsonArray();
		registeredUsers=gson.fromJson(array, type);
		System.out.println("REGUSERS:"+registeredUsers==null);

	}
	
	private void registerUser(String name,String password){
		BCrypt bcrypt=new BCrypt();
		registeredUsers.add(new Player(name,bcrypt.hashpw(password, bcrypt.gensalt())));
		
	}
	
	
	private void login(String name,String password,DataInputStream in,DataOutputStream out){
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
