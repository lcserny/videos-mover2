package net.cserny.videosmover.component;

import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.util.EnumSet;

public class RadioButtonTableCell<S,T extends Enum<T>> extends TableCell<S,T> {

    private EnumSet<T> enumeration;

    public RadioButtonTableCell(EnumSet<T> enumeration) {
        this.enumeration = enumeration;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            HBox hb = new HBox(5);
            hb.setAlignment(Pos.CENTER);
            final ToggleGroup group = new ToggleGroup();

            for (Enum<T> enumElement : enumeration) {
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
                RadioButtonTableCell.this.commitEdit((T) newValue.getUserData());
            });

            setGraphic(hb);
        }
    }
}
