package login;

import java.io.IOException;

import Controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import menu.MenuController;

public class LoginController {
	
	private MenuController mcont;
	private Stage stage;
	
	@FXML
	private TextField userNameField;
	
	@FXML
	private TextField passwordField;
	
	@FXML
	public void loginButtonAction(ActionEvent event) throws IOException{
		Controller.connectToServer(userNameField.getText(), passwordField.getText(), "LOGIN");
		stage.close();
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
