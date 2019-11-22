package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import sample.controllers.Controller;
import sample.obj.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class which makes request to the MSSQL
 */

public class DbRequest extends Task<ObservableList<Data>> {

    private Statement statement = null;
    private ResultSet rs = null;
    private DbConnection dbConn;

    private CustomAlert alert;

    private long startTimeUnix;
    private long endTimeUnix;
    private String machineName;

    private SortedSet<String> productNameList = new TreeSet<>();
    private SortedSet<String> topologyList = new TreeSet<>();
    private SortedSet<String> modelList = new TreeSet<>();
    private SortedSet<String> partNumberList = new TreeSet<>();
    private SortedSet<String> errorTypeList = new TreeSet<>();
    private SortedSet<String> machineList = new TreeSet<>();

    private Set<String> panelsCodes= new HashSet<>();

    //List to fill combo box 'cbProdMachines' in Controller.class
    private List<String> machinesNames;

    private static ObservableList<Data> datalist = FXCollections.observableArrayList();

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    //With year week at the start of full date
    //private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ww dd/MM/yyyy HH:mm:ss");

    public DbRequest (DbConnection connection, long startTime, long endTime, String machineName){
        this.startTimeUnix = startTime;
        this.endTimeUnix = endTime;
        this.machineName = machineName;
        this.dbConn = connection;
        statementInit();
    }

    public DbRequest (DbConnection connection){
        this.dbConn = connection;
        statementInit();
    }

    private void statementInit(){
        try {
            statement = dbConn.getConnection().createStatement();
        } catch (SQLException ex){
            alert = new CustomAlert("An Error Occurred!", "Statement initialization error!",ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public ObservableList<Data> getFullData (long startTime, long endTime){

        long start = System.currentTimeMillis();
        datalist.clear();
        /**
         * Full MSSQL request
         */
        try {
            rs = statement.executeQuery("SELECT [PRODUCT].[Product_Name], " +
                    "[TESTED_OBJECT].[Topology], " +
                    "[TESTED_OBJECT].[Model], " +
                    "[PART_NUMBER].[Part_Number], " +
                    "[TESTED_OBJECT].[Repair_Button_Comment], " +
                    "[MACHINE].[Machine_Name], " +
                    "[PANELS].[Panel_Bar_Code], " +
                    "[PANELS].[Nb_Of_Tested_Object], " +
                    "[TESTED_OBJECT].[Repair_Numeric_Date_Hour], " +
                    "[OPERATOR].[Operator_Name] " +
                    "FROM (((((([TESTED_OBJECT] " +
                    "INNER JOIN [CARDS] ON [TESTED_OBJECT].[Card_ID] = [CARDS].[Card_ID]) " +
                    "INNER JOIN [PART_NUMBER] ON [TESTED_OBJECT].[Part_Number_ID] = [PART_NUMBER].[Part_Number_ID]) " +
                    "INNER JOIN [PANELS] ON [CARDS].[Panel_ID] = [PANELS].[Panel_ID]) " +
                    "INNER JOIN [MACHINE] ON [PANELS].[Machine_ID] = [MACHINE].[Machine_ID]) " +
                    "INNER JOIN [PRODUCT] ON [PANELS].[Product_ID] = [PRODUCT].[Product_ID]) " +
                    "INNER JOIN [OPERATOR] ON [TESTED_OBJECT].[Operator_ID] = [OPERATOR].[Operator_ID])" +
                    "WHERE [TESTED_OBJECT].[Repair_Numeric_Date_Hour] between " + startTime + " and " + endTime);

            getAnswerFromDB(rs);
            //TODO: optimize write data from result set (rs)

            long end = System.currentTimeMillis() - start;
            System.out.println("Request Cycle time: "+ end + " ms");

        } catch (SQLException ex){
            alert = new CustomAlert("An Error Occurred!", "Database request error!", ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
        return getDataList();
    }

    public ObservableList<Data> getDataByMachine (long startTime, long endTime, String machine){

        long start = System.currentTimeMillis();

        datalist.clear();
        try{
            rs = statement.executeQuery("SELECT [PRODUCT].[Product_Name], " +
                    "[TESTED_OBJECT].[Topology], " +
                    "[TESTED_OBJECT].[Model], " +
                    "[PART_NUMBER].[Part_Number], " +
                    "[TESTED_OBJECT].[Repair_Button_Comment], " +
                    "[MACHINE].[Machine_Name], " +
                    "[PANELS].[Panel_Bar_Code], " +
                    "[PANELS].[Nb_Of_Tested_Object], " +
                    "[TESTED_OBJECT].[Repair_Numeric_Date_Hour], " +
                    "[OPERATOR].[Operator_Name] " +
                    "FROM (((((([TESTED_OBJECT] " +
                    "INNER JOIN [CARDS] ON [TESTED_OBJECT].[Card_ID] = [CARDS].[Card_ID]) " +
                    "INNER JOIN [PART_NUMBER] ON [TESTED_OBJECT].[Part_Number_ID] = [PART_NUMBER].[Part_Number_ID]) " +
                    "INNER JOIN [PANELS] ON [CARDS].[Panel_ID] = [PANELS].[Panel_ID]) " +
                    "INNER JOIN [MACHINE] ON [PANELS].[Machine_ID] = [MACHINE].[Machine_ID]) " +
                    "INNER JOIN [PRODUCT] ON [PANELS].[Product_ID] = [PRODUCT].[Product_ID]) " +
                    "INNER JOIN [OPERATOR] ON [TESTED_OBJECT].[Operator_ID] = [OPERATOR].[Operator_ID])" +
                    "WHERE [MACHINE].[Machine_Name] ='"+ machine + "' AND " +
                    "[TESTED_OBJECT].[Repair_Numeric_Date_Hour] between " + startTime + " and " + endTime);


            getAnswerFromDB(rs);

            long end = System.currentTimeMillis() - start;
            System.out.println("Request Cycle time: "+ end + " ms");

        } catch (SQLException ex){
            alert = new CustomAlert("An Error Occurred!", "Database request error!", ex.toString(), Alert.AlertType.ERROR);
            return null;
        }
        return getDataList();
    }

    public List<String> getMachineNamesFromDB(){
        machinesNames = new ArrayList<>();

        final String strMachineType = "Vision";
        // Types: '1' - Vision, '2' - Repair
        final int numMachineType = 1;

        try{
            rs = statement.executeQuery("SELECT [Machine_Name] FROM [MACHINE] WHERE [MACHINE].[Machine_type] = " + numMachineType);

            while(rs.next()){
                machinesNames.add(rs.getString(1));
            }
        } catch (SQLException ex){
            alert = new CustomAlert("An Error Occurred!", "Machine database list initialization error!",ex.toString(), Alert.AlertType.ERROR);
        }

        return machinesNames;
    }

    private void setData(String productName, String topology, String model, String partNumber, String errorType, String machineName, String panelCode, String date, String operator, String numOfComponents){

        datalist.add(new Data(productName,topology ,model, partNumber ,errorType, machineName, panelCode, date, operator, numOfComponents));

        //Filling SearchLists
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

    public ObservableList<Data> getDataList(){
        return datalist;
    }

    public Set<String> getPanelsCodes (){
        return panelsCodes;
    }

    //Method which gets ResultSet as a param.
    //Get columns from rs and save it in list like an object
    private void getAnswerFromDB(ResultSet rs){
        try{
//            long start = System.currentTimeMillis();
            while(rs.next()){

                String productName = rs.getString("Product_Name");
                String topology = rs.getString("Topology");
                String model = rs.getString("Model");
                String partNumber = rs.getString("Part_Number");
                String repairComment = rs.getString("Repair_Button_Comment");
                String machineName = rs.getString("Machine_Name");
                String panelBarCode = rs.getString("Panel_Bar_Code");
                String valueOfTestedObjects = rs.getString("Nb_Of_Tested_Object");
                long timeStamp = rs.getLong("Repair_Numeric_Date_Hour");
                Date time = new Date(timeStamp * 1000);
                String operator = rs.getString("Operator_Name");

                panelsCodes.add(panelBarCode);
                setData(productName, topology, model, partNumber, repairComment, machineName, panelBarCode, DATE_FORMAT.format(time), operator, valueOfTestedObjects);

            }
//            long end = System.currentTimeMillis() - start;
//            System.out.println("Data list filled into the object: " + end + " ms");

        } catch (SQLException ex){
            alert = new CustomAlert("An Error Occurred!", "Error while getting answer from DB!", ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @Override
    protected ObservableList<Data> call() throws Exception {
        if(machineName != null) {
            if (machineName.equals("All")) {
                return getFullData(startTimeUnix, endTimeUnix);
            } else {
                return getDataByMachine(startTimeUnix, endTimeUnix, machineName);
            }
        }
        return  null;
    }
}