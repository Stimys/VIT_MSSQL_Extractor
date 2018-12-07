package sample.controllers;

import javafx.scene.control.Alert;
import sample.DbRequest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ExportToFile {
    String localpath = System.getProperty("user.dir");
    private String fileName;
    private final String exportingFilePath = localpath + File.separator +"Files"+ File.separator;
    private Alert erroralert  = new Alert(Alert.AlertType.ERROR);
    private boolean successful;

    public ExportToFile(String filename){
        this.fileName = filename;
    }

    public boolean export(){
        successful = false;
        try {
            PrintWriter writer = new PrintWriter(exportingFilePath+fileName, "UTF-8");
            writer.println("Product Name \t Topology \t Model \t Part Number \t Error Type \t Machine Name \t Panel Bar Code \t Date \t Operator ID");
            for(int i =0; i < DbRequest.getDataList().size(); i++){
                writer.println(DbRequest.getDataList().get(i).getProductName()+"\t"+DbRequest.getDataList().get(i).getTopology()+"\t"+DbRequest.getDataList().get(i).getModel()+"\t"+DbRequest.getDataList().get(i).getPartNumber()+"\t"+DbRequest.getDataList().get(i).getErrorType()+"\t"+DbRequest.getDataList().get(i).getMachineName()+"\t"+DbRequest.getDataList().get(i).getPanelCode()+"\t"+DbRequest.getDataList().get(i).getDate()+"\t"+DbRequest.getDataList().get(i).getOperatorID());
            }
            writer.close();
            successful = true;
        } catch (IOException e){
            erroralert.setTitle("An Error Occurred!");
            erroralert.setHeaderText("Request from DataBase Error!");
            erroralert.setContentText(e.toString());
            erroralert.showAndWait();
        }

        return successful;
    }
}
