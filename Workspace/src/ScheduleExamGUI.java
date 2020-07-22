import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ScheduleExamGUI
{
  private VBox mainBox = new VBox();
  private ComboBox<Integer> hourCmb  = new ComboBox();
  private ComboBox<Integer> minCmb  = new ComboBox();
  private Stage stage;

  public ScheduleExamGUI(DayViewTabGUI dayView, Exam exam, MyDate date){
    // Create
    for (int i = 8; i < 17; i++) { if(i != 13){ hourCmb.getItems().add(i); } }
    for (int i = 0; i < 59; i += 5) { minCmb.getItems().add(i); }
    Label label = new Label("Exam starts at: ");
    hourCmb.setPromptText("Hour");
    minCmb.setPromptText("Minutes");
    Button saveBtn = new Button("Save");
    // Listeners
    saveBtn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent actionEvent) {
        if (hourCmb.getSelectionModel().getSelectedItem() != null ){
          int hour = hourCmb.getSelectionModel().getSelectedItem();
          if (minCmb.getSelectionModel().getSelectedItem() == null){ minCmb.setValue(0); }
          int min = minCmb.getSelectionModel().getSelectedItem();
          Exam newExam = exam.copy();
          newExam.schedule(date, new Clock(hour, min));
          boolean isValid = isValid(newExam);
          if(isValid){
            Exam.allExams.remove(exam);
            Exam.allExams.add(newExam);
            Exam.allExamsIO.write(Exam.allExams);
            stage.close();
            dayView.sync();
          }
        }
      }
      public boolean isValid(Exam exam){
        Class thisClass = exam.getClassAttending();
        Teacher thisTeacher = exam.getTeacher();
        Classroom thisClassroom = exam.getClassroom();
        MyDate thisDate = exam.getStartDate();
        Clock startTime = exam.getStartTime();
        Clock endTime = exam.getEndTime();
        Method isOverlapping = Exam.inspect.getMethod("isOverlapping", MyDate.class, Clock.class, Clock.class);

        if (!thisTeacher.isAvailableOnDate(thisDate)){
          Alert notDone = new Alert(Alert.AlertType.ERROR);
          notDone.setTitle("Exam not scheduled.");
          notDone.setHeaderText(null);
          notDone.setContentText("The teacher is not available for this time (date reserved).");
          notDone.show();
          return false;
        }

        MyList<Exam> thisTeacherExams = new MyList<>(Exam.allExams.findBy("getTeacher", thisTeacher));
        boolean isTeacherFree = thisTeacherExams.findBy(isOverlapping, true, thisDate, startTime, endTime).isEmpty();
        if(!isTeacherFree){
          Alert notDone = new Alert(Alert.AlertType.ERROR);
          notDone.setTitle("Exam not scheduled.");
          notDone.setHeaderText(null);
          notDone.setContentText("The teacher is not available for this time (another exam).");
          notDone.show();
          return false;
        }

        MyList<Exam> thisClassExams = new MyList<>(Exam.allExams.findBy("getClassAttending", thisClass));
        boolean isClassFree = thisClassExams.findBy("getStartDate", thisDate).isEmpty();
        if(!isClassFree){
          Alert notDone = new Alert(Alert.AlertType.ERROR);
          notDone.setTitle("Exam not scheduled.");
          notDone.setHeaderText(null);
          notDone.setContentText("The class is not available for this day (another exam).");
          notDone.show();
          return false;
        }

        MyList<Exam> thisClassroomExams = new MyList<>(Exam.allExams.findBy("getClassroom", thisClassroom));
        boolean isClassroomFree = thisClassroomExams.findBy(isOverlapping, true, thisDate, startTime, endTime).isEmpty();
        if(!isClassroomFree){
          Alert notDone = new Alert(Alert.AlertType.ERROR);
          notDone.setTitle("Exam not scheduled.");
          notDone.setHeaderText(null);
          notDone.setContentText("The classroom is not available for this time.");
          notDone.show();
          return false;
        }

        MyList<Exam> prevDayExams = new MyList<>(Exam.allExams.findBy("getStartDate", thisDate.getPreviousDay()));
        boolean aDayGap = prevDayExams.findBy("getClassAttending", thisClass).isEmpty();
        if(!aDayGap){
          Alert notDone = new Alert(Alert.AlertType.ERROR);
          notDone.setTitle("Exam not scheduled.");
          notDone.setHeaderText(null);
          notDone.setContentText("There is an exam the day before for that class.");
          notDone.show();
          return false;
        }

        return true;
      }
    });
    // Style
    mainBox.setSpacing(5);
    mainBox.setPadding(new Insets(5));
    mainBox.setAlignment(Pos.CENTER);
    label.setFont(new Font("Arial", 15));
    // Assembly
    mainBox.getChildren().addAll(label, hourCmb, minCmb, saveBtn);

    stage = new Stage();
    stage.setTitle("Schedule exam");
    Scene scene = new Scene(mainBox, 170, 200);
    stage.setScene(scene);
    stage.show();
  }


}
