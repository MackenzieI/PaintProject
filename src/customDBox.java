package pain.t;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

class customDBox {
    int val;
    
    /**
    * dialog box for shape tools in DrawTool.java
    * select colors for edge and fill
    * @param gc GraphicsContext to change color
    */
    public customDBox(GraphicsContext gc) {
        
        Dialog dialog = new Dialog();
        dialog.setTitle("Select color");
        Label edge_text = new Label("Edges: ");
        Label fill_text = new Label("Fill: ");

        ColorPicker edgeColor = new ColorPicker();
        edgeColor.setValue(edgeColor.getValue());
        ColorPicker fillColor = new ColorPicker();
        fillColor.setValue(Color.LIGHTGRAY);

        GridPane pane = new GridPane();
        pane.add(edge_text,0,0);
        pane.add(edgeColor,1,0);
        pane.add(fill_text, 0, 1);
        pane.add(fillColor,1,1);

        //color picker and label in main don't change to match
        edgeColor.setOnAction(e -> {
            gc.setStroke(edgeColor.getValue());
        });
        fillColor.setOnAction(e -> {
            gc.setFill(fillColor.getValue());
        });

        ButtonType close = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(pane);
        dialog.getDialogPane().getButtonTypes().add(close);
        dialog.show();
    }
    
    /**
    * dialog box for settings menu in PainT.java
    * change size of the window and set panning option
    * @param scroll ScrollPane to edit panning
    * @param scene Scene to edit width and height of the window
    */
    public customDBox(ScrollPane scroll, Canvas canvas, Scene scene, Stage stage) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Settings");
        Label pan_text = new Label("Panning: ");
        Label window_text = new Label("Window size: ");
        
        ListView view = new ListView();
        view.getItems().addAll("Panning", "No Panning");
        view.setPrefHeight(25);

        TextField width_text = new TextField("Width: " + scene.getWidth()); //this should be stage, actually
        TextField height_text = new TextField("Height: " + scene.getHeight());        

        GridPane pane = new GridPane();
        pane.add(pan_text,0,0);
        pane.add(view,1,0);
        pane.add(window_text, 0, 1);
        pane.add(width_text,1,1);
        pane.add(height_text,2,1);
        
        width_text.setOnAction((ActionEvent t) -> {
            try {
                int width = Integer.parseInt(width_text.getText());
                    stage.setWidth(width);
                } catch (NumberFormatException e) {
                System.out.println("On-Action failed: " + e);
            }
        });        
        
        height_text.setOnAction((ActionEvent t) -> {
            try {
                int height = Integer.parseInt(height_text.getText());
                    stage.setHeight(height);
                } catch (NumberFormatException e) {
                System.out.println("On-Action failed: " + e);
            }
        });       
        
        view.setOnMouseClicked(e -> {
            String result = view.getSelectionModel().getSelectedItem().toString();
            if (result == "No Panning") {
                //System.out.println("no panning selected");
                scroll.setPannable(false);
            }
            else if (result == "Panning") {
                scroll.setPannable(true);
            }
        });

        ButtonType close = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(pane);
        dialog.getDialogPane().getButtonTypes().add(close);
        dialog.show();
    }

    /**
     * Returns the Value set by the Dialog Box
     */
    public int getVal() {
        return val;
    }
}
