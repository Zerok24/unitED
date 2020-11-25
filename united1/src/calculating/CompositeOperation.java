package calculating;

import java.util.ArrayList;
import java.util.List;

/**
 * A Composite of operations.
 * 
 * @author Sagacious Media
 */
public class CompositeOperation implements Operation
{
  private List<Operation> components;

  /**
   * Class constructor.
   */
  public CompositeOperation()
  {
    components = new ArrayList<>();
  }

  /**
   * Adds this operation to the composite.
   * 
   * @param operation
   *          to add to the composite
   */
  public void addOperation(Operation operation)
  {
    components.add(operation);
  }

  /**
   * Gets the components stored in this composite. The composition may contain other Composites or
   * Leafs or both.
   * 
   * @return the list of Operations in the composite
   */
  public List<Operation> getComponents()
  {
    return this.components;
  }

  /**
   * No functionality for calculate.
   * 
   * @throws MixedUnitException if the units make the computation impossible and the situation
   *                            cannot be resolved by conversion of either or both operands and
   *                            units
   */
  @Override
  public Measurement calculate() throws MixedUnitException
  {
    return null;
  }

  /**
   * Override of Object.toString.
   * 
   * @return String representation of the Operations in the composite's list
   */
  @Override
  public String toString()
  {
    String compositeString = "";

    for (Operation o : components)
    {
      compositeString += o.toString();
      compositeString += "\n";
    }
    return compositeString;
  }
}
