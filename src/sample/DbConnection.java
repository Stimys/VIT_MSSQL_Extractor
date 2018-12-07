package sample;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Class to make connection between client and DB
 */
public class DbConnection {

    private Connection conn = null;
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public void makeConnection(String db_connect_string,
                          String db_userid,
                          String db_password) {
        /**
         * Check if the DB connection is successful
         */
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(db_connect_string,
                    db_userid, db_password);

        } catch (Exception e) {
            alert.setTitle("An Error Occurred!");
            alert.setHeaderText("Error while connecting to the MSSQL database!");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    public Connection getConnection(){
        return conn;
    }
}