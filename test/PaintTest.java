package pain.t;

import java.util.Stack;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PainTTest extends Application{
    
    public PainTTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

     /**
     * Test of canvas, of class PainT.
     */
    @Test
    public void testHas500Canvas() {
        System.out.println("Has 500 canvas");
        Canvas canvas = new Canvas(500,500); 
        assertThat(canvas.getHeight(), is((double)500));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    }

    /**
     * Test of UNDO method, of class PainT.
     */
    @Test
    public void testUNDO() {
        System.out.println("UNDO");
        Stack<Event> Undo = null;
        Stack<Event> Redo = null;
        PainT.UNDO(Undo, Redo);
    }

    /**
     * Test of selectedToolListener method, of class PainT.
     */
    @Test
    public void testSelectedToolListener() {
        System.out.println("selectedToolListener");
        Canvas canvas = new Canvas(500,500); 
        GraphicsContext gc = null;  
        ColorPicker colorpicker = new ColorPicker();    
        Label colorLabel = null;    
        Label toolLabel = null;
        int selectedTool = 0;
        PainT instance = new PainT();
        instance.selectedToolListener(canvas, gc, colorpicker, colorLabel, selectedTool, toolLabel);
    }

    /**
     * Test of main method, of class PainT.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        PainT.main(args);
    }
    
}
