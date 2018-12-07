package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import sample.controllers.Controller;
import sample.obj.Data;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Class which makes request to the DB
 */

public class DbRequest {

    private int arrayCounter;
    private Statement statement = null;
    private ResultSet rs = null;
    private Alert erroralert  = new Alert(Alert.AlertType.ERROR);

    private Set<String> productNameList;
    private Set<String> topologyList;
    private Set<String> modelList;
    private Set<String> partNumberList;
    private Set<String> errorTypeList;
    private Set<String> machineList;

    public static int panels;
    public static long falseAlarmValue;
    public static long componentsValue;
    public static long errors;

    public static ArrayList<String> topFalseAlarmModels = new ArrayList<>();
    public static ArrayList<Integer> topFalseAlarmValue = new ArrayList<>();
    private static ObservableList<Data> datalist = FXCollections.observableArrayList();

    public void makeRequest (long startTime, long endTime, DbConnection connection){


        productNameList = new LinkedHashSet<>();
        topologyList = new LinkedHashSet<>();
        modelList = new LinkedHashSet<>();
        partNumberList = new LinkedHashSet<>();
        errorTypeList = new LinkedHashSet<>();
        machineList = new LinkedHashSet<>();

        boolean arrayFirstTime = true;
        boolean modelfound = false;
        arrayCounter= 0;
        panels = 0;
        errors = 0;
        falseAlarmValue = 0;
        componentsValue = 0;

        if (!Controller.firstLunch){
            datalist.clear();
            topFalseAlarmModels.clear();
            topFalseAlarmValue.clear();
        }

        try{
            statement = connection.getConnection().createStatement();

            rs = statement.executeQuery("SELECT Panel_Bar_Code, Panel_Numeric_Date, Nb_Of_Tested_Object FROM PANELS " +
                    "WHERE PANELS.Panel_Numeric_Date between "+startTime+" and "+ endTime);

            while (rs.next()){
                componentsValue += rs.getLong(3);
                panels++;
            }

            rs = statement.executeQuery("SELECT Model, Repair_Button_Comment, Repair_Numeric_Date_Hour FROM TESTED_OBJECT " +
                    "WHERE Repair_Numeric_Date_Hour between "+startTime+" and "+ endTime);

            /**
             * Fill arrays topFalseAlarmModels and topFalseAlarmValues
             * Counting values of false alarms and all errors
             */

            while(rs.next() ) {
                errors++;
                if (rs.getString(2).equals("FALSE ALARM") || rs.getString(2).equals("False Alarm")) {
                    falseAlarmValue++;
                    if (arrayFirstTime) {
                        topFalseAlarmModels.add(0, rs.getString(1));
                        topFalseAlarmValue.add(0, 1);
                        arrayFirstTime = false;
                    } else {

                        for (int i = 0; i < topFalseAlarmModels.size(); i++) {
                            if (rs.getString(1).equals(topFalseAlarmModels.get(i))) {
                                arrayCounter = topFalseAlarmValue.get(i);
                                arrayCounter++;
                                topFalseAlarmValue.set(i, arrayCounter);
                                modelfound = true;
                                i = topFalseAlarmModels.size();
                            }
                        }
                        if (modelfound == false) {
                            topFalseAlarmModels.add(rs.getString(1));
                            topFalseAlarmValue.add(1);
                        } else {
                            modelfound = false;
                        }
                    }
                }
            }

            /**
             * MSSQL request
             */
            rs = statement.executeQuery("SELECT PRODUCT.Product_Name, " +
                    "TESTED_OBJECT.Topology, " +
                    "TESTED_OBJECT.Model, " +
                    "PART_NUMBER.Part_Number, " +
                    "TESTED_OBJECT.Repair_Button_Comment, " +
                    "MACHINE.Machine_Name, " +
                    "PANELS.Panel_Bar_Code, " +
                    "TESTED_OBJECT.Repair_Numeric_Date_Hour, " +
                    "OPERATOR.Operator_Name "+
                    "FROM ((((((TESTED_OBJECT " +
                    "INNER JOIN CARDS ON TESTED_OBJECT.Card_ID = CARDS.Card_ID) " +
                    "INNER JOIN PART_NUMBER ON TESTED_OBJECT.Part_Number_ID = PART_NUMBER.Part_Number_ID) " +
                    "INNER JOIN PANELS ON CARDS.Panel_ID = PANELS.Panel_ID) " +
                    "INNER JOIN MACHINE ON PANELS.Machine_ID = MACHINE.Machine_ID) " +
                    "INNER JOIN PRODUCT ON PANELS.Product_ID = PRODUCT.Product_ID) " +
                    "INNER JOIN OPERATOR ON TESTED_OBJECT.Operator_ID = OPERATOR.Operator_ID)"+
                    "WHERE TESTED_OBJECT.Repair_Numeric_Date_Hour between "+startTime+" and "+ endTime);

//            /**
//             * If checkbox 'Save report' is checked, so save all data to the xml file
//             */
//            if (saveReport) {
//                String localpath = System.getProperty("user.dir");
//                String filepath = localpath + File.separator +"Files"+ File.separator + Controller.fileName;
//                PrintWriter writer = new PrintWriter(filepath, "UTF-8");
//
//                writer.println("Product Name \t Topology \t Model \t Part Number \t Error Type \t Machine Name \t Panel Bar Code \t Date \t Operator ID");
//
//                while (rs.next()) {
//                    Long timestamp = rs.getLong(8);
//                    java.util.Date time = new java.util.Date(timestamp * 1000);
//
//                    writer.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6) + "\t" + rs.getString(7) + "\t" + time + "\t" + rs.getString(9));
//                    setData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7), time.toString(),rs.getString(9));
//                    fillingFilterLists(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
//                }
//
//                    writer.close();
//                }
//                else{
                    while (rs.next()) {
                        Long timestamp = rs.getLong(8);
                        java.util.Date time = new java.util.Date(timestamp * 1000);

                        setData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7), time.toString(),rs.getString(9));
                        fillingFilterLists(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
                    }

                //fillingDataFilterList(productNameList,topologyList,modelList,partNumberList,errorTypeList,machineList);
            Thread.sleep(100);
        } catch (Exception e){
            erroralert.setTitle("An Error Occurred!");
            erroralert.setHeaderText("Request from DataBase Error!");
            erroralert.setContentText(e.toString());
            erroralert.showAndWait();
        }
    }

    private void setData(String productName, String topology, String model, String partNumber, String errorType, String machineName, String panelCode, String date, String operator){

        datalist.add(new Data(productName,topology ,model, partNumber ,errorType, machineName, panelCode, date, operator));
    }

    private void fillingFilterLists(String productName, String topology, String model, String partNumber, String errorType, String machineName){
        productNameList.add(productName);
        topologyList.add(topology);
        modelList.add(model);
        partNumberList.add(partNumber);
        errorTypeList.add(errorType);
        machineList.add(machineName);
    }

    public Set<Set<String>> fillingDataFilterList(Set<Set<String>> dataForFilterList){
        dataForFilterList.add(productNameList);
        dataForFilterList.add(topologyList);
        dataForFilterList.add(modelList);
        dataForFilterList.add(partNumberList);
        dataForFilterList.add(errorTypeList);
        dataForFilterList.add(machineList);

        return  dataForFilterList;
    }


    public static ObservableList<Data> getDataList(){
        return datalist;
    }

/*    public Set<Set<String>> getDataForFilterList(){
        return dataForFilterList;
    }*/
}