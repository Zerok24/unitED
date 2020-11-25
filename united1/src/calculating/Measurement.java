package calculating;

/**
 * Represents a measurement.
 * 
 * @author Sagacious Media
 */
public class Measurement
{
  private Double operand;
  private String unit;
  
  /**
   * Initializes attributes.
   * 
   * @param operand the operand
   * @param unit the unit
   */
  public Measurement(Double operand, String unit)
  {
    this.operand = operand;
    this.unit = unit;
  }

  /**
   * Gets the operand of the measurement.
   * 
   * @return the operand
   */
  public Double getOperand()
  {
    return operand;
  }

  /**
   * Gets the unit of the measurement.
   * 
   * @return the unit
   */
  public String getUnit()
  {
    return unit;
  }
  
  /**
   * Sets the operand of a Measurement.
   * 
   * @param operand the operand to set the Measurement to
   */
  public void setOperand(Double operand)
  {
    this.operand = operand;
  }
  
  /**
   * Sets the unit of a Measurement.
   * 
   * @param unit the unit to set the measurement to
   */
  public void setUnit(String unit)
  {
    this.unit = unit;
  }
  
  /**
   * Creates a String representation of the measurement (e.g. 24.3 ibs).
   * 
   * @return a String representation of the object
   */
  @Override
  public String toString() 
  {
    String result;
    if (unit == null)
    {
      result = operand.toString();
    }
    else
    {
      result = (operand + " " + unit);
    }
    
    return result;
  }
  
  /**
   * A factory method to create Measurement objects.
   * 
   * @param operand the operand
   * @param unit the unit
   * @return the object created by parsing the arguments of this method
   */
  public static Measurement parseMesurement(String operand, String unit)
  {
    Measurement result;
    try
    {
      Double o = Double.parseDouble(operand);
      result = new Measurement(o, unit);
    }
    catch (NumberFormatException nfe)
    {
      result = null;
    }
    
    return result;
  }
}
