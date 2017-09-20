package net.cserny.videosmover.learn;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;


public class TestLearnControlsFx extends Application {
    private Pane root = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        addCustomTable();

        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void addCustomTable() {
        ObservableList<SomeRowType> list = FXCollections.observableArrayList(
                new SomeRowType("1", "2"),
                new SomeRowType("3", "4")
        );

        TableView<SomeRowType> table = new TableView<>();
        table.setPrefWidth(300);
        table.setPrefHeight(150);

        TableColumn<SomeRowType, String> oneColumn = new TableColumn<>("One");
        oneColumn.setPrefWidth(50);
        oneColumn.setCellValueFactory(new PropertyValueFactory<>("one"));

        // TODO: try to do twoColumn and actionCol as one > a CustomTextField one?

        TableColumn<SomeRowType, String> twoColumn = new TableColumn<>("Two");
        twoColumn.setPrefWidth(50);
        twoColumn.setCellValueFactory(new PropertyValueFactory<>("two"));

        Image image = new Image(getClass().getResourceAsStream("/images/application.png"));
        Image altImage = new Image(getClass().getResourceAsStream("/images/scan-button.png"));

        TableColumn<SomeRowType, String> actionCol = new TableColumn<>();
        actionCol.setPrefWidth(198);
        actionCol.setStyle( "-fx-alignment: CENTER-RIGHT;");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        actionCol.setCellFactory(param -> new ActionColCell(image, altImage));

        table.setItems(list);
        table.getColumns().add(oneColumn);
        table.getColumns().add(twoColumn);
        table.getColumns().add(actionCol);

        root.getChildren().add(table);
    }

    public static class SomeRowType {
        private final SimpleStringProperty one;
        private final SimpleStringProperty two;

        public SomeRowType(String one, String two) {
            this.one = new SimpleStringProperty(one);
            this.two = new SimpleStringProperty(two);
        }

        public String getOne() {
            return one.get();
        }

        public void setOne(String one) {
            this.one.set(one);
        }

        public String getTwo() {
            return two.get();
        }

        public void setTwo(String two) {
            this.two.set(two);
        }
    }

    private class ActionColCell extends TableCell<SomeRowType, String> {
        private final Button button;
        private final Image mainImage;
        private final Image altImage;

        public ActionColCell(Image mainImage, Image altImage) {
            this.mainImage = mainImage;
            this.altImage = altImage;

            ImageView imageView = new ImageView(mainImage);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);

            this.button = new Button("", imageView);
            this.button.setStyle("-fx-text-fill: white; -fx-background-color: red; -fx-cursor: hand;");
            Tooltip tooltip = new Tooltip("Some tooltip text");
            this.button.setTooltip(tooltip);
        }

        public Button getButton() {
            return button;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            VBox vBox = new VBox();
            Button one = new Button("one");
            one.setPrefWidth(100);
            Button two = new Button("two");
            two.setPrefWidth(100);
            Button three = new Button("three");
            three.setPrefWidth(100);
            Button four = new Button("four");
            four.setPrefWidth(100);
            vBox.getChildren().addAll(one, two, three, four);

            PopOver popOver = new PopOver(vBox);
            popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
            popOver.setOnHidden(event -> ((ImageView) button.getGraphic()).setImage(mainImage));

            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                button.setOnAction(event -> {
                    if (popOver.isShowing()) {
                        popOver.hide();
                    } else {
                        popOver.show(button);
                        ((ImageView) button.getGraphic()).setImage(altImage);
                    }
                });
                setGraphic(button);
                setText(null);
            }
        }
    }
}
