package sample;

import javafx.collections.ObservableList;
import sample.obj.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Statistics {

    private int defectValue;
    private int falseAlarmValue;
    private int warningValue;
    private long testedComponents;

    private double errorRate;
    private double falseAlarmRate;
    private double actualDefectsRate;
    private double inspectionRate;

    private Map<String, Integer> falseAlarmsByComp = new HashMap<>();
    private Map<String, Integer> warningByComp = new HashMap<>();
    private ObservableList<Data> datalist;

    public Statistics (ObservableList<Data> data){
        this.datalist = data;
        defectValue = data.size();
        testedComponents = getTestedCompValue();
        calculatingFalseAndWarning();
        calculatingRates();

    }

    public int getDefectsValue (){
        return  defectValue;
    }

    //Getting False Alarms and Warning value from data list
    private void calculatingFalseAndWarning (){
        falseAlarmValue = 0;
        warningValue = 0;
        for(Data dataElem : datalist){
            if(dataElem.getErrorType().equals("FALSE ALARM") || dataElem.getErrorType().equals("False Alarm")){
                falseAlarmValue ++;
                fillListByDefectType(dataElem, falseAlarmsByComp);
            } else if (dataElem.getErrorType().equals("ACCEPTABLE") || dataElem.getErrorType().equals("Acceptable")) {
                warningValue ++;
                fillListByDefectType(dataElem, warningByComp);
            }
        }
    }

    //Getting Value of tested components
    private long getTestedCompValue (){
        long value = 0;
        for(Data dataElem : datalist){
            value += Long.parseLong(dataElem.getNumOfTestedComponents());
        }
        return value;
    }

    public int getFalseAlarms (){
        return falseAlarmValue;
    }

    public long getTestedComp() {
        return testedComponents;
    }

    public int getActualDefectsValue (){
        return defectValue - falseAlarmValue - warningValue;
    }

    public Map<String, Integer> getFalseAlarmsByComp (){ return falseAlarmsByComp; }

    public Map<String, Integer> getWarningListByComp () { return warningByComp;}

    public int getWarningValue () {return warningValue;}

    private void fillListByDefectType(Data dataElement, Map<String, Integer> list){
        Integer value;
        if(list.containsKey(dataElement.getModel())){
            value = list.get(dataElement.getModel());
            list.put(dataElement.getModel(), value + 1);
        } else {
            list.put(dataElement.getModel(), 1);
        }
    }

    //Sorting HashMap list of falseAlarmsByComp
    public Map<String, Integer> getSortedFalseAlarmList (){
        return falseAlarmsByComp
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    private void calculatingRates (){
        double a = defectValue;
        double b = testedComponents;

        errorRate = (a/b)*100;
        a = falseAlarmValue;
        falseAlarmRate = (a/b)*100;
        a = getActualDefectsValue();
        actualDefectsRate = (a/b) * 100;
        b = defectValue;
        inspectionRate = (a/b)*100;
    }

    public double getErrorRate (){ return errorRate; }

    public double getFalseAlarmRate(){ return falseAlarmRate; }

    public double getActualDefectsRate(){
        return actualDefectsRate;
    }

    public double getInspectionRate() {return inspectionRate;}
}
