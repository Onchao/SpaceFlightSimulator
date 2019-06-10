package utility;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class ControlBox {
    public static VBox createControlBox(){
        VBox box = new VBox();
        box.getChildren().addAll(
                nameBox("  Controls:"),
                singleControlBox("A","turn left"),
                singleControlBox("D","turn right"),
                singleControlBox("Z","full throttle"),
                singleControlBox("X","cut throttle"),
                singleControlBox("SHIFT  ,  W","increase throttle"),
                singleControlBox("CTRL  ,  S","decrease throttle"),
                singleControlBox("SPACE", "activate next stage"),
                intervalBox(),
                singleControlBox("TAB","cycle origin"),
                singleControlBox("M","cycle zoom"),
                singleControlBox("MOUSE SCROLL","zoom in / zoom out"),
                intervalBox(),
                singleControlBox("+","time warp increase"),
                singleControlBox("-","time warp decrease"),
                singleControlBox("ESC","pause"));
        return box;
    }

    private static HBox singleControlBox(String key, String desc){
        HBox box = new HBox();
        Label keyL = new Label(key);
        Label descL = new Label(desc);

        box.setStyle("-fx-padding: 7;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 4;"
                + "-fx-border-radius: 5;" + "-fx-border-color: rgba(0, 255, 255, 0.8);"
                + "-fx-background-color: rgba(0, 255, 255, 0.2);"
                + "-fx-background-radius: 5;" + "-fx-background-insets: 4;"
        );

        keyL.setTextFill(Color.rgb(0,255,255,0.8));
        descL.setTextFill(Color.rgb(0,255,255,0.8));

        box.setMinWidth(400);
        box.setMaxWidth(400);

        HBox keyBox = new HBox(keyL);
        HBox descBox = new HBox(descL);

        keyBox.setAlignment(Pos.CENTER_LEFT);
        descBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(descBox, Priority.ALWAYS);
        HBox.setHgrow(keyBox, Priority.ALWAYS);

        box.getChildren().addAll(keyBox,descBox);

        return box;
    }

    private static HBox intervalBox(){
        HBox box = new HBox();
        box.setMinHeight(12);
        box.setMaxHeight(12);
        return box;
    }

    private static HBox nameBox(String name){
        Label nameL = new Label(name);
        nameL.setStyle("-fx-font-size: 22px;" + "-fx-font-weight: bold;");
        nameL.setTextFill(Color.rgb(0,255,255,0.8));
        return new HBox(nameL);
    }
}
