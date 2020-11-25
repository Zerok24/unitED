package calculating.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * I/O utility to read-in .cup files.
 * 
 * @author Sagacious Media
 */
public class CupcakeReader
{
  /**
   * Reads a .cup file generated from recording user calculations into a collection of Strings.
   * 
   * @param in a BufferedReader whose cursor points to the first line in the .cup file.
   * @return the operations inside the .cup file
   * @throws IOException if the BufferedReader throws an IOException while reading the .cup file
   */
  public static List<String> readRecording(BufferedReader in) throws IOException
  {
    List<String> operations = new ArrayList<String>();
    String line;
    
    while ((line = in.readLine()) != null)
    {
      operations.add(line);
    }
    
    return operations;
  }
}
