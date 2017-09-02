package net.cserny.videosMover2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import net.cserny.videosMover2.dto.VideoRow;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by leonardo on 02.09.2017.
 */
public class MainController implements Initializable
{
    @FXML
    private Button scanButton;
    @FXML
    private TableView<VideoRow> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // config table and stuff
    }

    public void loadTableView(ActionEvent event) {
        // use scaning service and populate tableview
    }
}
