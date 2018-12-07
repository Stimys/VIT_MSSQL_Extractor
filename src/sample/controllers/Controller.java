package sample.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.collections.transformation.SortedList;
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
     * Images for buttons
     */

    //Image image = new Image(getClass().getResourceAsStream("assets/today_arrow.jpg"));

    /**
     * Declaring all main variables
     */
    public static String fileName;
    public static long actualErrors = 0;
    public static boolean firstLunch = true;

    private DropShadow btnShadow = new DropShadow();
    private long unixStartTime = 0;
    private long unixEndTime = 0;
    private SetTime time = new SetTime();

    private final String[] searchTypes = {"Product","Topology","Model","Part Number","Defect","Machine"};

    //private List<String> linesList;
    private List<ProductLine> lineObjList;

    private DbRequest dbRequest;

    DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    SimpleDateFormat fulldateformat = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    Alert warningalert = new Alert(Alert.AlertType.WARNING);
    Alert erroralert  = new Alert(Alert.AlertType.ERROR);
    Alert informationalert = new Alert(Alert.AlertType.INFORMATION);

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

//    @FXML
//    private CheckBox chbSaveReport;

    @FXML
    private DatePicker dpEndDate;

    @FXML
    private ComboBox<String> cbEndHour;

    @FXML
    private ComboBox<String> cbEndMinute;

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

//    @FXML
//    private MenuItem menuGetImages;

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
    private Label lbVersion;

    @FXML
    private Button btnTodayFast;

    @FXML
    private Button btnMonthAgoFast;

    @FXML
    private Button btnWeekAgoFast;

    @FXML
    void initialize() {

        lbVersion.setText("v."+Main.VERSION);
        errorInformationPanel.setVisible(false);
        btnExport.setDisable(true);

        //linesList = new LinesIni().initialization();
        lineObjList = new LineXMLIni().parse();
        /**
         * Adding values to the combo boxes
         */
        //for(String line : linesList) cbDBSelect.getItems().add(line);

        if(lineObjList.size() != 0){
            for(int i = 0; i < lineObjList.size(); i++){
                cbDBSelect.getItems().add(lineObjList.get(i).getName());
            }
            cbDBSelect.setValue(lineObjList.get(0).getName());
        }
        else{
            warningalert.setTitle("Warning");
            warningalert.setHeaderText(null);
            warningalert.setContentText("Initialization.xml file is empty! No lines to be to display");

            warningalert.showAndWait();
        }

        setRealTime();

        for(int i =0; i < 6; i++){
            cbSearchType.getItems().add(searchTypes[i]);
        }

        cbStartHour.getItems().addAll("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        cbStartMinute.getItems().addAll("00","05","10","15","20","25","30","35","40","45","50","55");
        cbEndHour.getItems().addAll("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        cbEndMinute.getItems().addAll("00","05","10","15","20","25","30","35","40","45","50","55");

        /**
         * Creating main columns
         * The basis is Data class
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
         * Adding button animation
         */
        btnShadow.setColor(Color.WHITE);
        btnShadow.setRadius(20);
        btnUpdate.setOnMouseEntered(event -> {btnUpdate.setEffect(btnShadow);});
        btnUpdate.setOnMouseExited(event -> {btnUpdate.setEffect(null);});

        /**
         * Button 'Update' listener
         */
        btnUpdate.setOnAction(event -> {

            /**
             * Starting time period for Cycletimer
             */
            long startTime = System.currentTimeMillis();
            actualErrors = 0;

            /**
             * Checking if all fields are filled
             * If not, showing error message, else execute database request
             */
            if(dpStartDate.getValue() == null || dpEndDate.getValue() == null || cbStartHour.getValue()==null || cbStartMinute.getValue()==null || cbEndHour.getValue()==null || cbEndMinute.getValue()==null || cbDBSelect.getValue() == null) {
                warningalert.setTitle("Input Error!");
                warningalert.setHeaderText(null);
                warningalert.setContentText("Please check selected date, time and production line!");

                warningalert.showAndWait();
            }
            else {
                /**
                 * Collecting date from Datepicker and time combo boxes
                 * And leading it to the UNIX time format
                 */
                LocalDate date = dpStartDate.getValue();
                String stringStartTime = date.format(dateformat) + " " + cbStartHour.getValue() + ":" + cbStartMinute.getValue();
                date = dpEndDate.getValue();
                String stringEndTime = date.format(dateformat) + " " + cbEndHour.getValue() + ":" + cbEndMinute.getValue();

                try {
                    Date fulldate = fulldateformat.parse(stringStartTime);
                    unixStartTime = fulldate.getTime() / 1000;

                    fulldate = fulldateformat.parse(stringEndTime);
                    unixEndTime = fulldate.getTime() / 1000;


                } catch (Exception e) {
                    erroralert.setTitle("An Error Occurred!");
                    erroralert.setHeaderText("Date is NULL or dateformar Error!");
                    erroralert.setContentText(e.toString());
                    erroralert.showAndWait();
                }

                /**
                 * Creating new Database connection and  Database request
                 * IPs, dataBasenames, user and password are taking from DbConfig.properties
                 */
                DbConnection dbConnection = new DbConnection();
                //DataBaseConfigInitialization config = new DataBaseConfigInitialization();
                dbRequest = new DbRequest();

                /**
                 * Based on selected DB(production line), making DB connection initialization and sending request
                 */
                for(ProductLine line : lineObjList){
                    if(line.getName().equals(cbDBSelect.getValue())){
                        dbConnection.makeConnection("jdbc:sqlserver://"+line.getIp()+";database="+line.getDataBaseName(),line.getUser(),line.getPassword());
                        fileName = line.getName()+"_Report.xls";
                        dbRequest.makeRequest(unixStartTime, unixEndTime, dbConnection);
                    }
                }

                /**
                 * Adding list with all data to the table
                 */
                //tvDataTable.setItems(DbRequest.datalist);

                tvDataTable.setItems(DbRequest.getDataList());

                /**
                 * If there was no request yet, so adding all columns to the main table, else just refresh the table with new data
                 */
                if (firstLunch) {
                    tvDataTable.getColumns().addAll(productNameColumn, topologyColumn, modelColumn, partNumberColumn, errorColumn, machineColumn, codeColumn, dateColumn, operatorColumn);
                    errorInformationPanel.setVisible(true);
                    firstLunch = false;
                    btnExport.setDisable(false);
                }
                else{
                   tvDataTable.refresh();
                }

                /**
                 * Filling additional information values
                 */
                lbPanelsValue.setText(Integer.toString(DbRequest.panels));
                lbErrorValue.setText(Long.toString(DbRequest.errors));
                lbFalseAlarmValue.setText(Long.toString(DbRequest.falseAlarmValue));
                actualErrors = DbRequest.errors - DbRequest.falseAlarmValue;
                lbActualErrors.setText(Long.toString(actualErrors));
                long endTime = System.currentTimeMillis() - startTime;

//                /**
//                 * If checkbox 'Save report' is checked, so creating and showing new window from FXML file
//                 */
//                if(chbSaveReport.isSelected()){
//                    FXMLLoader loader = new FXMLLoader();
//                    loader.setLocation(getClass().getResource("/sample/scenes/FileOpenWindow.fxml"));
//                    try {
//                        loader.load();
//                    }
//                    catch (IOException e){
//                        erroralert.setTitle("An Error Occurred!");
//                        erroralert.setHeaderText("Error in opening file: FileOpenWindow.fxml");
//                        erroralert.setContentText(e.toString());
//                        erroralert.showAndWait();
//                    }
//
//                    Parent root = loader.getRoot();
//                    Stage stage = new Stage();
//                    stage.setScene(new Scene(root));
//                    stage.setTitle("Report File");
//                    stage.setResizable(false);
//                    stage.initModality(Modality.WINDOW_MODAL);
//                    stage.initOwner(Main.primaryStage);
//                    stage.showAndWait();
//                }
                /**
                 * Showing cycle time
                 */
                lbCycleTime.setText(endTime + " ms;"+ " Rows: "+ DbRequest.getDataList().size());
            }
        });

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
            informationalert.setTitle("About");
            informationalert.setHeaderText(null);
            informationalert.setContentText("Extractor for VITechnology MSSQL database\n"+
                    "Created by Pribytkov Petr\n" +
                    "Creation date: 02.04.2018\n" +
                    "Email: Petr.Pribytkov@eolane.com\n" +
                     "Last changes: "+ Main.LAST_VERSION_DATE+ "\n" +
                    "Version: "+ Main.VERSION);
            informationalert.showAndWait();
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
                e.printStackTrace();
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("Error in opening file: initialization.xml");
                erroralert.setContentText(e.toString());
                erroralert.showAndWait();
            }
        });

        /**
         * 'More' button listener
         * Showing window with all addition information
         */
        btnMoreInformation.setOnAction(event -> {



            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/scenes/AdditionalInformation.fxml"));

            try{
                loader.load();
            } catch (IOException e){
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("Error in opening file: AdditionalInformation.fxml");
                erroralert.setContentText(e.toString());
                erroralert.showAndWait();
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
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("Main Folder is not exist!");
                erroralert.setContentText(e.toString());
                erroralert.showAndWait();
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

                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("Nothing to search!");
                erroralert.setContentText(null);
                erroralert.showAndWait();
            }
            else{
                search(cbSearchObject.getValue());
            }

        });

        btnClear.setOnAction(event -> {
            cbSearchObject.setValue("");
            tvDataTable.setItems(DbRequest.getDataList());
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
            }
            else{
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("There was no Data request yet. Please make request, to work with 'Search' function");
                erroralert.setContentText(null);
                erroralert.showAndWait();
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
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("Error in opening file: GetImagesForm.fxml");
                erroralert.setContentText(e.toString());
                erroralert.showAndWait();
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
         * Launch 'VITImageManaget.exe', which is located in the main 'VIT' folder
         */
        menuVITImageManager.setOnAction(event -> {
            String pathVITImageManager = "C:"+File.separator+"VIT"+File.separator+"VITImageManager.exe";
            File check = new File(pathVITImageManager);

            try{
                if(check.exists()){
                    Runtime.getRuntime().exec("explorer "+pathVITImageManager);
                }
                else{
                    erroralert.setTitle("An Error Occurred!");
                    erroralert.setHeaderText("File C:/VIT/VITImageManager.exe is not found!");
                    erroralert.setContentText(null);
                    erroralert.showAndWait();
                }

            } catch (IOException e){
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("File C:/VIT/VITImageManager.exe is not found!");
                erroralert.setContentText(e.toString());
                erroralert.showAndWait();
            }
        });

        btnExport.setOnAction(event -> {
            if(!firstLunch){
                ExportToFile export = new ExportToFile(fileName);
                if(export.export()){
                    informationalert.setTitle("Successful");
                    informationalert.setHeaderText("Exporting completed");
                    informationalert.setContentText(fileName+" has been save in 'Files' folder");
                    informationalert.showAndWait();
                }
            } else{
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("No data to export!");
                erroralert.setContentText(null);
                erroralert.showAndWait();
            }
        });
    }

    /**
     * Module which sorts the data in datalist (MainTable)
     * @param searchText
     */
    private void search(String searchText){
        DataFilter filter = new DataFilter(DbRequest.getDataList(), searchText);
        SortedList<Data> sortedData = new SortedList<>(filter.search());
        sortedData.comparatorProperty().bind(tvDataTable.comparatorProperty());
        tvDataTable.setItems(sortedData);
        tvDataTable.refresh();
    }

    private void setRealTime(){
        cbStartHour.setValue("00");
        cbStartMinute.setValue("05");
        cbEndHour.setValue("23");
        cbEndMinute.setValue("55");

        dpStartDate.setValue(time.setRealTime());
        dpEndDate.setValue(time.setRealTime());
    }

    private void setTimeToPrivWeek(){
        cbStartHour.setValue("00");
        cbStartMinute.setValue("05");
        cbEndHour.setValue("23");
        cbEndMinute.setValue("55");

        dpStartDate.setValue(time.setPrivWeekStart());
        dpEndDate.setValue(time.setPrivWeekEnd());
    }

    private void setTimeToPrivMonth(){
        cbStartHour.setValue("00");
        cbStartMinute.setValue("05");
        cbEndHour.setValue("23");
        cbEndMinute.setValue("55");

        dpStartDate.setValue(time.setPrivMonthStart());
        dpEndDate.setValue(time.setPrivMonthStartEnd());
    }
}