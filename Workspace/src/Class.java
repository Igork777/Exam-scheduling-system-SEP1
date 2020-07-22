import java.io.Serializable;



public class Class extends Inspectable implements Serializable
{
    private String name;
    private int numberOfStudents;
    private Classroom homeClassroom;
    public static MyList<Class> allClasses = new MyList<>();
    public static MyFileIO<MyList<Class>> allClassesIO = new MyFileIO<>("allClasses");
    public static Inspectable inspect;
    /**
     * Three-argument constructor.Initialization of a name, number of students and classroom fields
     * @param name is assigned to the field name in the class Class
     * @param numberOfStudents is assigned to the field numberOfStudents in the class Class
     * @param homeClassroom is assigned to the field homeClassroom in the class Class
     */
    public Class(String name, int numberOfStudents, Classroom homeClassroom)
    {
        setName(name);
        setNumberOfStudents(numberOfStudents);
        setHomeClassroom(homeClassroom);
        inspect = makeInspectable(this.getClass());
    }

    /**
     * Gets the class field name
     * @return the Class's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the class field numberOfStudents
     * @return the number of students in the Class
     */
    public int getNumberOfStudents () { return numberOfStudents; }

    /**
     * Gets the class field homeClassroom
     * @return the Class's home classroom
     */
    public Classroom getHomeClassroom(){ return homeClassroom.copy(); }


    /**
     * Is the number of students within specified value
     * @param min minimal number of students to return true
     * @param max maximal number of students to return true
     * @return boolean depending on whether the number of students is within specified value
     */

    public boolean isNumberOfStudentsWithin(int min, int max)
    {
        return numberOfStudents >= min && numberOfStudents <= max;
    }


    /**
     * Sets the value to Class's field name
     * @param name is assigned to the field name in the class Class
     */
    public void setName (String name)
    {
        this.name = name;
    }

    /**
     * Sets the value to the field numberOfStudents
     * @param numberOfStudents is assigned to the field numberOfStudents in the class Class
     */
    public void setNumberOfStudents (int numberOfStudents)
    {
        Util.validIfPositive(numberOfStudents);
        this.numberOfStudents = numberOfStudents;
    }

    /**
     * Sets the value to the homeClassroom field
     * @param classroom is assigned to the field homeClassroom in the class Class
     */
    public void setHomeClassroom (Classroom classroom)
    { homeClassroom = classroom.copy(); }

    /**
     * Checks either passed argument is the same Class object as original Class object
     * if passed parameter classroom can be cast to this object and if yes, than method compares all the fields of the two Class objects.
     * @param obj passed parameter, which has to be compared with the original Class object
     * @return the boolean type. If the passed parameter can't be cast to Class object, than it returns false.
     * If the passed parameter can be cast, than the passed parameter will be casted to the Class object. Afterwards
     * all the fields in both Class objects will be compared. If all the fields are equal , than method returns true,
     * but if at least 1 field is not equal, than method returns false;
     */
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Class))
            return false;
        Class tempClass = (Class)obj;
        return this.name.equals(tempClass.name) && this.numberOfStudents == tempClass.numberOfStudents && this.homeClassroom
            .equals(tempClass.homeClassroom);
    }

    /**
     * Returns copy of "this" object
     * @return copy of the object
     */
    public Class copy()
    {
        return new Class(name, numberOfStudents, homeClassroom);
    }
    /**
     * The method returns a string with the information about the Class object
     * @return string, where the information about name, number of Students and home classroom is provided
     */
    @Override
    public String toString()
    {
        return name +"\n" + numberOfStudents + " students";
    }
}
