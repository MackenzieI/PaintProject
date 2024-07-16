package pain.t;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
new features to implement
* Multiple image file types (open and save) - at least 3.
* have an Undo/Redo using an abstract data type ("stack" preferred/recommended); you should not use the FX built in tool!
* be able to draw a "regular" (same side length) polygon with the user specifying the # of sides.
* [The Big One] Select a piece of the image and move it.  
* have a timer that allows for "autosave" [every X seconds, it saves a temporary copy - it should NOT overwrite the existing file if any]; ideally this should be threaded.  
This timer should be visible/invisible to the user at their option.  X should be something that can be set by the user.  If changed, it should persist on the next use of the software on that machine.]  
* Logging with (based on) threading is required
* Select-Copy-paste (versus select-move-and-paste)

Some more: a second one.
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
        //displays active tool
        Label toolLabel = new Label ("Active Tool: " + selectedTool + "\nNone"); 
        
        ToolBar tb = new ToolBar();
        
        inputstream = new FileInputStream("C:\\Users\\Mackenzie\\Documents\\cs250\\Pain(t)\\pictures\\default.png"); 
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
        MenuItem itemSettings = new MenuItem("Settings");
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
        CheckMenuItem citemEyedropper = new CheckMenuItem("Eyedropper Tool");
        MenuItem itemUndo = new MenuItem("Undo"); //doesn't do anything yet
        MenuItem itemRedo = new MenuItem("Redo"); //doesn't do anything yet        
        CheckMenuItem citemRectangle = new CheckMenuItem("Rectangle"); 
        CheckMenuItem citemRoundRect = new CheckMenuItem("Round Rectangle");
        CheckMenuItem citemSquare = new CheckMenuItem("Square");
        CheckMenuItem citemEllipse = new CheckMenuItem("Ellipse");
        CheckMenuItem citemErase = new CheckMenuItem("Erase Tool");
        CheckMenuItem citemCircle = new CheckMenuItem("Circle");
        CheckMenuItem citemPolygon = new CheckMenuItem("Polygon");
        
        //initialize icons
        ImageView iconfile = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\fileicon.png")));
        ImageView iconsave = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\saveicon.png")));
        ImageView iconclear = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\clearicon.png")));
        ImageView iconproject = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\projecticon.png")));
        ImageView iconsettings = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\settingsicon.png")));
        ImageView iconundo = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\undoicon.png")));
        ImageView iconredo = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\redoicon.png")));
        ImageView iconclose = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\exiticon.png")));
        ImageView iconhelp = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\helpicon.png")));
        ImageView iconabout = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\abouticon.png")));
        ImageView iconline = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\lineicon.png")));
        ImageView iconline2 = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\lineicon.png")));        
        ImageView iconpencil = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\pencilicon.png")));
        ImageView iconwidth = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\sizeicon.png")));
        ImageView iconsize = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\sizeicon.png")));
        ImageView icontext = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\texticon.png")));
        ImageView iconeyedropper = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\droppericon.png")));
        ImageView iconerase = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\eraseicon.png")));
        ImageView iconshapes = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\shapesicon.png")));        
        ImageView iconrectangle = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\rectangleicon.png")));
        ImageView iconroundrectangle = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\roundrectangleicon.png")));
        ImageView iconsquare = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\squareicon.png")));
        ImageView iconellipse = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\ellipseicon.png")));
        ImageView iconcircle = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\circleicon.png")));
        ImageView iconpolygon = new ImageView(new Image(new FileInputStream("C:\\Users\\Mackenzie\\Documents\\polygonicon.png")));

        //set icon graphics
        itemFile.setGraphic(iconfile);
        itemSave.setGraphic(iconsave);
        itemClear.setGraphic(iconclear);
        itemProject.setGraphic(iconproject);
        itemSettings.setGraphic(iconsettings);
        itemUndo.setGraphic(iconundo);
        itemRedo.setGraphic(iconredo);
        itemClose.setGraphic(iconclose);
        itemHelp.setGraphic(iconhelp);
        itemAbout.setGraphic(iconabout);
        menuLine.setGraphic(iconline);
        citemStraightLine.setGraphic(iconline2);
        citemPencil.setGraphic(iconpencil);
        itemLineWidth.setGraphic(iconwidth);
        itemResize.setGraphic(iconsize);
        itemText.setGraphic(icontext);
        citemEyedropper.setGraphic(iconeyedropper);
        citemErase.setGraphic(iconerase);
        menuShapes.setGraphic(iconshapes);
        citemRectangle.setGraphic(iconrectangle);
        citemRoundRect.setGraphic(iconroundrectangle);
        citemSquare.setGraphic(iconsquare);
        citemEllipse.setGraphic(iconellipse);
        citemCircle.setGraphic(iconcircle);
        citemPolygon.setGraphic(iconpolygon);
        
        //set tooltips
        colorPicker1.setTooltip(new Tooltip("Select color to use for lines"));
        colorLabel.setTooltip(new Tooltip("Displays selected color\n" + colorLabel.getText().toString()));
        toolLabel.setTooltip(new Tooltip("Displays active tool\n " + toolLabel.getText().toString()));
        tb.setTooltip(new Tooltip("Menu and color picker"));
        tab.setTooltip(new Tooltip("Opened tab"));
        Tooltip.install(iconfile, new Tooltip("Open and select file\nKey: ALT F"));
        Tooltip.install(iconsave, new Tooltip("Save file as\nKey: CTRL S"));
        Tooltip.install(iconclear, new Tooltip("Clear image to a blank white image"));
        Tooltip.install(iconproject, new Tooltip("Open a new tab"));
        Tooltip.install(iconsettings, new Tooltip("Open basic settings options"));
        Tooltip.install(iconundo, new Tooltip("Undo last action"));
        Tooltip.install(iconredo, new Tooltip("Redo last deleted action"));  
        Tooltip.install(iconclose, new Tooltip("Close the program"));
        Tooltip.install(iconhelp, new Tooltip("Help finding tools and shortcuts")); 
        Tooltip.install(iconabout, new Tooltip("Short about description")); 
        Tooltip.install(iconline, new Tooltip("Set line width and select straight or pencil line tools"));
        Tooltip.install(iconline2, new Tooltip("Draw a straight line")); 
        Tooltip.install(iconpencil, new Tooltip("Draw a freehand pencil line")); 
        Tooltip.install(iconwidth, new Tooltip("Change the width of the line")); 
        Tooltip.install(iconsize, new Tooltip("Change the height and width of the drawing canvas"));     
        Tooltip.install(icontext, new Tooltip("Insert text onto the canvas")); 
        Tooltip.install(iconeyedropper, new Tooltip("Click on the canvas to get that pixel's color")); 
        Tooltip.install(iconerase, new Tooltip("erase drawings and image on the canvas")); 
        Tooltip.install(iconshapes, new Tooltip("Select a shape from a menu")); 
        Tooltip.install(iconrectangle, new Tooltip("Draw a rectangle onto the canvas")); 
        Tooltip.install(iconroundrectangle, new Tooltip("Draw a round rectangle onto the canvas")); 
        Tooltip.install(iconsquare, new Tooltip("Draw a square onto the canvas")); 
        Tooltip.install(iconellipse, new Tooltip("Draw an ellipse onto the canvas")); 
        Tooltip.install(iconcircle, new Tooltip("Draw a circle onto the canvas")); 
        Tooltip.install(iconpolygon, new Tooltip("Draw a polygon onto the canvas")); 
        
        //help menu item event
        itemHelp.setOnAction((ActionEvent t) -> {
            Dialog<String> dialog = new Dialog<String>();
            Dialog response = dialog;
            dialog.setTitle("Help");
            dialog.setContentText("Help\n\nOptions tab: select and save files. You can clear image, "
                    + "create a new project (not functional), access settings, undo and redo (not functional)."
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
            dialog.setContentText("About\n\nProgram: Paint\nVersion 1.6\nAuthor: Mackenzie Albright");            
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
            //System.exit(0);
            if (!findChange(canvas)) {
                Platform.exit();
            } else {
                ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                ButtonType dontsave = new ButtonType("Don't save", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"save", save, dontsave);
                alert.setTitle("Do you wish to save?");
                alert.setContentText("A change has been made to the canvas. Do you wish to save?");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == dontsave) {
                    Platform.exit();
                } else {
                    System.out.println(alert.getButtonTypes().toString());
                }
            }
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
                    inputstream = new FileInputStream("C:\\Users\\Mackenzie\\Documents\\cs250\\Pain(t)\\pictures\\default.png");
                    Image image = new Image(inputstream);
                    gc.drawImage(image, 0, 0);
                    //Undo.push(t);
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
        citemEyedropper.setOnAction(e -> {
                if (citemEyedropper.isSelected()) {
                    selectedTool = 1; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);            
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
                if (selectedTool == 2) {
                    selectedTool = 0;
                    selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
                }
                else {
                    selectedTool = 2;
                    selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
                }
                /*
                if (citemErase.isSelected()) {
                    selectedTool = 2; }
                else {
                    selectedTool = 0; }

                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);*/
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
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
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
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
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
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
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
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
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
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
            }
        });
        
        //action event for ellipse tool
        citemEllipse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemEllipse.isSelected()) {
                    selectedTool = 8; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
            }
        });         
        
        //action event for circle tool
        citemCircle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemCircle.isSelected()) {
                    selectedTool = 9; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
            }
        });      
        
        //action event for polygon tool
        citemPolygon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (citemPolygon.isSelected()) {
                    selectedTool = 10; }
                else {
                    selectedTool = 0; }
                
                selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
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
        slider.setBlockIncrement(100);
        //improve, scales out very far, slider in weird place, scroll pane doesn't interact with it, etc etc
        final Tooltip tooltip = new Tooltip();
        //add zoom action and tooltip to slider
        slider.valueProperty().addListener(new ChangeListener<Number>() { //affect scrollpane too
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
        menuOpt.getItems().add(itemSettings);
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
        menuOpt.getItems().add(itemRedo);
        menuShapes.getItems().add(citemRectangle);
        menuShapes.getItems().add(citemRoundRect);
        menuShapes.getItems().add(citemSquare);
        menuShapes.getItems().add(citemEllipse);
        menuDraw.getItems().add(citemErase);
        menuShapes.getItems().add(citemCircle);
        menuShapes.getItems().add(citemPolygon);
        
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
        pane.setConstraints(toolLabel,0,1);
        toolLabel.setTranslateX(250);
        pane.getChildren().add(toolLabel);
        
        //set scene
        Scene scene = new Scene(pane, canvas.getWidth()+10, canvas.getHeight()+175);  
        
        //settings menu item event
        itemSettings.setOnAction((ActionEvent t) -> {
            customDBox dialog = new customDBox(scroll, canvas, scene, primaryStage);
        });        
        
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
            
            selectedToolListener(canvas, gc, colorPicker1, colorLabel, selectedTool, toolLabel);
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
    * finds if a change was made to the default canvas
    * opens a confirmation box before closing program
    * @param canvas canvas to compare
    */
    public boolean findChange(Canvas canvas) {
        boolean isChanged = false;
        try { 
            inputstream = new FileInputStream("C:\\Users\\Mackenzie\\Documents\\cs250\\Pain(t)\\pictures\\default.png");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
        }
        Image image = new Image(inputstream);        
        
        SnapshotParameters params = new SnapshotParameters();
        WritableImage wi = canvas.snapshot(params, null);
        //doesn't work
        //compare snapshot to default image to detect if a change was made to the canvas
        try {
            for(int x = 0; x < wi.getWidth(); x++){
                for(int y = 0; y < wi.getHeight(); y++){
                    if (!wi.getPixelReader().getColor(x,y).equals(image.getPixelReader().getColor(x,y))) {
                        isChanged = true;
                    }
                }
            }            
        } catch(Exception IndexOutOfBounds) {
            isChanged = true;
        }
        //condition if resized smaller
        if (wi.getWidth() < image.getWidth() || wi.getHeight() < image.getHeight()) {
            isChanged = true;
        }
        return isChanged;
    }
    
    /*
    * 
    */
    public void selectedToolListener(Canvas canvas, GraphicsContext gc, ColorPicker colorPicker1, Label colorLabel, int selectedTool, Label toolLabel) {
        switch (selectedTool) {
            case 1:
                Color color = (Color) gc.getStroke();
                //try to get color from gc
                colorPicker1.setValue(color);
                colorLabel.setText("Color: " + color.toString()); 
                DrawTool.eyeDropper(canvas, gc);
                //updates when menu clicked, not canvas clicked
                toolLabel.setText("Active Tool: " + selectedTool+"\nEyedropper");
                break;
            case 2:
                DrawTool.erase(canvas,gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nEraser");                
                break;
            case 3:
                DrawTool.pencil(canvas, gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nPencil");
                break;
            case 4:
                DrawTool.straightLine(canvas, gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nLine");
                break;
            case 5:
                DrawTool.rectangle(canvas,gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nRectangle");
                break;
            case 6:
                DrawTool.roundRectangle(canvas, gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nRounded Rectangle");
                break;
            case 7:
                DrawTool.square(canvas, gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nSquare");
                break;
            case 8:
                DrawTool.ellipse(canvas, gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nEllipse");
                break;
            case 9:
                DrawTool.circle(canvas, gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nCircle");
                break;
            case 10:
                DrawTool.polygon(canvas, gc);
                toolLabel.setText("Active Tool: " + selectedTool+"\nPolygon");
                break;                
            default:
                toolLabel.setText("Active Tool: " + selectedTool+"\nNone");
                break;
        }
    }    

    public static void main(String[] args) {
        launch(args);
    }    
}
