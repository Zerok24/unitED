package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import calculating.*;

class LeafOperationTest
{

  private static UnitDigraph ud;
  
  @BeforeAll
  static void setup() 
  {
    ud = UnitDigraph.createInstance();
    ud.addUnit("Muffins");
    ud.addUnit("Cupcakes");
    ud.addEdge("Muffins", "Cupcakes", 10.0);
    ud.addEdge("Cupcakes", "Muffins", 10.0);
    ud.addUnit("Cake");
  }
  
  @Test
  void testLeafOperation()
  {
    Measurement m = new Measurement(2.0, "ibs");
    Measurement n = new Measurement(3.6, "ibs");
    assertThrows(IllegalArgumentException.class, () -> new LeafOperation(null, m, "ibs", null));
    assertThrows(IllegalArgumentException.class, () -> new LeafOperation(m, null, "ibs", null));
    assertThrows(IllegalArgumentException.class, () -> new LeafOperation(m, n, null, null));
    assertThrows(IllegalArgumentException.class, () -> new LeafOperation(null, null, "ibs", null));
    assertThrows(IllegalArgumentException.class, () -> new LeafOperation(m, null, null, null));
    assertThrows(IllegalArgumentException.class, () -> new LeafOperation(null, m, null, null));
    assertThrows(IllegalArgumentException.class, () -> new LeafOperation(null, null, null, null));
    assertThrows(IllegalArgumentException.class, () -> new LeafOperation(m, n, "", null));

    try
    {
      new LeafOperation(m, n, "ibs", null);
    }
    catch (IllegalArgumentException iae)
    {
      fail("IllegalArgumentException thrown when not expected!");
    }
  }

  @Test
  void testCalculateMultiplyWhenOneUnitIsNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", null);

    LeafOperation op = new LeafOperation(a, b, "*", null);

    Measurement expected = new Measurement(196.938, "ibs");

    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateMultiplyBothUnitAreNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", null);
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "*", null);
    Measurement expected = new Measurement(196.938, null);
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertTrue(actual.getUnit() == null);
  }

  @Test
  void testCalculateMultiplyWithNotUniqueUnits() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", "ft");
    LeafOperation op = new LeafOperation(a, b, "*", null);
    Measurement expected = new Measurement(196.938, "ibs-ft");
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateMultiplyWithOneComboUnit() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs-ft");
    Measurement b = Measurement.parseMesurement("4.2", "ft");
    LeafOperation op = new LeafOperation(a, b, "*", null);
    Measurement expected = new Measurement(196.938, "ibs-ft-ft");
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateMultiplyWhenTheFirstUnitIsNullAndTheOtherIsCombo() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", null);
    Measurement b = Measurement.parseMesurement("4.2", "ibs-ft");
    LeafOperation op = new LeafOperation(a, b, "*", null);
    Measurement expected = new Measurement(196.938, "ibs-ft");
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateMultiplyWhenFirstIsNullAndSecongISAUniqueUnit() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", null);
    Measurement b = Measurement.parseMesurement("4.2", "ibs");
    LeafOperation op = new LeafOperation(a, b, "*", null);
    Measurement expected = new Measurement(196.938, "ibs");
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateAddTwoLikeUnits() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", "ibs");

    LeafOperation op = new LeafOperation(a, b, "+", null);

    Measurement expected = new Measurement(51.09, "ibs");

    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateAddWithTwoCmboUnits() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "cupcake-cupcake");
    Measurement b = Measurement.parseMesurement("4.2", "cupcake-cupcake");
    LeafOperation op = new LeafOperation(a, b, "+", null);
    Measurement expected = new Measurement(51.09, "cupcake-cupcake");
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateAddWhenBothUnitsAreNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", null);
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "+", null);
    Measurement expected = new Measurement(51.09, null);
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(actual.getUnit(), null);

  }

  @Test
  void testCalculateDivide() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", null);

    LeafOperation op = new LeafOperation(a, b, "/", null);

    Measurement expected = new Measurement(11.164285, "ibs");

    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateDivideWhenUnitIsNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", null);
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "/", null);
    Measurement expected = new Measurement(11.164285, null);
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertTrue(actual.getUnit() == null);
  }

  @Test
  void testCalculateDivideWhenTwoUniqueUnits() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", "ft");
    LeafOperation op = new LeafOperation(a, b, "/", null);
    Measurement expected = new Measurement(11.164285, "ibs/ft");
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateDivideWhenOneUnitIsNullAndTheOtherIsCombo() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs-ft");
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "/", null);
    Measurement expected = new Measurement(11.164285, "ibs-ft");
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertTrue(expected.getUnit().equals(actual.getUnit()));
  }

  @Test
  void testCalculateDivideWhenTwoUnitsAreTheSameTest() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", "ibs");
    LeafOperation op = new LeafOperation(a, b, "/", null);
    Measurement expected = new Measurement(11.164285, null);
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(null, actual.getUnit());
  }

  @Test
  void testCalculateDivideWhenComboUnitAndNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "mi/g");
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "/", null);
    Measurement expected = new Measurement(11.164285, "mi/g");
    Measurement actual = op.calculate();
    assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .000001);
    assertEquals(expected.getUnit(), actual.getUnit());
  }

  @Test
  void testCalculateDivideWhenTheFirstUnitIsNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", null);
    Measurement b = Measurement.parseMesurement("4.2", "ibs");
    LeafOperation op = new LeafOperation(a, b, "/", null);
    Measurement expected = new Measurement(11.164285, "1/ibs");
    Measurement actual = op.calculate();
    assertEquals(expected.getOperand(), actual.getOperand(), .000001);
    assertTrue(expected.getUnit().equals(actual.getUnit()));
  }

  @Test
  void testIntegerPowerRightUnitNull()
  {
    Measurement a = Measurement.parseMesurement("-2.035", "ibs-ft");
    Measurement b = Measurement.parseMesurement("5", null);
    LeafOperation lo = new LeafOperation(a, b, "^", null);
    Measurement expected = Measurement.parseMesurement("-34.89973005", "ibs-ft^5.0");
    Measurement actual;
    try
    {
      actual = lo.calculate();
      assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .00001,
          "Incorrect " + "operand result when operandOne is negative and operandTwo is positive");
      assertTrue(expected.getUnit().equals(actual.getUnit()),
          "Incorrect unit in result when" + " unitOne is non-null and unitTwo is null");
    }
    catch (MixedUnitException mue)
    {
      fail();
    }
  }
  
  @Test
  void testIntegerPowerLeftUnitNullRightUnitNotNull()
  {
    Measurement a = Measurement.parseMesurement("2.7", null);
    Measurement b = Measurement.parseMesurement("2", "cake");
    LeafOperation badLo = new LeafOperation(a, b, "^", null);
    assertThrows(MixedUnitException.class, () -> badLo.calculate());
  }
  
  @Test
  void testIntegerPowerBothUnitsNull()
  {
    Measurement a = Measurement.parseMesurement("2.7", null);
    Measurement b = Measurement.parseMesurement("2", null);
    LeafOperation lo = new LeafOperation(a, b, "^", null);
    Measurement expected = Measurement.parseMesurement("7.29", null);
    try
    {
      Measurement actual = lo.calculate();
      assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .00001);
      assertEquals(expected.getUnit(), actual.getUnit());
    }
    catch (MixedUnitException mue)
    {
      fail("MixedUnitException thrown when not expected.");
    }
  }
  
  @Test
  void testIntegerPowerRightUnitNullLeftUnitHasPower()
  {
    Measurement a = Measurement.parseMesurement("2.7", "ibs-ft^2");
    Measurement b = Measurement.parseMesurement("2", null);
    LeafOperation lo = new LeafOperation(a, b, "^", null);
    Measurement expected = Measurement.parseMesurement("7.29", "ibs-ft^4.0");
    try
    {
      Measurement actual = lo.calculate();
      assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .00001,
          "Incorrect operand" + " result when both operands are positive");
      assertTrue(expected.getUnit().equals(actual.getUnit()), "Incorrect unit in result when"
          + " unitOne is non-null and unitTwo is null (unitOne contains an exponent of its own)");
    }
    catch (MixedUnitException mue)
    {
      fail("MixedUnitException thrown when not expected.");
    }
  }
  
  @Test
  void testIntegerPowerLeftUnitRaisedToNonNumberPower()
  {
    Measurement a = Measurement.parseMesurement("2.7", "ibs-ft^gibberish");
    Measurement b = Measurement.parseMesurement("2", null);
    LeafOperation lo = new LeafOperation(a, b, "^", null);
    Measurement expected = Measurement.parseMesurement("7.29", "ibs-ft^gibberish^2.0");
    Measurement actual;
    try
    {
      actual = lo.calculate();
      assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .00001,
          "Incorrect operand" + " result when both operands are positive");
      assertTrue(expected.getUnit().equals(actual.getUnit()), "Incorrect unit in result when"
          + " unitOne is non-null and unitTwo is null (unitOne contains an exponent of its own)");
    }
    catch (MixedUnitException mue)
    {
      fail("MixedUnitException thrown when not expected.");
    }

    a = Measurement.parseMesurement("2.7", "ibs-ft^gibberish^2.0");
    b = Measurement.parseMesurement("2", null);
    lo = new LeafOperation(a, b, "^", null);
    expected = Measurement.parseMesurement("7.29", "ibs-ft^gibberish^2.0^2.0");
    try
    {
      actual = lo.calculate();
      assertTrue(Math.abs(expected.getOperand() - actual.getOperand()) < .00001,
          "Incorrect operand" + " result when both operands are positive");
      assertTrue(expected.getUnit().equals(actual.getUnit()), "Incorrect unit in result when"
          + " unitOne is non-null and unitTwo is null (unitOne contains an exponent of its own)");
    }
    catch (MixedUnitException mue)
    {
      fail("MixedUnitException thrown when not expected.");
    }
  }

  @Test
  void testToStringWhrnUnitsAreTheSame()
  {
    Measurement a = Measurement.parseMesurement("2.8", "ibs");
    Measurement b = Measurement.parseMesurement("4", "ibs");
    LeafOperation lo = new LeafOperation(a, b, "+", null);
    String expected = "2.8 ibs + 4.0 ibs = 6.8 ibs";
    String actual = lo.toString();
    assertTrue(expected.toString().equals(actual.toString()));

  }
  
  @Test
  void testToStringWhenUnitsAreDifferent()
  {
    Measurement a = Measurement.parseMesurement("2.8", "ft");
    Measurement b = Measurement.parseMesurement("4", "ibs");
    LeafOperation lo = new LeafOperation(a, b, "+", null);
    String expected = "Mixed Unit Error";
    String actual = lo.toString();
    assertEquals(expected.toString(), actual.toString());

  }

  @Test
  void testParseLeafOperationWhenReqMultTest()
  {
    String expression = "2 ibs * 3";
    Measurement a = Measurement.parseMesurement("2", "ibs");
    Measurement b = Measurement.parseMesurement("3", null);

    LeafOperation expected = new LeafOperation(a, b, "*", null);
    LeafOperation actual = LeafOperation.parseLeafOperation(expression, null);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  void testParseLeafOperationNullTest()
  {
    String expression = "2 ibs + five pounds";
    LeafOperation actual = LeafOperation.parseLeafOperation(expression, null);
    assertEquals(null, actual);
  }

  @Test
  void testParseLeafOperationSubtractionTest()
  {
    String expression = "2 \u2013 3.2 ibs";
    Measurement a = Measurement.parseMesurement("2", null);
    Measurement b = Measurement.parseMesurement("3.2", "ibs");
    LeafOperation expected = new LeafOperation(a, b, "\u2013", null);
    LeafOperation actual = LeafOperation.parseLeafOperation(expression, null);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  void testParseLeafOperationExpoenentTest()
  {
    String expression = "2.0 mi/h^2.0 ^ 3.0";
    Measurement a = Measurement.parseMesurement("2.0", "mi/h^2.0");
    Measurement b = Measurement.parseMesurement("3.0", null);
    LeafOperation expected = new LeafOperation(a, b, "^", null);
    LeafOperation actual = LeafOperation.parseLeafOperation(expression, null);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  void testParseLeafOperationTwoExponentTest()
  {
    String expression = "2.0 mi^2.0 ^ 3.0";
    Measurement a = Measurement.parseMesurement("2.0", "mi^2.0");
    Measurement b = Measurement.parseMesurement("3.0", null);
    LeafOperation expected = new LeafOperation(a, b, "^", null);
    LeafOperation actual = LeafOperation.parseLeafOperation(expression, null);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  void testCalculateDivideUnitsAreBothNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("40.01", null);
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "/", null);
    Measurement actual = op.calculate();
    assertEquals(null, actual.getUnit());

  }

  @Test
  void testCalculateDivideUnitsAreBothTheSame() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs-ft");
    Measurement b = Measurement.parseMesurement("4.2", "ibs-ft");
    LeafOperation op = new LeafOperation(a, b, "/", null);

    assertTrue(Math.abs(11.164285 - op.calculate().getOperand()) < .000001);
    assertEquals(null, op.calculate().getUnit());
  }

  @Test
  void testCalculateSubtractUnitsAreBothTheSame() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs-ft");
    Measurement b = Measurement.parseMesurement("4.2", "ibs-ft");
    LeafOperation op = new LeafOperation(a, b, "\u2013", null);

    assertEquals(new Measurement(42.69, null).getOperand(), op.calculate().getOperand());
    assertEquals("ibs-ft", op.calculate().getUnit());
  }

  @Test
  void testCalculateSubtractUnitsBothAreNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", null);
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "\u2013", null);
    op.calculate();
    assertEquals(new Measurement(42.69, null).getOperand(), op.calculate().getOperand());
    assertEquals(op.calculate().getUnit(), null);
  }

  @Test
  void testCalculateSubtractMixedUnitsOneNull()
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "\u2013", null);
    assertThrows(MixedUnitException.class, () -> op.calculate());
  }

  @Test
  void testCalculateSubtractMixedUnits() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", "fts");
    LeafOperation op = new LeafOperation(a, b, "\u2013", null);
    //Measurement measurement = op.calculate();
    assertThrows(MixedUnitException.class, () -> op.calculate());
  }

  @Test
  void testCalculateAddMixedUnitsOneNull()
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "+", null);
    assertThrows(MixedUnitException.class, () -> op.calculate());
  }

  @Test
  void testCalculateAdditionThrowsMixedUnitExceptions()
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", "fts");
    LeafOperation op = new LeafOperation(a, b, "+", null);
    assertThrows(MixedUnitException.class, () -> op.calculate());
  }

  @Test
  void testCalculateMultUnitsBothAreTheSame() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs-ft");
    Measurement b = Measurement.parseMesurement("4.2", "ibs-ft");
    LeafOperation op = new LeafOperation(a, b, "*", null);

    assertEquals(new Measurement(46.89 * 4.2, null).getOperand(), op.calculate().getOperand());
    assertEquals("ibs-ft-ibs-ft", op.calculate().getUnit());
  }

  @Test
  void testCalculateMultUnitsBothAreNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", null);
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "*", null);
    op.calculate();
    assertEquals(new Measurement(46.89 * 4.2, null).getOperand(), op.calculate().getOperand());
    assertEquals(op.calculate().getUnit(), null);
  }

  @Test
  void testCalculateMultMixedUnitsOneNull() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("46.89", "ibs");
    Measurement b = Measurement.parseMesurement("4.2", null);
    LeafOperation op = new LeafOperation(a, b, "*", null);
    assertEquals(new Measurement(46.89 * 4.2, null).getOperand(), op.calculate().getOperand());
    assertEquals(op.calculate().getUnit(), "ibs");
  }

  @Test
  void testCalculateMultLikeUnits() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("10", "Cake");
    Measurement b = Measurement.parseMesurement("10", "Cake");
    LeafOperation op = new LeafOperation(a, b, "*", null);
    Measurement actual = op.calculate();
    assertTrue(Math.abs(100 - actual.getOperand()) < .00001);
    assertEquals("Cake-Cake", op.calculate().getUnit());

  }

  @Test
  void testLikeUnitsTestThrowsException() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("5", null);
    Measurement b = Measurement.parseMesurement("1", "b");
    LeafOperation ob = new LeafOperation(a, b, "+", null);
    assertThrows(MixedUnitException.class, () -> ob.calculate());
  }

  @Test
  void testLikeUnitsThrowsExceptionTest() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("5", "kb");
    Measurement b = Measurement.parseMesurement("1", "b");
    LeafOperation ou = new LeafOperation(a, b, "+", null);
    assertThrows(MixedUnitException.class, () -> ou.calculate());
  }

  @Test
  void testLikeUnitsThrowsExceptionWhenOneUnitIsNullTest() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("5", "kb");
    Measurement b = Measurement.parseMesurement("1", null);
    LeafOperation oy = new LeafOperation(a, b, "+", null);
    assertThrows(MixedUnitException.class, () -> oy.calculate());
  }

  @Test
  void testLikeUnitsAddscorrectlyTest() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("5", null);
    Measurement b = Measurement.parseMesurement("1", null);
    LeafOperation op = new LeafOperation(a, b, "+", null);
    assertEquals(new Measurement(6.0, null).toString(), op.calculate().toString());
  }
  
  @Test
  void testLikeUnitsAddsCupcakesAndMuffinsCorrecctlyTest() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("1", "Cupcakes");
    Measurement b = Measurement.parseMesurement("1", "Cupcakes");
    LeafOperation op = new LeafOperation(a, b, "+", "Muffins");
    Measurement actual = op.calculate();
    assertEquals(new Measurement(20.0, "Muffins").toString(), actual.toString());
  }
  
  @Test
  void testConvertToLeftUnitCustomUnits() throws MixedUnitException
  {
    ud.addUnit("w");
    ud.addUnit("kw");
    ud.addEdge("w", "kw", 0.001);
    ud.addEdge("kw", "w", 1000.0); 
    LeafOperation op = LeafOperation.parseLeafOperation("2 kw + 200 w", "kw");
    
    Measurement expected = Measurement.parseMesurement("2.2", "kw");
    Measurement actual = op.calculate();
    
    assertEquals(expected.getOperand(), actual.getOperand(), .000001);
    assertTrue(expected.getUnit().equals(actual.getUnit()));
  }
  
  @Test
  void testLikeUnitsSubtractCupcakesAndMuffinsCorrecctlyTest() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("1", "Cupcakes");
    Measurement b = Measurement.parseMesurement("1", "Cupcakes");
    LeafOperation op = new LeafOperation(a, b, "\u2013", "Muffins");
    Measurement actual = op.calculate();
    assertEquals(new Measurement(0.0, "Muffins").toString(), actual.toString());
  }
  @Test
  void testLikeUnitsMultiplyCupcakesAndMuffinsCorrecctlyTest() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("1", "Cupcakes");
    Measurement b = Measurement.parseMesurement("1", "Cupcakes");
    LeafOperation op = new LeafOperation(a, b, "*", "Muffins");
    Measurement actual = op.calculate();
    assertEquals(new Measurement(100.0, "Muffins-Muffins").toString(), actual.toString());
  }

  @Test
  void testLikeUnitsDivideCupcakesAndMuffinsCorrecctlyTest() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("1", "Cupcakes");
    Measurement b = Measurement.parseMesurement("1", "Cupcakes");
    LeafOperation op = new LeafOperation(a, b, "/", "Muffins");
    Measurement actual = op.calculate();
    assertEquals(new Measurement(1.0, null).toString(), actual.toString());
  }
  
  @Test
  void testLikeUnitsAddsCupcakesPlusMuffinsandConvertsTOMuffinsCorrecctlyTest() 
      throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("10", "Muffins");
    Measurement b = Measurement.parseMesurement("1", "Cupcakes");
    LeafOperation op = new LeafOperation(a, b, "+", "Muffins");
    Measurement actual = op.calculate();
    assertEquals(new Measurement(20.0, "Muffins").toString(), actual.toString());
  }
  
  @Test
  void testLikeUnitsOnlyOneCanBeConverted() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("10", "Muffins");
    Measurement b = Measurement.parseMesurement("1", null);
    LeafOperation op = new LeafOperation(a, b, "*", "Muffins");
    Measurement actual = op.calculate();
    assertEquals(new Measurement(10.0, "Muffins").toString(), actual.toString());
  }
  
  @Test
  void testLikeUnitsNoneCanBeConverted() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("10", null);
    Measurement b = Measurement.parseMesurement("1", null);
    LeafOperation op = new LeafOperation(a, b, "*", "Muffins");
    Measurement actual = op.calculate();
    assertEquals(new Measurement(10.0, null).toString(), actual.toString());
  }
  
  @Test
  void testLikeUnitsOnlyRightCanBeConverted() throws MixedUnitException
  {
    Measurement a = Measurement.parseMesurement("10",null);
    Measurement b = Measurement.parseMesurement("1",  "Muffins");
    LeafOperation op = new LeafOperation(a, b, "*", "Muffins");
    Measurement actual = op.calculate();
    assertEquals(new Measurement(10.0, "Muffins").toString(), actual.toString());
  }
  @Test
  void testUnknownUnitsThrowsIllegalArgmentException() throws MixedUnitException
  {
    assertThrows(IllegalArgumentException.class, () -> ud.addEdge("meters", "ft", 1.0));
  }
  @Test
  void testAlreadyAddedUnitIllegalArgmentException() throws MixedUnitException
  {
    assertThrows(IllegalArgumentException.class, () -> ud.addUnit("Muffins"));
  }
  
}
