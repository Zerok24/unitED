package calculating.io;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * I/O utility to write out a series of operation as .cup files.
 * 
 * @author Emma Macaluso, Sagacious Media
 */
public class CupcakeWriter
{
  /**
   * Write out the operation to a .cup file. 
   *
   * @param out BufferedReader to use to write out
   * @param operation line to write out to the file
   * @throws IOException if file is unavailable
   */
  public static void writeout(BufferedWriter out, String operation) throws IOException
  {
    out.write(operation);
    out.newLine(); 
  }

}
