import java.io.Serializable;
import java.util.ArrayList;

public class Course extends Inspectable implements Serializable
{
  public MyList<Class> classes;
  public MyList<Teacher> teachers;
  private String name;
  private boolean oralExamTaken;
  private boolean writtenExamTaken;
  private boolean semesterExamTaken;
  private int oralExamDuration;
  private int writtenExamDuration;
  private int semesterExamDuration;
  public static Inspectable inspect;

  public static MyList<Course> allCourses = new MyList<>();
  public static MyFileIO<MyList<Course>> allCoursesIO = new MyFileIO<>("allCourses");

  /**
   * 7 argument constructor which assigns all the parameters to the proper fields
   *
   * @param name                 the name of the course
   * @param oralExam             shows whether this course has an oralExam
   * @param writtenExam          shows whether this course has a written exam
   * @param semesterExam         shows whether this course has a semester exam
   * @param oralExamDuration     shows the duration of the oral exam
   * @param writtenExamDuration  shows the duration of the written exam
   * @param semesterExamDuration shows the duration of the semester exam
   */
  public Course(String name, boolean oralExam, boolean writtenExam, boolean semesterExam, int oralExamDuration, int writtenExamDuration, int semesterExamDuration) {
    this(name, oralExam, writtenExam, semesterExam, oralExamDuration, writtenExamDuration, semesterExamDuration, new ArrayList<Class>(), new ArrayList<Teacher>());
  }

  /**
   * 8 argument constructor which assigns all the parameters to the proper fields
   *
   * @param name                 the name of the course
   * @param oralExam             shows whether this course has an oralExam
   * @param writtenExam          shows whether this course has a written exam
   * @param semesterExam         shows whether this course has a semester exam
   * @param oralExamDuration     shows the duration of the oral exam
   * @param writtenExamDuration  shows the duration of the written exam
   * @param classes              arrayList of the classes
   * @param semesterExamDuration shows the duration of the semester exam
   */
  public Course(String name, boolean oralExam, boolean writtenExam, boolean semesterExam, int oralExamDuration, int writtenExamDuration, ArrayList<Class> classes, int semesterExamDuration) {
    this(name, oralExam, writtenExam, semesterExam, oralExamDuration, writtenExamDuration, semesterExamDuration, classes, new ArrayList<Teacher>());
  }

  /**
   * 8 argument constructor which assigns all the parameters to the proper fields
   *
   * @param name                 the name of the course
   * @param oralExam             shows whether this course has an oralExam
   * @param writtenExam          shows whether this course has an written exam
   * @param semesterExam         shows whether this course has an semester exam
   * @param oralExamDuration     shows the duration of the oral exam
   * @param writtenExamDuration  shows the duration of the written exam
   * @param semesterExamDuration shows the duration of the semester exam
   * @param teachers             arrayList of the teachers
   */
  public Course(String name, boolean oralExam, boolean writtenExam, boolean semesterExam, int oralExamDuration, int writtenExamDuration, int semesterExamDuration, ArrayList<Teacher> teachers) {
    this(name, oralExam, writtenExam, semesterExam, oralExamDuration, writtenExamDuration, semesterExamDuration, new ArrayList<Class>(), teachers);
  }

  /**
   * 9 argument constructor which assigns all the parameters to the proper fields
   * if any of the exams exists - its duration is assigned to the field with the exam duration, otherwise 0 is assigned
   * @param name                 the name of the course
   * @param oralExam             shows whether this course has an oralExam
   * @param writtenExam          shows whether this course has a written exam
   * @param semesterExam         shows whether this course has a semester exam
   * @param oralExamDuration     shows the duration of the oral exam
   * @param writtenExamDuration  shows the duration of the written exam
   * @param semesterExamDuration shows the duration of the semester exam
   * @param classes              arrayList of the classes
   * @param teachers             arrayList of the teachers
   */
  public Course(String name, boolean oralExam, boolean writtenExam, boolean semesterExam, int oralExamDuration, int writtenExamDuration, int semesterExamDuration, ArrayList<Class> classes, ArrayList<Teacher> teachers)
  {
    setName(name);
    setOralExamTaken(oralExam);
    setWrittenExamTaken(writtenExam);
    setSemesterExamTaken(semesterExam);
    setOralExamDuration(oralExam ? oralExamDuration : 0);
    setWrittenExamDuration(writtenExam ? writtenExamDuration : 0);
    setSemesterExamDuration(semesterExam ? semesterExamDuration : 0);
    this.classes = new MyList<>(Util.copyArrList(classes));
    this.teachers = new MyList<>((Util.copyArrList(teachers)));
    inspect = makeInspectable(this.getClass());
  }

  // GETTERS

  /**
   * Returns the name of the course
   * @return value of name field
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the teachers teaching this course
   * @return A MyList of teachers teaching this course
   */
  public MyList<Teacher> getTeachers() { return teachers; }

  /**
   * Returns the classes attending this course
   * @returnA MyList of classes attending this course
   */
  public MyList<Class> getClasses() { return classes; }

  /**
   * checks if list of classes contains the passed parameter
   * @param cls Class parameter. If it exists in the list classes, true value is returned
   * @return true or false depending on existence of the Class' object in the classes arrayList
   */
  public boolean isClassAttending(Class cls){
    return classes.contains(cls);
  }
  public boolean isTeacherAttending(Teacher teacher){
    return teachers.contains(teacher);
  }
  /**
   * Determines whether a student will have an oral exam after the finishing the course
   * @return value of the field oralExamTaken
   */
  public boolean isOralExamTaken() {
    return oralExamTaken;
  }

  /**
   * Determines whether a student will have an written exam after the finishing this course
   * @return value of the field writtenExamTaken
   */
  public boolean isWrittenExamTaken() {
    return writtenExamTaken;
  }

  /**
   * Determines whether a student will have a semester exam after finishing this course
   * @return value of the field semesterExamTaken
   */
  public boolean isSemesterExamTaken() {
    return semesterExamTaken;
  }
  /**
   * Returns the time of the oral exam if it exists, otherwise returns 0
   * @return the value of the field oralExamDuration
   */
  public int getOralExamDuration() {
    return oralExamDuration;
  }

  /**
   * Returns the time of the written exam if it exists, otherwise returns 0
   * @return the value of the field writtenExamDuration
   */
  public int getWrittenExamDuration() {
    return writtenExamDuration;
  }

  /**
   * Returns the time of the semester exam if it exists, otherwise returns 0
   * @return the value of the field semesterExamDuration
   */
  public int getSemesterExamDuration() {
    return semesterExamDuration;
  }

  public int getDurationOf(Exam.Types type){
    if(type == Exam.Types.ORAL){
      return getOralExamDuration();
    }else if(type == Exam.Types.WRITTEN){
      return getWrittenExamDuration();
    }else if(type == Exam.Types.SEMESTER){
      return getSemesterExamDuration();
    }else{return 0;}
  }
  //SETTERS

  /**
   * Assigns name of the course
   * @param name of String parameters, which assigns to the name field
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Assigns whether oral exam should be taken
   * @param oralExamTaken argument with the true or false values
   */
  public void setOralExamTaken(boolean oralExamTaken) {
    this.oralExamTaken = oralExamTaken;
  }

  /**
   * Assigns whether written exam should be taken
   * @param writtenExamTaken argument with the true or false values
   */
  public void setWrittenExamTaken(boolean writtenExamTaken) {
    this.writtenExamTaken = writtenExamTaken;
  }

  /**
   * Assigns whether semester exam should be taken
   * @param semesterExamTaken argument with the true or false values
   */
  public void setSemesterExamTaken(boolean semesterExamTaken) {
    this.semesterExamTaken = semesterExamTaken;
  }

  /**
   * Assigns the duration of the oral exam if it exists, if not 0 is assigned
   * @param oralExamDuration argument with the amount of minutes
   */
  public void setOralExamDuration(int oralExamDuration) {
    Util.validIfPositive(oralExamDuration);
    this.oralExamDuration = oralExamDuration;
  }

  /**
   * Assigns the duration of the written exam if it exists, if not 0 is assigned
   * @param writtenExamDuration argument with the amount of minutes
   */
  public void setWrittenExamDuration(int writtenExamDuration) {
    Util.validIfPositive(writtenExamDuration);
    this.writtenExamDuration = writtenExamDuration;
  }

  /**
   * Assigns the duration of the semester exam if it exists, if not 0 is assigned
   *
   * @param semesterExamDuration argument with the amount of minutes
   */
  public void setSemesterExamDuration(int semesterExamDuration) {
    Util.validIfPositive(semesterExamDuration);
    this.semesterExamDuration = semesterExamDuration;
  }

  /**
   * Returns identical copy of "this" object
   * @return copy of the object
   */
  public Course copy() {
    return new Course(name, oralExamTaken, writtenExamTaken, semesterExamTaken, oralExamDuration, writtenExamDuration, semesterExamDuration, classes.getList(), teachers.getList());
  }

  /**
   * Determines whether passed argument Course obj is equal to "this" Course object of the class Course
   * @param obj passed argument, which has to be compared with the inner  object
   * @return true if objects are equals and false otherwise
   */
  public boolean equals(Object obj) {
    if (!(obj instanceof Course))
      return false;
    Course course = (Course) obj;
    return this.teachers.equals(course.teachers) && this.classes.equals(course.classes) && this.oralExamTaken == course.oralExamTaken && this.writtenExamTaken == course.writtenExamTaken && this.semesterExamTaken == course.semesterExamTaken && this.oralExamDuration == course.oralExamDuration && this.writtenExamDuration == course.writtenExamDuration && this.semesterExamDuration == course.semesterExamDuration;
  }

  /**
   * Returns the output of the whole object Course
   * @return a String with the information about Course object
   */
  public String toString()
  {
   return name;
  }

  /**
   * Returns the exam information in order to store it inside the table in GUI
   * @return the important information for the GUI
   */
  public String getExamInfo()
  {
    ArrayList<String> strings = new ArrayList<>();

    if(isOralExamTaken()){
      strings.add("Oral - "+oralExamDuration+" mins");
    }
    if(isWrittenExamTaken()){
      strings.add("Written - "+writtenExamDuration+" mins");
    }
    if(isSemesterExamTaken()){
      strings.add("Semester - "+semesterExamDuration+" mins");
    }
    return String.join("\n", strings);
  }
}
