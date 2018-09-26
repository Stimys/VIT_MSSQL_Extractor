package sample.controllers;

import java.io.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Properties;

public class ConfigSetupController {

    Properties prob = new Properties();
    InputStream input = null;
    OutputStream output = null;
    Alert alert = new Alert(Alert.AlertType.ERROR);
    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    private TextField tf5kdatabaseIP;

    @FXML
    private TextField tf3kdatabaseIP;

    @FXML
    private TextField tf5kdatabasename;

    @FXML
    private TextField tfSiemensdatabaseName;

    @FXML
    private TextField fujiLongdatabaseName;

    @FXML
    private TextField tfUser;

    @FXML
    private TextField tfPassword;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnClose;

    @FXML
    void initialize() {
        try{
            String localpath = System.getProperty("user.dir");
            String filepath = localpath + File.separator +"config\\DbConfig.properties";
            input = new FileInputStream(filepath);

            prob.load(input);

            tf5kdatabaseIP.setText(prob.getProperty("5KServerIP"));
            tf3kdatabaseIP.setText(prob.getProperty("3KServerIP"));
            tf5kdatabasename.setText(prob.getProperty("5K_DbName"));
            tfSiemensdatabaseName.setText(prob.getProperty("3K_Siemens"));
            fujiLongdatabaseName.setText(prob.getProperty("3K_FujiLong"));
            tfUser.setText(prob.getProperty("DbUser"));
            tfPassword.setText(prob.getProperty("DbPsw"));

        } catch (IOException e){
            alert.setTitle("An Error Occurred!");
            alert.setHeaderText("Reading DBConfig.properties Error! Window initialization Error!");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
        finally{
            try{
                input.close();
            }catch (IOException e){
                alert.setTitle("An Error Occurred!");
                alert.setHeaderText("Close DBConfig.properties Error!");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
        }

        btnEdit.setOnAction(event -> {
            btnEdit.setDisable(true);
            btnSave.setDisable(false);

            tf5kdatabaseIP.setEditable(true);
            tf3kdatabaseIP.setEditable(true);
            tf5kdatabasename.setEditable(true);
            tfSiemensdatabaseName.setEditable(true);
            fujiLongdatabaseName.setEditable(true);
            tfUser.setEditable(true);
            tfPassword.setEditable(true);

            tf5kdatabaseIP.setStyle("-fx-control-inner-background: #FFFFFF");
            tf3kdatabaseIP.setStyle("-fx-control-inner-background: #FFFFFF");
            tf5kdatabasename.setStyle("-fx-control-inner-background: #FFFFFF");
            tfSiemensdatabaseName.setStyle("-fx-control-inner-background: #FFFFFF");
            fujiLongdatabaseName.setStyle("-fx-control-inner-background: #FFFFFF");
            tfUser.setStyle("-fx-control-inner-background: #FFFFFF");
            tfPassword.setStyle("-fx-control-inner-background: #FFFFFF");
        });

        btnSave.setOnAction(event -> {
            btnEdit.setDisable(false);
            btnSave.setDisable(true);

            tf5kdatabaseIP.setEditable(false);
            tf3kdatabaseIP.setEditable(false);
            tf5kdatabasename.setEditable(false);
            tfSiemensdatabaseName.setEditable(false);
            fujiLongdatabaseName.setEditable(false);
            tfUser.setEditable(false);
            tfPassword.setEditable(false);

            tf5kdatabaseIP.setStyle("-fx-control-inner-background:  #D3D3D3");
            tf3kdatabaseIP.setStyle("-fx-control-inner-background: #D3D3D3");
            tf5kdatabasename.setStyle("-fx-control-inner-background: #D3D3D3");
            tfSiemensdatabaseName.setStyle("-fx-control-inner-background: #D3D3D3");
            fujiLongdatabaseName.setStyle("-fx-control-inner-background: #D3D3D3");
            tfUser.setStyle("-fx-control-inner-background: #D3D3D3");
            tfPassword.setStyle("-fx-control-inner-background: #D3D3D3");

            try{
                String localpath = System.getProperty("user.dir");
                String filepath = localpath + File.separator +"config\\DbConfig.properties";
                output = new FileOutputStream(filepath);

                prob.setProperty("5KServerIP", tf5kdatabaseIP.getText());
                prob.setProperty("3KServerIP", tf3kdatabaseIP.getText());
                prob.setProperty("5K_DbName", tf5kdatabasename.getText());
                prob.setProperty("3K_Siemens", tfSiemensdatabaseName.getText());
                prob.setProperty("3K_FujiLong", fujiLongdatabaseName.getText());
                prob.setProperty("DbUser", tfUser.getText());
                prob.setProperty("DbPsw", tfPassword.getText());

                prob.store(output , null);


                informationAlert.setTitle("Information");
                informationAlert.setHeaderText("Configuration file has been saved");
                informationAlert.setContentText(null);
                informationAlert.showAndWait();

            } catch (IOException e){
                alert.setTitle("An Error Occurred!");
                alert.setHeaderText("Reading DBConfig.properties Error! Window initialization Error!");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
            finally {
                try {
                    output.close();
                } catch (IOException e) {
                    alert.setTitle("An Error Occurred!");
                    alert.setHeaderText("Close DBConfig.properties Error!");
                    alert.setContentText(e.toString());
                    alert.showAndWait();
                }
            }
        });

        btnClose.setOnAction(event -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }
}
