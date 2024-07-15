package pain.t;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
new features to implement
* Draw a square, a rectangle, an ellipse, and a circle, with fill    (don't forget color for inside and the edge of the shape).
* "Smart" save ("you've made changes...")
* Multiple image file types (open and save) - at least 3.
* Color "grabber"/dropper tool.
* have an Undo/Redo using an abstract data type ("stack" preferred/recommended); you should not use the FX built in tool!
* be able to draw a "regular" (same side length) polygon with the user specifying the # of sides.
* [The Big One] Select a piece of the image and move it.  
* have at least 3 unit tests.  (More info coming in class...)
* have a timer that allows for "autosave" [every X seconds, it saves a temporary copy - it should NOT overwrite the existing file if any]; ideally this should be threaded.  
This timer should be visible/invisible to the user at their option.  X should be something that can be set by the user.  If changed, it should persist on the next use of the software on that machine.]  
* have icons for tools
* have mouseover help for controls (if a icons+text as the tool, a short explanation is sufficient - if icons only were used, just a text name is sufficient)
*/

public class PainT extends Application {
    //global variables
    FileInputStream inputstream;
    
    //undo stack
    static Stack<Event> Undo = new Stack<Event>();
    static Stack<Event> Redo = new Stack<Event>();
        
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        GridPane pane = new GridPane();
        //create color picker and set value to default to black
        ColorPicker colorPicker1 = new ColorPicker();
        colorPicker1.setValue(Color.BLACK);
        //displays rgb values
        Label colorLabel = new Label("Color: " + colorPicker1.getValue().toString());        
        
        ToolBar tb = new ToolBar();
        
        inputstream = new FileInputStream("C:\\Users\\Mackenzie\\Pictures\\default.png"); 
        Image image = new Image(inputstream);
        
        //canvas and context
        Canvas canvas = new Canvas(500,500); 
        GraphicsContext gc = canvas.getGraphicsContext2D();    
        gc.drawImage(image,0,0);
        //set line color and width
        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK); 
        
        //create tabs and tabpane
        TabPane tabPane = new TabPane();
        Tab tab = new Tab();
        tab.setText("new tab");
        tab.setContent(canvas);
        tabPane.getTabs().add(tab);
        
        //scroll pane
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);      
        scroll.setPannable(true); //add feature to allow user to opt out of panning
        scroll.setContent(canvas);
        //create menu and menubar
        Menu menu1 = new Menu("Options");
        Menu menu2 = new Menu("Draw"); 
        Menu menu3 = new Menu("Close");
        Menu menu4 = new Menu("Help");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        menuBar.getMenus().add(menu2);
        menuBar.getMenus().add(menu3);
        menuBar.getMenus().add(menu4);
        //add items to toolbar
        tb.getItems().add(menuBar);
        tb.getItems().add(colorPicker1);
        //add items to menu
        Menu menuItem1 = new Menu("Select File  ALT F");
        MenuItem menuItem2 = new MenuItem("Close Program");
        MenuItem menuItem4 = new MenuItem("Help"); 
        MenuItem menuItem5 = new MenuItem("About"); 
        MenuItem menuItem6 = new MenuItem("Save File    CTRL S"); //saves as file, fix?
        Menu menuItem7 = new Menu("Line");
        MenuItem menuItem8 = new MenuItem("Resize"); 
        Menu menuItem9 = new Menu("Shapes"); //doesn't do anything yet
        MenuItem menuItem10 = new MenuItem("Clear Image");         
        MenuItem menuItem11 = new MenuItem("Width"); 
        CheckMenuItem menuItemP = new CheckMenuItem("Pencil");
        CheckMenuItem menuItemS = new CheckMenuItem("Straight");
        MenuItem menuItem12 = new MenuItem("Insert Text"); 
        MenuItem menuItem13 = new MenuItem("New Project");
        CheckMenuItem menuItem14 = new CheckMenuItem("Eyedropper Tool"); //if checked, eyedropper is activated
        MenuItem menuItem15 = new MenuItem("Undo"); //doesn't do anything yet
        CheckMenuItem menuItem16 = new CheckMenuItem("Rectangle"); 
        CheckMenuItem menuItem17 = new CheckMenuItem("Round Rectangle");
        CheckMenuItem menuItem18 = new CheckMenuItem("Square");
        CheckMenuItem menuItem19 = new CheckMenuItem("Ellipses");
        CheckMenuItem menuItem20 = new CheckMenuItem("Erase Tool");
        
        //help menu item event
        menuItem4.setOnAction((ActionEvent t) -> {
            Dialog<String> dialog = new Dialog<String>();
            Dialog response = dialog;
            dialog.setTitle("Help");
            dialog.setContentText("Help\n\nOptions tab: select and save files."
                    + "\nDraw tab: draw lines on image.\nSelect color with color picker on the right."
                    + "\nScroll bars appear when the image is bigger than the screen.\nYou can zoom in and out using the slider at the bottom."
                    + "\n\nKeyboard Shortcuts\ncontrol s: save\nalt f: open file\ncontrol r: resize");  
            ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(close);               
            dialog.showAndWait();
        });
        //about menu item event
        menuItem5.setOnAction((ActionEvent t) -> {
            Dialog<String> dialog = new Dialog<String>();
            Dialog response = dialog;
            dialog.setTitle("Help");
            dialog.setContentText("About\n\nProgram: Paint\nVersion 1.4\nAuthor: Mackenzie Albright");            
            ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(close);                       
            dialog.showAndWait();
        });        
        
        /*
        * open a text input dialog when item 12 is clicked
        * user enters text that is turned into a text object
        * on the image. not fully implemented yet
        */
        menuItem12.setOnAction((ActionEvent event) -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.getEditor().setText("Enter Text");
            dialog.setTitle("Insert text");
            dialog.setContentText("Insert text to place on the image.");
            dialog.show(); //don't use show and wait
            
            dialog.getEditor().setOnAction((ActionEvent t) -> {
                //create text and place on image
                //IMPLEMENT EVENTUALLY
                //size, font, bold, italic, etc, allow user to drag text to where they want
                String text_input = dialog.getEditor().getText();
                gc.strokeText(text_input, 100, 100);
                Undo.push(t);
            });      
        });
        //open new tab when item 13 is clicked
        //doesn't work yet
        menuItem13.setOnAction((ActionEvent event) -> {
            Tab tab2 = new Tab();
            tab.setText("new tab");
            //tab.setContent(null);
            tabPane.getTabs().add(tab);
            Undo.push(event);
        });
        
        //exit program when item 2 is clicked
        menuItem2.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });

        //open file when item 1 is clicked
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
        //open file explorer and set it to image display when item 1 is clicked
            @Override
            public void handle(ActionEvent event) {
                pain.t.PaintMethods.file(primaryStage, canvas, gc);
                Undo.push(event);
        }});            
        //save/save as action event
        menuItem6.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                pain.t.PaintMethods.save(primaryStage, canvas);
                Undo.push(event);
            }
        });        
        //clear image when clear image menu item is clicked
        menuItem10.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    inputstream = new FileInputStream("C:\\Users\\Mackenzie\\Pictures\\default.png");
                    Image image = new Image(inputstream);
                    gc.drawImage(image, 0, 0);
                    Undo.push(t);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });        
        //resize
        menuItem8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pain.t.PaintMethods.resize(canvas);    
                Undo.push(event);
            }
        });
    
        //impact line and color label when a new color is selected by color picker
        colorPicker1.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                gc.setStroke(colorPicker1.getValue());      
                colorLabel.setText("Color: " + colorPicker1.getValue().toString());
                Undo.push(event);
            }
        });        
               
        //action event for eyedropper
        menuItem14.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (menuItem14.isSelected()) {
                    pain.t.DrawTool.eyeDropper(canvas, gc);
                    Color color;
                    color = (Color) gc.getStroke();
                    Undo.push(event);
                    //try to get color from gc
                    colorPicker1.setValue(color);
                    colorLabel.setText("Color: " + color.toString()); 
                    //not changing to selected color
                }
                else {
                    //nothing
                }
            }
        });        
        
        //open input text dialog to enter specified line width
        menuItem11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                TextInputDialog td = new TextInputDialog("Enter line width and press enter and ok.");
                td.show();
                td.getEditor().setOnAction((ActionEvent event) -> {
                    try {
                        int line_width = Integer.parseInt(td.getEditor().getText());
                        gc.setLineWidth(line_width);      
                        Undo.push(event);
                    } catch (NumberFormatException e) {
                        System.out.println("On-Action failed: " + e);
                    }
                });                
            }
        });           
        
        
        
        //action event for undo
        menuItem15.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UNDO(Undo, Redo);
                //empty stack exception
            }
        });
                
        //action event for erase tool 
        menuItem20.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (((CheckMenuItem)event.getSource()).isSelected()) {
                    pain.t.DrawTool.erase(canvas, gc);
                    Undo.push(event);
                }
                else
                    ;
            }            
        });        
        
        //action event for pencil tool
        menuItemP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e)
            {
                if (menuItemP.isSelected()) {
                    pain.t.DrawTool.pencil(canvas, gc);
                    Undo.push(e);
                }
                else
                    ;
            }
        });
        
        //action event for straight line tool
        menuItemS.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (menuItemS.isSelected()) {
                    pain.t.DrawTool.straightLine(canvas,gc);
                    Undo.push(event);
                }
                else {
                    //nothing
                    //doesn't deselect when it should?
                }
            }
        });        
        
        //action event for rectangle tool
        menuItem16.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (menuItem16.isSelected()) {
                    pain.t.DrawTool.rectangle(canvas, gc);
                    Undo.push(event);
                }
                else {
                    //nothing
                    //doesn't deselect when it should?
                }
            }
        });
        
        //action event for round rectangle tool
        menuItem17.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (menuItem17.isSelected()) {
                    pain.t.DrawTool.roundRectangle(canvas,gc);
                    Undo.push(event);
                }
                else {
                    //nothing
                    //doesn't deselect when it should?
                }
            }
        });            
        
        //action event for square tool
        menuItem18.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (menuItem18.isSelected()) {
                    pain.t.DrawTool.square(canvas, gc);
                    Undo.push(event);
                }
                else {
                    //nothing
                    //doesn't deselect when it should?
                }
            }
        });
        
        //action event for ellipse tool
        menuItem19.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (menuItem19.isSelected()) {
                    pain.t.DrawTool.ellipse(canvas,gc);
                    Undo.push(event);
                }
                else {
                    //nothing
                    //doesn't deselect when it should?
                }
            }
        });         

        
        //zoom slider tool
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);       
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);
        //improve, scales out very far, slider in weird place, scroll pane doesn't interact with it, etc etc
        final Tooltip tooltip = new Tooltip();
        //add zoom action and tooltip to slider
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    canvas.setScaleX(new_val.doubleValue());
                    canvas.setScaleY(new_val.doubleValue());
                    tooltip.setText(String.format("%.2f", new_val));
                    slider.setTooltip(tooltip);
            }
        });        
                
        
        //add menu items
        menu1.getItems().add(menuItem1);
        menu1.getItems().add(menuItem6);
        menu1.getItems().add(menuItem10);
        menu1.getItems().add(menuItem13);
        menu3.getItems().add(menuItem2);
        menu2.getItems().add(menuItem7);
        menu4.getItems().add(menuItem4);
        menu4.getItems().add(menuItem5);
        menu2.getItems().add(menuItem9);
        menu2.getItems().add(menuItem8);
        menuItem7.getItems().add(menuItem11);
        menuItem7.getItems().add(menuItemP);
        menuItem7.getItems().add(menuItemS);
        menu2.getItems().add(menuItem12);
        menu2.getItems().add(menuItem14);
        menu1.getItems().add(menuItem15);
        menuItem9.getItems().add(menuItem16);
        menuItem9.getItems().add(menuItem17);
        menuItem9.getItems().add(menuItem18);
        menuItem9.getItems().add(menuItem19);
        menu2.getItems().add(menuItem20);
        
        //add menu and image view to vbox and center vbox        
        VBox vBox = new VBox();       
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.getChildren().add(tb);
        vBox.getChildren().add(colorPicker1);
        vBox.getChildren().add(tabPane);
        pane.getChildren().add(vBox);
        pane.getChildren().add(canvas);
        vBox.getChildren().add(scroll);
        pane.setConstraints(slider, 0,2);
        pane.getChildren().add(slider);
        pane.setConstraints(colorLabel,0,1);
        pane.getChildren().add(colorLabel);

        //set scene
        Scene scene = new Scene(pane, 600, 600);    
        /*
        * add keyboard controls to scene
        * @ inputs control s: save
        * @ inputs alt f: open file
        * @ inputs control r: resize
        * @ inputs escape: exit
        * @ inputs shift e: erase
        */
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()==KeyCode.S && key.isControlDown()) {
                pain.t.PaintMethods.save(primaryStage, canvas);
            }
            if(key.getCode()==KeyCode.F && key.isAltDown()) {
                pain.t.PaintMethods.file(primaryStage,canvas, gc);
            }
            if(key.getCode()==KeyCode.R && key.isControlDown()) {
                pain.t.PaintMethods.resize(canvas);
            }        
            if(key.getCode()==KeyCode.ESCAPE) {
                System.exit(0);
            }
            //bug present here too in the drawing tools
            if(key.getCode()==KeyCode.N) {
                pain.t.DrawTool.pencil(canvas, gc);
            }
            if(key.getCode()==KeyCode.L) {
                pain.t.DrawTool.straightLine(canvas, gc);
            }            
            if(key.getCode()==KeyCode.E && key.isShiftDown()) {
                pain.t.DrawTool.erase(canvas, gc);
            }
        });
        
        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);

        primaryStage.show();

    } 


    /*
    * @param Undo take a stack Undo and pop it from the list
    * @param Redo take a stack Redo and add it to the list
    */
    static void UNDO(Stack<Event> Undo, Stack<Event> Redo)
        {
            Event X = (Event)Undo.peek();
            
            Undo.pop();
            Redo.push(X);
        }    
        
    public static void main(String[] args) {
        launch(args);
    }    
}
