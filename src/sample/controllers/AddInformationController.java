package sample.controllers;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import sample.DbRequest;

/**
 * Class which calculates and shows additional information in separate window
 */
public class AddInformationController  {

    private double errorRate = 0;
    private double falseAlarmRate = 0;
    private double actualDefectsRate = 0;
    private double qualityOfInspection = 0;
    private int topFalseAlarm, index, counter;
    private  ArrayList<String> models = new ArrayList<>();
    private ArrayList<Integer> values = new ArrayList<>();
    private final DecimalFormat dcformat = new DecimalFormat("#.####");

    @FXML
    private TextArea taTopFalseAlarmsArea;

    @FXML
    private Label lbTestedComponents;

    @FXML
    private Label lbErrorRate;

    @FXML
    private Label lbFalseAlarmRate;

    @FXML
    private Label lbActualDefectsRate;

    @FXML
    private Label lbQualityOfInspection;

    @FXML
    private Label lbTopOne;

    @FXML
    private Label lbTopTwo;

    @FXML
    private Label lbTopThree;

    @FXML
    private Label lbTopFour;

    @FXML
    private Label lbTopFive;

    @FXML
    void initialize() {

        counter = 0;
        topFalseAlarm = 0;
        index = 0;

        /**
         * Сopying arrays
         */
        for(int i = 0; i < DbRequest.topFalseAlarmValue.size(); i++){
            models.add(i, DbRequest.topFalseAlarmModels.get(i));
            values.add(i, DbRequest.topFalseAlarmValue.get(i));
        }

        lbTopOne.setVisible(false);
        lbTopTwo.setVisible(false);
        lbTopThree.setVisible(false);
        lbTopFour.setVisible(false);
        lbTopFive.setVisible(false);
        taTopFalseAlarmsArea.setVisible(false);
        taTopFalseAlarmsArea.setEditable(false);


        dcformat.setRoundingMode(RoundingMode.CEILING);


        calculatingVariables();


        /**
         * Set rounding mode
         */
        lbTestedComponents.setText(Long.toString(DbRequest.componentsValue));
        lbErrorRate.setText(dcformat.format(errorRate));
        lbFalseAlarmRate.setText(dcformat.format(falseAlarmRate));
        lbActualDefectsRate.setText(dcformat.format(actualDefectsRate));

        /**
         * Set color text to qualityofinspection. If less than 40% - RED, else GREEN
         */
        if (qualityOfInspection < 40){
            lbQualityOfInspection.setTextFill(Color.web("#8B0000"));
            lbQualityOfInspection.setText(dcformat.format(qualityOfInspection));
        }
        else {
            lbQualityOfInspection.setTextFill(Color.web("#00FF00"));
            lbQualityOfInspection.setText(dcformat.format(qualityOfInspection));
        }
        int n;

        /**
         * Set 'n' depends how much variables at the array
         */
        if(values.size() > 4){
            n = 5;
        }
        else if (values.size() == 4){
            n= values.size();
        }
        else if (values.size() == 3){
            n= values.size();
        }
        else if (values.size() == 2){
            n= values.size();
        }
        else if (values.size() == 1){
            n= values.size();
        }
        else{
            n = 0;

            taTopFalseAlarmsArea.setVisible(true);
            taTopFalseAlarmsArea.appendText("No False Alarm values in DataBase");
        }

        while (n>0) {
            topFalseAlarm = values.get(0);
            index = 0;

            /**
             * Adding text about TopFalseAlarms in TextArea
             */
            for (int i = 0; i < values.size(); i++) {

                if (topFalseAlarm < values.get(i)) {
                    topFalseAlarm = values.get(i);
                    index = i;
                }
            }

            counter++;
            if (counter==1) {
                taTopFalseAlarmsArea.setVisible(true);
                taTopFalseAlarmsArea.appendText("1) "+models.get(index)+ " - "+ topFalseAlarm+"\n");
            }
            else if(counter==2){
                taTopFalseAlarmsArea.appendText("2) "+models.get(index)+ " - "+ topFalseAlarm+"\n");
            }
            else if(counter==3){
                taTopFalseAlarmsArea.appendText("3) "+models.get(index)+ " - "+ topFalseAlarm+"\n");
            }
            else if (counter==4){
                taTopFalseAlarmsArea.appendText("4) "+models.get(index)+ " - "+ topFalseAlarm+"\n");
            }
            else{
                taTopFalseAlarmsArea.appendText("5) "+models.get(index)+ " - "+ topFalseAlarm);
            }
            values.remove(index);
            models.remove(index);
            n--;
        }
        /**
         * Cleaning arrays to the next calculations
         */
        models.clear();
        values.clear();
    }

    /**
     * Function which makes all calculations
     */
    private void calculatingVariables(){
        double a = DbRequest.errors;
        double b = DbRequest.componentsValue;
        this.errorRate = (a/b)*100;
        a = DbRequest.falseAlarmValue;
        this.falseAlarmRate = (a/b)*100;
        a = Controller.actualErrors;
        this.actualDefectsRate = (a/b)*100;
        b = DbRequest.errors;
        this.qualityOfInspection = (a/b)*100;
    }
}
