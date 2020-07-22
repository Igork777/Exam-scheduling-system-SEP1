import java.io.Serializable;
public class DateInterval extends Inspectable implements Serializable
{
  private MyDate startDate;
  private MyDate endDate;
  public static Inspectable inspect;

  /**
   * 2-arg Constructor for DateInterval object.
   * @param startDate MyDate object specifying the start of a period.
   * @param endDate MyDate object specifying the end of a period.
   */
  public DateInterval(MyDate startDate, MyDate endDate)
  {
    setStartDate(startDate.copy());
    setEndDate(endDate.copy());
    inspect = makeInspectable(this.getClass());
  }

  // = = = = = = = = = = = = =
  // =        SETTERS        =
  // = = = = = = = = = = = = =

  /**
   * Sets a start date of a date interval
   * @param startDate MyDate object
   */
  public void setStartDate(MyDate startDate)
  {
    this.startDate = startDate;
  }

  /**
   * Sets an end date of a date interval.
   * @param endDate MyDate object.
   */
  public void setEndDate(MyDate endDate)
  {
    this.endDate = endDate;
  }

  // = = = = = = = = = = = = =
  // =        GETTERS        =
  // = = = = = = = = = = = = =

  /**
   * Getter for start date.
   * @return start date of the DateInterval
   */
  public MyDate getStartDate()
  {
    return startDate.copy();
  }

  /**
   * Getter for end date.
   * @return end date of the DateInterval
   */
  public MyDate getEndDate()
  {
    return endDate.copy();
  }

  /**
   * Checks whether two DateIntervals overlap , therefore are able to be merged.
   * @param other Date interval you want to check to merge with
   * @return boolean value informing whether two DateIntervals can be merged.
   */
  public boolean isMergeableWith(DateInterval other)
  {
    boolean rightContact = !other.startDate.isBefore(startDate) && !other.startDate.isAfter(endDate);
    boolean leftContact = !other.endDate.isBefore(startDate) && !other.endDate.isAfter(endDate);
    return rightContact || leftContact;
  }

  /**
   * A method able to merge two DateIntervals provided they overlap.
   * New DateInterval will have the soonest start date and the latest end date.
   * @param other Date interval you want to merge with
   * @return new DateInterval object with updated startDate and endDate
   * @throws IllegalArgumentException when two Date Intervals cannot be merged
   */
  public DateInterval mergeWith(DateInterval other)
      throws IllegalArgumentException
  {
    DateInterval newDateInterval;
    if (isMergeableWith(other))
    {
      MyDate newStartDate;
      MyDate newEndDate;

      //first start date is set as a new start date
      if (startDate.isBefore(other.startDate))
        newStartDate = startDate.copy();
      else
        newStartDate = other.startDate.copy();

      //last start date is set as a new end date
      if (endDate.isBefore(other.endDate))
        newEndDate = other.endDate.copy();
      else
        newEndDate = endDate.copy();

      newDateInterval = new DateInterval(newStartDate, newEndDate);
    }
    else
    {
      throw new IllegalArgumentException("DateIntervals cannot be merged");
    }
    return newDateInterval;
  }

  public boolean contains(MyDate date){
    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }

  /**
   * Subtracts a date interval from an original interval by adjusting either end or start date
   * @param other DateInterval that you want to subtract (subtrahend).
   * @return void.
   * @throws IllegalArgumentException When two date intervals have no intersection or
   * first's start date is before second's and first's end date is after second's.
   * Same applies when first and second date intervals value swap.
   */
  public void subtract(DateInterval other)
      throws IllegalArgumentException
  {
    if (isMergeableWith(other))
    {
      if (!startDate.isBefore(other.startDate) && other.endDate.isBefore(endDate))
      {
        MyDate tempStartDate = other.endDate.copy();
        tempStartDate.nextDay();
        startDate = tempStartDate;
      }
      else if (!other.endDate.isBefore(endDate) && startDate.isBefore(other.startDate))
      {
        MyDate tempEndDate = other.startDate.copy();
        tempEndDate.previousDay();
        endDate = tempEndDate;
      }
      else
      {
        throw new IllegalArgumentException("DateIntervals cannot be subtracted(Incompatible intervals)");
      }
    }
    else
    {
      throw new IllegalArgumentException("DateIntervals cannot be subtracted(Cannot be merged)");
    }
  }

  /**
   * Gives a written representation of the DateInterval object.
   * @return String start - end.
   */
  @Override public String toString()
  {
    return startDate + "-" + endDate + "; ";
  }

  /**
   * Creates a copy of a DateInterval object
   * @return new DateInterval with the same fields
   */
  public DateInterval copy()
  {
    return new DateInterval(startDate, endDate);
  }

  /**
   * Compares whether two DateIntervals are the same:
   * @param obj DateInterval to compare.
   * @return true or false depending on the values of the DateIntervals
   */
  @Override public boolean equals(Object obj)
  {
    if (!(obj instanceof DateInterval))
      return false;

    DateInterval other = (DateInterval) obj;

    return startDate.equals(other.startDate) && endDate.equals(other.endDate);
  }
}
