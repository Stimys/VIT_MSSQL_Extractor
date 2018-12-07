package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.GetImages;
import sample.ini.RepairStationConfigIni;
import sample.ini.RepStationXMLIni;
import sample.obj.RepairStation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for GetImagesForm.fxml
 */

public class GetImageController{

    public static final String AOI_DV1_NAME = "AOI_DV_1";
    public static final String AOI_DV2_NAME = "AOI_DV_2";
    public static final String AOI_DV3_NAME = "AOI_DV_3";
    //public static final String VITEC_1_NAME = "VITEC-1-REPAIR";
    public static final String VITEC_3_NAME = "VITEC-3-REPAIR";

    private String processStatus = "Processing";

    private DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private SimpleDateFormat fulldateformat = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    private LocalDate localDate;
    private Date date;

    private Alert warningalert = new Alert(Alert.AlertType.WARNING);
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    private boolean btnSaveIPClicked = false;

    private RepairStationConfigIni repairStation;


    //xml
    private List<RepairStation> repairStationList;

    //

    @FXML
    private DatePicker tpImageStartDate;

    @FXML
    private ComboBox<String> cbImageStartTime;

    @FXML
    private DatePicker tpImageEndDate;

    @FXML
    private ComboBox<String> cbImageEndTime;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnDownload;

    @FXML
    private ComboBox<String> cbMachineName;

    @FXML
    private TextField tfIP;

    @FXML
    private Label lbStatus;

    @FXML
    private ComboBox<String> cbDefectType;

    @FXML
    private Button btnSetIP;

    @FXML
    void initialize() {

        btnSetIP.setMinWidth(48);
        lbStatus.setVisible(false);
        tfIP.setEditable(false);
        tfIP.setStyle("-fx-control-inner-background:  #aeaeae");

        btnDownload.setDisable(true);
        btnSetIP.setDisable(true);

        repairStationList = new LinkedList<>();
        repairStationList = new RepStationXMLIni().parse(repairStationList);

        cbImageStartTime.getItems().addAll("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        cbImageEndTime.getItems().addAll("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        cbDefectType.getItems().addAll("False Alarm", "Acceptable", "Missing", "Position", "Rotation","Polarity");

        //cbMachineName.getItems().addAll(AOI_DV1_NAME,AOI_DV2_NAME,AOI_DV3_NAME,VITEC_3_NAME);

        for(RepairStation repStation : repairStationList)cbMachineName.getItems().addAll(repStation.getName());

        /**
         * Button 'Close' listener.
         * To close GetImageForm.xml file
         */
        btnClose.setOnAction(event -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });

        /**
         * Fill IP text field, after choosing machine name from the combo box
         */
        cbMachineName.setOnAction(event -> {
            for(int i = 0; i < repairStationList.size(); i++){
                if(cbMachineName.getValue().equals(repairStationList.get(i).getName())){
                    tfIP.setText(repairStationList.get(i).getIp());
                }
            }
        });


        /**
         * Button 'Download' listener.
         * To start process of downloading images from machine.
         */
        btnDownload.setOnAction(event -> {

            /**
             * Checking that all fields are filled.
             */
            if(cbImageStartTime.getValue()==null || cbImageEndTime.getValue()==null || cbMachineName.getValue()==null || tpImageStartDate.getValue() == null || tpImageEndDate.getValue() == null || cbDefectType.getValue()== null){
                warningalert.setTitle("Warning!");
                warningalert.setHeaderText("Please fill all fields!");
                warningalert.setContentText(null);
                warningalert.showAndWait();
            }
            else{
                try {
                    btnDownload.setDisable(true);
                    lbStatus.setVisible(true);
                    lbStatus.setText(processStatus);

                    /**
                     * Takes date from DatePicker and time from combo box and leads it to UNIX time
                     */
                    localDate = tpImageStartDate.getValue();
                    String startFileTime = dateformat.format(localDate) + " " + cbImageStartTime.getValue()+":00";
                    date = fulldateformat.parse(startFileTime);
                    Long startUnixTime = date.getTime() / 1000;
                    String parse = startUnixTime.toString() + "000";
                    startUnixTime = Long.parseLong(parse);

                    localDate = tpImageEndDate.getValue();
                    String endFileTime = localDate.format(dateformat) + " " + cbImageEndTime.getValue()+":00";
                    date = fulldateformat.parse(endFileTime);
                    Long endUnixTime = date.getTime() / 1000;
                    parse = endUnixTime.toString() + "000";
                    endUnixTime = Long.parseLong(parse);

                    /**
                     * Creates object of GetImages class.
                     * Send machine name, start and end UNIX time
                     */
                    GetImages getImages = new GetImages(cbMachineName.getValue(), startUnixTime, endUnixTime);
                    getImages.setDefectType(cbDefectType.getValue());
                    getImages.start();

                    getImages.join();

                    lbStatus.setText("Copied images: " + getImages.getValueOfCopiedFiles());
                    alert.setTitle("Complete!");
                    alert.setHeaderText("Copy has been finished!");
                    alert.setContentText("Copied images: " + getImages.getValueOfCopiedFiles());
                    alert.showAndWait();

                }
                catch (Exception e){
                    System.out.print(e);
                }
                btnDownload.setDisable(false);

            }
        });

        /**
         * Button 'Set IP' listener.
         * Makes IP text field editable.
         * Gets IP string from RepairStationConfigIni Class
         */
        btnSetIP.setOnAction(event -> {
            if(cbMachineName.getValue()!=null) {
                if (!btnSaveIPClicked) {
                    btnSetIP.setText("Save");
                    tfIP.setEditable(true);
                    tfIP.setStyle("-fx-control-inner-background: #FFFFFF");
                    btnSaveIPClicked = true;
                    cbMachineName.setDisable(true);
                } else {
                    tfIP.setEditable(false);
                    btnSetIP.setText("Set IP");
                    tfIP.setStyle("-fx-control-inner-background:  #aeaeae");

                    repairStation = new RepairStationConfigIni(cbMachineName.getValue());
                    repairStation.setIP(tfIP.getText());

                    cbMachineName.setDisable(false);
                    btnSaveIPClicked = false;
                }
            }
            else{
                warningalert.setTitle("Input Error!");
                warningalert.setHeaderText("Please select machine name!");
                warningalert.showAndWait();
            }
        });


    }
}