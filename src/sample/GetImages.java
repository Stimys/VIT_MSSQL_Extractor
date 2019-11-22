package sample;

import sample.controllers.GetImageController;
import sample.ini.RepairStationConfigIni;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Class which downloads images from selected Repair Machine
 */

public class GetImages extends Thread {

    private Long startTime;
    private Long endTime;

//    public static  String AOI_DV1_IP = "\\\\10.100.127.171\\";
//    public static  String AOI_DV2_IP = "\\\\10.100.127.172\\";
//    public static  String AOI_DV3_IP = "\\\\10.100.127.173\\";
//    public static  String AOI_1_REPAIR_IP = "\\\\10.100.127.181\\";
//    public static  String AOI_3_REPAIR_IP = "\\\\10.100.127.183\\";

    private String currentMachineIP = null;

    private final String AOI_5K_DIR = "Users\\Paulius\\Desktop\\IMAGES\\";
    //private final String AOI_DV2_DIR = "Users\\Paulius\\Desktop\\IMAGES\\";
    //private final String AOI_DV3_DIR = "Users\\Paulius\\Desktop\\IMAGES\\";
    //private final String AOI_1_REPAIR_DIR = "d\\Images\\";
    private final String AOI_3_REPAIR_DIR = "data (d)\\Images\\";

    private final String AOI_5K_FALSE_ALARM_FOLDER = "False_Alrm";
    private final String AOI_5K_ACCEPTABLE_FOLDER = "acceptable";
    private final String AOI_5K_MISSING_FOLDER = "missing";
    private final String AOI_5K_POSITION_FOLDER = "position";
    private final String AOI_5K_ROTATION_FOLDER = "rotation";
    private final String AOI_5K_POLARITY_FOLDER = "polarity";

    private final String AOI_3_FALSE_ALARM_FOLDER = "False_Alarm";
    private final String AOI_3_ACCEPTABLE_FOLDER = "Acceptable";
    private final String AOI_3_MISSING_FOLDER = "Missing";
    private final String AOI_3_POSITION_FOLDER = "Position";
    private final String AOI_3_ROTATION_FOLDER = "Rotation";
    private final String AOI_3_POLARITY_FOLDER = "Polarity";

    private File folder;
    private File dir;
    private File[] listOfFiles;
    private File[] listOfFilesInDir;

    private String localpath = System.getProperty("user.dir");
    private String targetDir = localpath + File.separator +"Images";

    private Path destFolder= Paths.get(targetDir);
    private Path destFile ;

    private Integer copiedFileCounter = 0;

    private String sourceDir;

    private RepairStationConfigIni repairStation;

    public GetImages(String sourceDir, Long startTime, Long endTime){
        this.sourceDir = sourceDir;
        this.startTime = startTime;
        this.endTime =  endTime;
    }

    public  String getSourceDir(){
        return sourceDir;
    }

    public Integer getValueOfCopiedFiles(){
        return copiedFileCounter;
    }

    /**
     * Module which initializes from where program should download images.
     * @param defectType
     */

    public void setDefectType(String defectType){

        repairStation = new RepairStationConfigIni(sourceDir);
        String currentIP = "\\\\"+repairStation.getIP()+"\\";

        if(sourceDir.equals(GetImageController.AOI_DV1_NAME)){
            folder = new File(currentIP+AOI_5K_DIR+ defectTypeFolderSelect(defectType));
        }
        else if(sourceDir.equals(GetImageController.AOI_DV2_NAME)){
            folder = new File(currentIP+AOI_5K_DIR+defectTypeFolderSelect(defectType));
        }
        else if(sourceDir.equals(GetImageController.AOI_DV3_NAME)){
            folder = new File(currentIP+AOI_5K_DIR+defectTypeFolderSelect(defectType));
        }
//        else if(sourceDir.equals(GetImageController.VITEC_1_NAME)){
//            folder = new File(AOI_1_REPAIR_IP + AOI_1_REPAIR_DIR+defectTypeFolderSelect(defectType));
//
//        }
        else if(sourceDir.equals(GetImageController.VITEC_3_NAME)){
            folder = new File(currentIP + AOI_3_REPAIR_DIR+defectTypeFolderSelect(defectType));
        }
    }

    /**
     * Initializing download folder by defect type
     * @param folderDefectType
     * @return String folderName;
     */
    private String defectTypeFolderSelect(String folderDefectType){
        String folderName = null;


        switch (folderDefectType){
            case "False Alarm":
                if(sourceDir.equals(GetImageController.AOI_DV1_NAME) || sourceDir.equals(GetImageController.AOI_DV2_NAME) || sourceDir.equals(GetImageController.AOI_DV3_NAME)){
                    folderName = AOI_5K_FALSE_ALARM_FOLDER;
                }
                else{
                    folderName = AOI_3_FALSE_ALARM_FOLDER;
                }

                break;
            case "Acceptable":

                if(sourceDir.equals(GetImageController.AOI_DV1_NAME) || sourceDir.equals(GetImageController.AOI_DV2_NAME) || sourceDir.equals(GetImageController.AOI_DV3_NAME)){
                    folderName = AOI_5K_ACCEPTABLE_FOLDER;
                }
                else{
                    folderName = AOI_3_ACCEPTABLE_FOLDER;
                }

                break;
            case "Missing":

                if(sourceDir.equals(GetImageController.AOI_DV1_NAME) || sourceDir.equals(GetImageController.AOI_DV2_NAME) || sourceDir.equals(GetImageController.AOI_DV3_NAME)){
                    folderName = AOI_5K_MISSING_FOLDER;
                }
                else{
                    folderName = AOI_3_MISSING_FOLDER;
                }
                break;
            case "Position":

                if(sourceDir.equals(GetImageController.AOI_DV1_NAME) || sourceDir.equals(GetImageController.AOI_DV2_NAME) || sourceDir.equals(GetImageController.AOI_DV3_NAME)){
                    folderName = AOI_5K_POSITION_FOLDER;
                }
                else{
                    folderName = AOI_3_POSITION_FOLDER;
                }
                break;
            case "Rotation":

                if(sourceDir.equals(GetImageController.AOI_DV1_NAME) || sourceDir.equals(GetImageController.AOI_DV2_NAME) || sourceDir.equals(GetImageController.AOI_DV3_NAME)){
                    folderName = AOI_5K_ROTATION_FOLDER;
                }
                else{
                    folderName = AOI_3_ROTATION_FOLDER;
                }
                break;
            case "Polarity":

                if(sourceDir.equals(GetImageController.AOI_DV1_NAME) || sourceDir.equals(GetImageController.AOI_DV2_NAME) || sourceDir.equals(GetImageController.AOI_DV3_NAME)){
                    folderName = AOI_5K_POLARITY_FOLDER;
                }
                else{
                    folderName = AOI_3_POLARITY_FOLDER;
                }
                break;

                default:
                    break;

        }

        return folderName;
    }

    /**
     *Launches copy process
     */
    public void run() {

        listOfFiles = folder.listFiles();
        if(listOfFiles != null){
            for(File file : listOfFiles){
                if(file.isDirectory()) {
                    if (file.lastModified() >= startTime && file.lastModified() <= endTime) {
                        dir = new File(folder+"\\"+file.getName());
                        try {
                            Files.copy(file.toPath(), destFolder.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                        }
                        catch (IOException e){
                            System.out.print(e);
                        }
                        listOfFilesInDir = dir.listFiles();
                        for(File fileInDir : listOfFilesInDir){
                            destFile = Paths.get(targetDir+"\\"+file.getName());
                            try {
                                Files.copy(fileInDir.toPath(), destFile.resolve(fileInDir.getName()), StandardCopyOption.REPLACE_EXISTING);

                            }
                            catch(IOException e){
                                System.out.print(e);
                            }
                            //System.out.println(fileInDir.getName());
                            copiedFileCounter++;
                        }
                    }
                }
            }
        }
    }
}
