
package calculating;

/**
 * Represents an operation (e.g. 3 ibs + 2.4 ibs).
 * 
 * @author Sagacious Media
 */
public interface Operation
{
  /**
   * Computes the operation.
   * 
   * @return the result of the computation
   * @throws MixedUnitException
   *           if the units make the computation impossible and the situation cannot be resolved by
   *           conversion of either or both operands and units
   */
  public Measurement calculate() throws MixedUnitException;

  /**
   * Generates a String representation of an Operation.
   * 
   * @return a String representation of an Operation
   */
  public String toString();

}
