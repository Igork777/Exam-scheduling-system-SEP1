import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ClassTabGUI extends Tab
{
  private HBox mainHBox, nameHBox, numberOfStudentsHBox, homeClassroomHBox, buttonsHBox;
  private VBox firstVBox, secondVBox;
  private FlowPane center;
  private GridPane labelsPlusFields;
  private Label labelName, labelNumberOfStudents, labelHomeClassroom;
  private TextField textFieldName, textFieldNumberOfStudents;
  private Button applyButton, removeButton, addNewButton;
  private ComboBox<Classroom> homeClassroomNameComboBox;
  private TableView <Class>  classTable;
  private TableColumn<Class, String> nameColumn, numberOfStudentsColumn, homeClassroomColumn;
  private BorderPane borderPane;
  private ClassListener classListener;
  public ClassTabGUI(String title)
  {
    super(title);
    classListener = new ClassListener();
    applyButton = new Button("Apply");
    removeButton = new Button("Remove");
    addNewButton = new Button("Add new");
    buttonsHBox = new HBox(10, applyButton,removeButton, addNewButton);
    buttonsHBox.setAlignment(Pos.CENTER_RIGHT);
    homeClassroomNameComboBox = new ComboBox<>();

    homeClassroomNameComboBox.getItems().addAll(Classroom.allClassrooms.getList());

    homeClassroomNameComboBox.setPromptText("Classroom");

    labelName = new Label("Name:");
    labelNumberOfStudents = new Label("Number of students:");
    labelHomeClassroom = new Label("Home classroom:");
    textFieldName = new TextField();
    textFieldNumberOfStudents = new TextField();


    classTable = new TableView<>();
    nameColumn = new TableColumn<>("Name");
    numberOfStudentsColumn = new TableColumn<>("Number of\nStudents");
    homeClassroomColumn = new TableColumn<>("Home Classroom");
    nameColumn.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
    numberOfStudentsColumn.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
    homeClassroomColumn.getStyleClass().add("-fx-alignment: LEFT-RIGHT;");
    nameColumn.setPrefWidth(150);
    numberOfStudentsColumn.setPrefWidth(100);
    homeClassroomColumn.setPrefWidth(250);
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    numberOfStudentsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfStudents"));
    homeClassroomColumn.setCellValueFactory(new PropertyValueFactory<>("homeClassroom"));
    classTable.getColumns().addAll(nameColumn, numberOfStudentsColumn, homeClassroomColumn);
    labelsPlusFields = new GridPane();
    labelsPlusFields.addRow(0, labelName, textFieldName);
    labelsPlusFields.addRow(1, labelNumberOfStudents, textFieldNumberOfStudents);
    labelsPlusFields.addRow(2, labelHomeClassroom, homeClassroomNameComboBox);
    labelsPlusFields.setVgap(10);
    labelsPlusFields.setHgap(15);
    center = new FlowPane(labelsPlusFields);
    center.setAlignment(Pos.CENTER);
    borderPane = new BorderPane();
    borderPane.setMinWidth(670);
    borderPane.setCenter(center);
    borderPane.setBottom(buttonsHBox);
    borderPane.setPadding(new Insets(15));
    mainHBox = new HBox(classTable,borderPane);
    addNewButton.setOnAction(classListener);
    removeButton.setOnAction(classListener);

    applyButton.setOnAction(new saveBtnListener(Class.allClassesIO, Class.allClasses)
    {
      public void onSave() {
        Class.allClassesIO.write(Class.allClasses);
        homeClassroomNameComboBox.getSelectionModel().clearSelection();
        homeClassroomNameComboBox.setPromptText("Classroom");
        textFieldName.clear();
        textFieldNumberOfStudents.clear();
      }
      public boolean valid(Object obj) { return Class.allClasses.size() > 0; }
    });

    sync();
    super.setContent(mainHBox);
  }
  public class ClassListener implements EventHandler<ActionEvent>
  {

    @Override
    public void handle(ActionEvent e)
    {
      if(e.getSource() == addNewButton)
      {
        if(textFieldName.getText().trim().isEmpty() || textFieldNumberOfStudents.getText().trim().isEmpty() || homeClassroomNameComboBox.getSelectionModel().isEmpty())
          JOptionPane.showMessageDialog(null, "Insufficient data", "Error",JOptionPane.ERROR_MESSAGE);
        else
        {
          try
          {
            int integer = Integer.parseInt(textFieldNumberOfStudents.getText());
            if(integer < 0)
              JOptionPane.showMessageDialog(null, "Number of students can't be negative", "Error", JOptionPane.ERROR_MESSAGE);
            else
            {
                boolean hasToBeAdded = true;
              Class class1 = new Class(textFieldName.getText(), Integer.parseInt(textFieldNumberOfStudents.getText()), homeClassroomNameComboBox.getSelectionModel().getSelectedItem());
              for (int i = 0; i <classTable.getItems().size() ; i++)
              {
                    if(classTable.getItems().get(i).getName().equals(class1.getName()))
                    {
                        JOptionPane.showMessageDialog(null, "You can't add the class with the same name", "Error", JOptionPane.ERROR_MESSAGE);
                        hasToBeAdded = false;
                        break;
                    }
                    else if(classTable.getItems().get(i).getHomeClassroom().equals(class1.getHomeClassroom()))
                    {
                        JOptionPane.showMessageDialog(null, "Two classes can't have the same home classroom", "Error", JOptionPane.ERROR_MESSAGE);
                        hasToBeAdded = false;
                        break;
                    }
              }
              if(hasToBeAdded)
              {
                  Class.allClasses.add(class1);
                  classTable.getItems().add(class1);
                  homeClassroomNameComboBox.getSelectionModel().clearSelection();
                  homeClassroomNameComboBox.setPromptText("Classroom");
                  textFieldName.clear();
                  textFieldNumberOfStudents.clear();

              }
            }
          }
          catch (NumberFormatException ex)
          {
            JOptionPane.showMessageDialog(null, "Number of student is not an whole number","Error",JOptionPane.ERROR_MESSAGE);
          }
        }
      }
      else if (e.getSource() == removeButton)
      {
            if(classTable.getSelectionModel().getSelectedItem() == null)
                JOptionPane.showMessageDialog(null, "You should choose a class in order to delete it", "Error", JOptionPane.ERROR_MESSAGE);
            else
            {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Remove class");
              alert.setHeaderText(null);
              alert.setContentText("Are you sure that you want to delete a class?");
              if (alert.showAndWait().get() == ButtonType.OK) {
                Class thisClass = classTable.getSelectionModel().getSelectedItem();
                Class.allClasses.remove(thisClass);
                Method isClassAttending = Course.inspect.getMethod("isClassAttending", Class.class);
                ArrayList<Course> coursesAttendedByClass = Course.allCourses.findBy(isClassAttending, true, thisClass);
                for (int i = 0; i < coursesAttendedByClass.size(); i++) {
                  coursesAttendedByClass.get(i).getClasses().remove(thisClass);
                }
                classTable.getItems().remove(thisClass);
                Class.allClassesIO.write(Class.allClasses);
                Course.allCoursesIO.write(Course.allCourses);
              }
            }
      }
      //else if (e.getSource() == )
    }
  }

  public void sync(){
    MyList<Class> fileClasses = Class.allClassesIO.read();
    if (!Class.allClasses.equals(fileClasses) && fileClasses != null){
        Class.allClasses = fileClasses;
    }
    classTable.getItems().clear();
    classTable.getItems().addAll(Class.allClasses.getList());

    homeClassroomNameComboBox.getItems().clear();
    homeClassroomNameComboBox.getItems().addAll(Classroom.allClassrooms.getList());
  }
}
