package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;

import calculating.*;
import calculating.io.CupcakeReader;
import calculating.io.CupcakeWriter;

/**
 * Takes in the users data and Displays Users input. (it is the actionLister of the ButtonPanel
 * object)
 * 
 * @author Sagacious Media
 */
public class CalculatorPad extends JPanel implements ActionListener, KeyListener
{
  public static final long serialVersionUID = 1L;
  private final JFileChooser fc = new JFileChooser(); // for playback
  private final JFileChooser fcr = new JFileChooser(); // for record
  private final JPopupMenu copypasteMenu = new JPopupMenu();
  private final String[] defaultUnits = {"Enter Unit", "Cupcakes", "Muffins", "Cake"};
  private boolean isNegative;
  private boolean isRecording;
  private BufferedWriter out; // for recording
  private String contentsOfDisplay;
  private JTextField display;
  private JTextField inputField;
  private JFrame historyPane;
  private JButton historyButton;
  private JComboBox<String> units;
  private JComboBox<String> resultUnits;
  private JTextArea history;
  private LikeUnitWindow likeUnitFrame;
  private List<String> playbackEquations;
  private ListIterator<String> playbackIterator;
  private List<String> equationHistory;
  private Timer playbackTimer;
  private UnitDigraph digraph;

  /**
   * The Constructor of the CalculatorPad.
   */
  public CalculatorPad()
  {
    super();

    equationHistory = new ArrayList<String>();
    isNegative = false;
    isRecording = false;
    contentsOfDisplay = "";
    display = new JTextField(10);
    inputField = new JTextField(5);
    playbackEquations = new ArrayList<>();
    playbackIterator = playbackEquations.listIterator();
    units = new JComboBox<String>(defaultUnits);
    units.setEditable(true);
    resultUnits = new JComboBox<String>(defaultUnits);
    resultUnits.setEditable(true);

    digraph = UnitDigraph.createInstance();
    digraph.addUnit("Muffins");
    digraph.addUnit("Cupcakes");
    digraph.addEdge("Muffins", "Cupcakes", .10);
    digraph.addEdge("Cupcakes", "Muffins", 10.0);
    digraph.addUnit("Cake");
    digraph.addEdge("Cupcakes", "Cake", .10);
    digraph.addEdge("Cake", "Cupcakes", 10.0);

    setLayout();

    historyPane = new JFrame();
    historyPane.setLayout(new BorderLayout());
    historyPane.setSize(250, 200);
    historyPane.setTitle("Calculation History");
    historyPane.setUndecorated(true);
    historyPane.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

    history = new JTextArea();
    history.setEditable(false);
    JMenuItem copyMenuItem = new JMenuItem("Copy");
    copyMenuItem.addActionListener(this);
    JMenuItem pasteMenuItem = new JMenuItem("Paste");
    pasteMenuItem.addActionListener(this);
    copypasteMenu.add(copyMenuItem);
    copypasteMenu.add(pasteMenuItem);

    history.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent e)
      {
        switch (e.getModifiersEx())
        {
          case InputEvent.BUTTON3_DOWN_MASK:
          {
            copypasteMenu.show(e.getComponent(), e.getX(), e.getY());
            copypasteMenu.setInvoker(e.getComponent());
            break;
          }
          default:
            break;
        }
      }
    });
    historyButton = new JButton("<");
    historyButton.addActionListener(this);
    historyButton.setPreferredSize(new Dimension(40, 40));
    historyButton.setFont(new Font("Arial", Font.PLAIN, 11));

    JScrollPane scroll = new JScrollPane(history, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroll.setVisible(true);

    JPanel historyPanel = new JPanel(new GridBagLayout());
    historyPanel.add(historyButton);

    historyPane.add(historyPanel, BorderLayout.EAST);
    historyPane.add(scroll, BorderLayout.CENTER);
    historyPane.setVisible(false);

    likeUnitFrame = new LikeUnitWindow(this);
    likeUnitFrame.setVisible(false);
  }

  /**
   * {@inheritDoc} If the event was caused by the pressing of a soft button in the calculator it
   * processes the event.
   * 
   * @param event
   *          {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent event)
  {
    String command;

    command = event.getActionCommand();
    if (command.equals("R"))
    {
      clearPrevError();
      contentsOfDisplay = "";
      inputField.setText("");
      units.setSelectedIndex(0);
      display.setText("");
      isNegative = false;
    }
    else if (command.equals("C"))
    {
      clearPrevError();
      inputField.setText("");
      units.setSelectedIndex(0);
      isNegative = false;
    }
    else if (command.equals("="))
    {
      String unit = getSelectedUnit();
      String input = unit.equals(defaultUnits[0]) ? inputField.getText()
          : inputField.getText() + " " + unit;
      if (isValidInput(input))
      {
        contentsOfDisplay = display.getText();
        String building = contentsOfDisplay;
        contentsOfDisplay = evaluateExpression(contentsOfDisplay + " " + input);
        building += " " + input + " = " + contentsOfDisplay;
        inputField.setText("");
        units.setSelectedIndex(0);
        display.setText(contentsOfDisplay);

        String contentsOfHistory = "";
        for (String eq : equationHistory)
        {
          contentsOfHistory = contentsOfHistory.concat(eq);
          contentsOfHistory = contentsOfHistory.concat("\n");
        }
        history.setText(contentsOfHistory);

        isNegative = false;

        if (isRecording)
        {
          try
          {
            CupcakeWriter.writeout(out, building);
          }
          catch (IOException ioe)
          {
            ioe.printStackTrace();
          }
        }
      }
      else
      {
        display.setText("Error: Not a valid input");
      }
    }
    else if (command.equals("-"))
    {
      clearPrevError();
      String unit = getSelectedUnit();
      String input = unit.equals(defaultUnits[0]) ? inputField.getText()
          : inputField.getText() + " " + unit;
      if (isValidInput(input))
      {
        contentsOfDisplay += input + " " + "\u2013";
        display.setText(contentsOfDisplay);
        inputField.setText("");
        units.setSelectedIndex(0);
        isNegative = false;

      }
      else if (!display.getText().equals(""))
      {
        contentsOfDisplay = display.getText() + " " + "\u2013";
        display.setText(contentsOfDisplay);
        isNegative = false;
      }
      else
      {
        display.setText("Error: Not a valid input");
        isNegative = false;
      }

    }
    else if (command.equals("/") || command.equals("*") || command.equals("+"))
    {
      clearPrevError();
      String unit = getSelectedUnit();
      String input = unit.equals(defaultUnits[0]) ? inputField.getText()
          : inputField.getText() + " " + unit;
      if (isValidInput(input) && display.getText().equals(""))
      {
        contentsOfDisplay += input + " " + command;
        display.setText(contentsOfDisplay);
        inputField.setText("");
        units.setSelectedIndex(0);
        isNegative = false;
      }
      else if (!display.getText().equals(""))
      {
        contentsOfDisplay = display.getText() + " " + command;
        display.setText(contentsOfDisplay);
        isNegative = false;

      }
      else
      {
        display.setText("Error: Not a valid input");
      }

    }
    else if (command.equals("\u232b")) // Backspace Button
    {
      clearPrevError();
      String input = inputField.getText();
      if (input.equals("-"))
      {
        isNegative = false;
      }
      if (input.length() > 0)
      {
        input = input.substring(0, input.length() - 1);
        inputField.setText(input);
      }

    }
    else if (command.equals("+/-"))
    {
      clearPrevError();
      String input = inputField.getText();

      if (!isNegative)
      {
        inputField.setText("-" + input);
        isNegative = true;
      }
      else
      {
        inputField.setText(input.substring(1, input.length()));
        isNegative = false;
      }

    }

    else if (command.equals(">"))
    {

      Container c = this.getParent().getParent();
      historyPane.setLocation(c.getLocationOnScreen());

      final java.util.Timer tim = new java.util.Timer();
      long delay = 10;
      long period = 100;
      
      // move the window so it appears to be sliding
      
      tim.scheduleAtFixedRate(new java.util.TimerTask()
      {
        int inc = 1;

        public void run()
        {
          historyPane.setVisible(true);
          int w = 50;
          if ((int) historyPane.getLocationOnScreen().getX() + w < c.getWidth()
              + c.getLocationOnScreen().getX())
          {

            historyPane.setLocation((int) c.getLocationOnScreen().getX() + w * inc,
                (int) c.getLocationOnScreen().getY() + c.getHeight() / 3);

          }
          else
          {
            historyPane.setLocation((int) c.getLocationOnScreen().getX() + c.getWidth(),
                (int) c.getLocationOnScreen().getY() + c.getHeight() / 3);
            tim.cancel();
          }
          inc += 1;
        }
      }, delay, period);
      historyButton = ((JButton) event.getSource());
      historyButton.setVisible(false);
    }
    else if (command.equals("<"))
    {
      Container c = this.getParent().getParent();

      final java.util.Timer tim = new java.util.Timer();
      long delay = 10;
      long period = 100;
      
      // move the window it it appears to be sliding
      tim.scheduleAtFixedRate(new java.util.TimerTask()
      {
        public void run()
        {
          int w = 50;
          if (historyPane.getLocationOnScreen().getX() + historyPane.getWidth() > c.getWidth()
              + c.getLocationOnScreen().getX())
          {
            historyPane.setLocation((int) historyPane.getLocationOnScreen().getX() - w,
                (int) c.getLocationOnScreen().getY() + c.getHeight() / 3);
          }
          else
          {
            historyPane.setVisible(false);
            tim.cancel();
          }
        }
      }, delay, period);

      historyButton.setVisible(true);
    }
    else if (command.equals("1/x"))
    {
      String input = inputField.getText();
      double in;
      try
      {
        in = Double.parseDouble(input);
        in = 1 / in;
        String s = String.format("%.2f", in);
        inputField.setText(s);
      }
      catch (NumberFormatException nfe)
      {
        inputField.setText("");
      }

    }
    else if (command.equals("0") || command.equals("1") || command.equals("2")
        || command.equals("3") || command.equals("4") || command.equals("5") || command.equals("6")
        || command.equals("7") || command.equals("8") || command.equals("9"))
    {
      clearPrevError();
      inputField.setText(inputField.getText() + command);
    }
    else if (command.equals("add"))
    {
      likeUnitFrame.setVisible(true);
      likeUnitFrame.setAlwaysOnTop(true);
    }
    else if (command.equals("add"))
    {
      likeUnitFrame.setVisible(true);
    }
    else if (command.equals("Add Unit"))
    {
      String newUnit = likeUnitFrame.getUnit();
      String associated = likeUnitFrame.getAssociatedUnit();
      Double wUtoA = likeUnitFrame.getConversionFactorUToA();
      Double wAtoU = likeUnitFrame.getConversionFactorAToU();
      digraph = UnitDigraph.createInstance();

      digraph.addUnit(newUnit);
      digraph.addUnit(associated);
      digraph.addEdge(newUnit, associated, wUtoA);
      digraph.addEdge(associated, newUnit, wAtoU);
      likeUnitFrame.setVisible(false);
    }
    else if (command.equals("convert"))
    {
      String unit = (String) resultUnits.getSelectedItem();
      String input = unit.equals(defaultUnits[0]) ? "0.0" : "0.0" + " " + unit;
      if (isValidInput(input))
      {
        contentsOfDisplay = display.getText() + " + ";
        contentsOfDisplay = evaluateExpression(contentsOfDisplay + " " + input);
        display.setText(contentsOfDisplay);
      }
    }
    else if (command.equals("^"))
    {
      clearPrevError();
      String unit = getSelectedUnit();
      String input = unit.equals(defaultUnits[0]) ? inputField.getText()
          : inputField.getText() + " " + unit;
      if (isValidInput(input) && display.getText().equals(""))
      {
        contentsOfDisplay += input + " " + "^";
        display.setText(contentsOfDisplay);
        inputField.setText("");
        units.setSelectedIndex(0);
        isNegative = false;
      }
      else if (!display.getText().equals(""))
      {
        contentsOfDisplay = display.getText() + " " + command;
        display.setText(contentsOfDisplay);
        isNegative = false;
      }
      else
      {
        display.setText("Error: Not a valid input");
      }

    }
    else if (command.equals("Print"))

    {
      try
      {
        history.print();
      }
      catch (PrinterException e)
      {
        e.printStackTrace();
      }
    }
    else if (command.equals("Start Playback"))
    {
      int returnVal = fc.showOpenDialog(getParent());
      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
        File f = fc.getSelectedFile();
        BufferedReader in;

        try
        {
          in = new BufferedReader(new FileReader(f));
          playbackEquations = CupcakeReader.readRecording(in);
          playbackIterator = playbackEquations.listIterator();
          playbackTimer = new Timer(5000, this);
          playbackTimer.setActionCommand("Next");
          playbackTimer.start();
          ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Next");
          actionPerformed(ae);
        }
        catch (IOException ioe)
        {
          ioe.printStackTrace();
        }

      }
    }
    else if (command.equals("Next"))
    {
      playbackTimer.restart();
      if (playbackIterator.hasNext())
      {
        display.setText(playbackIterator.next());
      }
      else
      {
        display.setText("Playback finished");
        playbackTimer.stop();
      }
    }
    else if (command.equals("Previous"))
    {
      playbackTimer.restart();

      // previous needs to be called twice.
      if (playbackIterator.hasPrevious())
      {
        playbackIterator.previous();
      }

      if (playbackIterator.hasPrevious())
      {
        display.setText(playbackIterator.previous());
        playbackIterator.next();
      }
    }
    else if (command.equals("Stop Playback"))
    {
      display.setText("");
      playbackTimer.stop();
    }
    else if (command.equals("Start Recording"))
    {
      if (!isRecording)
      {
        isRecording = true;

        int returnVal = fcr.showOpenDialog(getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
          File f = fcr.getSelectedFile();

          try
          {
            out = new BufferedWriter(new FileWriter(f));

          }
          catch (IOException ioe)
          {
            ioe.printStackTrace();
          }

        }
      }

    }
    else if (command.equals("Stop Recording"))
    {
      isRecording = false;
      try
      {
        out.close();
      }
      catch (IOException ioe)
      {
        ioe.printStackTrace();
      }
    }
    else if (command.equals("Copy"))
    {
      JTextArea textArea = (JTextArea) copypasteMenu.getInvoker();
      textArea.copy();
    }
    else if (command.equals("Paste"))
    {
      JTextArea textArea = (JTextArea) copypasteMenu.getInvoker();
      textArea.paste();
    }
    else if (command.contentEquals("JMU"))
    {
      changeColors("JMU");
    }
    else if (command.contentEquals("Default"))
    {
      changeColors("Default");
    }
    else if (command.contentEquals("Dark"))
    {
      changeColors("Dark");
    }
  }

  /**
   * {@inheritDoc} If the character of the typed key is an operator, it will cause an
   * {@link ActionEvent} to be sent to the local {@link #actionPerformed(ActionEvent event)
   * actionPerformed} implementation as if the soft key for the operator were pressed.
   */
  @Override
  public void keyTyped(KeyEvent e)
  {
    processKey(e);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void keyPressed(KeyEvent e)
  {
    processKey(e);
  }

  /**
   * {@inheritDoc} If the character of the released key is an operator, it will cause the operator
   * that was shown in the input field to be removed.
   * 
   * @param e
   *          {@inheritDoc}
   */
  @Override
  public void keyReleased(KeyEvent e)
  {
    processKey(e);
  }

  /**
   * Processes {@link #keyTyped(KeyEvent) keyTyped} and {@link #keyReleased(KeyEvent) keyReleased}
   * events. Treats operators as if the soft button in the GUI were pressed.
   * 
   * @param e
   *          the event to be processed
   * @see KeyEvent
   */
  private void processKey(KeyEvent e)
  {
    int id = e.getID();

    if (id == KeyEvent.KEY_TYPED)
    {
      char c = e.getKeyChar();
      String event = String.valueOf(c);
      if (event.matches("[+*^\\/-]"))
      {
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, event);
        actionPerformed(ae);
      }
    }
    else if (id == KeyEvent.KEY_RELEASED)
    {
      if (inputField.getText().matches("[+*^\\/-]"))
      {
        inputField.setText("");
      }
    }

  }

  /**
   * Evaluates the current expression.
   * 
   * @param expression
   *          the mathematical expression in infix notation
   * @return a String of the value of the current expression
   */
  private String evaluateExpression(String expression)
  {
    String result;
    LeafOperation lo;
    if (!resultUnits.getSelectedItem().equals("Enter Unit"))
    {
      lo = LeafOperation.parseLeafOperation(expression, (String) resultUnits.getSelectedItem());
    }
    else
    {
      lo = LeafOperation.parseLeafOperation(expression, null);
    }

    // Early exit
    if (lo == null)
    {
      return "Error: Cannot parse expression";
    }

    try
    {
      Measurement calculation = lo.calculate();
      result = calculation.toString();

      String buildingEquation = expression + " = " + result;
      equationHistory.add(buildingEquation);
    }
    catch (MixedUnitException mue)
    {
      result = "Error: Mixed unit type";
    }

    return result;
  }

  /**
   * Adds the unit in the drop down menu if it is unique.
   * 
   * @return The unit in the drop down menu
   */
  private String getSelectedUnit()
  {
    String unit = (String) units.getSelectedItem();

    if (units.getSelectedIndex() == -1)
    {
      units.addItem(unit);
    }
    return unit;
  }

  /**
   * Determines whether the contents of the inputField are valid.
   * 
   * @param input
   *          the contents of the inputField
   * @return true if the contents of the inputField are valid, false otherwise
   */
  private boolean isValidInput(String input)
  {
    final String regex = "\\-?[0-9]+(\\.[0-9]+)?( (1/)?[a-zA-Z]+(\\-[a-zA-Z]+|/[a-zA-Z]+)?)?";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input);

    return matcher.matches();
  }

  /**
   * If the user starts an action after an error occurs clear the error message.
   */
  private void clearPrevError()
  {
    String error = "Error: Not a valid input";
    if (display.getText().contains(error))
    {
      display.setText(display.getText().replace(error, ""));
    }
    error = "Error: Cannot parse expression";
    if (display.getText().contains(error))
    {
      display.setText("");
    }

  }

  /**
   * Changes the color scheme of the calculator pad.
   * 
   * @param scheme
   *          the desired color scheme
   */
  private void changeColors(String scheme)
  {
    // Purple, Light Purple
    Color[] jmu = {new Color(181, 123, 206), new Color(218, 204, 230)};
    // Slate, Light Gray
    Color[] dark = {new Color(51, 51, 51), new Color(214, 214, 214)};

    switch (scheme)
    {
      case "Dark":
        display.setBackground(dark[0]);
        display.setForeground(Color.white);
        inputField.setBackground(dark[1]);
        break;
      case "Default":
        display.setBackground(Color.PINK);
        display.setForeground(UIManager.getColor("TextField.foreground"));
        inputField.setBackground(UIManager.getColor("TextField.background"));
        break;
      case "JMU":
        display.setBackground(jmu[0]);
        display.setForeground(UIManager.getColor("TextField.foreground"));
        inputField.setBackground(jmu[1]);
        break;
      default:
        break;
    }

  }

  /**
   * Sets up the layout of the CalculatorPad.
   */
  private void setLayout()
  {
    BorderLayout layout = new BorderLayout();
    layout.setHgap(10);

    display.setEditable(false);
    display.setBackground(Color.PINK);

    JButton convertButton = new JButton("convert");
    convertButton.addActionListener(this);

    inputField.setText("");
    inputField.setHorizontalAlignment(JTextField.RIGHT);
    inputField.addKeyListener(this);

    JButton button = new JButton("add");
    button.addActionListener(this);

    JPanel displayPanel = new JPanel();
    displayPanel.setPreferredSize(new Dimension(300, 50));
    GridLayout grid = new GridLayout(2, 1);
    grid.setHgap(10);
    grid.setVgap(10);
    displayPanel.setLayout(grid);
    displayPanel.add(display);
    displayPanel.add(inputField);

    JPanel unitOptionPanel = new JPanel();
    unitOptionPanel.setPreferredSize(new Dimension(200, 50));
    unitOptionPanel.setLayout(grid);

    unitOptionPanel.add(resultUnits);
    unitOptionPanel.add(convertButton);
    unitOptionPanel.add(units);
    unitOptionPanel.add(button);

    FlowLayout flow = new FlowLayout();
    setLayout(flow);
    add(displayPanel);
    add(unitOptionPanel);
  }
}
