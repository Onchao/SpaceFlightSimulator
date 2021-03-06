package utility;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class CustomWidgets {
    public static Button customButton(String name){
        Button button = new Button(name);
        String iddleButton = "-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 10;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.3);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 10;"
                + "-fx-font-size: 24px;" + "-fx-font-weight: bold;";
        String hoveredButton = "-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 10;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.5);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 10;"
                + "-fx-font-size: 24px;" + "-fx-font-weight: bold;";

        button.setStyle(iddleButton);
        button.setOnMouseEntered(e -> button.setStyle(hoveredButton));
        button.setOnMouseExited(e -> button.setStyle(iddleButton));
        button.setTextFill(Color.rgb(0,255,255,0.8));

        return button;
    }

    public static Button customSmallButton(String name){
        Button button = new Button(name);
        String iddleButton = "-fx-padding: 3;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.3);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 5;"
                + "-fx-font-size: 16px;" + "-fx-font-weight: bold;";
        String hoveredButton = "-fx-padding: 3;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.5);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 5;"
                + "-fx-font-size: 16px;" + "-fx-font-weight: bold;";

        button.setStyle(iddleButton);
        button.setOnMouseEntered(e -> button.setStyle(hoveredButton));
        button.setOnMouseExited(e -> button.setStyle(iddleButton));
        button.setTextFill(Color.rgb(0,255,255,0.8));

        return button;
    }


    public static TextField customTextField (String content) {
        TextField textField = new TextField(content);

        String style = "-fx-padding: 7;"
                + "-fx-background-color: rgb(8, 8, 32);"
                + "-fx-text-fill: rgb(0, 255, 255);"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 1;"
                + "-fx-border-radius: 4;"
                + "-fx-border-color: rgba(0, 255, 255, 0.8);";

        textField.setMaxWidth(50);
        textField.setStyle(style);

        return textField;
    }

    public static Label customLabel (String text, int fontSize) {
        Label label = new Label(text);

        String style = "-fx-text-fill: rgb(0, 255, 255);"
                + "-fx-font-size: " + fontSize + ";";

        label.setStyle(style);
        return label;
    }
    public static Label customBoldLabel (String text, int fontSize) {
        Label label = new Label(text);

        String style = "-fx-text-fill: rgb(0, 255, 255);"
                + "-fx-font-size: " + fontSize + ";" + "-fx-font-weight: bold; ";

        label.setStyle(style);
        return label;
    }

    public static <T> ListView<T> customListView () {
        ListView<T> listView = new ListView<>();

        String style = ".list-cell {" +
                "-fx-background-color: rgb(8, 8, 32);" +
                "}";

        listView.setStyle(style);

        return listView;
    }

    public static ProgressBar customProgressBar (String color) {
        ProgressBar progressBar = new ProgressBar();

        String style = "-fx-accent: " + color + ";" +
                "-fx-background: rgba(0, 255, 255, 0.1);" +
                "-fx-control-inner-background: rgba(0, 255, 255, 0.1);" +
                "-fx-border-style: solid inside;" +
                "-fx-border-radius: 5px;" +
                "-fx-border-width: 2px;" +
                "-fx-border-color: " + color + ";";

        progressBar.setStyle(style);

        return progressBar;
    }
}
