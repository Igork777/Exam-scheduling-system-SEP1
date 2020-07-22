import java.io.Serializable;

public class ExamTerm implements Serializable
{
  private MyDate startDate;
  private MyDate endDate;

  /**
   * Sets the value for startDate field of the instance
   * @param startDate the MyDate object ro be set as the startDate
   */
  public void setStartDate(MyDate startDate) { this.startDate = startDate; }
  /**
   * Sets the value for endDate field of the instance
   * @param endDate the MyDate object ro be set as the endDate
   */
  public void setEndDate(MyDate endDate) { this.endDate = endDate; }
  /**
   * A getter for the startDate field of the instance
   * @return value of startDate field
   */
  public MyDate getStartDate() { return startDate; }
  /**
   * A getter for the endDate field of the instance
   * @return value of endDate field
   */
  public MyDate getEndDate() { return endDate; }

  /**
   * methods with no parameters, which returns all the important information about the ExamTerm class
   * @return start date and end date of the exam term
   */
  @Override public String toString()
  {
    return "Start: "+startDate+"\n"+"End: "+endDate;
  }
}
