package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import calculating.io.CupcakeReader;

class CupcakeReaderTest
{



  
  @BeforeAll
  static void setUpBeforeClass() throws Exception
  {
    // For coverage
    @SuppressWarnings("unused")
    CupcakeReader coverageIsABadMeasurementOfTests = new CupcakeReader();
    coverageIsABadMeasurementOfTests = null;
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception
  {
  }

  @Test
  void testReadRecording()
  {
    File f = new File(getClass().getResource("example01.cup").getFile());
    List<String> expected = new ArrayList<>();
    expected.add("5.12 ibs-ft * 2.0 = 10.24 ibs-ft");
    expected.add("10.0 - 2.0 = 8.0");
    expected.add("44.0 ft / 2.0 ft = 22.0");
    expected.add("2.0 ft ^ 2 = 4.0 ft^2");
    expected.add("9 m/h / 3 = 3 m/h");
    expected.add("900 m + 15 km = 15.9 km");
    
    try
    {
      BufferedReader in = new BufferedReader(new FileReader(f));
      List<String> actual = CupcakeReader.readRecording(in);
      assertLinesMatch(expected, actual);
    }
    catch (IOException e)
    {
      fail("IOException thrown in correctly formatted");
    }
  }

}
