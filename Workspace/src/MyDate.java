import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Class storing date - date, month, year
 */
public class MyDate implements Serializable
{
  private int day;
  private int month;
  private int year;
  public final static String[] week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

  /**
   * 3-arg constructor specifying day, month and year
   * @param day int day in a month
   * @param month int month in a year
   * @param year int year
   */
  public MyDate(int day, int month, int year)
  {
    setYear(year);
    setMonth(month);
    setDay(day);
  }

  /**
   * 1-arg constructor creating a copy of parameter MyDate object
   * @param obj MyDate object
   */
  public MyDate(MyDate obj){ this(obj.day, obj.month, obj.year); }

  /**
   * No-arg constructor creating MyDate object with today's date
   */
  public MyDate() { this(MyDate.today()); }

  /**
   * Sets the date to be the next day's date
   */
  public void nextDay(){
    if (this.daysInMonth() >= this.day + 1)
    {
      this.day++;
    }
    else
    {
      this.day = 1;
      if (this.month == 12)
      {
        this.month = 1;
        this.year++;
      }
      else
      {
        this.month++;
      }
    }
  }

  /**
   * Sets the date to be the previous day's date
   */
  public void previousDay()
  {
    if (day>1)
    {
      day--;
    }
    else if (month>1)
    {
      month--;
      day = daysInMonth();
    }
    else
    {
      year--;
      month = 12;
      day = daysInMonth();
    }
  }

  public MyDate getPreviousDay()
  {
    MyDate date = this.copy();
    if (date.day>1)
    {
      date.day--;
    }
    else if (date.month>1)
    {
      date.month--;
      date.day = daysInMonth();
    }
    else
    {
      date.year--;
      date.month = 12;
      date.day = daysInMonth();
    }
    return date;
  }

  /**
   * Sets the date to be next month's date
   */
  public void nextMonth(){
    if (month < 12){
      month++;
    }else{
      year++;
      month = 1;
    }
    if (day > daysInMonth()){ day = daysInMonth();}
  }

  /**
   * A static method returning today's date
   * @return
   */
  public static MyDate today(){
    GregorianCalendar currentDate = new GregorianCalendar();
    int thisDay = currentDate.get(GregorianCalendar.DATE);
    int thisMonth = currentDate.get(GregorianCalendar.MONTH)+1;
    int thisYear = currentDate.get(GregorianCalendar.YEAR);
    return new MyDate(thisDay, thisMonth, thisYear);
  }

  /**
   * Sets the date to be n days later
   * @param n int how many days to count up
   */
  public void nextDays(int n){
    for (int i = 0; i < n; i++) { this.nextDay(); }
  }

  /**
   * A method comparing whether the date is before comparing date
   * @param compare MyDate to compare with
   * @return boolean true if this date is before compare date, false if they are equal or this date is after compare date
   */
  public boolean isBefore(MyDate compare)
  {
    return (this.year < compare.year) || (this.year <= compare.year && this.month < compare.month) || (this.year <= compare.year && this.month <= compare.month && this.day < compare.day);
  }

  /**
   * A method comparing whether the date is after comparing date
   * @param compare MyDate to compare with
   * @return boolean true if this date is after compare date, false if they are equal or this date is before compare date
   */
  public boolean isAfter(MyDate compare)
  {
    return (this.year > compare.year)
        || (this.year >= compare.year && this.month > compare.month)
        || (this.year >= compare.year && this.month >= compare.month && this.day > compare.day);
  }

  /**
   * A method return whether the month of two dates is same taking year into consideration too
   * @param date
   * @return
   */
  public boolean sameMonth(MyDate date){
    return this.month == date.month && this.year == date.year;
  }

  /**
   * Equals method comparing whether two dates are the same day
   * @param obj MyDate to compare with
   * @return boolean true if the days are same, false if not
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof MyDate){
      MyDate newObj = (MyDate)obj;
      return (this.year == newObj.year && this.month == newObj.month && this.day == newObj.day);
    }else return false;
  }

  /**
   * Creates a copy of MyDate object
   * @return MyDate copy
   */
  public MyDate copy() { return new MyDate(day, month, year); }

  /**
   * Method count a difference between two dates
   * @param from MyDate to calculate the difference from
   * @return long amount of days
   */
  public long daysFrom(MyDate from){
    int days = 0;

    while(from.isBefore(this)){
      from.nextDay();
      days++;
    }
    return days;
  }

  /**
   * Static method returning order of a month
   * @param month name of the month in String
   * @return int order of a month
   */
  public static int getMonthNumber(String month){
    switch (month){
      case "January": return 1;
      case "February": return 2;
      case "March": return 3;
      case "April": return 4;
      case "May": return 5;
      case "June": return 6;
      case "July": return 7;
      case "August": return 8;
      case "September": return 9;
      case "October": return 10;
      case "November": return 11;
      case "December": return 12;
      default : return -1;
    }
  }

  /**
   * Static method returning name of a month
   * @param month order of the month
   * @return String name of the month
   */
  public static String getMonthName(int month){
    switch (month){
      case 1: return "January";
      case 2: return "February";
      case 3: return "March";
      case 4: return "April";
      case 5: return "May";
      case 6: return "June";
      case 7: return "July";
      case 8: return "August";
      case 9: return "September";
      case 10: return "October";
      case 11: return "November";
      case 12: return "December";
      default: return "ERROR";
    }
  }

  /**
   * Month name getter
   * @return String month name
   */
  public String getMonthName(){
    return getMonthName(this.month);
  }

  /**
   * Method calculating the day of the week of a date
   * @return String day of the week
   */
  public String dayOfWeek(){
    int q = this.day;
    int m = this.month;
    int year = this.year;
    if (m <= 2){
      year -= 1;
      m += 12;
    }
    int k = year % 100;
    int j = year / 100;

    int h = ( q + ((13*(m+1)) / 5) + k + (k/4) + (j/4) + (5*j) ) % 7;
    switch(h){
      case 0: return "Saturday";
      case 1: return "Sunday";
      case 2: return "Monday";
      case 3: return "Tuesday";
      case 4: return "Wednesday";
      case 5: return "Thursday";
      case 6: return "Friday";
      default: return "ERROR";
    }
  }

  /**
   * Method returning the date of the first day in that week
   * @return MyDate of monday from that week
   */
  public MyDate startOfWeek(){
    MyDate res = copy();
    int dayCount = Util.getIndex(week, this.dayOfWeek());

    for (int i = dayCount; i > 0; i--) {
      res.previousDay();
    }

    return res;
  }

  /**
   * Method returning the date of the first day in that month
   * @return MyDate first date of the date's month
   */
  public MyDate startOfMonth(){
    return new MyDate(1, month, year);
  }

  /**
   * Method returning the date of the last day in that month
   * @return MyDate last date of the date's month
   */
  public MyDate endOfMonth(){
    return new MyDate(this.daysInMonth(), month, year);
  }

  /**
   * Method returning amount of days in this date
   * @return int amount of days
   */
  public int daysInMonth(){
    return daysInMonth(this.year, this.month);
  }

  /**
   * Static method returning amount of days in specified month and year
   * @param year int year
   * @param month int month
   * @return int amount of days
   */
  public static int daysInMonth(int year, int month){
    switch(month){
      case 1:
      case 3:
      case 5:
      case 7:
      case 8:
      case 10:
      case 12:
        return 31;
      case 2:
        return isLeapYear(year) ? 29 : 28;
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      default:
        return 0;
    }
  }

  /**
   * Day setter
   * @param d int day
   */
  public void setDay(int d)
  {
    if (d < 1 || daysInMonth() < d){ throw new IllegalDateException("Invalid day"); }
    this.day = d;
  }

  /**
   * Month setter
   * @param m int month
   */
  public void setMonth(int m)
  {
    if (m > 12 || m < 1){ throw new IllegalDateException("Invalid month");}
    this.month = m;
  }

  /**
   * Year setter
   * @param y int year
   */
  public void setYear(int y)
  {
    if (y < 1) {throw new IllegalDateException("Invalid year");}
    this.year = y;;
  }

  /**
   * Day getter
   * @return int day
   */
  public int getDay(){ return day; }

  /**
   * Month getter
   * @return int month
   */
  public int getMonth(){ return month; }

  /**
   * Year getter
   * @return int year
   */
  public int getYear(){ return year; }

  /**
   * Static method checking if a year is a leap year
   * @param year int year to check
   * @return boolean is year leap year or not
   */
  public static boolean isLeapYear(int year){ return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0 ) ); }

  /**
   * Method checking if a year is a leap year
   * @return boolean is year leap or not
   */
  public boolean isLeapYear(){ return isLeapYear(this.year); }

  /**
   * toString method for date
   * @return String "DD/MM/YY"
   */
  public String toString() { return "" + day + '/' + month + '/' + year; }
}
