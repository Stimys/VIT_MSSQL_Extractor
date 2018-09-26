package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;

public class LogInController{

    private Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private final String PASSWORD = "MS128";

    @FXML
    private Button btnLogIn;

    @FXML
    private Button btnInfo;

    @FXML
    private PasswordField tfPassword;

    @FXML
    void initialize() {

        btnInfo.setOnAction(event -> {
            informationAlert.setTitle("About");
            informationAlert.setHeaderText(null);
            informationAlert.setContentText("Extractor for VITechnology MSSQL DataBase based on JavaFX technology\n"+
                    "Created by Pribytkov Petr\n" +
                    "Creation date: 02.04.2018\n" +
                    "Email: Petr.Pribytkov@eolane.com\n" +
                    "Version: "+ Main.VERSION);
            informationAlert.showAndWait();
        });

        btnLogIn.setOnAction(event -> {
            if(tfPassword.getText().equals("") || tfPassword.getText()==null || tfPassword.getText().isEmpty()){
                errorAlert.setTitle("Password field is empty!");
                errorAlert.setHeaderText("Please enter the password!");
                errorAlert.setContentText(null);
                errorAlert.showAndWait();
            }
            else if(tfPassword.getText().equals(PASSWORD)){

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sample/scenes/sample.fxml"));

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                    errorAlert.setTitle("An Error Occurred!");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText(e.toString());
                    errorAlert.showAndWait();
                }

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("DataBase Extractor Ver. "+ Main.VERSION);
                stage.setResizable(false);
                stage.showAndWait();
            }
            else{
                errorAlert.setTitle("Wrong password!");
                errorAlert.setHeaderText("Please check the password. Its wrong!");
                errorAlert.setContentText(null);
                errorAlert.showAndWait();
            }
        });

    }
}

