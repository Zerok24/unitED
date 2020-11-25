package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import calculating.*;

class UnitDigraphTest
{

  private static UnitDigraph udg;
  
  @BeforeAll
  static void setUpBeforeClass() throws Exception
  {
    udg = UnitDigraph.createInstance();
    udg.addUnit("kg");
    udg.addUnit("g");
    udg.addEdge("g", "kg", 0.001);
    udg.addEdge("kg", "g", 1000.0);
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception
  {
    udg = null;
  }

  @Test
  void testWeightOneAdjacent()
  {
    Double expected = 1000.0;
    Double actual = udg.weight("kg", "g");
    assertTrue(Math.abs(expected - actual) < .000001);
  }
  
  @Test
  void testWeightNotAdjacent()
  {
    Double expected = null;
    Double actual = udg.weight("g", "mg");
    assertEquals(expected, actual);
    
    actual = udg.weight("gif", "mg");
    assertEquals(expected, actual);
  }
  
  @Test
  void testWeightMultipleAdjacent()
  {
    udg.addUnit("ibs");
    udg.addEdge("ibs", "g", 0.453592);
    udg.addEdge("g", "ibs", 2.20462);
    
    Double expected = 2.20462;
    Double actual = udg.weight("g", "ibs");
    
    assertTrue(Math.abs(expected - actual) < .000001);
  }
  
  @Test
  void testAddEdgeMultiple()
  {
    try
    {
      udg.addUnit("l");
      udg.addUnit("kl");
      udg.addUnit("gl");
      udg.addUnit("ml");
      
      udg.addEdge("l", "ml", .0001);
      udg.addEdge("l", "kl", 1000.0);
      udg.addEdge("l", "gl", 1000000000.0);
    }
    catch (IllegalArgumentException iae)
    {
      fail("IllegalArgumentException thrown when multiple units are added to be adjecent to one"
          + " unit");
    }
 
  }
  
  @Test
  void testAddEdgeUnitDoesNotExist()
  {
    assertThrows(IllegalArgumentException.class, () -> udg.addEdge("stones", "kg", 6.35));
    assertThrows(IllegalArgumentException.class, () -> udg.addEdge("kg", "stones", 0.157473));
  }

  @Test
  void testAddUnitAlreadyExists()
  {
    assertThrows(IllegalArgumentException.class, () -> udg.addUnit("g"));
  }
  
  @Test
  void testCreateInstanceAlreadyExists()
  {
    UnitDigraph expected = udg;
    UnitDigraph actual = UnitDigraph.createInstance();
    
    assertTrue(expected.equals(actual));
  }
}
