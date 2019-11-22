package sample.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.*;
import sample.ini.LineXMLIni;
import sample.obj.Data;
import sample.obj.ProductLine;

import java.util.*;
import java.util.List;

/**
 * Controller of the main window
 */

public class Controller {

    /**
     * Declaring all main variables
     */
    public static String fileName;
    public static long actualErrors = 0;
    public static boolean firstLunch = true;
    public static Map<String, Integer> topFalseAlarmsByComp;

    public static double errorRate;
    public static double falseAlarmRate;
    public static double actualDefectsRate;
    public static double inspectionRate;
    public static long testedComponents;

    private CustomAlert alert;

    private DropShadow btnShadow = new DropShadow();
    private long unixStartTime = 0;
    private long unixEndTime = 0;
    private SetTime time = new SetTime();

    private final String[] searchTypes = {"Product","Topology","Model","Part Number","Defect","Machine"};

    //private List<String> linesList;
    private List<ProductLine> lineObjList;

    private DbRequest dbRequest;

    private ObservableList<Data> dataListFromDB = FXCollections.observableArrayList();

    /**
     * Connecting all window elements from XML file
     */

    @FXML
    private ComboBox<String> cbDBSelect;

    @FXML
    private ComboBox<String> cbSearchType;

    @FXML
    private ComboBox<String> cbSearchObject;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private ComboBox<String> cbStartHour;

    @FXML
    private ComboBox<String> cbStartMinute;

    @FXML
    private DatePicker dpEndDate;

    @FXML
    private ComboBox<String> cbEndHour;

    @FXML
    private ComboBox<String> cbEndMinute;

    @FXML
    private ComboBox<String> cbProdMachines;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableView<Data> tvDataTable;

    @FXML
    private Label lbCycleTime;

    @FXML
    private Label lbPanelsValue;

    @FXML
    private Label lbErrorValue;

    @FXML
    private Label panelLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private MenuItem menuDBconfig;

    @FXML
    private MenuItem menuFolder;

    @FXML
    private MenuItem menuExit;

    @FXML
    private MenuItem menuAbout;

    @FXML
    private MenuItem menuImageExtractor;

    @FXML
    private MenuItem menuVITImageManager;

    @FXML
    private Label falseAlarmlabel;

    @FXML
    private Label lbFalseAlarmValue;

    @FXML
    private Label actualErrorsLabel;

    @FXML
    private Label lbWarnings;

    @FXML
    private Label lbActualErrors;

    @FXML
    private AnchorPane errorInformationPanel;

    @FXML
    private Button btnMoreInformation;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnExport;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnTodayFast;

    @FXML
    private Button btnMonthAgoFast;

    @FXML
    private Button btnWeekAgoFast;

    @FXML
    private ProgressIndicator process;

    @FXML
    void initialize() {
        process.setDisable(true);
        process.setVisible(false);

        errorInformationPanel.setVisible(false);
        lineObjList = new LineXMLIni().parse();
        /**
         * Adding values to the combo boxes
         */
        if(lineObjList.size() != 0){
            for(int i = 0; i < lineObjList.size(); i++){
                cbDBSelect.getItems().add(lineObjList.get(i).getName());
            }
            cbDBSelect.setValue(lineObjList.get(0).getName());
        }
        else{
//            showAlertWindow("Warning", null, "Initialization.xml file is empty! No lines to be to display", Alert.AlertType.WARNING);
            alert = new CustomAlert("Warning", null, "Initialization.xml file is empty! No lines to be to display", Alert.AlertType.WARNING);
        }

        setRealTime();

        for(int i =0; i < 6; i++){
            cbSearchType.getItems().add(searchTypes[i]);
        }

        cbProdMachines.getItems().add("All");
        cbStartHour.getItems().addAll("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        cbStartMinute.getItems().addAll("00","05","10","15","20","25","30","35","40","45","50","55");
        cbEndHour.getItems().addAll("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        cbEndMinute.getItems().addAll("00","05","10","15","20","25","30","35","40","45","50","55");

        /**
         * Creating main columns
         * The base is Data class
         */
        TableColumn <Data, String> productNameColumn = new TableColumn<>("Product Name");
        TableColumn <Data, String> topologyColumn = new TableColumn<>("Topology");
        TableColumn <Data, String> modelColumn = new TableColumn<>("Model");
        TableColumn <Data, String> partNumberColumn = new TableColumn<>("Part Number");
        TableColumn <Data, String> errorColumn = new TableColumn<>("Error Type");
        TableColumn <Data, String> machineColumn = new TableColumn<>("Machine Name");
        TableColumn <Data, String> codeColumn = new TableColumn<>("Panel Code");
        TableColumn <Data, String> dateColumn = new TableColumn<>("Date");
        TableColumn <Data, String> operatorColumn = new TableColumn<>("Operator ID");

        /**
         * Adding columns properties
         */
        productNameColumn.setMinWidth(100);
        productNameColumn.setEditable(false);
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        topologyColumn.setMinWidth(60);
        topologyColumn.setEditable(false);
        topologyColumn.setCellValueFactory(new PropertyValueFactory<>("topology"));
        modelColumn.setMinWidth(110);
        modelColumn.setEditable(false);
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        partNumberColumn.setMinWidth(80);
        partNumberColumn.setEditable(false);
        partNumberColumn.setCellValueFactory(new PropertyValueFactory<>("partNumber"));
        errorColumn.setMinWidth(90);
        errorColumn.setEditable(false);
        errorColumn.setCellValueFactory(new PropertyValueFactory<>("errorType"));
        machineColumn.setMinWidth(100);
        machineColumn.setEditable(false);
        machineColumn.setCellValueFactory(new PropertyValueFactory<>("machineName"));
        codeColumn.setMinWidth(240);
        codeColumn.setEditable(false);
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("panelCode"));
        dateColumn.setMinWidth(180);
        dateColumn.setEditable(false);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        operatorColumn.setMinWidth(80);
        operatorColumn.setEditable(false);
        operatorColumn.setCellValueFactory(new PropertyValueFactory<>("operatorID"));

        /**
         * Adding 'Update' button animation
         */
        btnShadow.setColor(Color.WHITE);
        btnShadow.setRadius(20);
        btnUpdate.setOnMouseEntered(event -> {btnUpdate.setEffect(btnShadow);});
        btnUpdate.setOnMouseExited(event -> {btnUpdate.setEffect(null);});

        //Fill machine list combo box
        fillCbMachineList();

        /**
         * Button 'Update' listener
         */
        btnUpdate.setOnAction(event -> {

            /**
             * Starting time period for Cycle timer
             */
            long startTime = System.currentTimeMillis();
            actualErrors = 0;

            /**
             * Check if all fields are filled
             */
            if(dpStartDate.getValue() == null || dpEndDate.getValue() == null || cbStartHour.getValue()==null || cbStartMinute.getValue()==null || cbEndHour.getValue()==null || cbEndMinute.getValue()==null || cbDBSelect.getValue() == null) {
//                showAlertWindow("Input Error!", null, "Please check selected date, time and production line!", Alert.AlertType.WARNING);
                alert = new CustomAlert("Input Error!", null, "Please check selected date, time and production line!", Alert.AlertType.WARNING);
            }
            else {
//                LocalDate date = dpStartDate.getValue();
//                String stringStartTime = date.format(dateformat) + " " + cbStartHour.getValue() + ":" + cbStartMinute.getValue();
//                date = dpEndDate.getValue();
//                String stringEndTime = date.format(dateformat) + " " + cbEndHour.getValue() + ":" + cbEndMinute.getValue();
//
//                try {
//                    Date fulldate = fulldateformat.parse(stringStartTime);
//                    unixStartTime = fulldate.getTime() / 1000;
//
//                    fulldate = fulldateformat.parse(stringEndTime);
//                    unixEndTime = fulldate.getTime() / 1000;
//
//
//                } catch (Exception e) {
//                    alert = new CustomAlert("An Error Occurred!", "Date is NULL or date format Error!", e.toString(), Alert.AlertType.ERROR);
//                }

                btnUpdate.setVisible(false);
                process.setDisable(false);
                process.setVisible(true);

                cbSearchObject.setValue("");
                cbSearchType.setValue("");
                cbSearchType.setDisable(true);
                btnClear.setDisable(true);
                btnSearch.setDisable(true);
                unixStartTime = timeToUNIX(dpStartDate.getValue(), cbStartHour.getValue(), cbStartMinute.getValue());
                unixEndTime = timeToUNIX(dpEndDate.getValue(), cbEndHour.getValue(), cbEndMinute.getValue());

                /**
                 * Creating new Database connection and  Database request
                 * IPs, dataBasenames, user and password are from initialization.xml
                 *
                 * Based on selected DB(production line), making DB connection initialization and sending request
                 */
                ProductLine selectedLine = getSelectedDB();
                //Open new connection to the DB
                DbConnection dbConnection = new DbConnection();
                //Make connection to the Db
                dbConnection.makeConnection("jdbc:sqlserver://"+selectedLine.getIp()+";database="+selectedLine.getDataBaseName(),selectedLine.getUser(),selectedLine.getPassword());

                dbRequest = new DbRequest(dbConnection, unixStartTime, unixEndTime, cbProdMachines.getValue());

                if(!cbProdMachines.getValue().equals("All")){
                    fileName = cbProdMachines.getValue()+"_Report.xls";
                } else {
                    fileName = selectedLine.getName()+"_Report.xls";
                }

                //Handler for event state
                dbRequest.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        dataListFromDB = dbRequest.getDataList();
                        /**
                         * Adding list with all data to the table
                         */
                        tvDataTable.setItems(dataListFromDB);

                        /**
                         * If there was no request yet, so add all columns to the main table, else just refresh the table with new data
                         */
                        if (firstLunch) {
                            tvDataTable.getColumns().addAll(productNameColumn, topologyColumn, modelColumn, partNumberColumn, errorColumn, machineColumn, codeColumn, dateColumn, operatorColumn);
                            errorInformationPanel.setVisible(true);
                            firstLunch = false;
                        }
                        else{
                            tvDataTable.refresh();
                        }

                        process.setDisable(true);
                        process.setVisible(false);
                        btnUpdate.setVisible(true);
                        btnClear.setDisable(false);
                        btnSearch.setDisable(false);
                        cbSearchType.setDisable(false);

                        /**
                         * Filling additional information values
                         */
                        Statistics stat = new Statistics(dataListFromDB);

                        lbPanelsValue.setText(String.valueOf(dbRequest.getPanelsCodes().size()));
                        lbErrorValue.setText(String.valueOf(stat.getDefectsValue()));
                        lbFalseAlarmValue.setText(String.valueOf(stat.getFalseAlarms()));
                        lbActualErrors.setText(String.valueOf(stat.getActualDefectsValue()));
                        lbWarnings.setText(String.valueOf(stat.getWarningValue()));
                        topFalseAlarmsByComp = stat.getSortedFalseAlarmList();

                        errorRate = stat.getErrorRate();
                        falseAlarmRate = stat.getFalseAlarmRate();
                        actualDefectsRate = stat.getActualDefectsRate();
                        inspectionRate = stat.getInspectionRate();
                        testedComponents = stat.getTestedComp();

                        long endTime = System.currentTimeMillis() - startTime;

                        /**
                         * Set cycle time
                         */
                        lbCycleTime.setText(endTime + " ms;"+ " Rows: "+ dataListFromDB.size());
                    }

                });
                new Thread(dbRequest).start();
            }
        });

        //Change list of machines if different DB server selected
        cbDBSelect.setOnAction(event -> fillCbMachineList());

        /**
         * Menu 'Exit' button listener
         * To close main program window
         */
        menuExit.setOnAction(event -> Platform.exit());

        /**
         * Menu 'About' button listener
         * Showing window with all information
         */
        menuAbout.setOnAction(event -> {
            alert = new CustomAlert("About", null, "Extractor for VITechnology MSSQL database\n"+
                    "Created by Pribytkov Petr\n" +
                    "Creation date: 02.04.2018\n" +
                    "Email: Petr.Pribytkov@eolane.com\n" +
                    "Last changes: "+ Main.LAST_VERSION_DATE+ "\n" +
                    "Version: "+ Main.VERSION, Alert.AlertType.INFORMATION);
        });

        /**
         * Menu 'DataBase Configuration' button listener
         * Showing window with DBs configurations
         */
        menuDBconfig.setOnAction(event -> {

            String iniXMLFilePath = System.getProperty("user.dir") + File.separator+ "Config"+ File.separator + "initialization.xml";

            try {
                    Runtime.getRuntime().exec("explorer "+iniXMLFilePath);

            } catch (IOException e) {
//                showAlertWindow("An Error Occurred!", "Error in opening file: initialization.xml", e.toString(), Alert.AlertType.ERROR);
                alert = new CustomAlert("An Error Occurred!", "Error in opening file: initialization.xml", e.toString(), Alert.AlertType.ERROR);
            }
        });

        /**
         * 'More' button listener
         * Showing window with statistics
         */
        btnMoreInformation.setOnAction(event -> {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/scenes/AdditionalInformation.fxml"));

            try{
                loader.load();
            } catch (IOException e){
                alert = new CustomAlert("An Error Occurred!", "Error in opening file: AdditionalInformation.fxml", e.toString(), Alert.AlertType.ERROR);
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Additional Information");
            stage.setResizable(false);
            stage.showAndWait();
        });

        btnWeekAgoFast.setOnAction(event -> { setTimeToPrivWeek(); });
        btnMonthAgoFast.setOnAction(event -> {setTimeToPrivMonth(); });
        btnTodayFast.setOnAction(event -> { setRealTime(); });

        /**
         * Menu 'Open Directory' button listener
         * Opening root directory of the program
         */
        menuFolder.setOnAction(event -> {
            String localpath = System.getProperty("user.dir");

            try {
                Runtime.getRuntime().exec("explorer "+localpath);
            }
            catch (IOException e){
                alert = new CustomAlert("An Error Occurred!", "Main Folder is not exist!", e.toString(), Alert.AlertType.ERROR);
            }
        });

        /**
         * 'Search' button listener
         * Starts search module (function)
         */
        btnSearch.setOnAction(event -> {

            /**
             * Checks if there was request to the database, if not, so shows error window, else execute search function
             */
            if(cbSearchObject.getValue() == null){
//                showAlertWindow("An Error Occurred!", "Nothing to search!", null, Alert.AlertType.ERROR);
                alert = new CustomAlert("An Error Occurred!", "Nothing to search!", null, Alert.AlertType.ERROR);
            }
            else{
                search(cbSearchObject.getValue());
            }

        });

        //Action to the 'Clear' button
        btnClear.setOnAction(event -> {
            cbSearchObject.setValue("");
            cbSearchType.setValue("");
            tvDataTable.setItems(dataListFromDB);
        });

        cbSearchType.setOnAction(event -> {
            if(!firstLunch) {
                Integer selectObject = 0;
                Set<Set<String>> objectList = new LinkedHashSet<>();
                objectList = dbRequest.fillingDataFilterList(objectList);

                Iterator<Set<String>> iterator = objectList.iterator();

                if (cbSearchType.getValue().equals(searchTypes[0])) {
                    //do nothing
                } else if (cbSearchType.getValue().equals(searchTypes[1])) {
                    selectObject = 1;

                } else if (cbSearchType.getValue().equals(searchTypes[2])) {
                    selectObject = 2;

                } else if (cbSearchType.getValue().equals(searchTypes[3])) {
                    selectObject = 3;

                } else if (cbSearchType.getValue().equals(searchTypes[4])) {
                    selectObject = 4;

                } else if (cbSearchType.getValue().equals(searchTypes[5])) {
                    selectObject = 5;

                }

                for (int i = 0; i < selectObject; i++) {
                    iterator.next();
                }

                Iterator<String> sIterator = iterator.next().iterator();
                cbSearchObject.getItems().clear();

                while (sIterator.hasNext()) {
                    cbSearchObject.getItems().add(sIterator.next());
                }
            } else if(cbSearchType.getValue().equals("") || cbSearchObject.getValue().equals("") ){
                //DO nothing
            }
            else{
                alert = new CustomAlert("An Error Occurred!", "There was no Data request yet. Please make request, to work with 'Search' function", null, Alert.AlertType.ERROR);
            }
        });

        /**
         * Menu 'ImageExtractor' button listener
         * Opens image downloader window
         */
        menuImageExtractor.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/scenes/GetImagesForm.fxml"));

            try{
                loader.load();
            } catch (IOException e){
//                showAlertWindow("An Error Occurred!", "Error in opening file: GetImagesForm.fxml", e.toString(), Alert.AlertType.ERROR);
                alert = new CustomAlert("An Error Occurred!", "Error in opening file: GetImagesForm.fxml", e.toString(), Alert.AlertType.ERROR);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Download Images");
            stage.setResizable(false);
            stage.showAndWait();
        });

        /**
         * Menu 'VITImageManager' button listener
         * Launch 'VITImageManager.exe', which is located in the main 'VIT' folder
         */
        menuVITImageManager.setOnAction(event -> {
            String pathVITImageManager = "C:"+File.separator+"VIT"+File.separator+"VITImageManager.exe";
            File check = new File(pathVITImageManager);

            try{
                if(check.exists()){
                    Runtime.getRuntime().exec("explorer "+pathVITImageManager);
                }
                else{
//                    showAlertWindow("An Error Occurred!", "File C:/VIT/VITImageManager.exe is not found!", null, Alert.AlertType.ERROR);
                    alert = new CustomAlert("An Error Occurred!", "File C:/VIT/VITImageManager.exe is not found!", null, Alert.AlertType.ERROR);
                }

            } catch (IOException e){
//                showAlertWindow("An Error Occurred!", "File C:/VIT/VITImageManager.exe is not found!", e.toString(), Alert.AlertType.ERROR);
                alert = new CustomAlert("An Error Occurred!", "File C:/VIT/VITImageManager.exe is not found!", e.toString(), Alert.AlertType.ERROR);
            }
        });

        btnExport.setOnAction(event -> {
            if(!firstLunch){
                ExportToFile export = new ExportToFile(fileName);
                if(export.exportToFile(dataListFromDB)){
//                    showAlertWindow("Successful", "Exporting completed", fileName+" has been save in 'Files' folder", Alert.AlertType.INFORMATION);
                    alert = new CustomAlert("Successful", "Exporting completed", fileName+" has been save in 'Files' folder", Alert.AlertType.INFORMATION);
                }
            } else{
//                showAlertWindow("An Error Occurred!", "No data to export!", null, Alert.AlertType.ERROR);
                alert = new CustomAlert("An Error Occurred!", "No data to export!", null, Alert.AlertType.ERROR);
            }
        });
    }

    private void fillCbMachineList(){
        ProductLine line = getSelectedDB();
        cbProdMachines.getItems().clear();

        if(line != null){
            if(line.getName().equals("Siemens")){
                cbProdMachines.getItems().add("All");
            } else {
                //Open connection to the DB
                DbConnection dbConnection = new DbConnection();
                //Make connection to the Db
                dbConnection.makeConnection("jdbc:sqlserver://"+line.getIp()+";database="+line.getDataBaseName(),line.getUser(),line.getPassword());

                dbRequest = new DbRequest(dbConnection);
                List<String> machinesList = dbRequest.getMachineNamesFromDB();

                //If machines more than 1, add String "All"
                if(machinesList.size() > 1){
                    cbProdMachines.getItems().add("All");
                }

                //Filling machine names from List to the combo box
                for(String machines : machinesList){
                    cbProdMachines.getItems().add(machines);
                }

                //dbRequest.makeRequest(unixStartTime, unixEndTime);
            }
            cbProdMachines.setValue(cbProdMachines.getItems().get(0));
        } else {
            alert = new CustomAlert("Warning!", "Selected DB is null!", null, Alert.AlertType.WARNING);
        }
    }

    private ProductLine getSelectedDB (){
        for(ProductLine line : lineObjList){
            if(line.getName().equals(cbDBSelect.getValue())){
                return line;
            }
        }

        return null;
    }

    /**
     * Module which sorts the data in datalist (MainTable)
     * @param searchText
     */
    private void search(String searchText){
        DataFilter filter = new DataFilter(dataListFromDB, searchText);
        SortedList<Data> sortedData = new SortedList<>(filter.search());
        sortedData.comparatorProperty().bind(tvDataTable.comparatorProperty());
        tvDataTable.setItems(sortedData);
        tvDataTable.refresh();
    }

    /**
     * Collecting date from Datepicker and time combo boxes
     * And leading it to the UNIX time format
     */
    private Long timeToUNIX(LocalDate date, String hour, String minute){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        Long unixTime = null;

        String strTime = date.format(dateFormat) + " " + hour + ":" + minute;

        try {
            Date fullDate = fullDateFormat.parse(strTime);
            unixTime = fullDate.getTime() / 1000;
        } catch (Exception e) {
            alert = new CustomAlert("An Error Occurred!", "Date is NULL or date format Error!", e.toString(), Alert.AlertType.ERROR);
        }

        return unixTime;
    }

    /**
     * Methods to set time
     */

    private void setRealTime(){
        setComboBoxTime();
        dpStartDate.setValue(time.setRealTime());
        dpEndDate.setValue(time.setRealTime());
    }

    private void setTimeToPrivWeek(){
        setComboBoxTime();
        dpStartDate.setValue(time.setPrivWeekStart());
        dpEndDate.setValue(time.setPrivWeekEnd());
    }

    private void setTimeToPrivMonth(){
        setComboBoxTime();
        dpStartDate.setValue(time.setPrivMonthStart());
        dpEndDate.setValue(time.setPrivMonthStartEnd());
    }

    private void setComboBoxTime(){
        cbStartHour.setValue("00");
        cbStartMinute.setValue("05");
        cbEndHour.setValue("23");
        cbEndMinute.setValue("55");
    }

}