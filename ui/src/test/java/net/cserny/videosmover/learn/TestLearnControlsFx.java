package net.cserny.videosmover.learn;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;


public class TestLearnControlsFx extends Application {
    private Pane root = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        addCustomTable();
//        addImageFromUrl();

        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void addImageFromUrl() {
        String imageSource = "http://image.tmdb.org/t/p/w185//adw6Lq9FiC9zjYEpOqfq03ituwp.jpg";
        ImageView imageView = new ImageView(new Image(imageSource));

        root.getChildren().add(imageView);
    }

    private void addCustomTable() {
        TableView<SomeRowType> table = new TableView<>();
        table.setPrefWidth(300);
        table.setPrefHeight(150);

        TableColumn<SomeRowType, String> oneColumn = new TableColumn<>("One");
        oneColumn.setPrefWidth(50);
        oneColumn.setCellValueFactory(new PropertyValueFactory<>("one"));
        oneColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<SomeRowType, String> twoColumn = new TableColumn<>("Two");
        twoColumn.setPrefWidth(248);
        twoColumn.setCellValueFactory(new PropertyValueFactory<>("two"));
        twoColumn.setCellFactory(param -> new CustomTextFieldCell());

        SomeRowType row1 = new SomeRowType("1", "2");
//        row1.twoProperty().addListener((observable, oldValue, newValue) -> {
//            TableColumn<SomeRowType, String> column = (TableColumn<SomeRowType, String>) table.getColumns().get(1);
//            CustomTextFieldCell customCell = (CustomTextFieldCell) column.getCellFactory().call(column);
//            customCell.changeColors();
//        });
        SomeRowType row2 = new SomeRowType("3", "4");
//        row2.twoProperty().addListener((observable, oldValue, newValue) -> {
//            TableColumn<SomeRowType, String> column = (TableColumn<SomeRowType, String>) table.getColumns().get(1);
//            CustomTextFieldCell customCell = (CustomTextFieldCell) column.getCellFactory().call(column);
//            customCell.changeColors();
//        });
        table.setItems(FXCollections.observableArrayList(row1, row2));
        table.getColumns().add(oneColumn);
        table.getColumns().add(twoColumn);

        Button button = new Button("Change first button color");
        button.setOnAction(event -> {
            table.getItems().get(1).setTwo("hello");
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, button);

        root.getChildren().add(vBox);
    }

    public static class SomeRowType {
        private SimpleStringProperty one;
        private SimpleStringProperty two;

        public SomeRowType(String one, String two) {
            this.one = new SimpleStringProperty(one);
            this.two = new SimpleStringProperty(two);
        }

        public String getOne() {
            return one.get();
        }

        public SimpleStringProperty oneProperty() {
            return one;
        }

        public void setOne(String one) {
            this.one.set(one);
        }

        public String getTwo() {
            return two.get();
        }

        public SimpleStringProperty twoProperty() {
            return two;
        }

        public void setTwo(String two) {
            this.two.set(two);
        }

        @Override
        public String toString() {
            return "SomeRowType{" +
                    "one=" + one +
                    ", two=" + two +
                    '}';
        }
    }

    private class CustomTextFieldCell extends TableCell<SomeRowType, String> {
        private final CustomTextField customTextField;
        private final Button button;
        private final Image mainImage;
        private final Image altImage;
        private final PopOver popOver;
        private StringProperty boundProperty = null;

        public CustomTextFieldCell() {
            this.mainImage = new Image(getClass().getResourceAsStream("/images/application.png"));
            this.altImage = new Image(getClass().getResourceAsStream("/images/scan-button.png"));
            this.button = initButton();
            this.popOver = initPopOver();
            this.customTextField = initCustomTextField();

            setGraphic(this.customTextField);
        }

        public void changeColors() {
            if (button.getStyle().equals("-fx-text-fill: white; -fx-background-color: red; -fx-cursor: hand;")) {
                button.setStyle("-fx-text-fill: white; -fx-background-color: blue; -fx-cursor: hand;");
            } else {
                button.setStyle("-fx-text-fill: white; -fx-background-color: red; -fx-cursor: hand;");
            }

            // TODO: de ce intra twice aici pt primul rand???
        }

        private CustomTextField initCustomTextField() {
            CustomTextField customTextField = new CustomTextField();
            customTextField.setRight(button);
            // this is the important part
            customTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals("") && !oldValue.equals(newValue)) {
                    changeColors();
                }
            });
            return customTextField;
        }

        private PopOver initPopOver() {
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

            return popOver;
        }

        private Button initButton() {
            ImageView imageView = new ImageView(mainImage);
            imageView.setFitHeight(15);
            imageView.setFitWidth(15);

            Button button = new Button("", imageView);
            button.setStyle("-fx-text-fill: white; -fx-background-color: red; -fx-cursor: hand;");
            button.setTooltip(new Tooltip("Some tooltip text"));
            button.setOnAction(event -> {
                if (popOver.isShowing()) {
                    popOver.hide();
                } else {
                    popOver.show(button);
                    ((ImageView) button.getGraphic()).setImage(altImage);
                }
            });

            return button;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            ContentDisplay contentDisplay = ContentDisplay.TEXT_ONLY;
            if (!empty) {
                contentDisplay = ContentDisplay.GRAPHIC_ONLY;
                if (boundProperty == null) {
                    boundProperty = (SimpleStringProperty) getTableColumn().getCellObservableValue(getIndex());
                    customTextField.textProperty().bindBidirectional(boundProperty);
                }
            }
            setContentDisplay(contentDisplay);
        }
    }
}
