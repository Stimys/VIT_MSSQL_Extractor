package sample;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataBaseConfigInitialization {

    String localpath = System.getProperty("user.dir");
    String filepath = localpath + File.separator +"config"+File.separator+"DbConfig.properties";

    Properties prob = new Properties();
    InputStream input = null;

    String serverConnection = "jdbc:sqlserver://";
    String user, password;

    Alert alert = new Alert(Alert.AlertType.ERROR);

    public String configInitialization (String productionLine){

        try{
            input = new FileInputStream(filepath);

            prob.load(input);

            if(productionLine.equals("5KServer")){
                serverConnection+= prob.getProperty("5KServerIP");
                serverConnection+=";database=";
                serverConnection+= prob.getProperty("5K_DbName");
            }
            else if(productionLine.equals("Siemens")){
                serverConnection+= prob.getProperty("3KServerIP");
                serverConnection+=";database=";
                serverConnection+= prob.getProperty("3K_Siemens");
            }
            else if(productionLine.equals("FujiLong")){
                serverConnection+= prob.getProperty("3KServerIP");
                serverConnection+=";database=";
                serverConnection+= prob.getProperty("3K_FujiLong");
            }

        } catch (IOException e){
            alert.setTitle("An Error Occurred!");
            alert.setHeaderText("Reading DBConfig.properties Error! ServerInitialization Error!");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
        finally{
            try{
                input.close();
            }catch (IOException e){
                alert.setTitle("An Error Occurred!");
                alert.setHeaderText("Closing DBConfig.properties Error!");
                alert.setContentText(e.toString());

                alert.showAndWait();
            }
        }
        return serverConnection;
    }

    public String userInitialization (){

        String localpath = System.getProperty("user.dir");
        String filepath = localpath + File.separator +"config\\DbConfig.properties";

        try{
            input = new FileInputStream(filepath);

            prob.load(input);

            user = prob.getProperty("DbUser");

        } catch (IOException e){
            alert.setTitle("An Error Occurred!");
            alert.setHeaderText("Reading DBConfig.properties Error! User initialization Error!");
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
        return user;
    }

    public String passwordInitialization(){

        try {

            input = new FileInputStream(filepath);

            prob.load(input);

            password = prob.getProperty("DbPsw");

        } catch (IOException e){
            alert.setTitle("An Error Occurred!");
            alert.setHeaderText("Reading DBConfig.properties Error! Password initialization Error!");
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

        return password;
    }

}
