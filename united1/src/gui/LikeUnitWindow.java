package gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A JFrame to enter a new pair of like units and define the relationships between them.
 * 
 * @author Sagacious Media
 */
public class LikeUnitWindow extends JFrame
{

  private static final long serialVersionUID = 1L;
  private ActionListener calculatorPad;
  private JTextField unitField;
  private JTextField associatedField;
  private JTextField conversionTextField;

  /**
   * Class constructor.
   * 
   * @param calculatorPad the CalculatorPad instance
   */
  public LikeUnitWindow(ActionListener calculatorPad) 
  {
    super();
    
    this.calculatorPad = calculatorPad;
    
    unitField = new JTextField(10);
    associatedField = new JTextField(10);
    conversionTextField = new JTextField(10);
    
    setLayout();
  }
  
  /**
   * Gets the new user defined unit.
   * 
   * @return the new unit
   */
  public String getUnit()
  {
    return unitField.getText();
  }
  
  /**
   * Gets the (possibly new) associated unit.
   * 
   * @return the associated unit
   */
  public String getAssociatedUnit()
  {
    return associatedField.getText();
  }
  
  /**
   * Gets the amount that the new unit's operand must be multiplied be to convert to the associated
   * unit.
   * 
   * @return the conversion factor
   */
  public Double getConversionFactorUToA()
  {
    return Double.parseDouble(conversionTextField.getText());
  }
  
  /**
   * Gets the amount that the associated unit's operand must be multiplied to convert to the new
   * unit. It's the inverse of {@link #getConversionFactorUToA() getConversionFactorUToA}.
   * 
   * @return the conversion
   */
  public Double getConversionFactorAToU()
  {
    return (1 / getConversionFactorUToA());
  }
  
  /**
   * Sets the layout of this JFrame.
   */
  private void setLayout()
  {   
    setSize(625, 200);
    setTitle("Adding New Like Unit");
    
    JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayout(4, 2));

    JLabel name, associated, weightUTA;
    name = new JLabel("Name of the new unit:");
    associated = new JLabel("Name of associated unit:");
    weightUTA = new JLabel("Conversion factor from new unit to associated unit:");

    panel1.add(name);
    panel1.add(unitField);
    panel1.add(associated);
    panel1.add(associatedField);
    panel1.add(weightUTA);
    panel1.add(conversionTextField);
    add(panel1, "North");

    JPanel panel2 = new JPanel();
    JButton addUnitButton = new JButton("Add Unit");
    addUnitButton.addActionListener(calculatorPad);
    panel2.add(addUnitButton);
    add(panel2, "South");
  }

}
