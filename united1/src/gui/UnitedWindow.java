package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * UnitedWindow object displays Window.
 * 
 * @author Sagacious Media
 */
public class UnitedWindow extends JFrame implements ActionListener
{

  public static final long serialVersionUID = 1L;
  private CalculatorPad calculatorPad;
  private ButtonPanel buttonPanel;
  private JLabel imageLabel;
  private JFileChooser fc = new JFileChooser();

  /**
   * The entry point of the application (which is executed in the main thread of execution).
   */
  public UnitedWindow()
  {
    super();
    setUpLayout();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setTitle("United");
    pack();
  }

  /**
   * Setup and layout this UnitedWindow.
   */
  private void setUpLayout()
  {
    BorderLayout gen = new BorderLayout();
    gen.setHgap(15);
    gen.setVgap(15);
    Container contentPane;

    contentPane = getContentPane();
    contentPane.setLayout(gen);
    contentPane.add(new JLabel(), BorderLayout.EAST);
    contentPane.add(new JLabel(), BorderLayout.WEST);
    imageLabel = new JLabel();
    imageLabel.setPreferredSize(new Dimension(100, 100));
    imageLabel.setIcon(new ImageIcon("unitED_Logo.png"));
    imageLabel.setVisible(true);
    calculatorPad = new CalculatorPad();

    buttonPanel = new ButtonPanel(calculatorPad);

    contentPane.add(imageLabel, BorderLayout.NORTH);
    contentPane.add(calculatorPad, BorderLayout.CENTER);
    contentPane.add(buttonPanel, BorderLayout.SOUTH);

    setJMenuBar(new CalculatorMenuBar(calculatorPad, this, buttonPanel));

    this.setAlwaysOnTop(true); // for history sliding
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
    String c = e.getActionCommand();

    switch (c)
    {
      case "Logo":
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
          File file = fc.getSelectedFile();
          imageLabel.setIcon(new ImageIcon(file.getPath()));
        }
        break;
      default:
        break;
    }
  }
}
