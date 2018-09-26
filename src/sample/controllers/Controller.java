package sample.controllers;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.*;

import java.util.Date;

public class Controller {

    public static String fileName;
    DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    SimpleDateFormat fulldateformat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    private DropShadow btnShadow = new DropShadow();

    Alert warningalert = new Alert(Alert.AlertType.WARNING);
    Alert erroralert  = new Alert(Alert.AlertType.ERROR);
    Alert informationalert = new Alert(Alert.AlertType.INFORMATION);

    public static boolean firstLunch = true;

    private long unixStartTime = 0;
    private long unixEndTime = 0;
    private SetTime time = new SetTime();
    public static long actualErrors = 0;

    private final String VARROCLINES = "Varroc Lines";
    private final String SIEMENSLINE = "Siemens Line";
    private final String FUJILONG = "FujiLong Line";
    private final String AOI5KSERVER = "5K Server";

    @FXML
    private ComboBox<String> cbDBSelect;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private ComboBox<String> cbStartHour;

    @FXML
    private ComboBox<String> cbStartMinute;

    @FXML
    private CheckBox chbSaveReport;

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
    private MenuItem menuPrevWeek;

    @FXML
    private MenuItem menuPrevMonth;

    @FXML
    private MenuItem menuToday;

    @FXML
    private MenuItem menuAbout;

    @FXML
    private MenuItem menuGetImages;

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
    private TextField tfSearchValue;

    @FXML
    void initialize() {

        errorInformationPanel.setVisible(false);

        cbDBSelect.getItems().addAll(AOI5KSERVER, SIEMENSLINE, FUJILONG);
        cbStartHour.getItems().addAll("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        cbStartMinute.getItems().addAll("00","05","10","15","20","25","30","35","40","45","50","55");
        cbEndHour.getItems().addAll("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        cbEndMinute.getItems().addAll("00","05","10","15","20","25","30","35","40","45","50","55");

        TableColumn <Data, String> productNameColumn = new TableColumn<>("Product Name");
        TableColumn <Data, String> topologyColumn = new TableColumn<>("Topology");
        TableColumn <Data, String> modelColumn = new TableColumn<>("Model");
        TableColumn <Data, String> partNumberColumn = new TableColumn<>("Part Number");
        TableColumn <Data, String> errorColumn = new TableColumn<>("Error Type");
        TableColumn <Data, String> machineColumn = new TableColumn<>("Machine Name");
        TableColumn <Data, String> codeColumn = new TableColumn<>("Panel Code");
        TableColumn <Data, String> dateColumn = new TableColumn<>("Date");
        TableColumn <Data, String> operatorColumn = new TableColumn<>("Operator ID");

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

        btnShadow.setColor(Color.WHITE);
        btnShadow.setRadius(20);
        btnUpdate.setOnMouseEntered(event -> {btnUpdate.setEffect(btnShadow);});

        btnUpdate.setOnMouseExited(event -> {btnUpdate.setEffect(null);});

        btnUpdate.setOnAction(event -> {
            long startTime = System.currentTimeMillis();
            actualErrors = 0;

            if(dpStartDate.getValue() == null || dpEndDate.getValue() == null || cbStartHour.getValue()==null || cbStartMinute.getValue()==null || cbEndHour.getValue()==null || cbEndMinute.getValue()==null || cbDBSelect.getValue() == null) {
                warningalert.setTitle("Input Error!");
                warningalert.setHeaderText(null);
                warningalert.setContentText("Please check selected date, time and production line!");

                warningalert.showAndWait();
            }
            else {
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

                DbConnection dbConnection = new DbConnection();
                DataBaseConfigInitialization config = new DataBaseConfigInitialization();
                DbRequest request = new DbRequest();

                if (cbDBSelect.getValue().equals(AOI5KSERVER)) {
                    dbConnection.dbConnect(config.configInitialization("5KServer"), config.userInitialization(), config.passwordInitialization());
                    fileName = fileNameInitialization(AOI5KSERVER);
                    request.request(unixStartTime, unixEndTime, dbConnection, chbSaveReport.isSelected());
                } else if (cbDBSelect.getValue().equals(SIEMENSLINE)) {
                    dbConnection.dbConnect(config.configInitialization("Siemens"), config.userInitialization(), config.passwordInitialization());
                    fileName = fileNameInitialization(SIEMENSLINE);
                    request.request(unixStartTime, unixEndTime, dbConnection, chbSaveReport.isSelected());

                } else if (cbDBSelect.getValue().equals(FUJILONG)) {
                    dbConnection.dbConnect(config.configInitialization("FujiLong"), config.userInitialization(), config.passwordInitialization());
                    fileName = fileNameInitialization(FUJILONG);
                    request.request(unixStartTime, unixEndTime, dbConnection, chbSaveReport.isSelected());
                }
                else if(cbDBSelect.getValue().equals(VARROCLINES)){
                    dbConnection.dbConnect(config.configInitialization("5KServer"), config.userInitialization(), config.passwordInitialization());
                    fileName = fileNameInitialization(VARROCLINES);
                    request.request(unixStartTime, unixEndTime, dbConnection, chbSaveReport.isSelected());

                }

                tvDataTable.setItems(DbRequest.datalist);

                if (firstLunch) {
                    tvDataTable.getColumns().addAll(productNameColumn, topologyColumn, modelColumn, partNumberColumn, errorColumn, machineColumn, codeColumn, dateColumn, operatorColumn);
                    errorInformationPanel.setVisible(true);
                    firstLunch = false;
                }
                else{
                   tvDataTable.refresh();
                }

                lbPanelsValue.setText(Integer.toString(DbRequest.panels));
                lbErrorValue.setText(Long.toString(DbRequest.errors));
                lbFalseAlarmValue.setText(Long.toString(DbRequest.falsealarmvalue));
                actualErrors = DbRequest.errors - DbRequest.falsealarmvalue;
                lbActualErrors.setText(Long.toString(actualErrors));
                long endTime = System.currentTimeMillis() - startTime;

                if(chbSaveReport.isSelected()){
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/sample/scenes/FileOpenWindow.fxml"));
                    try {
                        loader.load();
                    }
                    catch (IOException e){
                        erroralert.setTitle("An Error Occurred!");
                        erroralert.setHeaderText("Error in opening file: FileOpenWindow.fxml");
                        erroralert.setContentText(e.toString());
                        erroralert.showAndWait();
                    }

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Report File");
                    stage.setResizable(false);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(Main.primaryStage);
                    stage.showAndWait();
                }
                lbCycleTime.setText(endTime + " ms");
            }
        });

        menuExit.setOnAction(event -> Platform.exit());

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

        menuDBconfig.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/scenes/DataBaseConfiguration.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("Error in opening file: DataBaseConfiguration.fxml");
                erroralert.setContentText(e.toString());
                erroralert.showAndWait();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Database configuration");
            stage.setResizable(false);
            stage.showAndWait();
        });

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

        menuPrevWeek.setOnAction(event -> {
            cbStartHour.setValue("00");
            cbStartMinute.setValue("05");
            cbEndHour.setValue("23");
            cbEndMinute.setValue("55");

            dpStartDate.setValue(time.setPrivWeekStart());
            dpEndDate.setValue(time.setPrivWeekEnd());
        });

        menuPrevMonth.setOnAction(event -> {
            cbStartHour.setValue("00");
            cbStartMinute.setValue("05");
            cbEndHour.setValue("23");
            cbEndMinute.setValue("55");

            dpStartDate.setValue(time.setPrivMonthStart());
            dpEndDate.setValue(time.setPrivMonthStartEnd());

        });

        menuToday.setOnAction(event -> {

            cbStartHour.setValue("00");
            cbStartMinute.setValue("05");
            cbEndHour.setValue("23");
            cbEndMinute.setValue("55");

            dpStartDate.setValue(time.setRealTime());
            dpEndDate.setValue(time.setRealTime());
        });

        menuFolder.setOnAction(event -> {

            String localpath = System.getProperty("user.dir");
//            File mainDirectory = new File(localpath);
//            Desktop desktop = Desktop.getDesktop();

            try {
                //desktop.open(mainDirectory);
                Runtime.getRuntime().exec("explorer "+localpath);
            }
            catch (IOException e){
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("Main Folder is not exist!");
                erroralert.setContentText(e.toString());
                erroralert.showAndWait();
            }
        });

        btnSearch.setOnAction(event -> {
            if(!firstLunch){
                if(tfSearchValue.getText() == null || tfSearchValue.getText().isEmpty()){
                    tvDataTable.setItems(DbRequest.datalist);
                }
                else{
                    search();
                }

            }
            else{
                erroralert.setTitle("An Error Occurred!");
                erroralert.setHeaderText("There was no Data request yet. Please make request, to work with 'Search' function");
                erroralert.setContentText(null);
                erroralert.showAndWait();
            }

        });

        menuGetImages.setOnAction(event -> {
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
    }

    private void search(){
        DataFilter filter = new DataFilter(DbRequest.datalist, tfSearchValue.getText());
        SortedList<Data> sortedData = new SortedList<>(filter.search());
        sortedData.comparatorProperty().bind(tvDataTable.comparatorProperty());
        tvDataTable.setItems(sortedData);
        tvDataTable.refresh();
    }

    private String fileNameInitialization (String productionLineName){
        String filename = null;

        if(productionLineName.equals(AOI5KSERVER)){
            filename = "AOI_5K_Server_Report.xls";
        }
        else if(productionLineName.equals(SIEMENSLINE)){
            filename = "AOI_Siemens_Line_Report.xls";
        }
        else if(productionLineName.equals(FUJILONG)){
            filename = "AOI_FujiLong_Line_Report.xls";
        }
        else if(productionLineName.equals(VARROCLINES)){
            filename = "Varroc_Lines";
        }

        return filename;
    }
}