package login;

import java.io.IOException;

import Controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import menu.MenuController;

public class LoginController {

	
	
	private TextField userName;
	private TextField password;
	private MenuController mcont;
	private Stage stage;
	
	@FXML
	TextField userNameField;
	
	@FXML
	TextField passwordField;
	
	@FXML
	public void loginButtonAction() throws IOException{
		Controller.connectToServer(userNameField.getText(), passwordField.getText(), "LOGIN");
		stage.close();
	}
	
	@FXML
	public void cancelButtonAction(){
		stage.close();
	}
	
	@FXML
	public void registerButtonAction() throws IOException{
		Controller.connectToServer(userNameField.getText(), passwordField.getText(), "REGISTER");
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	
}
