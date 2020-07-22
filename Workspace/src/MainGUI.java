import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class MainGUI extends Application
{
  private VBox mainPane;
  // MENU BAR
  private MenuBar menuBar;
  private Menu fileMenu;
  private Menu editMenu;
  private Menu aboutMenu;
  private MenuItem exitMenuItem;
  private MenuItem aboutMenuItem;
  //  private CheckMenuItem editTableMenuItem;
  //  private CheckMenuItem editFieldsMenuItem;

  // TABS
  public static TabPane tabPane;
  private ClassTabGUI classTab;
  private ClassroomTabGUI classroomTab;
  private ExamTermViewTabGUI examTermTab;
  private TeacherTabGUI teachersTab;
  private CourseTabGUI courseTab;
  // LISTENERS
  private MyTabListener tabListener;
  private MyActionListener listener;

  /**
   * @param window The Stage object that will be displayed
   */
  public void start(Stage window) throws Exception
  {
    window.setTitle("VIA Exam Planner");
    // MenuBar
    menuBar = new MenuBar();
    // MenuItems
    fileMenu = new Menu("File");
    exitMenuItem = new MenuItem("Exit");
    exitMenuItem.setOnAction(listener);
    // Assembling the MenuBar
    fileMenu.getItems().add(exitMenuItem);
    menuBar.getMenus().add(fileMenu);
    // Listeners
    listener = new MyActionListener();
    tabListener = new MyTabListener();
    // TabPane
    tabPane = new TabPane();
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    tabPane.getSelectionModel().selectedItemProperty().addListener(tabListener);
    // add some tabs to the tabPane HERE using
    classTab = new ClassTabGUI("Class");
    classroomTab = new ClassroomTabGUI("Classroom"); // it should be a private Tab field
    examTermTab = new ExamTermViewTabGUI("Exam Term");
    teachersTab = new TeacherTabGUI("Teachers");
    courseTab = new CourseTabGUI("Course");

    classroomTab.updateClassroomList();
    teachersTab.sync(Teacher.allTeachers);
    classTab.sync();
    courseTab.sync();

    tabPane.getTabs().add(examTermTab);
    tabPane.getTabs().add(classroomTab);
    tabPane.getTabs().add(classTab);
    tabPane.getTabs().add(teachersTab);
    tabPane.getTabs().add(courseTab);

    mainPane = new VBox();
    mainPane.getChildren().add(menuBar);
    mainPane.getChildren().add(tabPane);

    Scene scene = new Scene(mainPane, 1200, 600);

    window.setScene(scene);
    window.setResizable(false);
    window.show();
  }

  private class MyActionListener implements EventHandler<ActionEvent>
  {
    public void handle(ActionEvent e)
    {
      if (e.getSource() == exitMenuItem)
      {
        Alert alert = new Alert(AlertType.CONFIRMATION,
            "Do you really want to exit the program?",
            ButtonType.YES, ButtonType.NO);
        alert.setTitle("Exit");
        alert.setHeaderText(null);

        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES)
        {
          System.exit(0);
        }
      }
//      else if (e.getSource() == editTableMenuItem)
//      {
//        if (editTableMenuItem.isSelected())
//        {
//          allStudentsTab.changeSelectableState(true);
//        }
//        else
//        {
//          allStudentsTab.changeSelectableState(false);
//        }
//      }
//      else if (e.getSource() == editFieldsMenuItem)
//      {
//        if (editFieldsMenuItem.isSelected())
//        {
//          changeCountryTab.changeEditableState(true);
//        }
//        else
//        {
//          changeCountryTab.changeEditableState(false);
//        }
//      }
//      else if (e.getSource() == aboutMenuItem)
//      {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setHeaderText(null);
//        alert.setTitle("About");
//        alert.setContentText("This is just a little program that demonstrates some of the GUI features in Java");
//        alert.showAndWait();
//      }
    }
  }


  private class MyTabListener implements ChangeListener<Tab>
  {
    public void changed(ObservableValue<? extends Tab> tab, Tab oldTab, Tab newTab)
    {
      // finding the Day View Tab
      DayViewTabGUI dayViewTab = null;
      for (int i = 0; i < tabPane.getTabs().size(); i++) {
        Tab thisTab = tabPane.getTabs().get(i);
        if (thisTab.getStyleClass().contains("dayView")){
          dayViewTab = (DayViewTabGUI)thisTab;
        }
      }
      if (newTab == classroomTab) {
        classroomTab.updateClassroomList();
      }else if (newTab == teachersTab){
        teachersTab.sync(Teacher.allTeachers);
      }else if(newTab == classTab){
        classTab.sync();
      }else if(newTab == courseTab){
        courseTab.sync();
      }else if(newTab == dayViewTab && dayViewTab != null){
        dayViewTab.sync();
      }
    }
  }
}
