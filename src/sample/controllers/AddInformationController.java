package sample.controllers;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

/**
 * Class which calculates and shows additional information in additional window
 */
public class AddInformationController  {

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
    void initialize() {

        Map<String, Integer> topFalseAlarms = Controller.topFalseAlarmsByComp;
        fillingTextAreaOfTopFalseAlarms(topFalseAlarms);

        /**
         * Set rounding mode
         */
        dcformat.setRoundingMode(RoundingMode.CEILING);

        lbTestedComponents.setText(String.valueOf(Controller.testedComponents));
        lbErrorRate.setText(dcformat.format(Controller.errorRate));
        lbFalseAlarmRate.setText(dcformat.format(Controller.falseAlarmRate));
        lbActualDefectsRate.setText(dcformat.format(Controller.actualDefectsRate));

        /**
         * Set color text to quality of inspection. If less than 40% - RED, else GREEN
         */
        if (Controller.inspectionRate < 40){
            lbQualityOfInspection.setTextFill(Color.web("#8B0000"));
            lbQualityOfInspection.setText(dcformat.format(Controller.inspectionRate));
        }
        else {
            lbQualityOfInspection.setTextFill(Color.web("#00FF00"));
            lbQualityOfInspection.setText(dcformat.format(Controller.inspectionRate));
        }
    }

    private void fillingTextAreaOfTopFalseAlarms(Map<String, Integer> falseAlarms){
        int b = Math.min(falseAlarms.size(), 5);
        Set<String> keys = falseAlarms.keySet();
        String[] keysArray = keys.toArray(new String[0]);

        for(int i =0; i < b; i++){
            taTopFalseAlarmsArea.appendText(i+1 +") "+ keysArray[i] + " - " + falseAlarms.get(keysArray[i])+"\n");
        }
    }
}
