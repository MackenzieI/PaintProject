package pain.t;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Desktop.Action;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.gc;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/*
new features to implement
save an image to a file (done)
allow for save as
draw a line (become part of the save image) (failed)
working space that resizes to fit the image being opened (tried)
should have scroll bars (implement better)
allow you to control the width of the line drawn (maybe)
have a color chooser (done)
have a help menu item with help and about options (done)
*/

public class PainT extends Application {
    //global variables
    double height;
    double width;    
    int line_width;
    VBox vBox;
    ImageView myImageView;   
    Image image;
    Path path;

    @Override
    public void start(Stage primaryStage) {
        //Group root = new Group();
        //unused 
        //Canvas canvas = new Canvas();

        StackPane pane = new StackPane();

        ColorPicker colorPicker1 = new ColorPicker();

        //path to draw line, line width is one by default
        line_width = 1;
        path = new Path();
        path.setStrokeWidth(line_width);
        //path.setStroke(Color.BLACK); //error

        ToolBar tb = new ToolBar();

        //create image view and default size

        ImageView myImageView = new ImageView(image);
        myImageView.setPreserveRatio(true);
        myImageView.setFitHeight(500);
        myImageView.setFitWidth(500);           

        //scrollbars 
        /*
        ScrollBar horzS = new ScrollBar();
        horzS.setOrientation(Orientation.HORIZONTAL);
        ScrollBar vertS = new ScrollBar();
        vertS.setOrientation(Orientation.VERTICAL);
        //horizontal scrollbar listener
        horzS.valueProperty().addListener((ObservableValue<? extends Number> ov, 
            Number old_val, Number new_val) -> {
                System.out.println(-new_val.doubleValue());
        }); 
        //vertical scrollbar listener
        vertS.valueProperty().addListener((ObservableValue<? extends Number> ov, 
            Number old_val, Number new_val) -> {
                System.out.println(-new_val.doubleValue());
        });    */

        // scrollpane, doesn't show

        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);         
        scroll.setContent(vBox);

        //create menu and menubar
        Menu menu1 = new Menu("Options");
        Menu menu2 = new Menu("Draw"); //add color chooser and line here
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
        MenuItem menuItem6 = new MenuItem("Save File");
        Menu menuItem7 = new Menu("Line");
        MenuItem menuItem8 = new MenuItem("Straight Line"); //doesn't do anything yet
        MenuItem menuItem9 = new MenuItem("Freehand Line"); //doesn't do anything yet
        MenuItem menuItem10 = new MenuItem("Clear Image");         
        MenuItem menuItem11 = new MenuItem("Width"); 
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
                    + "\nDraw tab: draw lines on image.\nSelect color with color picker on the right");  
            ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(close);               
            dialog.showAndWait();
        });
        //about menu item event
        menuItem5.setOnAction((ActionEvent t) -> {
            Dialog<String> dialog = new Dialog<String>();
            Dialog response = dialog;
            dialog.setTitle("Help");
            dialog.setContentText("About\n\nProgram: Paint\nVersion 1.2\nAuthor: Mackenzie Albright");            
            ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(close);                       
            dialog.showAndWait();
        });
        //create filechooser object for menu item 2
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        //Adding action on the menu item
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
        //open file explorer and set it to image display when item 1 is clicked
        public void handle(ActionEvent event) {
            myImageView.setImage(null);
            //Opening a dialog box
            File file = fileChooser.showOpenDialog(primaryStage);
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);      
            //set image view's image
            Image image = null;
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                myImageView.setImage(image);

            } catch (IOException ex) {
                Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
            }
            myImageView.setFitHeight(image.getHeight());
            myImageView.setFitWidth(image.getWidth());
        }});            
        //save/save as action event
        menuItem6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");

                File file = fileChooser.showSaveDialog(primaryStage);
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
        });        
        //clear image when clear image menu item is clicked
        menuItem10.setOnAction((ActionEvent t) -> {
            myImageView.setImage(null);
        });        
        //open input text dialog to enter specified line width
        menuItem11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                TextInputDialog td = new TextInputDialog("Enter line width.");
                td.show();
                String input = td.getEditor().getText();
                line_width = Integer.valueOf(input);
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
        menuItem7.getItems().add(menuItem8);
        menuItem7.getItems().add(menuItem9);
        menuItem7.getItems().add(menuItem11);

        //add menu and image view to vbox and center vbox        
        VBox vBox = new VBox();       
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.getChildren().add(tb);
        pane.getChildren().add(myImageView);
        vBox.getChildren().add(colorPicker1);
        pane.getChildren().add(vBox);
        //pane.getChildren().add(horzS);
        //pane.getChildren().add(vertS);
        pane.getChildren().add(scroll);
        //try to fit scene to image view
        height = myImageView.getFitHeight();
        width = myImageView.getFitWidth();   
        //set scene
        Scene scene = new Scene(pane, width, height);    

        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);
        primaryStage.show();

        /* for draw line mouse event
        scene.setOnMouseClicked(mouseHandler);
        scene.setOnMouseDragged(mouseHandler);
        scene.setOnMouseEntered(mouseHandler);
        scene.setOnMouseExited(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);        
        */
    }

    //draw line
    // error with event type?
    /*
    public void mouseHandler(MouseEvent mouseEvent) {
        ;
    } 
    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    @Override
        public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            path.getElements().clear();
            path.getElements()
                .add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            path.getElements()
                .add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
        }
        }
    }; */

    public static void main(String[] args) {
        launch(args);
    }    
}
