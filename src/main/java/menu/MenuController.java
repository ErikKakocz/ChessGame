package menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import login.LoginController;


public class MenuController extends Application {

	
	private static Stage stage;
	private Stage subStage;	
	int i=0;

	public MenuController() {
		super();
		
	}

	public void setStage(Stage s){
		this.stage=s;
		
	}

	public void createStage(Stage s) throws Exception{
		setStage(s);
		FXMLLoader loader=new FXMLLoader();
		System.out.println("setloc: "+(getClass().getResource("Menu.fxml")==null));
		loader.setLocation(getClass().getResource("Menu.fxml"));
		stage.setTitle("Chess");
		AnchorPane ancpane=(AnchorPane)loader.load();
		Scene scene=new Scene(ancpane);
		
		stage.setScene(scene);
		stage.show();
		
		
	}
	
	@Override
	public void start(Stage arg0) throws Exception {
		
		createStage(arg0);
		
		//loader.setController(this);
		
		
	}
	
	@FXML
	public void button1() throws IOException{
		subStage=new Stage();
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("/login/login.fxml"));
		AnchorPane ancpane=(AnchorPane)loader.load();
		LoginController ct=(LoginController)loader.getController();
		ct.setStage(subStage);
		Scene scene=new Scene(ancpane);
		subStage.setScene(scene);
		subStage.show();
	}
	
	@FXML
	public void button2(){
		closeWindow();
		
	}
	
	public void closeWindow(){
		stage.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
	
}
