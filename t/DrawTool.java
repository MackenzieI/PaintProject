package pain.t;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class DrawTool {
        /*
        * Draw a straight line onto the canvas. Live draw not yet supported.
        * @ param canvas takes a canvas
        * @ param gc takes a graphics context
        */
        static public void straightLine(Canvas canvas, GraphicsContext gc) {            
            canvas.addEventHandler(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    double x1, y1, x2, y2;

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
       
            });
        }
        
        /*
        * Draw a freehand/pencil tool line onto the canvas. 
        * @ param canvas takes a canvas
        * @ param gc takes a graphics context
        */
        static public void pencil(Canvas canvas, GraphicsContext gc) {            
            canvas.addEventHandler(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
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
            });
        }          
                
        /*
        * Draw a rectangle onto the canvas. 
        * @ param canvas takes a canvas
        * @ param gc takes a graphics context
        */
        static public void rectangle(Canvas canvas, GraphicsContext gc) {
            //can only stroke to the right
            canvas.addEventHandler(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    double x1, y1, x2, y2, w, h;

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
            });
        }
            
        /*
        * Draw a square onto the canvas. 
        * @ param canvas takes a canvas
        * @ param gc takes a graphics context
        */
        static public void square(Canvas canvas, GraphicsContext gc) {
            //can only stroke to the right
            canvas.addEventHandler(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    double x1, y1, x2, y2, w, h;

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
            });
        }
                
        /*
        * Draw an ellipse onto the canvas. 
        * @ param canvas takes a canvas
        * @ param gc takes a graphics context
        */
        static public void ellipse(Canvas canvas, GraphicsContext gc) {
            //can only stroke to the right
            //fills left like a square
            canvas.addEventHandler(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    double x1, y1, x2, y2, w, h;

                    @Override
                    public void handle(MouseEvent event) {
                        switch (event.getEventType().getName()) {
                            case "MOUSE_PRESSED":
                                x1 = event.getX();
                                y1 = event.getY();
                            case "MOUSE_DRAGGED":
                                x2 = event.getX();
                                y2 = event.getY();

                            case "MOUSE_RELEASED":
                                w = x2 - x1;
                                h = y2 = y1;
                                gc.strokeOval(x1, y1, w, h);
                                gc.fillOval(x1, y1, w, h);
                        }
                    }
            });
        }
                
        /*
        * Draw a rounded rectangle onto the canvas. 
        * @ param canvas takes a canvas
        * @ param gc takes a graphics context
        */
        static public void roundRectangle(Canvas canvas, GraphicsContext gc) {
            //can only stroke to the right
            canvas.addEventHandler(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    double x1, y1, x2, y2, w, h;

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
            });
        }        
                
        /*
        * Erase lines, shapes, and images on the canvas. 
        * @ param canvas takes a canvas
        * @ param gc takes a graphics context
        */
        static void erase(Canvas canvas, GraphicsContext gc) {
            canvas.addEventHandler(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {

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
            });
    }        
}
