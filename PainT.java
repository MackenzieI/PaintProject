package pain.t;

import java.awt.image.BufferedImage;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/*
new features to implement
save an image to a file as image tzpes
allow for save as
draw a line (become part of the save image) 
working space that resizes to fit the image being opened 
allow you to control the width of the line drawn (maybe)
add color to line
add width to line
* Draw a square, a rectangle, an ellipse, and a circle, with fill    (don't forget color for inside and the edge of the shape).
* "Smart" save ("you've made changes...")
* Multiple image file types (open and save) - at least 3.
* Keyboard UI controls (control S/Save, alt F/File menu, etc)
* A text label describing the color.
* Color "grabber"/dropper tool.
* Resize canvas (and resulting image) and zoom in/out.
Text tool
*/

public class PainT extends Application {
    //global variables
    double height;
    double width;    
    //int line_width;
    VBox vBox;
    ImageView myImageView;   
    Image image;
    Path path;  
    
    //Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        GridPane pane = new GridPane();
        //create color picker and set value to default to black
        ColorPicker colorPicker1 = new ColorPicker();
        colorPicker1.setValue(Color.BLACK);

        //path to draw line, line width is one by default
        path = new Path();
        
        ToolBar tb = new ToolBar();
        
        //create image view and default size
        ImageView myImageView = new ImageView(image);
        myImageView.setPreserveRatio(true);
        myImageView.setFitHeight(500);
        myImageView.setFitWidth(500);           
        //scroll pane
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);      
        scroll.setPannable(true);
        scroll.setContent(myImageView);
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
        Menu menuItem1 = new Menu("Select File");
        MenuItem menuItem2 = new MenuItem("Close Program");
        MenuItem menuItem4 = new MenuItem("Help"); 
        MenuItem menuItem5 = new MenuItem("About"); 
        MenuItem menuItem6 = new MenuItem("Save File"); //saves as file, fix?
        Menu menuItem7 = new Menu("Line");
        MenuItem menuItem8 = new MenuItem("Resize"); 
        Menu menuItem9 = new Menu("Shapes"); //doesn't do anything yet
        MenuItem menuItem10 = new MenuItem("Clear Image");         
        MenuItem menuItem11 = new MenuItem("Width"); 
        MenuItem menuItem12 = new MenuItem("Insert Text"); 
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
            dialog.showAndWait();
            
            dialog.getEditor().setOnAction((ActionEvent t) -> {
                //create text and place on image
                //IMPLEMENT EVENTUALLY
                //size, font, bold, italic, etc, allow user to drag text to where they want
                String text_input = dialog.getEditor().getText();
                Text text = new Text(text_input);
                //how to put on image?
            });      
        });
        //exit program when item 2 is clicked
        menuItem2.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });
        //help menu item event
        menuItem4.setOnAction((ActionEvent t) -> {
            Dialog<String> dialog = new Dialog<String>();
            Dialog response = dialog;
            dialog.setTitle("Help");
            dialog.setContentText("Help\n\nOptions tab: select and save files."
                    + "\nDraw tab: draw lines on image.\nSelect color with color picker on the right."
                    + "\nScroll bars appear when the image is bigger than the screen.\nYou can zoom in and out using the slider at the bottom.");  
            ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(close);               
            dialog.showAndWait();
        });
        //about menu item event
        menuItem5.setOnAction((ActionEvent t) -> {
            Dialog<String> dialog = new Dialog<String>();
            Dialog response = dialog;
            dialog.setTitle("Help");
            dialog.setContentText("About\n\nProgram: Paint\nVersion 1.3\nAuthor: Mackenzie Albright");            
            ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(close);                       
            dialog.showAndWait();
        });

        //open file when item 1 is clicked
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
        //open file explorer and set it to image display when item 1 is clicked
            @Override
            public void handle(ActionEvent event) {
                file(primaryStage,myImageView);
        }});            
        //save/save as action event
        menuItem6.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                save(primaryStage);
            }
        });        
        //clear image when clear image menu item is clicked
        menuItem10.setOnAction((ActionEvent t) -> {
            myImageView.setImage(null);
        });        
        //resize
        //not fully implemented yet
        menuItem8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog dialog = new Dialog();
                dialog.setTitle("Resize");
                TextField width_text = new TextField("Width: " + myImageView.getFitWidth());
                TextField height_text = new TextField("Height: " + myImageView.getFitHeight());

                GridPane pane = new GridPane();
                pane.add(width_text,0,0);
                pane.add(height_text, 0, 1);
                
                ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
                dialog.getDialogPane().setContent(pane);
                dialog.getDialogPane().getButtonTypes().add(close);                       
                dialog.showAndWait();
                width_text.setOnAction((ActionEvent t) -> { //did not change imageview 
                    try {
                        int width = Integer.parseInt(width_text.getText());
                        myImageView.setFitWidth(width);
                    } catch (NumberFormatException e) {
                        System.out.println("On-Action failed: " + e);
                    }
                });
            }
        });
        
        //canvas and context
        Canvas canvas = new Canvas(200,200); //must be smaller than vBox/imageview 
        GraphicsContext gc = canvas.getGraphicsContext2D();    
        //set line color and width
        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK); 
        //gc.setLineWidth(line_width);      
        //displays rgb values
        Label colorLabel = new Label("Color: " + colorPicker1.getValue().toString());
        //impact line and color label when a new color is selected by color picker
        colorPicker1.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                gc.setStroke(colorPicker1.getValue());      
                colorLabel.setText("Color: " + colorPicker1.getValue().toString());
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
                        
                    } catch (NumberFormatException e) {
                        System.out.println("On-Action failed: " + e);
                    }
                });                
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
                    myImageView.setScaleX(new_val.doubleValue());
                    myImageView.setScaleY(new_val.doubleValue());
                    tooltip.setText(String.format("%.2f", new_val));
                    slider.setTooltip(tooltip);
            }
        });        

        //add menu items
        menu1.getItems().add(menuItem1);
        menu1.getItems().add(menuItem6);
        menu1.getItems().add(menuItem10);
        menu3.getItems().add(menuItem2);
        menu2.getItems().add(menuItem7);
        menu4.getItems().add(menuItem4);
        menu4.getItems().add(menuItem5);
        menu2.getItems().add(menuItem9);
        menu2.getItems().add(menuItem8);
        menuItem7.getItems().add(menuItem11);
        menu2.getItems().add(menuItem12);

        //try to fit scene to image view
        height = myImageView.getFitHeight();
        width = myImageView.getFitWidth();   
        
        //add menu and image view to vbox and center vbox        
        VBox vBox = new VBox();       
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.getChildren().add(tb);
        pane.getChildren().add(myImageView);
        vBox.getChildren().add(colorPicker1);
        pane.getChildren().add(vBox);
        pane.getChildren().add(canvas);
        vBox.getChildren().add(scroll);
        pane.setConstraints(slider, 0,2);
        pane.getChildren().add(slider);
        pane.setConstraints(colorLabel,0,1);
        pane.getChildren().add(colorLabel);

        //set scene
        Scene scene = new Scene(pane, width, height);    
        /*
        * add keyboard controls to scene
        * @ inputs control s: save
        * @ inputs alt f: open file
        */
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()==KeyCode.S && key.isControlDown()) {
                   //System.out.println("You pressed save");
                   save(primaryStage);
            }
            if(key.getCode()==KeyCode.F && key.isAltDown()) {
                file(primaryStage,myImageView);
            }
        });
        
        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);

        primaryStage.show();
        /*
        myImageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                //System.out.println("Clicked, x:" + me.getSceneX() + " y:" + me.getSceneY());
                //the event will be passed only to the circle which is on front
                initX = me.getSceneX();
                initY = me.getSceneY();
                me.consume();
            }
        });        
        
        myImageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                //System.out.println("Dragged, x:" + me.getSceneX() + " y:" + me.getSceneY());
                if (me.getSceneX() < maxX && me.getSceneY() < maxY) {
                    Line line = new Line(initX, initY, me.getSceneX(), me.getSceneY());
                    line.setFill(null);
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(2);
                    pane.getChildren().add(line);
                }

                initX = me.getSceneX() > maxX ? maxX : me.getSceneX();
                initY = me.getSceneY() > maxY ? maxY : me.getSceneY();
            }
        });        
        */

        //draw line events, doesn't draw on saved image
        //add way to clear
        
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
                new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                gc.beginPath();
                gc.moveTo(event.getX(), event.getY());
                gc.stroke();
            }
        });
        
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
                new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                gc.lineTo(event.getX(), event.getY());
                gc.stroke();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
                new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {

            }
        });
    
    }
    
    /*
    * save file
    * @param stage filechooser opens in
    */
    public void save(Stage stage) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");
                
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(myImageView.getImage(),
                                null), "png", file);
                    } catch (IOException ex) {
                        Logger.getLogger(
                                PainT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
    /* 
    * open file
    * @param stage filechooser opens in 
    * @param imageview image saved from
    */
    public void file(Stage stage, ImageView iv) {
        //create filechooser object for menu item 2
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        iv.setImage(null);
        //Opening a dialog box
        File file = fileChooser.showOpenDialog(stage);
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);      
        //set image view's image
        Image image = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            iv.setImage(image);
            stage.setWidth(width);
            stage.setHeight(height);

        } catch (IOException ex) {
            Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        iv.setFitHeight(image.getHeight());
        iv.setFitWidth(image.getWidth());
    }
    
    public static void main(String[] args) {
        launch(args);
    }    
}
