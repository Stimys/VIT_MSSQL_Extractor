package sample.controllers;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import sample.DbRequest;

public class AddInformationController  {

    private double errorRate = 0;
    private double falseAlarmRate = 0;
    private double actualDefectsRate = 0;
    private double qualityOfInspection = 0;
    private double a,b;
    private int topFalseAlarm, index, counter;
    private  ArrayList<String> models = new ArrayList<>();
    private ArrayList<Integer> values = new ArrayList<>();
    private DecimalFormat dcformat = new DecimalFormat("#.####");

//    @FXML
//    private TextArea taTopFalseAlarms;

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

        for(int i = 0; i < DbRequest.topFalseAlarmValue.size(); i++){
            models.add(i, DbRequest.topFalseAlarmModels.get(i));
            values.add(i, DbRequest.topFalseAlarmValue.get(i));
        }

        lbTopOne.setVisible(false);
        lbTopTwo.setVisible(false);
        lbTopThree.setVisible(false);
        lbTopFour.setVisible(false);
        lbTopFive.setVisible(false);
//        taTopFalseAlarms.setVisible(false);
//        taTopFalseAlarms.setEditable(false);
//        taTopFalseAlarms.setBackground(java.awt.Color.GRAY);

        //Toolkit.getDefaultToolkit().getImage("@assests/logo_64px_blue.png");
        dcformat.setRoundingMode(RoundingMode.CEILING);
        a = DbRequest.errors;
        b = DbRequest.componentsValue;
        errorRate = (a/b)*100;
        a = DbRequest.falsealarmvalue;
        falseAlarmRate = (a/b)*100;
        a = Controller.actualErrors;
        actualDefectsRate = (a/b)*100;

        b = DbRequest.errors;
        qualityOfInspection = (a/b)*100;

        lbTestedComponents.setText(Long.toString(DbRequest.componentsValue));
        lbErrorRate.setText(dcformat.format(errorRate));
        lbFalseAlarmRate.setText(dcformat.format(falseAlarmRate));
        lbActualDefectsRate.setText(dcformat.format(actualDefectsRate));

        if (qualityOfInspection < 40){
            lbQualityOfInspection.setTextFill(Color.web("#8B0000"));
            lbQualityOfInspection.setText(dcformat.format(qualityOfInspection));
        }
        else {
            lbQualityOfInspection.setTextFill(Color.web("#00FF00"));
            lbQualityOfInspection.setText(dcformat.format(qualityOfInspection));
        }
        int n;

        if(values.size() > 4){
            n = 5;
        }
        else if (values.size() == 4){
            n=4;
        }
        else if (values.size() == 3){
            n=3;
        }
        else if (values.size() == 2){
            n=2;
        }
        else if (values.size() == 1){
            n=1;
        }
        else{
            n = 0;
            lbTopOne.setText("No False Alarm values in DataBase");
            lbTopOne.setVisible(true);
//            taTopFalseAlarms.setVisible(true);
//            taTopFalseAlarms.append("No False Alarm values in DataBase");
        }

        while (n>0) {
            topFalseAlarm = values.get(0);
            index = 0;

            for (int i = 0; i < values.size(); i++) {

                if (topFalseAlarm < values.get(i)) {
                    topFalseAlarm = values.get(i);
                    index = i;
                }
            }

            counter++;
            if (counter==1) {
                lbTopOne.setText("1) "+models.get(index) + " - " + topFalseAlarm);
                lbTopOne.setVisible(true);
//                taTopFalseAlarms.setVisible(true);
//                taTopFalseAlarms.append("1)"+models.get(index)+ " - "+ topFalseAlarm+"\n");
            }
            else if(counter==2){
                lbTopTwo.setText("2) "+models.get(index) + " - " + topFalseAlarm);
                lbTopTwo.setVisible(true);
//                taTopFalseAlarms.append("2)"+models.get(index)+ " - "+ topFalseAlarm+"\n");
            }
            else if(counter==3){
                lbTopThree.setText("3) "+models.get(index) + " - " + topFalseAlarm);
                lbTopThree.setVisible(true);
//                taTopFalseAlarms.append("3)"+models.get(index)+ " - "+ topFalseAlarm+"\n");
            }
            else if (counter==4){
                lbTopFour.setText("4) "+models.get(index) + " - " + topFalseAlarm);
                lbTopFour.setVisible(true);
//                taTopFalseAlarms.append("4)"+models.get(index)+ " - "+ topFalseAlarm+"\n");
            }
            else{
                lbTopFive.setText("5) "+models.get(index) + " - " + topFalseAlarm);
                lbTopFive.setVisible(true);
//                taTopFalseAlarms.append("5)"+models.get(index)+ " - "+ topFalseAlarm);
            }
            values.remove(index);
            models.remove(index);
            n--;
        }
        models.clear();
        values.clear();
    }
}
