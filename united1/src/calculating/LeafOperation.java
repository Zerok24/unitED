package calculating;

/**
 * The leaf of the Operation composite pattern. Represents a single operation.
 * 
 * @author Sagacious Media
 */
public class LeafOperation implements Operation
{
  private Measurement leftOperand;
  private Measurement rightOperand;
  private String operator;
  private String desiredUnit;
  /**
   * The constructor.
   * @param leftOperand
   *          the Measure on the left side of the operation
   * @param rightOperand
   *          the Measure on the right side of the operation
   * @param operator
   *          the operator in the binary operation (e.g. +, -, /, *)
   * @param desiredUnit
   *          the unit that the user specified the result of the calculation to be in
   * @throws IllegalArgumentException
   *           if any arguments are null
   */
  public LeafOperation(Measurement leftOperand, Measurement rightOperand,
      String operator, String desiredUnit) throws IllegalArgumentException
  {
    if (leftOperand == null || rightOperand == null || operator == null
        || operator.isEmpty())
    {
      throw new IllegalArgumentException(
          "LeafOperation constructor requires arguments to not be"
              + " null or empty");
    }

    this.leftOperand = leftOperand;
    this.rightOperand = rightOperand;
    this.operator = operator;
    this.desiredUnit = desiredUnit;
  }

  /**
   * Calculates the operation and returns the operand unit pair as a Measurement object.
   * 
   * @return the resulting number and resulting unit
   * @throws MixedUnitException
   *           if the units make the computation impossible and the situation cannot be resolved by
   *           conversion of either or both operands and units
   */
  @Override
  public Measurement calculate() throws MixedUnitException
  {
    Double evaluatedOperand = null;
    String evaluatedUnit = null;
    Measurement type = null;
    Double operandOne = leftOperand.getOperand();
    Double operandTwo = rightOperand.getOperand();
    String unitOne = leftOperand.getUnit();
    String unitTwo = rightOperand.getUnit();
    boolean likeUnits = false;

    if (LikeUnits.likeUnits(leftOperand, rightOperand, desiredUnit))
    {
      likeUnits = true;
      operandOne = leftOperand.getOperand();
      operandTwo = rightOperand.getOperand();
      unitOne = leftOperand.getUnit();
      unitTwo = rightOperand.getUnit();
    }
    switch (operator)
    {
      case "*":
        evaluatedOperand = operandOne * operandTwo;
        if (unitOne != null && unitTwo != null)
        {
          evaluatedUnit = String.format("%s-%s", unitOne, unitTwo);
          type = new Measurement(evaluatedOperand, evaluatedUnit);
        }
        else if (unitOne != null)
        {
          evaluatedUnit = unitOne;
          type = new Measurement(evaluatedOperand, evaluatedUnit);
        }
        else if (unitTwo != null)
        {
          evaluatedUnit = unitTwo;
          type = new Measurement(evaluatedOperand, evaluatedUnit);
        }
        else
        {
          type = calculateHelper(evaluatedOperand, unitOne, unitTwo, likeUnits);
        }
        break;
      case "+":
        if ((unitOne == null && unitTwo != null)
            || (unitOne != null && unitTwo == null)
            || (unitOne != null && unitTwo != null && !unitOne.equals(unitTwo))
            && !likeUnits)
        {
          throw new MixedUnitException();
        }
        else
        {
          evaluatedOperand = operandOne + operandTwo;
          type = calculateHelper(evaluatedOperand, unitOne, unitTwo, likeUnits);
        }
        break;
      case "\u2013":
        if ((unitOne == null && unitTwo != null)
            || (unitOne != null && unitTwo == null)
            || (unitOne != null && unitTwo != null && !unitOne.equals(unitTwo))
                && !likeUnits)
        {
          throw new MixedUnitException();
        }
        else
        {
          evaluatedOperand = operandOne - operandTwo;
          type = calculateHelper(evaluatedOperand, unitOne, unitTwo, likeUnits);
        }
        break;
      case "^":
        if (unitTwo != null)
        {
          throw new MixedUnitException();
        }
        
        evaluatedOperand = Math.pow(operandOne, operandTwo);    
        if (unitOne == null)
        {
          evaluatedUnit = null;
        } else
        {
          String[] unitOneSplit = unitOne.split("\\^");
          if (unitOneSplit.length < 2)
          {
            evaluatedUnit = String.format("%s^%s", unitOne, operandTwo);
          }
          else
          {
            try
            {
              Double existingExponent = Double.parseDouble(unitOneSplit[1]);
              evaluatedUnit = String.format("%s^%s", unitOneSplit[0], 
                  existingExponent * operandTwo);
            }
            catch (NumberFormatException nfe)
            {
              evaluatedUnit = String.format("%s^%s", unitOne, operandTwo);
            }           
          }
        }
        
        type = new Measurement(evaluatedOperand, evaluatedUnit);
        break;
      case "/":
        evaluatedOperand = operandOne / operandTwo;
        if (unitOne == null && unitTwo == null)
        {
          evaluatedUnit = null;
          type = new Measurement(evaluatedOperand, evaluatedUnit);
        }
        else if (unitOne == null)
        {
          evaluatedUnit = String.format("1/%s", unitTwo);
          type = new Measurement(evaluatedOperand, evaluatedUnit);
        }
        else if (unitTwo == null)
        {
          evaluatedUnit = unitOne;
          type = new Measurement(evaluatedOperand, evaluatedUnit);
        }
        else if (unitOne != null && unitTwo != null && unitOne.equals(unitTwo))
        {
          evaluatedUnit = null;
          type = new Measurement(evaluatedOperand, evaluatedUnit);
        }
        else
        {
          evaluatedUnit = String.format("%s/%s", unitOne, unitTwo);
          type = new Measurement(evaluatedOperand, evaluatedUnit);
        }
        break;
      default:
        break;
    }
    return type;

  }

  /**
   * The string representation of this Operation as it would be written mathematically in infix
   * notation.
   * 
   * @return a String representation of the object
   */
  @Override
  public String toString()
  {
    try
    {
      String s = (leftOperand + " " + operator + " " + rightOperand + " = "
          + this.calculate().toString());
      return s;
    }
    catch (MixedUnitException mue)
    {
      return "Mixed Unit Error";
    }
  }

  /**
   * A factory to more easily create LeafOperations.
   * 
   * @param expression
   *          the mathematical expression in infix notation
   * @param desiredUnit
   *          the unit that the user specified the result of the calculation to be in
   * @return a LeafOperation to represent the passed String expression
   */
  public static LeafOperation parseLeafOperation(String expression, String desiredUnit)
  {
    LeafOperation result;
    String operandOne, operandTwo;
    String unitOne, unitTwo;
    Measurement leftOperand, rightOperand;

    // Split expression by letters and digits
    String[] operands = expression.split("([+*\\u2013]| / | \\^ )");
    String[] operandOneWithUnit = operands[0].trim().split(" ");
    String operator = expression.replaceAll(
        "\\-?[0-9]+(\\.[0-9]+)?( (1/)?[a-zA-Z]+(\\-[a-zA-Z]+|/[a-zA-Z]+)?)?(\\^[0-9](\\.[0-9])?)?",
        "").trim();
    String[] operandTwoWithUnit = operands[1].trim().split(" ");

    if (operandOneWithUnit.length < 2)
    {
      operandOne = operandOneWithUnit[0];
      unitOne = null;
    }
    else
    {
      operandOne = operandOneWithUnit[0];
      unitOne = operandOneWithUnit[1].trim();
    }

    if (operandTwoWithUnit.length < 2)
    {
      operandTwo = operandTwoWithUnit[0];
      unitTwo = null;
    }
    else
    {
      operandTwo = operandTwoWithUnit[0];
      unitTwo = operandTwoWithUnit[1].trim();
    }

    leftOperand = Measurement.parseMesurement(operandOne, unitOne);
    rightOperand = Measurement.parseMesurement(operandTwo, unitTwo);

    try
    {
      LeafOperation lo = new LeafOperation(leftOperand, rightOperand, operator, desiredUnit);
      result = lo;
    }
    catch (IllegalArgumentException iae)
    {
      result = null;
    }
    return result;
  }

  /**
   * A helper function for the {@link #calculate() calculate} method. Sets the unit of the result
   * Measurement if conditions are met.
   * 
   * @param evaluated the evaluated operand (without the unit)
   * @param unitOne the unit of the first operand
   * @param unitTwo the unit of the second operand
   * @param likeUnits a boolean representing if the units are like
   * @return a Measurement that represents the result of the calculation
   */
  private Measurement calculateHelper(Double evaluated, String unitOne,
      String unitTwo, Boolean likeUnits)
  {
    String evaluatedUnit = null;
    Measurement calculated;
    if (unitOne == null && unitTwo == null)
    {
      evaluatedUnit = null;
    }
    else if (unitOne.equals(unitTwo))
    {
      evaluatedUnit = unitOne;
    } 
    calculated = new Measurement(evaluated, evaluatedUnit);
    return calculated;
  }
}
