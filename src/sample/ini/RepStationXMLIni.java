package sample.ini;

import javafx.scene.control.Alert;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import sample.obj.RepairStation;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RepStationXMLIni {
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private final String XML_INI_FILE_PATH = System.getProperty("user.dir")+ File.separator+"Config"+File.separator+"RepairStationIni.xml";
    private SAXParserFactory factory;
    private SAXParser saxParser;

    private boolean bName, bIp;
    private String sId,sName, sIp;
    private Integer counter;

    public List<RepairStation> parse(List<RepairStation> repairStationList){
        counter = 0;
        bName = false;
        bIp = false;

        factory = SAXParserFactory.newInstance();

        try{
            saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler(){
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("repair_machine")) {
                        sId = attributes.getValue("id");
                        repairStationList.add(new RepairStation());
                        repairStationList.get(counter).setId(Integer.parseInt(sId));
                    } else if (qName.equalsIgnoreCase("name")) {
                        bName = true;
                    } else if (qName.equalsIgnoreCase("ip")) {
                        bIp = true;
                    }
                }

                @Override
                public void characters(char ch[], int start, int length) throws SAXException {

                    if (bName) {
                        sName = new String(ch, start, length);
                        bName = false;
                        repairStationList.get(counter).setName(sName);
                    } else if (bIp) {
                        sIp = new String(ch, start, length);
                        bIp = false;
                        repairStationList.get(counter).setIp(sIp);
                        counter++;
                    }
                }

            };

            try {
                saxParser.parse(XML_INI_FILE_PATH, handler);

            } catch (IOException e) {
                errorAlert.setTitle("An Error Occurred!");
                errorAlert.setHeaderText("Parse RepairStationIni.xml Error!");
                errorAlert.setContentText(e.toString());
                errorAlert.showAndWait();
            }

        }catch (ParserConfigurationException e){
            errorAlert.setTitle("An Error Occurred!");
            errorAlert.setHeaderText("Parse RepairStationIni.xml Error!");
            errorAlert.setContentText(e.toString());
            errorAlert.showAndWait();

        } catch (SAXException e){
            errorAlert.setTitle("An Error Occurred!");
            errorAlert.setHeaderText("Parse RepairStationIni.xml Error!");
            errorAlert.setContentText(e.toString());
            errorAlert.showAndWait();
        }


        return repairStationList;
    }
}
