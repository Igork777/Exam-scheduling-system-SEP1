import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EditExamGUI
{
  private ComboBox<Course> courseCmb;
  private ComboBox<Exam.Types> typeCmb;
  private ComboBox<Class> classCmb;
  private ComboBox<Teacher> teacherCmb;
  private ComboBox<Classroom> classroomCmb;
  private GridPane editExamGPane;
  private CmbsListener listener = new CmbsListener();
  private Button saveBtn;
  private Stage stage;

  public EditExamGUI(DayViewTabGUI dayView){
    VBox mainBox = new VBox();
    // Create
    // Labels
    Label courseLab, typeLab, classLab, teacherLab, classroomLab;
    courseLab = new Label("Course:");
    typeLab = new Label("Type:");
    classLab = new Label("Class:");
    teacherLab = new Label("Teacher:");
    classroomLab = new Label("Classroom:");

    // ComboBoxes
    courseCmb = new ComboBox<>();
    typeCmb = new ComboBox<>();
    classCmb = new ComboBox<>();
    teacherCmb = new ComboBox<>();
    classroomCmb = new ComboBox<>();

    courseCmb.setOnAction(listener);
    typeCmb.setOnAction(listener);
    classCmb.setOnAction(listener);
    teacherCmb.setOnAction(listener);
    classroomCmb.setOnAction(listener);

    courseCmb.setPromptText("Course");
    typeCmb.setPromptText("Type");
    classCmb.setPromptText("Class");
    teacherCmb.setPromptText("Teacher");
    classroomCmb.setPromptText("Classroom");

    editExamGPane = new GridPane();

    saveBtn = new Button("Save");
    saveBtn.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent actionEvent) {
        Exam.allExams.add(new Exam(courseCmb.getValue(), typeCmb.getValue(), classCmb.getValue(), teacherCmb.getValue(), classroomCmb.getValue() ));
        Exam.allExamsIO.write(Exam.allExams);
        stage.close();
        dayView.sync();
      }
    });

    // Styling
    mainBox.setPadding(new Insets(5));
    mainBox.setSpacing(5);
    mainBox.setAlignment(Pos.CENTER);
    editExamGPane.setAlignment(Pos.CENTER);
    // Sync
    listener.sync();

    // Assembling
    editExamGPane = new GridPane();
    editExamGPane.setVgap(5);
    editExamGPane.setHgap(5);


    editExamGPane.addRow(0, courseLab, courseCmb);
    editExamGPane.addRow(1, teacherLab, teacherCmb);
    editExamGPane.addRow(2, classLab, classCmb);
    editExamGPane.addRow(3, typeLab, typeCmb);
    editExamGPane.addRow(4, classroomLab, classroomCmb);
    mainBox.getChildren().addAll(editExamGPane,saveBtn);


    stage = new Stage();
    stage.setTitle("Add exam");
    Scene scene = new Scene(mainBox, 200, 280);
    stage.setScene(scene);
    stage.show();
  }

  private class CmbsListener implements EventHandler<ActionEvent>{

    public boolean isSelected(ComboBox cmb){return cmb.getSelectionModel().getSelectedItem() != null;}

    public void clear(ComboBox cmb){cmb.getItems().clear();}

    public void feed(ComboBox cmb, ArrayList arr){ cmb.getItems().addAll(arr); }

    public void reset(ComboBox cmb, ArrayList arr){
      if(cmb.getItems() != arr){
        clear(cmb);
        feed(cmb, arr);
      }
    }
    public void sync(){
      if(!isSelected(courseCmb)){reset(courseCmb, Course.allCourses.getList());}
      teacherCmb.setDisable(!isSelected(courseCmb));
      classCmb.setDisable(!(isSelected(courseCmb) && isSelected(teacherCmb)));
      typeCmb.setDisable(!(isSelected(courseCmb) && isSelected(teacherCmb) && isSelected(classCmb)));
      classroomCmb.setDisable(!(isSelected(courseCmb) && isSelected(teacherCmb) && isSelected(classCmb) && isSelected(typeCmb)));
      saveBtn.setDisable(!(isSelected(courseCmb) && isSelected(teacherCmb) && isSelected(classCmb) && isSelected(typeCmb)));

      if(isSelected(courseCmb)){
        Course thisCourse = courseCmb.getValue();

        if(!isSelected(teacherCmb)){
          reset(teacherCmb, thisCourse.getTeachers().getList());
        }else{
          courseCmb.setDisable(true);
        }

        if(!isSelected(classCmb)){ reset(classCmb, thisCourse.getClasses().getList()); }else{teacherCmb.setDisable(true);}

        if(isSelected(classroomCmb)){typeCmb.setDisable(true);}

        if(isSelected(teacherCmb) && isSelected(classCmb)){
          Class thisClass = classCmb.getValue();

          if(!isSelected(typeCmb)){
            ArrayList<Exam.Types> types = new ArrayList<>();
            if (thisCourse.isOralExamTaken()){ types.add(Exam.Types.ORAL); }
            if (thisCourse.isWrittenExamTaken()){ types.add(Exam.Types.WRITTEN); }
            if (thisCourse.isSemesterExamTaken()){ types.add(Exam.Types.SEMESTER); }
            reset(typeCmb, types);
          }else{
            classCmb.setDisable(true);
            if(typeCmb.getValue() != Exam.Types.WRITTEN){
              if (!isSelected(classroomCmb)){
                Classroom homeClassroom = thisClass.getHomeClassroom();
                if (homeClassroom.getHasProjector()){
                  clear(classroomCmb);
                  classroomCmb.getItems().add(homeClassroom);
                  classroomCmb.setValue(homeClassroom);
                }else{
                  Alert notExported = new Alert(Alert.AlertType.INFORMATION);
                  notExported.setTitle("Sorry.");
                  notExported.setHeaderText(null);
                  notExported.setContentText("Your home classroom doesn't have a projector. Choose from the list of the classes with a projector.");
                  notExported.show();
                  reset(classroomCmb, Classroom.allClassrooms.findBy("getHasProjector", true));
                }
              }
            }else{
              if(!isSelected(classroomCmb)){
                reset(classroomCmb, Classroom.allClassrooms.getList());
              }
            }
          }
        }
      }
    }

    public Object val(ComboBox cmb){
      return cmb.getSelectionModel().getSelectedItem();
    }

    public void handle(ActionEvent e) {
      listener.sync();
    }
  }
}
