package calculating;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses a digraph-like data structure to store relationships between units that are represented
 * as Strings with their conversion represented as Doubles.
 * 
 * <br><br>Example of usage:
 * 
 *  <pre> {@code 
 *  UnitDigraph udg = UnitDigraph.createInstance();
 *  
 *  // add the units to the graph
 *  udg.addUnit("kg");
 *  udg.addUnit("g");
 *  
 *  // add the relationships between units
 *  udg.addEdge("g", "kg", 0.001);
 *  udg.addEdge("kg", "g", 1000);
 *  
 *  // To convert units access relationships
 *  operandOne = operandOne * udg.weight("g","kg"); // If the preferred conversion is from g to kg
 *  }</pre>
 *  
 * @author Sagacious Media
 */
public class UnitDigraph
{
  private static boolean exists = false;
  private static UnitDigraph instance;
  private List<String> rootIndex;
  private List<List<UnitNode>> adjacent;
   
  /**
   * A private constructor used by the singleton method {@link #createInstance() createInstance}.
   */
  private UnitDigraph()
  {
    rootIndex = new ArrayList<String>();
    adjacent = new ArrayList<List<UnitNode>>();
  }
  
  /**
   * Adds a unit with no adjacent units (like units) to the digraph.
   * 
   * @param name the name of the unit to be added
   * @throws IllegalArgumentException if the unit has already been added
   */
  public void addUnit(String name)
  {
    if (rootIndex.contains(name))
    {
      throw new IllegalArgumentException("Unit already added");
    }
    
    rootIndex.add(name);
    List<UnitNode> empty;
    empty = new ArrayList<UnitNode>();
    
    adjacent.add(empty);
  }
  
  /**
   * Adds an edge between two units with the weight being the conversion factor between them.
   * 
   * @param rootUnit the unit that will be converted from
   * @param likeUnit the unit that will be converted to
   * @param weight the conversion factor between rootUnit and likeUnit
   * 
   * @throws IllegalArgumentException if either of the units have not been added to the graph
   */
  public void addEdge(String rootUnit, String likeUnit, Double weight)
  {
    if (rootIndex.indexOf(rootUnit) == -1 || rootIndex.indexOf(likeUnit) == -1)
    {
      throw new IllegalArgumentException("Units must be added to graph before their relationships"
          + " can be accessed");
    }
    
    int adjacentIndex = rootIndex.indexOf(rootUnit);
    
    UnitNode u = new UnitNode(weight, likeUnit);
    adjacent.get(adjacentIndex).add(u);
  }
  
  /**
   * Finds the weight on the edge between two nodes (units). The weight represents the conversion 
   * factor to convert from one unit to another.
   * 
   * @param unitOne the first node (the unit being converted from)
   * @param unitTwo the second node (the unit being converted to)
   * @return the weight (the conversion factor) to convert from unitOne to unitTwo or null if there
   * is no such edge
   */
  public Double weight(String unitOne, String unitTwo)
  {
    Double result = null;
    int outerIndex = rootIndex.indexOf(unitOne);
    
    // Early exit
    if (outerIndex == -1)
    {
      return null;
    }
    
    List<UnitNode> innerList = adjacent.get(outerIndex);
    for (UnitNode node : innerList)
    {
      if (node.getName().equals(unitTwo))
      {
        result = node.getWeight();
      }
    }
    
    return result;
  }
  
  
  /**
   * Gives user access to this object using the singleton pattern.
   * 
   * @return the instance of this object
   */
  public static UnitDigraph createInstance() 
  {
	  
	  if (!exists) 
	  {
	    instance = new UnitDigraph();
		  exists = true;
		  return instance;
	  }
	  else 
	  {
	    return instance;
	  }
  }
  
  
  /**
   * Represents a node in a weighted digraph of units.
   * 
   * @author Sagacious Media
   */
  private class UnitNode 
  {
    private Double weight;
    private String name;
    
    /**
     * Constructor.
     * 
     * @param weight the weight between it and its parent
     * @param name the unit's name
     */
    public UnitNode(Double weight, String name)
    {
      this.weight = weight;
      this.name = name;
    }
    
    /**
     * Gets the weight between it and it's parent.
     * 
     * @return the weight
     */
    public Double getWeight()
    {
      return weight;
    }
    
    /**
     * Gets the name of the node.
     * 
     * @return the node's name
     */
    public String getName()
    {
      return name;
    }
  }
}
