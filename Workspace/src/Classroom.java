import java.io.Serializable;
public class Classroom extends Inspectable implements Serializable
{
  private String name;
  private int capacity;
  private boolean hasProjector;
  public static MyList<Classroom> allClassrooms = new MyList<Classroom>();
  public static MyFileIO<MyList<Classroom>> allClassroomsIO = new MyFileIO<>("allClassrooms");
  public static Inspectable inspect;
  /**
   * 2-argument constructor specifying name and capacity of the classroom. Projector information about being present is set to default - false.
   * @param name Name of the specific classroom.
   * @param capacity Capacity of the specific classroom.
   */
  public Classroom(String name, int capacity)
  {
    this(name, capacity, false);
  }

  /**
   * 3-argument constructor specifying name and capacity of the classroom and whether the class has a projector present.
   * @param name Name of the classroom.
   * @param capacity Capacity of the classroom.
   * @param hasProjector If a projector present in the classroom.
   */
  public Classroom(String name, int capacity, boolean hasProjector)
  {
    setName(name);
    setCapacity(capacity);
    setHasProjector(hasProjector);
    inspect = makeInspectable(this.getClass());
  }

  /**
   * Classroom name getter.
   * @return String with the name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Classroom capacity getter.
   * @return Integer with the capacity.
   */
  public int getCapacity()
  {
    return capacity;
  }

  /**
   * Projector's presence getter.
   * @return projector's presence information.
   */
  public boolean getHasProjector()
  {
    return hasProjector;
  }

  /**
   * Classroom's floor getter
   * @return int floor classroom is located on.
   */
  public int getFloor()
  {
    return name.length()>2 ? Character.getNumericValue(name.charAt(2)) : 9;
  }

  /**
   * Classroom name setter.
   * @param name name for the classroom
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Classroom capacity getter.
   * @param capacity for the classroom
   */
  public void setCapacity(int capacity)
  {
    Util.validIfPositive(capacity);
    this.capacity = capacity;
  }

  /**
   * Projector's presence getter.
   * @param hasProjector Is projector present.
   */
  public void setHasProjector(boolean hasProjector)
  {
    this.hasProjector = hasProjector;
  }

  /**
   * Is capacity within minimal and maximal value
   * @param min int minimal value capacity can hold to return true
   * @param max int maximal value capacity can hold to return true
   * @return boolean is within boundaries
   */
  public boolean isCapacityWithin(int min, int max)
  {
    return capacity >= min && capacity <= max;
  }

  /**
   * Checks equality of two Classroom object
   * @param obj to compare equality with.
   * @return True/False based on equality check.
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Classroom))
      return false;
    Classroom other = (Classroom) obj;
    return name.equals(other.name)
         && capacity == other.capacity && hasProjector == other.hasProjector;
  }

  /**
   * Makes copy of the classroom object
   * @return new Classroom object, with the identical fields and methods
   */
  public Classroom copy()
  {
    return new Classroom(name, capacity, hasProjector);
  }

  /**
   * Returns all the Classroom information in a String
   * @return String with name, capacity and projector presence
   */
  public String toString()
  {
    String proj = hasProjector ? "yes" : "no";
    return name + "\nprojector - "+proj+"\n"+ capacity + " places";
  }
}