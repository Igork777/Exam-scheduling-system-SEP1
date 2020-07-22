import java.io.Serializable;

public class Clock implements Serializable
{
  private int hour, minute, second;

  /**
   * constructor with 3 integer parameters
   * @param h amount of hours , which will be assigned to hour field
   * @param m amount of minutes, which will be assigned to minute field
   * @param s amount of seconds, which will be assigned to second field
   */
  public Clock(int h, int m, int s){
    hour = h;
    minute = m;
    second = s;
    validate();
  }

  /**
   * Constructor with 2 integer parameters hour and minute. Seconds are set to 0
   * @param h hour
   * @param m minute
   */
  public Clock(int h, int m){
    hour = h;
    minute = m;
    second = 0;
    validate();
  }

  /**
   * constructor with 1 integer parameter
   * @param timeInSeconds amount of seconds, which will be converted to hours minutes and seconds. Converted data will be assigned to fields hour, minutesm second
   */
  public Clock(int timeInSeconds){
    this.second = timeInSeconds;
    this.hour = 0;
    this.minute = 0;
    validate();
  }

  public Clock afterNSeconds(int seconds){
    Clock newClock = this.copy();
    newClock.second += seconds;
    newClock.validate();
    return newClock;
  }

  /**
   * method with 3 integer parameters , which assigns values to the fields hour, minute, second
   * @param h amount of hours, which will be assigned to field hour
   * @param m amount of minutes, which will be assigned to filed minute
   * @param s amount of seconds, which will be assigned to field seconds
   */
  public void set(int h, int m, int s){
    hour = h;
    minute = m;
    second = s;
    validate();
  }

  /**
   * method without parameters, which validates seconds, minutes and hours
   */
  private void validate(){
    if (second > 59){
      minute += second / 60;
      second %= 60;
    }else if(second < 0){
      minute -= second / -60;
      second %= -60;
      if (second < 0){minute --; second = 60 + second; }
    }
    if (minute > 59){
      hour += minute / 60;
      minute %= 60;
    }else if(minute < 0){
      hour -= minute / -60;
      minute %= - 60;
      if (minute < 0){hour --; minute = 60 + minute;}
    }
    if (hour > 23){
      hour = hour % 24;
    }else if(hour < 0){
      hour = 24 + (hour % -24);
    }
  }

  /**
   * method with no parameters, which converts all hours and minutes to seconds and then sums all the seconds
   * @return the whole amount of seconds
   */
  public int toSeconds(){
    int seconds = 0;
    seconds += second;
    seconds += minute * 60;
    seconds += hour * 60 * 60;
    return seconds;
  }

  /**
   * method with the Clock parameter, which counts difference in seconds between two clocks
   * @param compare Clock parameter, which is passed for the comparing and calculation with the "this" Clock
   * @return amount of seconds between two Clock values
   */
  public int secondsTo(Clock compare){
    final int fullDay = 24 * 3600;
    if (this.isBefore(compare)){
      return compare.toSeconds() - this.toSeconds();
    }else{
      return fullDay - this.toSeconds() + compare.toSeconds();
    }
  }


  /**
   * method with the Clock parameter, which returns the difference between two Clocks
   * @param compare Clock parameter, which will be compared with the "this" Clock
   * @return the Clock object, which is create from the difference between passed Clock and "this" Clock
   */
  public Clock timeTo(Clock compare){
    return new Clock(this.secondsTo(compare));
  }

  /**
   * method with the Clock parameter, which checks if "this" Clock is less than passed Clock
   * @param compare Clock parameter, which will be compared with "this" parameter
   * @return true or false after comparison of two Clocks
   */
  public boolean isBefore(Clock compare){
    return this.toSeconds() < compare.toSeconds();
  }

  public boolean isAfter(Clock compare){
    return this.toSeconds() > compare.toSeconds();
  }

  /**
   * method without parameters, which adds 1 second to "this" Clock
   */
  public void tick(){
    second++;
    validate();
  }

  //  Getters

  /**
   * method with no parameters, which returns the hours of the Clock object
   * @return value of the hour field
   */
  public int getHour() { return hour; }

  /**
   * method with no parameters, which returns the minutes of the Clock object
   * @return value of the minute field
   */
  public int getMinute() { return minute; }

  /**
   * method with no parameters, which returns the seconds of the Clock object
   * @return  value of the second field
   */
  public int getSecond() { return second; }

  /**
   * method with no parameters, which returns the String with all the information about the Clock object
   * @return the String, which represents the time
   */
  public String toString(){
    String hour = this.hour < 10 ? "0"+this.hour : ""+this.hour;
    String minute = this.minute < 10 ? "0"+this.minute : ""+this.minute;
    String second = this.second < 10 ? this.second == 0 ? "" : ":0"+this.second : ":"+this.second;
    return hour+":"+minute+second;
  }

  public Clock copy(){
    return new Clock(this.toSeconds());
  }
}
