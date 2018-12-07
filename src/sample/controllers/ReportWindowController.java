package sample.controllers;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller class for FileOpenWindow.fxml
 * Not active
 */
public class ReportWindowController {

    private final String localpath = System.getProperty("user.dir");
    Alert erroralert  = new Alert(Alert.AlertType.ERROR);

        @FXML
        private Button btnYes;

        @FXML
        private Button btnNo;

        @FXML
        private Button btnShowFolder;

        @FXML
        void initialize() {

            btnNo.setOnAction(event -> {
                Stage stage = (Stage) btnNo.getScene().getWindow();
                stage.close();
            });

            btnYes.setOnAction(event -> {
                Desktop desktop = Desktop.getDesktop();
                String filepath = localpath + File.separator +"Files"+File.separator+ Controller.fileName;
                File aoiReportFile = new File(filepath);

                try {
                    desktop.open(aoiReportFile);
                }
                catch (IOException e){
                    erroralert.setTitle("An Error Occurred!");
                    erroralert.setHeaderText("File AOI_Report.xls is not exist!");
                    erroralert.setContentText(e.toString());
                    erroralert.showAndWait();
                }

                Stage stage = (Stage) btnYes.getScene().getWindow();
                stage.close();
            });

            btnShowFolder.setOnAction(event -> {
                Desktop desktop = Desktop.getDesktop();
                String filepath = localpath + File.separator +"Files";
                File aoiReportFile = new File(filepath);

                try {
                    desktop.open(aoiReportFile);
                }
                catch (IOException e){
                    erroralert.setTitle("An Error Occurred!");
                    erroralert.setHeaderText("Folder 'Files' is not exist!");
                    erroralert.setContentText(e.toString());
                    erroralert.showAndWait();
                }
                Stage stage = (Stage) btnShowFolder.getScene().getWindow();
                stage.close();
            });
        }
}
