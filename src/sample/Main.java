/**
 * MSSQL VIT DataExtractor
 * Created by Petr Pribytkov
 * Date: 02.04.2018
 */

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public final static String VERSION = "4.1.0";
    public final static String LAST_VERSION_DATE = "07.12.2018";
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        /**
         * Creating new main stage
         * Set path to FXML file of the main window
         * Set up title, width, height.
         */

        Parent root = FXMLLoader.load(getClass().getResource("scenes/MainForm.fxml"));
        this.primaryStage = primaryStage;
        primaryStage.setTitle("DataBase Extractor Ver. "+ VERSION);
        primaryStage.setScene(new Scene(root, 1130, 820));
        //primaryStage.setScene(new Scene(root, 335, 165));
        //primaryStage.setMinWidth(1160);
        //primaryStage.setMinHeight(870);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
