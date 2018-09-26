package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public final static String VERSION = "3.6.2";
    public final static String LAST_VERSION_DATE = "07.08.2018";
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("scenes/sample.fxml"));
        this.primaryStage = primaryStage;
        primaryStage.setTitle("DataBase Extractor Ver. "+ VERSION);
        primaryStage.setScene(new Scene(root, 1125, 805));
        //primaryStage.setScene(new Scene(root, 335, 165));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
