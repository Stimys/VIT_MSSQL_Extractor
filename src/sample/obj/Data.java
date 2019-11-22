package sample.obj;

public class Data {
    private String productName;
    private String topology;
    private String model;
    private String partNumber;
    private String errorType;
    private String machineName;
    private String panelCode;
    private String date;
    private String operatorID;
    private String numOfTestedComponents;

    public Data (String productName,String topology, String model, String partNumber, String errorType, String machineName, String panelCode, String date, String operatorID, String numOfTestedComponents){
        this.productName = productName;
        this.topology = topology;
        this.model = model;
        this.partNumber = partNumber;
        this.errorType = errorType;
        this.machineName = machineName;
        this.panelCode = panelCode;
        this.date = date;
        this.operatorID = operatorID;
        this.numOfTestedComponents = numOfTestedComponents;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public String getTopology(){
        return topology;
    }

    public void setTopology(String topology){
        this.topology = topology;
    }

    public String getModel(){
        return model;
    }

    public void setModel (String model){
        this.model = model;
    }

    public String getPartNumber(){
        return partNumber;
    }

    public void setPartNumber (String partNumber){
        this.partNumber = partNumber;
    }

    public String getErrorType (){
        return errorType;
    }

    public void setErrorType (String errorType){
        this.errorType = errorType;
    }

    public String getMachineName (){
        return machineName;
    }

    public void setMachineName (String machineName){
        this.machineName = machineName;
    }

    public String getPanelCode (){
        return panelCode;
    }

    public void setPanelCode (String panelCode){
        this.panelCode = panelCode;
    }

    public String getDate (){
        return date;
    }

    public void setDate (String date){
        this.date = date;
    }

    public String getOperatorID (){
        return operatorID;
    }

    public void setOperatorID (String operatorID){
        this.operatorID = operatorID;
    }

    public String getNumOfTestedComponents (){
        return numOfTestedComponents;
    }

    public void setNumOfTestedComponents (String numOfTestedComponents){
        this.numOfTestedComponents = numOfTestedComponents;
    }
}
