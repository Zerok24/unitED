package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * The GUI component that displays the buttons.
 * 
 * @author Sagacious Media
 */
public class ButtonPanel extends JPanel implements ActionListener
{

  public static final long serialVersionUID = 1L;
  private ActionListener listener;
  private ArrayList<JButton> buttons;
  private JPanel buttonPanel;
  private JPanel historyPanel;
  private JPanel opPanel;
  private JPanel lastRow;

  /**
   * The Constructor.
   * 
   * @param listener
   *          an ActionListener object for the JButtons
   */
  public ButtonPanel(ActionListener listener)
  {
    super();
    this.listener = listener;
    buttons = new ArrayList<>();
    setUpLayout();

  }

  /**
   * Setup and layout this ButtonPanel.
   */
  private void setUpLayout()
  {

    BorderLayout gen = new BorderLayout();
    gen.setHgap(10);
    gen.setVgap(10);
    setLayout(gen);
    GridLayout numberGrid = new GridLayout(5, 3);
    numberGrid.setVgap(15);
    numberGrid.setHgap(15);
    buttonPanel = new JPanel(numberGrid);

    addButton("+/-", buttonPanel);
    addButton("C", buttonPanel);
    addButton("R", buttonPanel);
    for (Integer i = 1; i < 10; i++)
    {
      addButton(i.toString(), buttonPanel);
    }
    addButton("0", buttonPanel);

    addButton("\u232b", buttonPanel);

    lastRow = new JPanel();
    
    GridLayout opGrid = new GridLayout(5, 2);
    opGrid.setVgap(15);
    opGrid.setHgap(15);
    opPanel = new JPanel(opGrid);

    GridLayout hisGrid = new GridLayout(1, 1);
    hisGrid.setHgap(15);
    hisGrid.setVgap(15);
    historyPanel = new JPanel(hisGrid);

    addButton(">", historyPanel);
    addButton("+", opPanel);

    // ActionCommand for exponent is different from text on button.
    JButton exp = new JButton("x^y");
    exp.setActionCommand("^");
    exp.addActionListener(listener);
    opPanel.add(exp);
    buttons.add(exp);

    addButton("-", opPanel);
    addButton("1/x", opPanel);

    addButton("*", opPanel);
    addButton("(", opPanel);

    addButton("/", opPanel);
    addButton(")", opPanel);

    addButton("=", opPanel);

    add(buttonPanel, BorderLayout.WEST);
    add(opPanel, BorderLayout.CENTER);
    add(historyPanel, BorderLayout.EAST);
    add(lastRow, BorderLayout.SOUTH);
  }

  /**
   * Creates a Jbuttons with the given text name and adds them to this panel.
   * 
   * @param text
   *          the name of the GUI Jbutton
   * @param panel
   *          the panel to add the button to
   */
  private void addButton(String text, JPanel panel)
  {

    JButton button;
    button = new JButton(text);

    if (text.equals("R") || text.equals("C") || text.equals("\u232b") || text.equals("+/-"))
    {
      button.setForeground(new Color(150, 0, 13));
    }

    button.setSize(new Dimension(40, 40));
    button.setActionCommand(text);
    button.addActionListener(this.listener);
    panel.add(button);
    buttons.add(button);
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

    switch (command)
    {
      case "Dark":
        for (JButton b : buttons)
        {
          b.setBackground(new Color(178, 178, 178));
        }
        break;
      case "Default":
        for (JButton b : buttons)
        {
          b.setBackground(UIManager.getColor("Button.background"));
        }
        break;
      case "JMU":
        for (JButton b : buttons)
        {
          b.setBackground(new Color(203, 182, 119));
        }
        break;
      default:
        break;
    }

  }
}
