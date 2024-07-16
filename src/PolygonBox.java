/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pain.t;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Mackenzie
 */
public class PolygonBox {

    private static int val = 3;
       
     /**
    * dialog box for polygon in DrawTool.java
    * select colors for edge and fill
    * select number of sides for polygon
    * @param gc GraphicsContext to change color
    * @param n unused parameter, for polymorphism
    */
    public PolygonBox(GraphicsContext gc, int n) {  
        
        Dialog dialog = new Dialog();
        dialog.setTitle("Select color");
        Label side_text = new Label("# of sides: ");
        side_text.setTranslateY(30);
        Label edge_text = new Label("Edges: ");
        Label fill_text = new Label("Fill: ");

        ListView sideView = new ListView();
        sideView.setMaxSize(100, 175);
        sideView.setTranslateY(50);
        sideView.getItems().addAll("3","4","5","6","7","8","9","10");
        
        sideView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                String result = sideView.getSelectionModel().getSelectedItem().toString(); 
                switch (result) {
                    case "3":
                        val = 3;
                        break;
                    case "4":
                        val = 4;
                        break;
                    case "5":
                        val = 5;
                        break;
                    case "6":
                        val = 6;
                        break;
                    case "7":
                        val = 7;
                        break;
                    case "8":
                        val = 8;
                        break;
                    case "9":
                        val = 9;
                    case "10":
                        val = 10;
                    default:
                        val = 3;
                        break;
                }
            }
            
        });

        ColorPicker edgeColor = new ColorPicker();
        edgeColor.setValue(Color.BLACK);
        ColorPicker fillColor = new ColorPicker();
        fillColor.setValue(Color.LIGHTGRAY);

        GridPane pane = new GridPane();
        pane.add(side_text,0,0);
        pane.add(sideView,0,1);
        pane.add(edge_text,1,1);
        pane.add(edgeColor,2,1);
        pane.add(fill_text, 1, 2);
        pane.add(fillColor,2,2);

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
    * @return instance variable val
    */
    public int getVal() {
        return val;
    }
    
}
