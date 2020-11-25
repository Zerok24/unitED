package gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.text.DefaultEditorKit;

/**
 * Menu bar for the United Calculator.
 * 
 * @author Sagacious Media
 */
public class CalculatorMenuBar extends JMenuBar implements ActionListener
{

  public static final long serialVersionUID = 1L;
  private ActionListener calculatorPad;
  private ActionListener unitedWindow;
  private ActionListener buttonPanel;
  private JFrame aboutFrame;

  /**
   * Constructs a CalculatorMenuBar object.
   * 
   * @param calculatorPad
   *          ActionListener for the calculatorPad
   * @param unitedWindow
   *          ActionListener for the window
   * @param buttonPanel
   *          ActionListener with buttons for the window
   */
  public CalculatorMenuBar(ActionListener calculatorPad, ActionListener unitedWindow,
      ActionListener buttonPanel)
  {
    super();
    this.calculatorPad = calculatorPad;
    this.unitedWindow = unitedWindow;
    this.buttonPanel = buttonPanel;
    setUpLayout();
  }

  /**
   * Adds an item to the menu.
   * 
   * @param name
   *          The text displayed on the item
   * @param listener
   *          The {@link ActionListener} of the item
   * @param parent
   *          The component in which the item will be placed
   */
  private void addMenuItem(String name, ActionListener listener, JMenu parent)
  {
    JMenuItem item = new JMenuItem(name);
    item.addActionListener(listener);
    parent.add(item);
  }

  /**
   * Set up the layout of the menu bar.
   */
  private void setUpLayout()
  {

    JMenu file, color, help, record, playback, edit;
    JMenuItem menuItem = null;

    file = new JMenu("File");
    color = new JMenu("Colors");
    help = new JMenu("Help");

    record = new JMenu("Record");
    playback = new JMenu("Playback");

    edit = new JMenu("Edit");

    addMenuItem("Default", this, color);
    addMenuItem("JMU", this, color);
    addMenuItem("Dark", this, color);
    file.add(color);

    addMenuItem("Logo", this, file);
    addMenuItem("Print", this, file);
    addMenuItem("Start Recording", calculatorPad, record);
    addMenuItem("Stop Recording", calculatorPad, record);
    addMenuItem("Start Playback", calculatorPad, playback);
    addMenuItem("Stop Playback", calculatorPad, playback);
    addMenuItem("Next", calculatorPad, playback);
    addMenuItem("Previous", calculatorPad, playback);
    file.add(record);
    file.add(playback);
    add(file);

    edit.setMnemonic(KeyEvent.VK_E);
    menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
    menuItem.setText("Cut");
    menuItem.setMnemonic(KeyEvent.VK_T);
    edit.add(menuItem);

    menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
    menuItem.setText("Copy");
    menuItem.setMnemonic(KeyEvent.VK_C);
    edit.add(menuItem);

    menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
    menuItem.setText("Paste");
    menuItem.setMnemonic(KeyEvent.VK_P);
    edit.add(menuItem);
    add(edit);

    addMenuItem("About", this, help);
    addMenuItem("Web", this, help);
    add(help);

    // Set up about window
    aboutFrame = new JFrame();
    aboutFrame.setLayout(new BorderLayout());
    aboutFrame.setSize(200, 250);
    aboutFrame.setTitle("About");
    aboutFrame.add(setUpAboutPage());
    aboutFrame.setVisible(false);
  }


  /**
   * {@inheritDoc} If the event was caused by the pressing of a soft button in the calculator it
   * processes the event.
   * 
   * @param e
   *          {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();
    URI uri;
    Desktop desktop = Desktop.getDesktop();

    switch (command)
    {
      case "About":
        aboutFrame.setVisible(true);
        aboutFrame.setLocation(this.getWidth(), this.getHeight() * 4);
        break;
      case "Dark":
        calculatorPad.actionPerformed(e);
        buttonPanel.actionPerformed(e);
        break;
      case "Default":
        calculatorPad.actionPerformed(e);
        buttonPanel.actionPerformed(e);
        break;
      case "JMU":
        calculatorPad.actionPerformed(e);
        buttonPanel.actionPerformed(e);
        break;
      case "Logo":
        unitedWindow.actionPerformed(e);
        break;
      case "Print":
        calculatorPad.actionPerformed(e);
        break;
      case "Web":
        try
        {
          uri = new URI("http://w3stu.cs.jmu.edu/tuckerzp/United/United_Help.html");
          desktop.browse(uri);
        }
        catch (URISyntaxException e1)
        {
          e1.printStackTrace();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
        break;
      default:
        break;
    }
  }

  /**
   * Sets up the text in the about page.
   * 
   * @return A JTextArea for the about page
   */
  private JTextArea setUpAboutPage()
  {
    JTextArea about = new JTextArea();

    about.append("\n\n  United Calculator\n\n" + "  Version: 3.0\n" + "  Date: 2020.04.29\n\n"
        + "  Author: Sagacious Media");

    about.setEditable(false);

    return about;
  }

}
