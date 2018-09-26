package sample;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    Connection conn = null;
    Alert alert = new Alert(Alert.AlertType.ERROR);
    Alert informationalert = new Alert(Alert.AlertType.INFORMATION);

    public void dbConnect(String db_connect_string,
                          String db_userid,
                          String db_password) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(db_connect_string,
                    db_userid, db_password);

//            informationalert.setTitle("Information");
//            informationalert.setHeaderText(null);
//            informationalert.setContentText("DataBase is connected");
//
//            informationalert.showAndWait();

        } catch (Exception e) {
            alert.setTitle("An Error Occurred!");
            alert.setHeaderText("Error while connecting to the MSSQL database!");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }
}