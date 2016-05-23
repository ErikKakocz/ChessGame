package login;

import java.io.IOException;

import Controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import menu.MenuController;

public class LoginController {
	
	private MenuController mcont;
	private Stage stage,subStage;
	
	@FXML
	private TextField userNameField;
	
	@FXML
	private TextField passwordField;
	
	@FXML
	public void loginButtonAction(ActionEvent event) throws IOException{
		Controller.connectToServer(userNameField.getText(), passwordField.getText(), "LOGIN");
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("Lobby.fxml"));
		AnchorPane ancpane=(AnchorPane)loader.load();
		Scene lobby=new Scene(ancpane);
		subStage.setScene(lobby);
		subStage.show();
		stage.hide();
	}
	
	@FXML
	public void cancelButtonAction(){
		stage.close();
	}
	
	@FXML
	public void registerButtonAction(ActionEvent event) throws IOException{
		Controller.connectToServer(userNameField.getText(), passwordField.getText(), "REGISTER");
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
