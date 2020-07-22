import javafx.application.Application;

public class Main
{
  public static MyFileIO<ExamTerm> examTermIO = new MyFileIO("examTerm");
  public static ExamTerm examTerm = examTermIO.read();


  public static void main(String[] args)
  {
    if (examTerm == null){ examTerm = new ExamTerm(); }
    Application.launch(MainGUI.class);
  }
}
