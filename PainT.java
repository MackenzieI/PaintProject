package pain.t;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/*
view image from file
user chooses the file to open
have a menu bar
close politely
*/

public class PainT extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        //create menu and menubar
        Menu menu1 = new Menu("Options");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        //add items to menu
        Menu menuItem1 = new Menu("Select File");
        MenuItem menuItem2 = new MenuItem("Close Program");
        //exit program when item 2 is clicked
        menuItem2.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });
        //create image view and preserve ratio and width
        ImageView myImageView = new ImageView();
        myImageView.setPreserveRatio(true);
        myImageView.setFitWidth(200);
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
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                myImageView.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }});        
        //add menu items
        menu1.getItems().add(menuItem1);
        menu1.getItems().add(menuItem2);      
        //add menu and image view to vbox and center vbox
        VBox vBox = new VBox(menuBar);       
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.getChildren().add(myImageView);
        //set scene
        Scene scene = new Scene(vBox, 300, 250);
        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }    
}