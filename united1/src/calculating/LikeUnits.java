package calculating;

/**
 * A utility to determine whether units are like, and, if so, convert the units to match before
 * the computation is performed in LeafOperation.
 * 
 * @author Sagacious Media
 */
public class LikeUnits
{

  /**
   * Converts the liked Units to the desired units.
   * 
   * @param leftOperand the left operand of an operation
   * @param rightOperand the right operand of an operation
   * @param desiredUnit the unit that the user specified the result of the calculation to be in
   * @return Whether the conversion was successful
   */
  public static boolean likeUnits(Measurement leftOperand, Measurement rightOperand,
      String desiredUnit)
  {
    boolean likeUnits = false;

    if (desiredUnit != null)
    {
      likeUnits = converter(rightOperand, desiredUnit) && converter(leftOperand, desiredUnit);
    }
    else 
    {
      likeUnits = converter(leftOperand, rightOperand.getUnit());
    }
    return likeUnits;
  }

 /**
   * Converts a operand to the desiredUnit.
   * 
   * @param operand
   *          the operand to be converted
   * @param desiredUnit
   *          the unit that the operand is being converted too
   * @return whether conversion was true
   */
  public static boolean converter(Measurement operand, String desiredUnit)

  {
    UnitDigraph figraph = UnitDigraph.createInstance();
    String orginalUnit = operand.getUnit();
    Double equivalence = operand.getOperand();
    try
    {
      if((desiredUnit != null) && (orginalUnit != null) && desiredUnit.equals(orginalUnit)) 
      {
        equivalence = equivalence * 1.0;
        operand.setOperand(equivalence);
        operand.setUnit(desiredUnit);
        return true;
      }
      else if (figraph.weight(orginalUnit, desiredUnit) != null)
      {
        equivalence = equivalence * figraph.weight(orginalUnit, desiredUnit);
        operand.setOperand(equivalence);
        operand.setUnit(desiredUnit);
        return true;
      }
      else
      {
        return false;
      }
    }
    catch (IndexOutOfBoundsException e)
    {
      return false;
    }
  }
}
