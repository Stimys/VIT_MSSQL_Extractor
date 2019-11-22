package sample;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import sample.obj.Data;

import java.util.function.Predicate;

/**
 * Class for filtering data in DataTable
 */
public class DataFilter {

    private Alert erroralert  = new Alert(Alert.AlertType.ERROR);
    private FilteredList<Data> filteredList;
    private String searchValue;

    public DataFilter(ObservableList data, String searchValue){
        this.filteredList = new FilteredList<>(data, e-> true);
        this.searchValue = searchValue;
    }

    public String getSearchValue(){
        return searchValue;
    }

    public FilteredList<Data> getFilteredList(){
        return filteredList;
    }


    public FilteredList search (){

        if(searchValue == null || searchValue.isEmpty()){
            erroralert.setTitle("Error!");
            erroralert.setHeaderText(null);
            erroralert.setContentText("Please fill the search text field!");
            erroralert.showAndWait();
        }
        else{
            filteredList.setPredicate((Predicate<? super Data>) data ->{

                if(data.getDate().contains(searchValue)){
                    return true;
                }
                else if(data.getErrorType().contains(searchValue)){
                    return true;
                }
                else if(data.getMachineName().contains(searchValue)){
                    return true;
                }
                else if(data.getModel().contains(searchValue)){
                    return true;
                }
                else if(data.getOperatorID().contains(searchValue)){
                    return true;
                }
                else if(data.getPanelCode().contains(searchValue)){
                    return true;
                }
                else if(data.getPartNumber().contains(searchValue)){
                    return true;
                }
                else if(data.getTopology().contains(searchValue)){
                    return true;
                }
                else if(data.getProductName().contains(searchValue)){
                    return true;
                }

                return false;
            });
        }
        return filteredList;
    }
}
