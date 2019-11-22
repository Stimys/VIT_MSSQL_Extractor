package sample.ini;
import javafx.scene.control.Alert;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import sample.obj.ProductLine;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LineXMLIni {
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private final String XML_LINE_FILE_NAME = System.getProperty("user.dir")+ File.separator+"Config"+File.separator+"initialization.xml";
    private File xmlInput = new File(XML_LINE_FILE_NAME);
    private SAXParserFactory factory;
    private SAXParser saxParser;
    //private ProductLine lineElem;
    private List<ProductLine> productLineList;

    private boolean bName;
    private boolean bIp;
    private boolean bDbname;
    private boolean bUser;
    private boolean bPassword;

    private String sName, sIp, sDbname, sUser, sPassword, sId;

    private Integer counter;

    public List<ProductLine> parse() {
        counter = 0;
        //lineElem = new ProductLine();
        productLineList = new LinkedList<>();
        bName = false;
        bIp = false;
        bDbname = false;
        bUser = false;
        bPassword = false;

        factory = SAXParserFactory.newInstance();
        try {
            saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("line")) {
                        sId = attributes.getValue("id");
                        //lineElem.setId(Integer.parseInt(sId));

                        productLineList.add(new ProductLine());
                        productLineList.get(counter).setId(Integer.parseInt(sId));
                    } else if (qName.equalsIgnoreCase("name")) {
                        bName = true;
                    } else if (qName.equalsIgnoreCase("ip")) {
                        bIp = true;
                    } else if (qName.equalsIgnoreCase("dbname")) {
                        bDbname = true;
                    } else if (qName.equalsIgnoreCase("user")) {
                        bUser = true;
                    } else if (qName.equalsIgnoreCase("password")) {
                        bPassword = true;
                    }

                }
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {

                    if (bName) {
                        sName = new String(ch, start, length);
                        bName = false;
                        productLineList.get(counter).setName(sName);
                    } else if (bIp) {
                       sIp = new String(ch, start, length);
                        bIp = false;
                        productLineList.get(counter).setIp(sIp);
                    } else if (bDbname) {
                        sDbname =  new String(ch, start, length);
                        bDbname = false;
                        productLineList.get(counter).setDataBaseName(sDbname);
                    } else if (bUser) {
                        sUser =  new String(ch, start, length);
                        bUser = false;
                        productLineList.get(counter).setUser(sUser);
                    } else if (bPassword) {
                        sPassword =  new String(ch, start, length);
                        bPassword = false;
                        productLineList.get(counter).setPassword(sPassword);
                        counter++;
                    }
                }
            };

            try {
                saxParser.parse(xmlInput, handler);

            } catch (IOException e) {
                errorAlert.setTitle("An Error Occurred!");
                errorAlert.setHeaderText("Parse initialization.xml Error!");
                errorAlert.setContentText(e.toString());
                errorAlert.showAndWait();
            }

        } catch (ParserConfigurationException e) {
            errorAlert.setTitle("An Error Occurred!");
            errorAlert.setHeaderText("Parse initialization.xml Error!");
            errorAlert.setContentText(e.toString());
            errorAlert.showAndWait();
        } catch (SAXException e) {
            errorAlert.setTitle("An Error Occurred!");
            errorAlert.setHeaderText("Parse initialization.xml Error!");
            errorAlert.setContentText(e.toString());
            errorAlert.showAndWait();
        }

        return productLineList;
    }
}
