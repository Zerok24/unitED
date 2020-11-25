package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import calculating.io.CupcakeWriter;

class CupcakeWriterTest
{

  @Test
  void testCupcakeWriter()
  {

    File fActual = new File(getClass().getResource("write_actual.cup").getFile());
    File fExpected = new File(getClass().getResource("write_expected.cup").getFile());
    
    @SuppressWarnings("unused")
    CupcakeWriter write = new CupcakeWriter();

    try
    {

      BufferedWriter out = new BufferedWriter(new FileWriter(fActual));
      CupcakeWriter.writeout(out, "3.0 + 3.0 = 6.0");
      CupcakeWriter.writeout(out, "4.0 cupcake - 1 cupcake = 3 cupcake");
      CupcakeWriter.writeout(out, "9 m/h / 3 = 3 m/h");
      out.close();

      BufferedReader inActual = new BufferedReader(new FileReader(fActual));
      BufferedReader inExpected = new BufferedReader(new FileReader(fExpected));

      String l, r;
      while ((l = inExpected.readLine()) != null && (r = inActual.readLine()) != null)
      {
        assertEquals(l, r);
      }
      inActual.close();
      inExpected.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

  }

}
