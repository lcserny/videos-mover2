package net.cserny.videosmover.component;

import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.model.VideoType;

public class RadioButtonTableCell extends TableCell<VideoRow,VideoType> {

    @Override
    protected void updateItem(VideoType item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            HBox hb = new HBox(5);
            hb.setAlignment(Pos.CENTER);
            final ToggleGroup group = new ToggleGroup();

            for (VideoType enumElement : VideoType.values()) {
                RadioButton radioButton = new RadioButton(enumElement.toString());
                radioButton.setUserData(enumElement);
                radioButton.setToggleGroup(group);
                hb.getChildren().add(radioButton);
                if (enumElement.equals(item)) {
                    radioButton.setSelected(true);
                }
            }

            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                getTableView().edit(getIndex(), getTableColumn());
                RadioButtonTableCell.this.commitEdit((VideoType) newValue.getUserData());
            });

            setGraphic(hb);
        } else {
            setGraphic(null);
        }
    }
}
