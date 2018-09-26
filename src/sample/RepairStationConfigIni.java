package sample;

import javafx.scene.control.Alert;
import sample.controllers.GetImageController;
import java.io.*;
import java.util.Properties;

public class RepairStationConfigIni {

    private final String CONFIG_FILE_PATH = System.getProperty("user.dir")+ File.separator+"config"+File.separator+"RepairStationConfig.properties";

    private Properties prob = new Properties();
    private InputStream input = null;
    private OutputStream output = null;
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    private String machineName;

    public RepairStationConfigIni (String machineName){
        this.machineName = machineName;
    }

    public String getIP(){
        String ip = null;
        try {
            input = new FileInputStream(CONFIG_FILE_PATH);

            prob.load(input);

            if(machineName.equals(GetImageController.AOI_DV1_NAME)){
                ip = prob.getProperty("AOI_DV1_IP");
            } else if(machineName.equals(GetImageController.AOI_DV2_NAME)){
                ip = prob.getProperty("AOI_DV2_IP");
            } else if(machineName.equals(GetImageController.AOI_DV3_NAME)){
                ip = prob.getProperty("AOI_DV3_IP");
            } else if(machineName.equals(GetImageController.VITEC_3_NAME)){
                ip = prob.getProperty("AOI_3_REPAIR_IP");
            }
            input.close();

        } catch (IOException e){
            errorAlert.setTitle("An Error Occurred!");
            errorAlert.setHeaderText("Reading RepairStationConfig.properties Error!");
            errorAlert.setContentText(e.toString());
            errorAlert.showAndWait();
        }
        return ip;
    }

    public void setIP(String ip){
        try {
            input = new FileInputStream(CONFIG_FILE_PATH);

            prob.load(input);
            input.close();

            if(machineName.equals(GetImageController.AOI_DV1_NAME)){
                prob.setProperty("AOI_DV1_IP",ip);
            } else if(machineName.equals(GetImageController.AOI_DV2_NAME)){
                prob.setProperty("AOI_DV2_IP",ip);
            } else if(machineName.equals(GetImageController.AOI_DV3_NAME)){
                prob.setProperty("AOI_DV3_IP",ip);
            } else if(machineName.equals(GetImageController.VITEC_3_NAME)){
                prob.setProperty("AOI_3_REPAIR_IP",ip);
            }

            output = new FileOutputStream(CONFIG_FILE_PATH);
            prob.store(output, null);
            output.close();

            infoAlert.setTitle("Completed!");
            infoAlert.setHeaderText("Settings have been saved!");
            infoAlert.showAndWait();

        } catch (Exception e){
            errorAlert.setTitle("An Error Occurred!");
            errorAlert.setHeaderText("Reading RepairStationConfig.properties Error!");
            errorAlert.setContentText(e.toString());
            errorAlert.showAndWait();
        }
    }
}
