package pain.t;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class PaintMethods {
    
    /* 
    * open file
    * @param stage stage filechooser opens in 
    * @param canvas canvas image opens on
    * @param gc graphicscontext to draw image on canvas
    */
    static public void file(Stage stage, Canvas canvas, GraphicsContext gc) {
        Image image = null;
        //create filechooser object for menu item 2
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jfif"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Open Image");
        
        /*File directory = new File("Users\\Mackenzie\\Documents\\cs250\\Pain(t)\\pictures");     
        fileChooser.setInitialDirectory(directory);*/
        
        //Opening a dialog box
        File file = fileChooser.showOpenDialog(stage);
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);      
        //set image view's image
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());
            gc.drawImage(image, 0, 0);

        } catch (IOException ex) {
            Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    * save file
    * @param stage stage filechooser opens in
    * @param canvas canvas filechooser saves from
    */   
    static public void save(Stage stage, Canvas canvas) {
        FileChooser fileChooser = new FileChooser();
        //FileInputStream filestream = new FileInputStream("C:\\Users\\Mackenzie\\Documents\\fileicon.png");
        //fileChooser.setInitialDirectory(filestream);

        //String userDirectoryString = System.getProperty("user.home");
        
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jfif"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Save Image");
                
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage( (int) canvas.getWidth(),(int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage,
                    null), "png", file);
            } catch (IOException ex) {
                Logger.getLogger(
                    PainT.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }    
    
    /*
    * resize the size of the canvas
    * @param canvas canvas to resize
    */
    static public void resize(Canvas canvas) {
        //add message if user sets it smaller than current canvas
        Dialog dialog = new Dialog();
        dialog.setTitle("Resize");
        TextField width_text = new TextField("Width: " + canvas.getWidth());
        TextField height_text = new TextField("Height: " + canvas.getHeight());

        GridPane pane = new GridPane();
        pane.add(width_text,0,0);
        pane.add(height_text, 0, 1);
                
        ButtonType close = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(pane);
        dialog.getDialogPane().getButtonTypes().add(close);
        dialog.show();
        width_text.setOnAction((ActionEvent t) -> {
            try {
                int width = Integer.parseInt(width_text.getText());
                if (width < canvas.getWidth()) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setContentText("The selected width or height is smaller than the current width or height. Are you sure you want to resize?");
                    
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        canvas.setWidth(width);
                    }
                } else {
                    canvas.setWidth(width);
                }
                //canvas.setWidth(width);
            } catch (NumberFormatException e) {
                System.out.println("On-Action failed: " + e);
            }
        });
                
        height_text.setOnAction((ActionEvent t) -> { 
            try {
                int height = Integer.parseInt(height_text.getText());
                if (height < canvas.getHeight()) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setContentText("The selected width or height is smaller than the current width or height. Are you sure you want to resize?");
                    
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        canvas.setHeight(height);
                    }
                } else {
                    canvas.setHeight(height);
                }
            } catch (NumberFormatException e) {
                System.out.println("On-Action failed: " + e);
            }
        });                
    }     
}
