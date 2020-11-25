package united;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.UnitedWindow;

/**
 * A calculator application that uses units.
 * 
 * @author Sagacious Media
 *
 */
public class United implements Runnable
{

  /**
   * The entry point of the application.
   * 
   * @param args nothing
   */
  public static void main(String[] args)
  {
    
    // Call run() on the event dispatch thread
    United app = new United();
    try
    {
      SwingUtilities.invokeAndWait(app);
    }
    catch (InvocationTargetException ite)
    {
      ite.printStackTrace();
      System.exit(1);
    }
    catch (InterruptedException ie)
    {
      ie.printStackTrace();
      System.exit(2);
    }
    
  }
  
  /**
   * Run the GUI in the event dispatch thread.
   */
  @Override
  public void run()
  {
    setLookAndFeel();
    
    UnitedWindow window = new UnitedWindow();
    window.setVisible(true);
  }
  
  /**
   * Set the look and feel for the application.
   */
  private void setLookAndFeel()
  {
    // Setup the look and feel
    boolean done = false;
    try 
    {
      // Use Metal if it is available
      UIManager.LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
      for (int i=0; i<lfs.length && !done; i++)
      {
        if ("Metal".equals(lfs[i].getName())) 
        {
          UIManager.setLookAndFeel(lfs[i].getClassName());
          done = true;
        }
      }

      // If Metal isn't available, use the system look and feel
      if (!done)
      {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(lookAndFeel);
      }
    } 
    catch (ClassNotFoundException cnfe)
    {
      // Use the default look and feel
    }
    catch(IllegalAccessException iae)
    {
      // Use the default look and feel
    }
    catch (InstantiationException ie)
    {
      // Use the default look and feel
    }
    catch (UnsupportedLookAndFeelException ulale)
    {
      // Use the default look and feel
    }
    
  }
}
