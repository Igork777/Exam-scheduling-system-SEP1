import java.util.ArrayList;

public class XMLConverter
{
  public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

  /**
   * Converts exam object to String with xml formatting with all necessary information
   * @param exam exam to be transcripted
   * @return String in xml format
   */
  public String toXML(Exam exam)
  {
    String xmlFile =
        "<exam>\n"
        + "  <course>" + exam.getCourse().getName() + "</course>\n"
        + "  <type>" + exam.getExamType() + "</type>\n"
        + "  <teacher>" + exam.getTeacher().getFullName() + "</teacher>\n"
        + "  <classroom>" + exam.getClassroom().getName() + "</classroom>\n"
        + "  <class>" + exam.getClassAttending().getName() + "</class>\n"
        + "  <date>" + exam.getStartDate() + "</date>\n"
        + "  <time>" + exam.getStartTime() + "</time>\n"
        + "  <duration>" + exam.getDuration() + "</duration>\n"
        + "</exam>\n";
    return xmlFile;
  }

  /**
   * Converts arraylist of to String with xml formatting with all necessary information
   * @param exams exams to be transcripted
   * @return String in xml format
   */
  public String toXML(ArrayList<Exam> exams)
  {
    String xmlFile = "<exams>\n";
    for (int i = 0; i < exams.size(); i++)
    {
      xmlFile += toXML(exams.get(i));
    }
    xmlFile += "</exams>\n";
    return xmlFile;
  }
}
