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
import javafx.scene.image.ImageView;
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
* Undo and redo
* "Smart" save ("you've made changes...")
* Multiple image file types (open and save) - at least 3.
* have an Undo/Redo using an abstract data type ("stack" preferred/recommended); you should not use the FX built in tool!
* be able to draw a "regular" (same side length) polygon with the user specifying the # of sides.
* [The Big One] Select a piece of the image and move it.  
* have a timer that allows for "autosave" [every X seconds, it saves a temporary copy - it should NOT overwrite the existing file if any]; ideally this should be threaded.  
This timer should be visible/invisible to the user at their option.  X should be something that can be set by the user.  If changed, it should persist on the next use of the software on that machine.]  
*/

public class PainT extends Application {
    //global variables
    FileInputStream inputstream;
    int selectedTool;
    
    //undo stack
    static Stack<Event> Undo = new Stack<Event>();
    static Stack<Event> Redo = new Stack<Event>();
        
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        selectedTool = 0;
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
        Menu menuOpt = new Menu("Options");
        Menu menuDraw = new Menu("Draw"); 
        Menu menuClose = new Menu("Close");
        Menu menuHelp = new Menu("Help");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menuOpt);
        menuBar.getMenus().add(menuDraw);
        menuBar.getMenus().add(menuClose);
        menuBar.getMenus().add(menuHelp);
        //add items to toolbar
        tb.getItems().add(menuBar);
        tb.getItems().add(colorPicker1);
        //add items to menu
        Menu itemFile = new Menu("Select File");
        MenuItem itemClose = new MenuItem("Close Program");
        MenuItem itemHelp = new MenuItem("Help"); 
        MenuItem itemAbout = new MenuItem("About"); 
        MenuItem itemSave = new MenuItem("Save File"); //saves as file, fix?
        Menu menuLine = new Menu("Line");
        MenuItem itemResize = new MenuItem("Resize"); 
        Menu menuShapes = new Menu("Shapes"); 
        MenuItem itemClear = new MenuItem("Clear Image");         
        MenuItem itemLineWidth = new MenuItem("Width"); 
        CheckMenuItem citemPencil = new CheckMenuItem("Pencil");
        CheckMenuItem citemStraightLine = new CheckMenuItem("Straight");
        MenuItem itemText = new MenuItem("Insert Text"); 
        MenuItem itemProject = new MenuItem("New Project");
        CheckMenuItem citemEyedropper = new CheckMenuItem("Eyedropper Tool"); //if checked, eyedropper is activated
        MenuItem itemUndo = new MenuItem("Undo"); //doesn't do anything yet
        CheckMenuItem citemRectangle = new CheckMenuItem("Rectangle"); 
        CheckMenuItem citemRoundRect = new CheckMenuItem("Round Rectangle");
        CheckMenuItem citemSquare = new CheckMenuItem("Square");
        CheckMenuItem citemEllipse = new CheckMenuItem("Ellipses");
        CheckMenuItem citemErase = new CheckMenuItem("Erase Tool");
        
        //initialize icons
        ImageView icon1 = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\fileicon.png")));
        ImageView icon6 = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\saveicon.png")));

        //set icon graphics
        itemFile.setGraphic(icon1);
        itemSave.setGraphic(icon6);
        
        //set tooltips
        //itemFile.setTooltip(new Tooltip("Save canvas to file\nKeyboard Shortcut: Alt F"));
        colorPicker1.setTooltip(new Tooltip("Select color to use for lines"));
        colorLabel.setTooltip(new Tooltip("Displays selected color"));
        tb.setTooltip(new Tooltip("Menu and color picker"));
        tab.setTooltip(new Tooltip("Opened tab"));
        Tooltip.install(icon1, new Tooltip("Open and select file\nKey: ALT F"));
        Tooltip.install(icon6, new Tooltip("Save file as\nKey: CTRL S"));
        
        //help menu item event
        itemHelp.setOnAction((ActionEvent t) -> {
            Dialog<String> dialog = new Dialog<String>();
            Dialog response = dialog;
            dialog.setTitle("Help");
            dialog.setContentText("Help\n\nOptions tab: select and save files. You can clear image, "
                    + "create a new project (not functional), and undo (not functional) here too."
                    + "\nDraw tab: draw lines and shapes on image. Resize, text, eyedropper, and eraser tool are present here too. "
                    + "\nSelect color with color picker on the right. Bottom left displays the color."
                    + "\nScroll bars appear when the image is bigger than the screen.\nYou can zoom in and out using the slider at the bottom."
                    + "\n\nKeyboard Shortcuts\nCTRL S: save\nALT F: open file\nCTRL R: resize\nESC: exit\nSHIFT E: erase");  
            ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(close);               
            dialog.showAndWait();
        }); 
        
        //about menu item event
        itemAbout.setOnAction((ActionEvent t) -> {
            Dialog<String> dialog = new Dialog<String>();
            Dialog response = dialog;
            dialog.setTitle("Help");
            dialog.setContentText("About\n\nProgram: Paint\nVersion 1.5\nAuthor: Mackenzie Albright");            
            ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(close);                       
            dialog.showAndWait();
        });        
        
        /*
        * open a text input dialog when item 12 is clicked
        * user enters text that is turned into a text object
        * on the image. not fully implemented yet
        */
        itemText.setOnAction((ActionEvent event) -> {
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
        itemProject.setOnAction((ActionEvent event) -> {
            Tab tab2 = new Tab();
            tab.setText("new tab");
            //tab.setContent(null);
            tabPane.getTabs().add(tab);
            Undo.push(event);
        });
        
        //exit program when item 2 is clicked
        itemClose.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });

        //open file when item 1 is clicked
        itemFile.setOnAction(new EventHandler<ActionEvent>() {
        //open file explorer and set it to image display when item 1 is clicked
            @Override
            public void handle(ActionEvent event) {
                PaintMethods.file(primaryStage, canvas, gc);
                Undo.push(event);
        }});            
        //save/save as action event
        itemSave.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                PaintMethods.save(primaryStage, canvas);
                Undo.push(event);
            }
        });        
        //clear image when clear image menu item is clicked
        itemClear.setOnAction(new EventHandler<ActionEvent>() {
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
        itemResize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PaintMethods.resize(canvas);    
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
        citemEyedropper.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemEyedropper.isSelected()) {
                    selectedTool = 1; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
            }            
        });             
        
        //open input text dialog to enter specified line width
        itemLineWidth.setOnAction(new EventHandler<ActionEvent>() {
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
        itemUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UNDO(Undo, Redo);
                //empty stack exception
            }
        });
                
        //action event for erase tool 
        citemErase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemErase.isSelected()) {
                    selectedTool = 2; }
                else {
                    selectedTool = 0; }

                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
            }            
        });        
        
        //action event for pencil tool
        citemPencil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemPencil.isSelected()) {
                    selectedTool = 3; }
                else { 
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
            }
        });
        
        //action event for straight line tool
        citemStraightLine.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemStraightLine.isSelected()) {
                    selectedTool = 4; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
            }
        });        
        
        //action event for rectangle tool
        citemRectangle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemRectangle.isSelected()) {
                    selectedTool = 5; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
            }
        });
        
        //action event for round rectangle tool
        citemRoundRect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemRoundRect.isSelected()) {
                    selectedTool = 6; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
            }
        });            
        
        //action event for square tool
        citemSquare.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemSquare.isSelected()) {
                    selectedTool = 7; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
            }
        });
        
        //action event for ellipse tool
        citemEllipse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemEllipse.isSelected()) {
                    selectedTool = 7; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
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
                    tooltip.setText(String.format("Zoom percent: %.2f", new_val));
                    slider.setTooltip(tooltip);
            }
        });        
               
        //add menu items
        menuOpt.getItems().add(itemFile);
        menuOpt.getItems().add(itemSave);
        menuOpt.getItems().add(itemClear);
        menuOpt.getItems().add(itemProject);
        menuClose.getItems().add(itemClose);
        menuDraw.getItems().add(menuLine);
        menuHelp.getItems().add(itemHelp);
        menuHelp.getItems().add(itemAbout);
        menuDraw.getItems().add(menuShapes);
        menuDraw.getItems().add(itemResize);
        menuLine.getItems().add(itemLineWidth);
        menuLine.getItems().add(citemPencil);
        menuLine.getItems().add(citemStraightLine);
        menuDraw.getItems().add(itemText);
        menuDraw.getItems().add(citemEyedropper);
        menuOpt.getItems().add(itemUndo);
        menuShapes.getItems().add(citemRectangle);
        menuShapes.getItems().add(citemRoundRect);
        menuShapes.getItems().add(citemSquare);
        menuShapes.getItems().add(citemEllipse);
        menuDraw.getItems().add(citemErase);
        
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
        Scene scene = new Scene(pane, canvas.getWidth()+10, canvas.getHeight()+125);   
        
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
                PaintMethods.save(primaryStage, canvas);
            }
            if(key.getCode()==KeyCode.F && key.isAltDown()) {
                PaintMethods.file(primaryStage,canvas, gc);
            }
            if(key.getCode()==KeyCode.R && key.isControlDown()) {
                PaintMethods.resize(canvas);
            }        
            if(key.getCode()==KeyCode.ESCAPE) {
                System.exit(0);
            }
            //bug present here too in the drawing tools
            if(key.getCode()==KeyCode.N) {
                selectedTool = 3;
            }
            if(key.getCode()==KeyCode.L) {
                selectedTool = 4;
            }            
            if(key.getCode()==KeyCode.E && key.isShiftDown()) {
                selectedTool = 2;
            }
            
            selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool);
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
        
    /*
    * 
    */
    public void selectedToolListener(Canvas canvas, GraphicsContext gc, ColorPicker colorPicker1, Label colorLabel, int selectedTool) {
        switch (selectedTool) {
            case 1:
                Color color = (Color) gc.getStroke();
                //try to get color from gc
                colorPicker1.setValue(color);
                colorLabel.setText("Color: " + color.toString()); 
                DrawTool.eyeDropper(canvas, gc);
                //updates when menu clicked, not canvas clicked
                break;
            case 2:
                DrawTool.erase(canvas,gc);
                break;
            case 3:
                DrawTool.pencil(canvas, gc);
                break;
            case 4:
                DrawTool.straightLine(canvas, gc);
                break;
            case 5:
                DrawTool.rectangle(canvas,gc);
                break;
            case 6:
                DrawTool.roundRectangle(canvas, gc);
                break;
            case 7:
                DrawTool.square(canvas, gc);
                break;
            case 8:
                DrawTool.ellipse(canvas, gc);
                break;
            default:
                break;
        }
    }    

    public static void main(String[] args) {
        launch(args);
    }    
}
