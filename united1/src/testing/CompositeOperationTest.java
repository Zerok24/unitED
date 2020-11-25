package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import calculating.*;

class CompositeOperationTest
{

  @Test
  void testCalculate()
  {
    CompositeOperation comp = new CompositeOperation();
    try
    {
      assertNull(comp.calculate());
    }
    catch (MixedUnitException e)
    {
      e.printStackTrace();
    }
  }

  @Test
  void testToString()
  {
    CompositeOperation comp = new CompositeOperation();
    LeafOperation leaf1 = new LeafOperation(new Measurement(3.0, null), new Measurement(3.0, null),
        "+", null);
    LeafOperation leaf2 = new LeafOperation(new Measurement(4.0, "ft"), new Measurement(1.0, "ft"),
        "\u2013", null);
    LeafOperation leaf3 = new LeafOperation(new Measurement(2.0, "lbs"),
        new Measurement(3.0, "lbs"), "*", null);

    String expected = 
        "3.0 + 3.0 = 6.0\n4.0 ft \u2013 1.0 ft = 3.0 ft\n2.0 lbs * 3.0 lbs = 6.0 lbs-lbs\n";
    
    comp.addOperation(leaf1);
    comp.addOperation(leaf2);
    comp.addOperation(leaf3);
    
    assertEquals(expected, comp.toString());
  }
  
  @Test
  void testGetComponents()
  {
    CompositeOperation comp = new CompositeOperation();
    LeafOperation leaf1 = new LeafOperation(new Measurement(3.0, null), new Measurement(3.0, null),
        "+", null);
    LeafOperation leaf2 = new LeafOperation(new Measurement(4.0, "ft"), new Measurement(1.0, "ft"),
        "\u2013", null);
    LeafOperation leaf3 = new LeafOperation(new Measurement(2.0, "lbs"),
        new Measurement(3.0, "lbs"), "*", null);

    
    comp.addOperation(leaf1);
    comp.addOperation(leaf2);
    comp.addOperation(leaf3);
    
    List<Operation> list = comp.getComponents();
    assertTrue(list.contains(leaf1));
    assertTrue(list.contains(leaf2));
    assertTrue(list.contains(leaf3));
    assertTrue(list.size() == 3);

    
  }

}
