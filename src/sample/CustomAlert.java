package sample;

import javafx.scene.control.Alert;

public class CustomAlert {
    private String title;
    private String headerText;
    private String contentText;
    private Alert.AlertType alertType;


    public CustomAlert (String title, String headerText, String contentText, Alert.AlertType type){
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
        this.alertType = type;

        showAlert();;
    }

    private void showAlert (){
        Alert newAlert = new Alert(alertType);
        newAlert.setTitle(this.title);
        newAlert.setHeaderText(this.headerText);
        newAlert.setContentText(this.contentText);
        newAlert.showAndWait();
    }
}
