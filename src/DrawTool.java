package pain.t;

import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javax.swing.undo.UndoManager;
import javafx.scene.control.ColorPicker;

public class DrawTool {    
    //track events
    //static EventHandler<MouseEvent> line, pencil, rectangle, roundrect, square, ellipse, circle, polygon, eyedropper;
    /*static EventHandler<MouseEvent> line = new EventHandler<MouseEvent>() {
        double x1, y1, x2, y2;
        GraphicsContext gc;
        @Override
        public void handle(MouseEvent event) {
            switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        x1 = event.getX();
                        y1 = event.getY();
                        gc.beginPath();
                    case "MOUSE_RELEASED":
                        x2 = event.getX();
                        y2 = event.getY();
                        gc.strokeLine(x1, y1, x2, y2);
                }        }
    };*/

    private static EventHandler<MouseEvent> currentHandler = null;

    /**
     * Removes the current EventHandlers in use from canvas.
     * @param canvas takes a canvas
     */
    public static void removeHandler(Canvas canvas) {
        if (currentHandler != null) {
            canvas.removeEventHandler(MouseEvent.ANY, currentHandler);
            currentHandler = null;
        }
    }
    
    UndoManager undoM = new UndoManager();
    
    /**
    * Draw a straight line onto the canvas. Live draw not yet supported.
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    */
     static public void straightLine(Canvas canvas, GraphicsContext gc) {  
         
        EventHandler<MouseEvent> sLineHandler = new EventHandler<MouseEvent>() {
        double x1, y1, x2, y2;
        //GraphicsContext gc;
        @Override
        public void handle(MouseEvent event) {
            switch (event.getEventType().getName()) {
                case "MOUSE_PRESSED":
                    x1 = event.getX();
                    y1 = event.getY();
                    gc.beginPath();
                case "MOUSE_RELEASED":
                    x2 = event.getX();
                    y2 = event.getY();
                    gc.strokeLine(x1, y1, x2, y2); 
                }
            }
        };

         // Store the handler
         currentHandler = sLineHandler;
         canvas.addEventHandler(MouseEvent.ANY, sLineHandler);
     }
        
    /**
    * Draw a freehand/pencil tool line onto the canvas. 
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    * @param colorPicker the color picker to get the current color
    */
    static public void pencil(Canvas canvas, GraphicsContext gc, ColorPicker colorPicker) {
        EventHandler<MouseEvent> pencilHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gc.setStroke(colorPicker.getValue());
                gc.setLineWidth(1);
                switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        gc.beginPath();
                        gc.moveTo(event.getX(), event.getY());
                        gc.stroke();
                    case "MOUSE_DRAGGED":
                        gc.lineTo(event.getX(), event.getY());
                        gc.stroke();
                    case "MOUSE_RELEASED":
                }         
                //canvas.setOnMousePressed(line);
            }
        };
        // Store the handler
        currentHandler = pencilHandler;
        canvas.addEventHandler(MouseEvent.ANY, pencilHandler);
    }                     
                
    /**
    * Draw a rectangle onto the canvas. 
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    */
    static public void rectangle(Canvas canvas, GraphicsContext gc) {
        //can only stroke to the right
        EventHandler<MouseEvent> rectHandler = new EventHandler<MouseEvent>() {
            double x1, y1, x2, y2, w, h;
            customDBox dbox = new customDBox(gc);
            
            @Override
            public void handle(MouseEvent event) {
                switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        x1 = event.getX();
                        y1 = event.getY();
                    case "MOUSE_DRAGGED":
                        x2 = event.getX();
                        y2 = event.getY();
                        w = x2 - x1;
                        h = y2 = y1;
                    case "MOUSE_RELEASED":
                        gc.strokeRect(x1, y1, w, h);
                        gc.fillRect(x1, y1, w, h);
                }
            }
        };
        // Store the handler
        currentHandler = rectHandler;
        canvas.addEventHandler(MouseEvent.ANY, rectHandler);
    }
            
    /**
    * Draw a square onto the canvas. 
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    */
    static public void square(Canvas canvas, GraphicsContext gc) {
        //can only stroke to the right
        EventHandler<MouseEvent> squareHandler = new EventHandler<MouseEvent>() {
            double x1, y1, x2, y2, w, h;
            customDBox dbox = new customDBox(gc);

            @Override
            public void handle(MouseEvent event) {
                switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        x1 = event.getX();
                        y1 = event.getY();
                    case "MOUSE_DRAGGED":
                        x2 = event.getX();
                        y2 = event.getY();
                        w = x2 - x1;
                        //h = y2 = y1;
                    case "MOUSE_RELEASED":
                        gc.strokeRect(x1, y1, w, w);
                        gc.fillRect(x1, y1, w, w);
                }
            }
        };
        // Store the handler
        currentHandler = squareHandler;
        canvas.addEventHandler(MouseEvent.ANY, squareHandler);
    }

    /**
    * Draw an ellipse onto the canvas. 
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    */
    static public void ellipse(Canvas canvas, GraphicsContext gc) {
        //can only stroke to the right
        //fills left like a square

        EventHandler<MouseEvent> ellipseHandler = new EventHandler<MouseEvent>() {
            double x1, y1, x2, y2, w, h;
            customDBox dbox = new customDBox(gc);

            @Override
            public void handle(MouseEvent event) {
                switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        x1 = event.getX();
                        y1 = event.getY();
                    case "MOUSE_DRAGGED":
                        x2 = event.getX();
                        y2 = event.getY();
                        //call an undo when it draws over it
                    case "MOUSE_RELEASED":
                        w = x2 - x1;
                        h = y2 = y1;
                        gc.strokeOval(x1, y1, w, h);
                        gc.fillOval(x1, y1, w, h);
                }                                 
                        //if doesn't work canvas.setOnAction
            }
        };

        // Store the handler
        currentHandler = ellipseHandler;
        canvas.addEventHandler(MouseEvent.ANY, ellipseHandler);
    }

    /**
    * Draw an circle onto the canvas. 
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    */
    static public void circle(Canvas canvas, GraphicsContext gc) {
        //try using strokearc
        //can only stroke to the right
        //fills left like a square

        EventHandler<MouseEvent> circleHandler = new EventHandler<MouseEvent>() {
            double x1, y1, x2, y2, w, h;

            @Override
            public void handle(MouseEvent event) {
                switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case "MOUSE_DRAGGED":
                        x2 = event.getX();
                        y2 = event.getY();
                        break;
                    case "MOUSE_RELEASED":
                        w = x2 - x1;
                        gc.strokeOval(x1, y1, w, w);
                        gc.fillOval(x1, y1, w, w);
                        break;
                }
            }
        };

        // Store the handler
        currentHandler = circleHandler;
        canvas.addEventHandler(MouseEvent.ANY, circleHandler);
    }

    /**
    * Draw a rounded rectangle onto the canvas. 
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    */
    static public void roundRectangle(Canvas canvas, GraphicsContext gc) {
        //can only stroke to the right

        EventHandler<MouseEvent> rRectHandler = new EventHandler<MouseEvent>() {
            double x1, y1, x2, y2, w, h;
            customDBox dbox = new customDBox(gc);

            @Override
            public void handle(MouseEvent event) {
                switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        x1 = event.getX();
                        y1 = event.getY();
                    case "MOUSE_DRAGGED":
                        x2 = event.getX();
                        y2 = event.getY();
                        w = x2 - x1;
                        h = y2 = y1;
                    case "MOUSE_RELEASED":
                        gc.strokeRoundRect(x1, y1, w, h, 10, 10);
                        gc.fillRoundRect(x1, y1, w, h, 10, 10);
                }
            }
        };

        // Store the handler
        currentHandler = rRectHandler;
        canvas.addEventHandler(MouseEvent.ANY, rRectHandler);
    }        
                
    /**
    * Erase lines, shapes, and images on the canvas. 
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    */
    static void erase(Canvas canvas, GraphicsContext gc) { 
        EventHandler<MouseEvent> eraseHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(15);
                switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        gc.beginPath();
                        gc.moveTo(event.getX(), event.getY());
                        gc.stroke();
                    case "MOUSE_DRAGGED":
                        gc.lineTo(event.getX(), event.getY());
                        gc.stroke();
                    case "MOUSE_RELEASED":
                }
            }
        };
        // Store the handler
        currentHandler = eraseHandler;
        canvas.addEventHandler(MouseEvent.ANY, eraseHandler);
    }

    /**
    * Draw a polygon onto the canvas. 
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    * @param n take an int for the number of sides
    * doesn't work yet
    */
    static public void polygon(Canvas canvas, GraphicsContext gc) {
        //can only stroke to the right
        EventHandler<MouseEvent> polygonHandler = new EventHandler<MouseEvent>() {
            double x1, y1, x2, y2, x3, y3, x4, y4, x5, y5,
                    x6, y6, x7, y7, x8, y8, x9, y9, x10, y10;
            
            //in dialog box should ask for number of sides
            //get n from dbox
            customDBox dbox = new customDBox(gc);
            int n = dbox.getVal(); //or issue here?
            //int n = 3;
            double[] xarr = new double[n];
            double[] yarr = new double[n];

            @Override
            public void handle(MouseEvent event) {
                switch (event.getEventType().getName()) {
                    case "MOUSE_PRESSED":
                        //System.out.println(dbox.getVal());
                        x1 = event.getX();
                        y1 = event.getY();
                    case "MOUSE_DRAGGED":
                        //get length of sides here
                       // x2 = event.getX();
                       // y2 = event.getY();
                    case "MOUSE_RELEASED":/*
                        for (int i = 0; i < 6; i++) {
                            xarr[i] = x1 + (i *5);
                        }
                        for (int i = 0; i < 6; i++) {
                            yarr[i] = y1 + (i *5);
                        }                  */
                        x2 = event.getX();
                        y2 = event.getY();
                        //need actual math to find the next coordinates
                        //and insert them into the arrays
                        xarr[0] = x1; xarr[1] = x2; //issue showing here
                        yarr[0] = y1; yarr[1] = y2;
                        for (int i = 2; i < n; i++) {
                            //xarr[i] = 0.5 + Math.round(n*(2 * i));
                            //yarr[i] = 0.5 + Math.round(n*(2 * i + 1));
                        }
 
                        gc.strokePolygon(xarr,yarr,n);
                }
            }
        };
        // Store the handler
        currentHandler = polygonHandler;
        canvas.addEventHandler(MouseEvent.ANY, polygonHandler);
    }
        
    /**
    * pick color and set graphic context's stroke color
    * @param canvas takes a canvas
    * @param gc takes a graphics context
    */
    static public void eyeDropper(Canvas canvas, GraphicsContext gc) {            
        canvas.setOnMousePressed((MouseEvent e) -> {
            double x, y;
            Color color;
            if (e.getButton() == MouseButton.PRIMARY) {
                x = e.getX();
                y = e.getY();
                color = readColor(canvas, x, y);
                    
                gc.setStroke(color);
            }
        });
    }
        
    /**
    * read color at the coordinates method used in eye dropper
    * @param canvas takes a canvas to read color from
    * @param x coordinate on the x-axis
    * @param y coordinate on the y-axis 
    * @return the color of the pixel at x, y coordinates
    */
    static private Color readColor(Canvas canvas, double x, double y) {
        //user clicks on canvas and return color clicked 
        WritableImage image = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
        SnapshotParameters sp = new SnapshotParameters();
        WritableImage snapshot = canvas.snapshot(sp, image);

        PixelReader pr = snapshot.getPixelReader();        
        return pr.getColor((int)x, (int)y);                
    };       

    /*
    static void wipeCanvas(Canvas canvas) {
        canvas.removeEventHandler(EventType.ROOT, line);
    }
    */
}
