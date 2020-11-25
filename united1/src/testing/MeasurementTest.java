
package testing;

import static org.junit.jupiter.api.Assertions.*;

import calculating.Measurement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MeasurementTest
{

  private static Measurement measurement;
  
  @BeforeAll
  static void setUpBeforeClass() throws Exception
  {
    measurement = new Measurement(12.204, "ib-ft");
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception
  {
    measurement = null;
  }

  @Test
  void testMeasurment()
  {
    Measurement m = new Measurement(42.0, null);
    String actual = m.getUnit();
    
    assertEquals(null, actual);
  }
    
  @Test
  void testGetOperand()
  {
    Double expected = 12.204;
    Double actual = measurement.getOperand();
    
    assertTrue(Math.abs(expected - actual) < .000001);
  }

  @Test
  void testGetUnit()
  {
    String expected = "ib-ft";
    String actual = measurement.getUnit();
    
    assertTrue(expected.equals(actual));
  }

  @Test
  void testToString()
  {
    String expected = "12.204 ib-ft";
    String actual = measurement.toString();
    assertTrue(expected.equals(actual));
    
    Measurement m = new Measurement(15.7, null);
    expected = "15.7";
    actual = m.toString();
    assertTrue(expected.equals(actual));
    
  }

  @Test
  void testParseMesurment()
  {
    Measurement expected = new Measurement(-42.2003, "mi/g");
    Measurement actual = Measurement.parseMesurement("-42.2003", "mi/g");
    
    assertTrue(expected.toString().equals(actual.toString()));
    
    assertEquals(null, Measurement.parseMesurement("fourty-two", "ibs"));
  }

}
