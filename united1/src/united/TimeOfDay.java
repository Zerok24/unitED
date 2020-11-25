package united;

/**
 * A mutable encapsulation of the time during a day.
 *
 * @author Prof. David Bernstein, James Madison University
 */
public class TimeOfDay
{
  private static final int SECONDS_PER_MINUTE = 60;
  private static final int MINUTES_PER_HOUR   = 60;
  private static final int HOURS_PER_DAY      = 24;
  private static final int SECONDS_PER_HOUR   = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
  private static final int SECONDS_PER_DAY    = SECONDS_PER_HOUR * HOURS_PER_DAY;


  private static final int NOON               = 12;
  private static final int MIDNIGHT           = 12;
  private static final String AM              = "AM";
  private static final String PM              = "PM";
  
  private int    elapsed; // Number of seconds since midnight      



  /**
   * @param hours   Hours since/before (if positive/negative) midnight
   * @param minutes Minutes since/before (if positive/negative) midnight
   * @param seconds Seconds since/before (if positive/negative) midnight
   */
  public TimeOfDay(int hours, int minutes, int seconds)
  {
    // Emma Mac :)
    elapsed = 0;
    changeBy(hours, minutes, seconds);
  }

  /**
   * Change this TimeOfDay by the given number of hours, minutes,
   * and seconds.  Note that both positive and negative values are
   * allowed, and that the signs of the different parameters need
   * not agree. Positive signs represent changes forward in time,
   * and negative signs represent changes backward in time.
   * 
   * Zach Tucker made this
   *
   * @param hours   Hours forward/backward
   * @param minutes Minutes forward/backward
   * @param seconds Seconds forward/backward
   */
  public void changeBy(int hours, int minutes, int seconds)
  {
    int   adjustment;

    adjustment = hours   * SECONDS_PER_HOUR 
        + minutes * SECONDS_PER_MINUTE
        + seconds;

    elapsed = (elapsed + adjustment) % SECONDS_PER_DAY;

    if (elapsed < 0) elapsed += SECONDS_PER_DAY;
  }

  /**
   * Convert a military time to an equivalent String representation of
   * a civilian time. Note: This method does not validate the parameters.
   * 
   * George Tisdelle
   *
   * @param hour   The hour of the day in the interval [0, 23]
   * @param minute The minute of the day in the interval [0, 59]
   * @param second The second of the day in the interval [0, 59]
   * @return       A String representation of a time in civilian format
   */
  public static String formatAsCivilian(int hour, int minute, int second)
  {
    int        hh;        
    String     period;

    hh = hour;        
    if (hh < NOON) 
    {
      period = AM;
      if (hh == 0) hh = MIDNIGHT;
    } 
    else 
    {
      period = PM;
      if (hh > NOON) hh -= NOON;
    }
    
    return String.format("%2d:%02d:%02d%2s", hh, minute, second, period);
  }

  /**
   * Create a String representation of a military time.
   * Note: This method does not validate the parameters.
   *
   * @param hour   The hour of the day in the interval [0, 23]
   * @param minute The minute of the day in the interval [0, 59]
   * @param second The second of the day in the interval [0, 59]
   * @return        A String representation of a time in military format
   */
  public static String formatAsMilitary(int hour, int minute, int second)
  {
    return String.format("%02d:%02d:%02d", hour, minute, second);
  }

  /**
   * Return a String representation of this SimpleTime.
   * 
   * Bunguiu Norales 
   * 
   * @param civilian true for civilian time; false for military time
   * @return         A String representation of this SimpleTime
   */
  public String toString(boolean civilian)
  {
    int       hour, minute, second;

    hour   = (elapsed / SECONDS_PER_HOUR);
    minute = ((elapsed % SECONDS_PER_HOUR) / MINUTES_PER_HOUR);
    second = (elapsed % SECONDS_PER_HOUR) % MINUTES_PER_HOUR;


    if (civilian) return formatAsCivilian(hour, minute, second);
    else          return formatAsMilitary(hour, minute, second);
  }
}
