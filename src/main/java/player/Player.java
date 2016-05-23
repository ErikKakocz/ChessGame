package player;

public class Player {

	
	private int elo;
	private String name;
	private String password;
	
	
	public Player(String name, String password) {
		super();
		elo=0;
		this.name = name;
		this.password = password;
	}

	public Player(int elo,String name, String password) {
		super();
		this.elo=elo;
		this.name = name;
		this.password = password;
	}

	public int getElo() {
		return elo;
	}


	public void setElo(int elo) {
		this.elo = elo;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	@Override
	public String toString() {
		return "Player [elo=" + elo + ", name=" + name + ", password=" + password + "]";
	}

	public static GameAction gameActionTranslator(String s){
		if("LOGIN".equals(s))
			return GameAction.LOGIN;
		else if("REGISTER".equals(s))
			return GameAction.REGISTER;
		else if("MOVE".equals(s))
			return GameAction.MOVE;
		else if("GIVEUP".equals(s))
			return GameAction.GIVEUP;
		else 
			return GameAction.ENDTURN;
		
	}
	
	
}
