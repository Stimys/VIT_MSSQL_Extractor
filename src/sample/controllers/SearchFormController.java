package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.DataFilter;
import sample.DbRequest;

public class SearchFormController {

    @FXML
    private TextField lbSearch;

    @FXML
    private Button btnFind;

    @FXML
    void initialize() {

        btnFind.setOnAction(event -> {
        });

    }
}
