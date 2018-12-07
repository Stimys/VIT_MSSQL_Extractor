package sample.ini;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.LinkedList;
import java.util.List;


//Class is not active anymore

public class LinesIni {
    private final String LINES_INI_FILE = System.getProperty("user.dir")+ File.separator+"config"+File.separator+"Lines.ini";
    private Alert errorAlert;
    //private InputStream input = null;
    private int value;
    private List<String> lines;

    public List initialization(){
        lines = new LinkedList<>();
        value = 0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(LINES_INI_FILE));
            String text;

            while((text = reader.readLine()) != null){
                if(!(text.substring(0,1).equals("#"))) {
                    lines.add(text);
                    value++;
                }
            }

        } catch (IOException e){
            errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("An Error Occurred!");
            errorAlert.setHeaderText("Reading Lines.ini Error!");
            errorAlert.setContentText(e.toString());
            errorAlert.showAndWait();
        }

        return lines;
    }

    public Integer getLinesValue(){
        return value;
    }

}
