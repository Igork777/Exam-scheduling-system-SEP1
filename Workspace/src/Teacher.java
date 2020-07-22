import java.io.Serializable;

public class Teacher extends Inspectable implements Serializable
{
  private String firstName;
  private String lastName;
  private MyList<DateInterval> notAvailable;
  public static MyList<Teacher> allTeachers = new MyList<>();
  public static MyFileIO<MyList<Teacher>> allTeachersIO = new MyFileIO<>("allTeachers");
  public static Inspectable inspect;

  /**
   * 2-arg constructor specifying teacher's name.
   *
   * @param firstName First name of the teacher.
   * @param lastName  Last name of the teacher.
   */
  public Teacher(String firstName, String lastName)
  {
    setFirstName(firstName);
    setLastName(lastName);
    this.notAvailable = new MyList<>();
    inspect = makeInspectable(this.getClass());
  }

  // = = = = = = = = = = = = =
  // =        GETTERS        =
  // = = = = = = = = = = = = =

  /**
   * First name getter.
   * @return the value of the firstName field
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Last name getter.
   * @return the value of the lastName field
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * Full name getter
   * @return the value of the field full name
   */
  public String getFullName()
  {
    return firstName +" "+lastName;
  }

  /**
   * Not available getter.
   * @return MyList<DateInterval> all unavailable date intervals
   */
  public MyList<DateInterval> getNotAvailable()
  {
    return notAvailable.copy();
  }

  // = = = = = = = = = = = = =
  // =        SETTERS        =
  // = = = = = = = = = = = = =

  /**
   * Sets first name.
   * @param firstName assigns the value to the firstName
   */
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  /**
   * Sets last name.
   * @param lastName assigns the value to the lastName
   */
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }
  /**
   * Sets not available date intervals.
   * @param notAvailable is assigned to the notAvailable list field
   */
  public void setNotAvailable(MyList<DateInterval> notAvailable)
  {
    this.notAvailable = notAvailable.copy();
  }

  // = = = = = = = = = = = = =
  // =        METHODS        =
  // = = = = = = = = = = = = =

  /**
   * Add a new date interval element to not available list.
   * If the new date interval can be merged with already existing do then merge them, replacing the original date interval
   * @param newInterval DateInterval you want to add to the list
   */
  public void addNotAvailableInterval(DateInterval newInterval)
  {
    MyList<DateInterval> result = new MyList<>();
    MyList<DateInterval> mergeable = new MyList<>();
    mergeable.add(newInterval);

    for (int i = 0; i < notAvailable.size(); i++)
    {
      DateInterval thisInterval = notAvailable.get(i);
      if (newInterval.isMergeableWith(thisInterval))
      {
        mergeable.add(thisInterval);
      }
      else
      {
        result.add(thisInterval);
      }
    }

    for (int i = 0; i < mergeable.size(); i++)
    {
      newInterval = newInterval.mergeWith(mergeable.get(i));
    }
    result.add(newInterval);

    setNotAvailable(result);
  }

  /**
   * Adjusts date intervals in notAvailable list such that no date interval contains passed date interval passed in argument.
   *
   * @param remInt DateInterval, when teacher is no more unavailable.
   */
  public void removeNotAvailableInterval(DateInterval remInt)
  {
    MyList<DateInterval> tempMLDateInt = new MyList<>();
    for (int i = 0; i < notAvailable.size(); i++)
    {
      DateInterval thisInterval = notAvailable.get(i);
      //remove thisInterval from list if thisInterval is a subset of remInt (including improper)
      if (!thisInterval.getStartDate().isBefore(remInt.getStartDate())
          && !remInt.getEndDate().isBefore(thisInterval.getEndDate()))
      {
        notAvailable.removeAt(i);
      }
      //if remInt is proper subset of thisInterval split thisInterval subtracting remInt and adding second interval at the end
      else if (thisInterval.getStartDate().isBefore(remInt.getStartDate())
          && remInt.getEndDate().isBefore(thisInterval.getEndDate()))
      {
        //Handling first interval
        MyDate firstIEndDate = remInt.getStartDate();
        firstIEndDate.previousDay();
        thisInterval.setEndDate(firstIEndDate);
        notAvailable.removeAt(i);
        notAvailable.add(thisInterval);

        //Handling second interval to be added at the end of the list
        MyDate secondIStartDate = remInt.getEndDate();
        secondIStartDate.nextDay();
        notAvailable.add(new DateInterval(secondIStartDate, thisInterval.getEndDate()));
      }
      //subtract the remInt from thisInterval(intersection)
      else if (thisInterval.isMergeableWith(remInt))
      {
        thisInterval.subtract(remInt);
        notAvailable.setAt(i, thisInterval);
      }
    }
  }

  /**
   * Check for teacher's availability on passed date as an argument.
   *
   * @param date MyDate object with day, month and year fields.
   * @return Boolean - is this teacher available on a specific day or not.
   */
  public boolean isAvailableOnDate(MyDate date)
  {
    for (int i = 0; i < notAvailable.size(); i++)
    {
      DateInterval thisInterval = notAvailable.get(i);
      if (thisInterval.contains(date)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns a copy of a Teacher with all the fields
   *
   * @return Teacher object
   */
  public Teacher copy()
  {
    Teacher t1 = new Teacher(firstName, lastName);
    t1.notAvailable = this.notAvailable.copy();
    return t1;
  }

  /**
   * Gives a written representation of a Teacher object:
   * First name, last name and list of unavailable date intervals
   *
   * @return String
   */
  public String toString() { return getFullName(); }

  /**
   * Checks for equality with another object. First checking instance and then fields.
   *
   * @param obj Object to compare to.
   * @return Boolean - Both instance and fields are same or not.
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Teacher))
      return false;

    Teacher other = (Teacher) obj;

    return firstName.equals(other.firstName) && lastName.equals(other.lastName)
        && notAvailable.equals(other.notAvailable);
  }
}