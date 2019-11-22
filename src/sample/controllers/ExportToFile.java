package sample.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import sample.CustomAlert;
import sample.DbRequest;
import sample.obj.Data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ExportToFile {
    private String localpath = System.getProperty("user.dir");
    private String fileName;
    private final String exportingFilePath = localpath + File.separator +"Files"+ File.separator;
    private CustomAlert alert;

    public ExportToFile(String filename){
        this.fileName = filename;
    }

    public boolean exportToFile(ObservableList<Data> dataList){
        boolean successful = false;
        try {
            PrintWriter writer = new PrintWriter(exportingFilePath+fileName, "UTF-8");
            writer.println("Product Name \t Topology \t Model \t Part Number \t Error Type \t Machine Name \t Panel Bar Code \t Date \t Operator ID");
            for(int i = 0; i < dataList.size(); i++){
                writer.println(dataList.get(i).getProductName()+"\t"+dataList.get(i).getTopology()+"\t"+dataList.get(i).getModel()+"\t"+dataList.get(i).getPartNumber()+"\t"+dataList.get(i).getErrorType()+"\t"+dataList.get(i).getMachineName()+"\t"+dataList.get(i).getPanelCode()+"\t"+dataList.get(i).getDate()+"\t"+dataList.get(i).getOperatorID());
            }
            writer.close();
            successful = true;
        } catch (IOException e){
            alert = new CustomAlert("An Error Occurred!", "Error while data to file exporting!", e.toString(), Alert.AlertType.ERROR);
        }

        return successful;
    }
}
