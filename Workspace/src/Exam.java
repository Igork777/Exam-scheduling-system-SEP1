import java.io.Serializable;
public class Exam extends Inspectable implements Serializable
{
  private Course course;
  private Class classAttending;
  private Teacher teacher;
  private Classroom classroom;
  private Types examType;
  private Clock startTime;
  private MyDate startDate;
  private int duration;

  public enum Types
  {
    ORAL("Oral"),
    WRITTEN("Written"),
    SEMESTER("Semester");

    private String string;

    /**
     * 1 argument constructor for enumerator
     * @param string argument to be assigned to the field string inside the enum
     */
    private Types(String string) { this.string = string; }

    /**
     * method which returns the String equivalent of the enumerator
     * @return string field
     */
    public String toString(){ return string; }
  };

  public static MyList<Exam> allExams = new MyList<>();
  public static MyFileIO<MyList<Exam>> allExamsIO = new MyFileIO<>("allExams");

  public static Inspectable inspect;

  /**
   * 5 argument constructor for Exam object
   * @param course is assigned to the course field
   * @param examType is assigned to the examType field
   * @param classAttending is assigned to the classAttending field
   * @param teacher is assigned to the teacher field
   * @param classroom is assigned to the classroom field
   */
  public Exam(Course course, Types examType, Class classAttending, Teacher teacher, Classroom classroom) {
    setCourse(course);
    setClassAttending(classAttending);
    setTeacher(teacher);
    setClassroom(classroom);
    setExamType(examType);
    setDuration(course.getDurationOf(examType));
    inspect = makeInspectable(this.getClass());
  }

  // = = = = = = = = = = = = =
  // =        SETTERS        =
  // = = = = = = = = = = = = =

  /**
   * method with 1 Course argument, which assigns value to the field course
   * @param course passed Course argument, which will be assigned to the field course
   */
  public void setCourse(Course course)
  {
    this.course = course.copy();
  }

  /**
   * method with 1 Class argument, which assigns value to the classAttending course
   * @param classAttending passed Class argument, which will be assigned to the classAttending filed
   */
  public void setClassAttending(Class classAttending) { this.classAttending = classAttending.copy(); }

  /**
   * method with 1 Teacher argument, which assigns value to the teacher field
   * @param teacher passed Teacher argument, which will be assigned to the teacher field
   */
  public void setTeacher(Teacher teacher)
  {
    this.teacher = teacher.copy();
  }

  /**
   * method with 1 Classroom argument, which assigns value to the classroom field
   * @param classroom passed Classroom argument, which will be assigned to the classroom field
   */
  public void setClassroom(Classroom classroom) { this.classroom = classroom.copy(); }

  /**
   * method with 1 Types argument, which assigns value to the examType field
   * @param type passed Types argument, which will be assigned to the examType field
   */
  public void setExamType(Types type){ this.examType = type; }

  public void setDuration(int duration){ this.duration = duration;}

  public void schedule(MyDate date, Clock time){
    this.startDate = date;
    this.startTime = time;
  }
  public void unSchedule(){
    this.startDate = null;
    this.startTime = null;
  }
  // = = = = = = = = = = = = =
  // =        GETTERS        =
  // = = = = = = = = = = = = =

  public MyDate getStartDate(){ return this.startDate; }

  public Clock getStartTime(){ return this.startTime; }

  public Clock getEndTime(){ return startTime.afterNSeconds(duration * 60); }

  public boolean isOverlapping(MyDate date, Clock start, Clock end){

    if (startTime != null){
      boolean startContact = !startTime.isBefore(start) && !startTime.isAfter(end);
      boolean endContact = !getEndTime().isBefore(start) && !getEndTime().isAfter(end);
      return (startContact || endContact) && startDate.equals(date);
    }else return false;
  }

  public boolean isScheduled(){ return  this.startTime != null;}

  /**
   * method with no parameters, which returns the copy of the field course
   * @return copy of the value inside field course
   */
  public Course getCourse() { return course.copy(); }

  /**
   * method with no parameters, which returns the field examType
   * @return the value of the examType field
   */
  public Types getExamType(){ return this.examType; }

  /**
   * method, with no parameters, which returns the copy of Class classAttending field
   * @return the copy of the classAttending filed
   */
  public Class getClassAttending()
  {
    return classAttending.copy();
  }

  /**
   * method, with no parameters, which returns the copy of the Teacher teacher field
   * @return the copy of the teacher field
   */
  public Teacher getTeacher()
  {
    return teacher.copy();
  }

  /**
   * method, with no parameters, which returns the copy of the Classroom classroom field
   * @return the copy of the teacher field
   */
  public Classroom getClassroom()
  {
    return classroom.copy();
  }

  /**
   * Getter for duration of an exam
   * @return int duration in minutes
   */
  public int getDuration()
  {
    return duration;
  }

  /**
   * method, with 1 Object argument, which will be compared with the "this" Exam
   * @param other object, that has to be compared with the  "this" Exam
   * @return true if the object can be casted to "Exam" type and it has the same value of all the field. Otherwise false is returned.
   */
  public boolean equals(Object other){
    if (other instanceof Exam){
      Exam newExam = (Exam)other;
      return newExam.teacher.equals(teacher) && newExam.examType.equals(examType) && newExam.classAttending.equals(classAttending) && newExam.classroom.equals(classroom) && newExam.course.equals(course);
    }else return false;
  }

  public Exam copy(){
    Exam newExam = new Exam(course, examType, classAttending, teacher, classroom);
    if (isScheduled()){
      newExam.schedule(startDate, startTime);
    }
    return newExam;
  }

  public String toString() {
    return course.getName()+", "+startDate+" @ "+startTime;
  }
}
