package dufrois.gui;

/**
 * Constants used throughout the program in order to keep consistency
 * 
 * @author Christian Dufrois
 * @version 25.09.2017
 */
public class WindowVars
{
    /**
     * Space used by the window frame
     */
    public static final int NONUSABLE_HEIGHT = 47;
    
    public static final int NONUSABLE_WIDTH = 18;
    
    public static final int X_BORDER_BUFFER = 20;
    
    public static final int Y_BORDER_BUFFER = 20;
    
    public static final int FRAME_LEFT = 50;
    
    public static final int FRAME_TOP = 50;
    
    public static final int WIDTH = 1000;
    
    public static final int HEIGHT = 600;
    
    public static final int BUTTON_HEIGHT = 25;
    
    public static final int BUTTON_WIDTH = 100;
    
    public static int getUsableWidth()
    {
        return WIDTH - (2 * X_BORDER_BUFFER) - NONUSABLE_WIDTH;
    }
    
    public static int getUsableHeight()
    {
        return HEIGHT - (2 * Y_BORDER_BUFFER) - NONUSABLE_HEIGHT;
    }
}
