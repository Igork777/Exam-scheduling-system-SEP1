import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Optional;

public class CourseTabGUI extends Tab
{
    private HBox mainHbox, questionAboutOralExam, radiobuttonsAboutOralExam, minutesOralExamHBox, questionAboutWrittenExam, radiobuttonsAboutWrittenExam, minutesWrittenExamHBox, questionAboutSemesterExam, radiobuttonsAboutSemesterExam, minutesSemesterHBox, wrapperForButtonSelectApply, wrapperForClassButtons, wrapperForTeacherButtons, wrapperForCourseButtons, courseHBox, buttonHSaveClass, buttonHSaveTeacher;
    private Button buttonRemoveClass, buttonAddClass, applyMain, buttonRemoveTeacher, buttonAddTeacher, buttonChangeCourse,  buttonRemoveCourse, buttonAddCourse, buttonSaveCourse, buttonSaveClass, buttonSaveTeacher;
    private VBox firstVBox, secondVBox, thirdVBox, courseVBox, classVBox, teacherVBox;
    private Label labelCourse, labelQuestionAboutOralExam, labelMinutesOralExam, labelQuestionAboutWrittenExam, labelMinutesWrittenExam, labelQuestionAboutSemesterExam, labelMinutesSemesterExam;
    private TextField textFieldCourse, textFieldMinutesOralExam, textFieldMinutesWrittenExam, textFieldMinutesSemesterExam;
    private RadioButton yesAboutOralExam, noAboutOralExam, yesAboutWrittenExam, noAboutWrittenExam, yesAboutSemesterExam, noAboutSemesterExam;
    private ToggleGroup answersAboutOralExam, answerAboutWrittenExam, answerAboutSemesterExam;
    private TitledPane courseTitlePane, classTitlePane, teachersTitlePane;
    private TableView <Class> classTable = new TableView<>(), classTableWindow = new TableView<>();
    private TableView <Teacher> teachersTable = new TableView<>() , teacherTableWindow = new TableView<>();
    private TableColumn <Class, String> classColumnName, classColumnNumberOfStudents, classColumnClassroom;
    private TableView <Course> courseTable = new TableView<>();
    private TableColumn <Course, String> courseNameColumn;
    private TableColumn <Course, String> courseHasExamsColumn;
    private TableColumn <Teacher, String> teachersName , teachersNameWindow;
    private CourseListener courseListener;
    private CourseWindowListener courseWindowListener;
    private BorderPane realMain;
    private Stage windowCourse, windowClass, windowTeacher;
    private ClassWindowListener classWindowListener;
    private TeacherWindowListener teacherWindowListener;
    private CourseMouseListener courseMouseListener;
    private boolean isForChange = false;

    private TableColumn<Class, String> nameColumn, numberOfStudentsColumn, homeClassroomColumn;

  public void sync(){
    MyList<Course> fileCourses = Course.allCoursesIO.read();
    if(Course.allCourses.empty() && fileCourses != null){
      Course.allCourses = fileCourses;
    }
    courseTable.getItems().clear();
    courseTable.getItems().addAll(Course.allCourses.getList());
  }
    public CourseTabGUI (String title)
    {
        super(title);
        courseListener = new CourseListener();
        courseMouseListener = new CourseMouseListener();
        buttonRemoveClass = new Button("Remove");
        buttonAddClass = new Button("Add");
        wrapperForClassButtons = new HBox(40,buttonAddClass, buttonRemoveClass);
       buttonRemoveTeacher = new Button("Remove");
       buttonAddTeacher = new Button("Add");
       wrapperForTeacherButtons = new HBox(40,buttonAddTeacher, buttonRemoveTeacher);

       buttonChangeCourse = new Button("Edit");
       buttonRemoveCourse = new Button("Remove");
       buttonAddCourse = new Button("Add");
       wrapperForCourseButtons = new HBox(40, buttonChangeCourse, buttonAddCourse, buttonRemoveCourse);

         applyMain = new Button("Apply");
        applyMain.setPrefSize(140, 50);
      wrapperForButtonSelectApply = new HBox(10, applyMain);
        wrapperForButtonSelectApply.setAlignment(Pos.CENTER);
        wrapperForButtonSelectApply.setPadding(new Insets(10,0,0,0));

        courseTitlePane = new TitledPane();
        courseTitlePane.setText("Courses");
        courseNameColumn = new TableColumn<>("Name");
        courseHasExamsColumn = new TableColumn<>("Exams");
        courseNameColumn.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        courseHasExamsColumn.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        courseTable.setPrefWidth(350);
        courseTable.setPrefHeight(360);
        courseNameColumn.setPrefWidth(90);
        courseHasExamsColumn.setPrefWidth(260);
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseHasExamsColumn.setCellValueFactory(new PropertyValueFactory<>("examInfo"));
        courseTable.getColumns().addAll(courseNameColumn,courseHasExamsColumn);
        wrapperForCourseButtons.setAlignment(Pos.CENTER_RIGHT);
        firstVBox = new VBox(7,courseTable, wrapperForCourseButtons);
        firstVBox.setPadding(new Insets(0));
        firstVBox.setAlignment(Pos.CENTER_RIGHT);
        courseTitlePane.setContent(firstVBox);





        classTitlePane = new TitledPane();
        classTitlePane.setText("Classes");
        classTable.setPrefHeight(360);
        classColumnName = new TableColumn<>("Name");
        classColumnNumberOfStudents = new TableColumn<>("Number of students");
        classColumnClassroom = new TableColumn<>("Home classroom");
        classColumnName.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        classColumnNumberOfStudents.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        classColumnClassroom.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        classTable.setPrefWidth(458);
        classColumnName.setPrefWidth(136);
        classColumnNumberOfStudents.setPrefWidth(133);
        classColumnClassroom.setPrefWidth(189);
        classColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        classColumnNumberOfStudents.setCellValueFactory(new PropertyValueFactory<>("numberOfStudents"));
        classColumnClassroom.setCellValueFactory(new PropertyValueFactory<>("homeClassroom"));
        classTable.getColumns().addAll(classColumnName,classColumnNumberOfStudents, classColumnClassroom);


        teachersTitlePane = new TitledPane();
        teachersTitlePane.setText("Teachers");
        teachersTable.setPrefHeight(360);
        teachersName = new TableColumn<>("Name");
        teachersName.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        teachersName.setPrefWidth(350);
        teachersName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        teachersTable.getColumns().addAll(teachersName);
        wrapperForClassButtons.setAlignment(Pos.CENTER_RIGHT);
        secondVBox = new VBox(7, classTable, wrapperForClassButtons);
        secondVBox.setAlignment(Pos.CENTER_RIGHT);
        classTitlePane.setContent(secondVBox);
        secondVBox.setPadding(new Insets(0));
        wrapperForTeacherButtons.setAlignment(Pos.CENTER_RIGHT);
        thirdVBox = new VBox(7, teachersTable,wrapperForTeacherButtons);
        thirdVBox.setPadding(new Insets(0));
        thirdVBox.setAlignment(Pos.CENTER_RIGHT);
        teachersTitlePane.setContent(thirdVBox);
        mainHbox = new HBox(30, courseTitlePane, classTitlePane, teachersTitlePane);
        realMain = new BorderPane();
        realMain.setPadding(new Insets(15));
        realMain.setCenter(mainHbox);
        realMain.setBottom(wrapperForButtonSelectApply);


        buttonSaveClass = new Button("Save");
        buttonSaveClass.setPadding(new Insets(15,40,15,40));
        buttonHSaveClass = new HBox(buttonSaveClass);
        buttonHSaveClass.setPadding(new Insets(35,0,0,210));

        nameColumn = new TableColumn<>("Name");
        numberOfStudentsColumn = new TableColumn<>("Number of\nStudents");
        homeClassroomColumn = new TableColumn<>("Home Classroom");
        nameColumn.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        numberOfStudentsColumn.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        homeClassroomColumn.getStyleClass().add("-fx-alignment: LEFT-RIGHT;");
        nameColumn.setPrefWidth(150);
        numberOfStudentsColumn.setPrefWidth(130);
        homeClassroomColumn.setPrefWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        numberOfStudentsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfStudents"));
        homeClassroomColumn.setCellValueFactory(new PropertyValueFactory<>("homeClassroom"));
        classTableWindow.getColumns().addAll(nameColumn, numberOfStudentsColumn, homeClassroomColumn);


        buttonSaveTeacher = new Button("Save");
        buttonSaveTeacher.setPadding(new Insets(10,30,10,30));
        buttonHSaveTeacher = new HBox(buttonSaveTeacher);
        buttonHSaveTeacher.setPadding(new Insets(0,0,0,110));
        teachersNameWindow = new TableColumn<>("Name");
        teachersNameWindow.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        teachersNameWindow.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        teacherTableWindow.setPrefWidth(300);
        teacherTableWindow.setPrefHeight(400);
        teachersNameWindow.setPrefWidth(300);
        teacherTableWindow.getColumns().add(teachersNameWindow);

        applyMain.setOnAction(courseListener);
        buttonAddCourse.setOnAction(courseListener);
        buttonRemoveCourse.setOnAction(courseListener);
        buttonAddClass.setOnAction(courseListener);
        buttonRemoveClass.setOnAction(courseListener);
        buttonAddTeacher.setOnAction(courseListener);
        buttonRemoveTeacher.setOnAction(courseListener);
        courseTable.setOnMouseClicked(courseMouseListener);
        buttonChangeCourse.setOnAction(courseListener);
        sync();
        super.setContent(realMain);
    }
    //Daniel don't forget about listener
    public class CourseListener implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent e)
        {
            courseWindowListener = new CourseWindowListener();
            if (e.getSource() == applyMain){
              Course.allCoursesIO.write(Course.allCourses);
              System.out.println("saved");
              System.out.println(Course.allCoursesIO.read());
            }
            else if(e.getSource() == buttonAddCourse)
            {
                buttonSaveCourse = new Button("Save");
                windowCourse = new Stage();
                windowCourse.setTitle("Adding a course");

                        labelCourse = new Label("Course:");
                        labelMinutesOralExam = new Label("Duration of the oral exam :");
        labelMinutesWrittenExam = new Label("Duration of the written exam :");
        labelQuestionAboutWrittenExam = new Label("Does the course have a written exam ?");
        labelQuestionAboutSemesterExam = new Label("Does the course have a semester exam ?");
        labelMinutesSemesterExam = new Label("Duration of the semester exam :");
                        questionAboutSemesterExam = new HBox(labelQuestionAboutSemesterExam);
        questionAboutWrittenExam = new HBox(labelQuestionAboutWrittenExam);
        textFieldCourse = new TextField();
        textFieldMinutesSemesterExam =new TextField();
        textFieldMinutesWrittenExam = new TextField();
        textFieldMinutesOralExam = new TextField();
        textFieldMinutesOralExam.setEditable(false);
        textFieldMinutesOralExam.setDisable(true);
        textFieldMinutesWrittenExam.setEditable(false);
        textFieldMinutesWrittenExam.setDisable(true);
        textFieldMinutesSemesterExam.setEditable(false);
        textFieldMinutesSemesterExam.setDisable(true);
        minutesSemesterHBox = new HBox(20, labelMinutesSemesterExam, textFieldMinutesSemesterExam);
        minutesOralExamHBox = new HBox(44, labelMinutesOralExam, textFieldMinutesOralExam);
        minutesWrittenExamHBox = new HBox(29, labelMinutesWrittenExam, textFieldMinutesWrittenExam);
        answersAboutOralExam = new ToggleGroup();
        answerAboutSemesterExam = new ToggleGroup();
        labelQuestionAboutOralExam = new Label("Does the course have an oral exam ?");
        yesAboutOralExam = new RadioButton("Yes");
        noAboutOralExam = new RadioButton("No");
        answerAboutWrittenExam = new ToggleGroup();
        yesAboutWrittenExam = new RadioButton("Yes");
        noAboutWrittenExam =new RadioButton("No");
        yesAboutSemesterExam = new RadioButton("Yes");
        noAboutSemesterExam = new RadioButton("No");

        yesAboutWrittenExam.setToggleGroup(answerAboutWrittenExam);
        noAboutWrittenExam.setToggleGroup(answerAboutWrittenExam);
        yesAboutOralExam.setToggleGroup(answersAboutOralExam);
        noAboutOralExam.setToggleGroup(answersAboutOralExam);
        yesAboutSemesterExam.setToggleGroup(answerAboutSemesterExam);
        noAboutSemesterExam.setToggleGroup(answerAboutSemesterExam);
        answersAboutOralExam.selectToggle(noAboutOralExam);
        answerAboutWrittenExam.selectToggle(noAboutWrittenExam);
        answerAboutSemesterExam.selectToggle(noAboutSemesterExam);
        radiobuttonsAboutWrittenExam = new HBox(10,yesAboutWrittenExam,noAboutWrittenExam);
        radiobuttonsAboutOralExam = new HBox(10,yesAboutOralExam, noAboutOralExam);
        radiobuttonsAboutSemesterExam = new HBox(10, yesAboutSemesterExam, noAboutSemesterExam);
        questionAboutOralExam = new HBox(labelQuestionAboutOralExam);
        courseHBox = new HBox(20, labelCourse, textFieldCourse);
        firstVBox = new VBox(20,courseHBox, questionAboutOralExam, radiobuttonsAboutOralExam, minutesOralExamHBox, questionAboutWrittenExam,radiobuttonsAboutWrittenExam, minutesWrittenExamHBox, questionAboutSemesterExam,radiobuttonsAboutSemesterExam, minutesSemesterHBox);
                courseVBox = new VBox(20,courseHBox, questionAboutOralExam, radiobuttonsAboutOralExam, minutesOralExamHBox, questionAboutWrittenExam,radiobuttonsAboutWrittenExam, minutesWrittenExamHBox, questionAboutSemesterExam,radiobuttonsAboutSemesterExam, minutesSemesterHBox, buttonSaveCourse);
                        yesAboutOralExam.setOnAction(courseWindowListener);
                      noAboutOralExam.setOnAction(courseWindowListener);
        yesAboutWrittenExam.setOnAction(courseWindowListener);
                     noAboutWrittenExam.setOnAction(courseWindowListener);
                   yesAboutSemesterExam.setOnAction(courseWindowListener);
                    noAboutSemesterExam.setOnAction(courseWindowListener);
                    buttonSaveCourse.setOnAction(courseWindowListener);
                    courseVBox.setPadding(new Insets(10));
                    courseVBox.setAlignment(Pos.CENTER);
                Scene scene = new Scene(courseVBox, 400, 500);
                windowCourse.setScene(scene);
                windowCourse.show();


          }
            else if (e.getSource() == buttonRemoveCourse)
            {
                if(courseTable.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "You should choose the element you want to delete", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    Course course = courseTable.getSelectionModel().getSelectedItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Remove the course");
                    alert.setHeaderText(null);
                    alert.setContentText(
                            "Are you sure you want to delete a course:\n" + course );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK)
                    {

                        ArrayList<Exam> examsByCourse = Exam.allExams.findBy("getCourse", course);
                        Exam.allExams.remove(examsByCourse);
                        Exam.allExamsIO.write(Exam.allExams);
                        Course.allCourses.remove(course);
                        Course.allCoursesIO.write(Course.allCourses);
                        courseTable.getItems().remove(course);
                        classTable.getItems().clear();
                        teachersTable.getItems().clear();
                    }
                }
            }
            else if (e.getSource() == buttonAddClass)
            {
                windowClass = new Stage();
                classWindowListener = new ClassWindowListener();

                    if(!(classTableWindow.getItems().equals(Class.allClasses.getList())))
                    {
                        classTableWindow.getItems().clear();
                        classTableWindow.getItems().addAll(Class.allClasses.getList());
                    }

                classVBox = new VBox(classTableWindow, buttonHSaveClass);
                Scene scene = new Scene(classVBox, 530, 500);
                buttonSaveClass.setOnAction(classWindowListener);
                windowClass.setTitle("Adding classes");
                windowClass.setScene(scene);
                windowClass.show();
            }
            else if (e.getSource() == buttonRemoveClass)
            {
                if(classTable.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "You should choose a class in order to remove something", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                  Course thisCourse = courseTable.getSelectionModel().getSelectedItem();
                  Course.allCourses.remove(thisCourse);
                  Class class1 = classTable.getSelectionModel().getSelectedItem();
                  classTable.getItems().remove(class1);
                  thisCourse.classes.remove(class1);
                  Course.allCourses.add(thisCourse);
                  Course.allCoursesIO.write(Course.allCourses);
                }
            }
            else if (e.getSource() == buttonAddTeacher)
            {
                windowTeacher = new Stage();
                teacherWindowListener = new TeacherWindowListener();

                if(!(teacherTableWindow.getItems().equals(Teacher.allTeachers.getList())))
                {
                    teacherTableWindow.getItems().clear();
                    teacherTableWindow.getItems().addAll(Teacher.allTeachers.getList());
                }
                teacherVBox = new VBox(45, teacherTableWindow, buttonHSaveTeacher);
                Scene scene = new Scene(teacherVBox, 300,500);
                buttonSaveTeacher.setOnAction(teacherWindowListener);
                windowTeacher.setTitle("Adding teachers");
                windowTeacher.setScene(scene);
                windowTeacher.show();
            }
            else if (e.getSource() == buttonRemoveTeacher)
            {
                if(teachersTable.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "You should choose a teacher in order to remove one of them", "Error",JOptionPane.ERROR_MESSAGE);
                }

                else
                {
                    Course thisCourse = courseTable.getSelectionModel().getSelectedItem();
                    Course.allCourses.remove(thisCourse);
                    Teacher teacher = teachersTable.getSelectionModel().getSelectedItem();
                    teachersTable.getItems().remove(teacher);
                    thisCourse.teachers.remove(teacher);
                    Course.allCourses.add(thisCourse);
                    Course.allCoursesIO.write(Course.allCourses);
                }
            }
            else if (e.getSource() == buttonChangeCourse)
            {
                if(courseTable.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "You have to select the course in order to edit something", "Error",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    Course course = courseTable.getSelectionModel().getSelectedItem();
                    isForChange = true;
                    buttonSaveCourse = new Button("Save");
                    windowCourse = new Stage();
                    windowCourse.setTitle("Editing  the course");
//                    courseTable.getItems().remove(course);
//                    Course.allCourses.remove(course);
                    labelCourse = new Label("Course:");
                    labelMinutesOralExam = new Label("Duration of the oral exam :");
                    labelMinutesWrittenExam = new Label("Duration of the written exam :");
                    labelQuestionAboutWrittenExam = new Label("Does the course have a written exam ?");
                    labelQuestionAboutSemesterExam = new Label("Does the course have a semester exam ?");
                    labelMinutesSemesterExam = new Label("Duration of the semester exam :");
                    questionAboutSemesterExam = new HBox(labelQuestionAboutSemesterExam);
                    questionAboutWrittenExam = new HBox(labelQuestionAboutWrittenExam);
                    textFieldCourse = new TextField();
                    textFieldMinutesSemesterExam =new TextField();
                    textFieldMinutesWrittenExam = new TextField();
                    textFieldMinutesOralExam = new TextField();
                    textFieldCourse.setText(course.getName());
                    textFieldMinutesOralExam.setEditable(course.isOralExamTaken());
                    textFieldMinutesOralExam.setDisable(!(course.isOralExamTaken()));
                    textFieldMinutesWrittenExam.setEditable(course.isWrittenExamTaken());
                    textFieldMinutesWrittenExam.setDisable(!(course.isWrittenExamTaken()));
                    textFieldMinutesSemesterExam.setEditable(course.isSemesterExamTaken());
                    textFieldMinutesSemesterExam.setDisable(!(course.isSemesterExamTaken()));

                    if(course.isOralExamTaken())
                    {
                        textFieldMinutesOralExam.setText(Integer.toString(course.getOralExamDuration()));
                    }
                    if(course.isWrittenExamTaken())
                    {
                        textFieldMinutesWrittenExam.setText(Integer.toString(course.getWrittenExamDuration()));
                    }
                    if (course.isSemesterExamTaken())
                    {
                        textFieldMinutesSemesterExam.setText(Integer.toString(course.getSemesterExamDuration()));
                    }

                    minutesSemesterHBox = new HBox(20, labelMinutesSemesterExam, textFieldMinutesSemesterExam);
                    minutesOralExamHBox = new HBox(44, labelMinutesOralExam, textFieldMinutesOralExam);
                    minutesWrittenExamHBox = new HBox(29, labelMinutesWrittenExam, textFieldMinutesWrittenExam);
                    answersAboutOralExam = new ToggleGroup();
                    answerAboutSemesterExam = new ToggleGroup();
                    labelQuestionAboutOralExam = new Label("Does the course have an oral exam ?");
                    yesAboutOralExam = new RadioButton("Yes");
                    noAboutOralExam = new RadioButton("No");
                    answerAboutWrittenExam = new ToggleGroup();
                    yesAboutWrittenExam = new RadioButton("Yes");
                    noAboutWrittenExam =new RadioButton("No");
                    yesAboutSemesterExam = new RadioButton("Yes");
                    noAboutSemesterExam = new RadioButton("No");

                    yesAboutWrittenExam.setToggleGroup(answerAboutWrittenExam);
                    noAboutWrittenExam.setToggleGroup(answerAboutWrittenExam);
                    yesAboutOralExam.setToggleGroup(answersAboutOralExam);
                    noAboutOralExam.setToggleGroup(answersAboutOralExam);
                    yesAboutSemesterExam.setToggleGroup(answerAboutSemesterExam);
                    noAboutSemesterExam.setToggleGroup(answerAboutSemesterExam);


                    answersAboutOralExam.selectToggle(course.isOralExamTaken() ? yesAboutOralExam : noAboutOralExam);
                    answerAboutWrittenExam.selectToggle(course.isWrittenExamTaken() ? yesAboutWrittenExam : noAboutWrittenExam);
                    answerAboutSemesterExam.selectToggle(course.isSemesterExamTaken() ? yesAboutSemesterExam : noAboutSemesterExam);

                    radiobuttonsAboutWrittenExam = new HBox(10,yesAboutWrittenExam,noAboutWrittenExam);
                    radiobuttonsAboutOralExam = new HBox(10,yesAboutOralExam, noAboutOralExam);
                    radiobuttonsAboutSemesterExam = new HBox(10, yesAboutSemesterExam, noAboutSemesterExam);
                    questionAboutOralExam = new HBox(labelQuestionAboutOralExam);
                    courseHBox = new HBox(20, labelCourse, textFieldCourse);
                    firstVBox = new VBox(20,courseHBox, questionAboutOralExam, radiobuttonsAboutOralExam, minutesOralExamHBox, questionAboutWrittenExam,radiobuttonsAboutWrittenExam, minutesWrittenExamHBox, questionAboutSemesterExam,radiobuttonsAboutSemesterExam, minutesSemesterHBox);
                    courseVBox = new VBox(20,courseHBox, questionAboutOralExam, radiobuttonsAboutOralExam, minutesOralExamHBox, questionAboutWrittenExam,radiobuttonsAboutWrittenExam, minutesWrittenExamHBox, questionAboutSemesterExam,radiobuttonsAboutSemesterExam, minutesSemesterHBox, buttonSaveCourse);
                    yesAboutOralExam.setOnAction(courseWindowListener);
                    noAboutOralExam.setOnAction(courseWindowListener);
                    yesAboutWrittenExam.setOnAction(courseWindowListener);
                    noAboutWrittenExam.setOnAction(courseWindowListener);
                    yesAboutSemesterExam.setOnAction(courseWindowListener);
                    noAboutSemesterExam.setOnAction(courseWindowListener);
                    buttonSaveCourse.setOnAction(courseWindowListener);
                    courseVBox.setPadding(new Insets(10));
                    courseVBox.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(courseVBox, 400, 500);
                    windowCourse.setScene(scene);
                    windowCourse.show();
                }
            }

        }
    }
    public class TeacherWindowListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e)
        {
            if (e.getSource() == buttonSaveTeacher)
            {
                if (teacherTableWindow.getSelectionModel().getSelectedItem() == null)
                    JOptionPane.showMessageDialog(null, "You should choose a teacher in order to save it", "Error", JOptionPane.ERROR_MESSAGE);
                else if (courseTable.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "You should choose a course in order to add teachers", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    Teacher teacher = teacherTableWindow.getSelectionModel().getSelectedItem();
                    if (courseTable.getSelectionModel().getSelectedItem().teachers.contains(teacher))
                    {
                        JOptionPane.showMessageDialog(null, "Such teacher already exists in " + courseTable.getSelectionModel().getSelectedItem().getName() + " course", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else
                        {

                          Course thisCourse = courseTable.getSelectionModel().getSelectedItem();
                          Course.allCourses.remove(thisCourse);
                          teachersTable.getItems().add(teacher);
                          thisCourse.teachers.add(teacher);
                          Course.allCourses.add(thisCourse);
                          Course.allCoursesIO.write(Course.allCourses);
                    }
                }
                windowTeacher.close();
            }
        }
    }

    public class ClassWindowListener implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent e)
        {
            if(e.getSource() == buttonSaveClass)
            {
                if(classTableWindow.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "You should choose at least 1 class in order to save it", "Error", JOptionPane.ERROR_MESSAGE);

                }
                else if (courseTable.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "You should choose the course in order to add the classes", "Error", JOptionPane.ERROR_MESSAGE);

                }
                else
                {
                    Class class1 = classTableWindow.getSelectionModel().getSelectedItem();
                    if(courseTable.getSelectionModel().getSelectedItem().getClasses().contains(class1))
                    {
                        JOptionPane.showMessageDialog(null, "Such class already exists in the course " + courseTable.getSelectionModel().getSelectedItem().getName()  + " course", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                    else
                    {
                      Course thisCourse = courseTable.getSelectionModel().getSelectedItem();
                      Course.allCourses.remove(thisCourse);
                      classTable.getItems().add(class1);
                      thisCourse.getClasses().add(class1);
                      Course.allCourses.add(thisCourse);
                      Course.allCoursesIO.write(Course.allCourses);
                    }
                }
                windowClass.close();
            }

        }
    }
    public class CourseWindowListener implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent e)
        {
            if (e.getSource() == yesAboutOralExam)
            {
                textFieldMinutesOralExam.setDisable(false);
                textFieldMinutesOralExam.setEditable(true);
            }
            if (e.getSource() == noAboutOralExam)
                {
                    textFieldMinutesOralExam.setDisable(true);
                    textFieldMinutesOralExam.setEditable(false);
                    textFieldMinutesOralExam.clear();
                }
                 if (e.getSource() == yesAboutWrittenExam)
                {
                    textFieldMinutesWrittenExam.setDisable(false);
                    textFieldMinutesWrittenExam.setEditable(true);
                }
                if (e.getSource() == noAboutWrittenExam)
                {
                    textFieldMinutesWrittenExam.setDisable(true);
                    textFieldMinutesWrittenExam.setEditable(false);
                    textFieldMinutesWrittenExam.clear();
                }
                if (e.getSource() == yesAboutSemesterExam)
                {
                    textFieldMinutesSemesterExam.setDisable(false);
                    textFieldMinutesSemesterExam.setEditable(true);
                }
                if (e.getSource() == noAboutSemesterExam)
                {
                    textFieldMinutesSemesterExam.setDisable(true);
                    textFieldMinutesSemesterExam.setEditable(false);
                    textFieldMinutesSemesterExam.clear();
                }
                if (e.getSource() == buttonSaveCourse)
                {

                    boolean isInputFine = true;
                    if (textFieldCourse.getText().trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "Text field course is empty", "Error", JOptionPane.ERROR_MESSAGE);
                        isInputFine = false;
                    }
                    else if ((!yesAboutOralExam.isSelected()) && (!noAboutOralExam.isSelected()))
                    {
                        JOptionPane.showMessageDialog(null, "You should choose do you have or not an oral exam", "Error", JOptionPane.ERROR_MESSAGE);
                        isInputFine = false;
                    }
                    else if ((!yesAboutWrittenExam.isSelected()) && (!noAboutWrittenExam.isSelected()))
                    {
                        JOptionPane.showMessageDialog(null, "You should choose do you have or not an written exam", "Error", JOptionPane.ERROR_MESSAGE);
                        isInputFine = false;
                    }
                    else if ((!yesAboutSemesterExam.isSelected()) && (!noAboutSemesterExam.isSelected()))
                    {
                        JOptionPane.showMessageDialog(null, "You should choose do you have or not an semester exam", "Error", JOptionPane.ERROR_MESSAGE);
                        isInputFine = false;
                    }
                    else if (yesAboutOralExam.isSelected() && textFieldMinutesOralExam.getText().trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "Text field with the duration of the oral exam is empty", "Error", JOptionPane.ERROR_MESSAGE);
                        isInputFine = false;
                    }
                    else if (yesAboutWrittenExam.isSelected() && textFieldMinutesWrittenExam.getText().trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "Text field with the duration of the written exam is empty", "Error", JOptionPane.ERROR_MESSAGE);
                        isInputFine = false;
                    }
                    else if (yesAboutSemesterExam.isSelected() && textFieldMinutesSemesterExam.getText().trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "Text field with the duration of the written exam is empty", "Error", JOptionPane.ERROR_MESSAGE);
                        isInputFine = false;
                    }
                     if (yesAboutOralExam.isSelected() && !(textFieldMinutesOralExam.getText().trim().isEmpty()))
                    {
                        try
                        {
                            int a = Integer.parseInt(textFieldMinutesOralExam.getText());
                            if (a <= 0) {
                                JOptionPane.showMessageDialog(null, "Duration of the oral exam can't be a negative number or zero", "Error", JOptionPane.ERROR_MESSAGE);
                                isInputFine = false;
                            }
                        } catch (NullPointerException ex) {
                            isInputFine = false;
                            windowCourse.close();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Text field with the duration of the oral exam is not a whole number", "Error", JOptionPane.ERROR_MESSAGE);
                            isInputFine = false;
                            windowCourse.close();
                        }
                    }
                     if (yesAboutWrittenExam.isSelected() && !(textFieldMinutesWrittenExam.getText().trim().isEmpty()))
                    {
                        try {
                            int a = Integer.parseInt(textFieldMinutesWrittenExam.getText());
                            if (a <= 0) {
                                JOptionPane.showMessageDialog(null, "Duration of the written exam can't be a negative number or zero", "Error", JOptionPane.ERROR_MESSAGE);
                                isInputFine = false;

                            }
                        } catch (NullPointerException ex) {
                            isInputFine = false;
                            windowCourse.close();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Text field with the duration of the written exam is not a whole number", "Error", JOptionPane.ERROR_MESSAGE);
                            isInputFine = false;
                            windowCourse.close();
                        }
                    }
                     if (yesAboutSemesterExam.isSelected() && !(textFieldMinutesSemesterExam.getText().trim().isEmpty()))
                    {
                        try {
                            int a = Integer.parseInt(textFieldMinutesSemesterExam.getText());
                            if (a <= 0) {
                                JOptionPane.showMessageDialog(null, "Duration of the semester exam can't be a negative number or zero", "Error", JOptionPane.ERROR_MESSAGE);
                                isInputFine = false;

                            }
                        } catch (NullPointerException ex) {
                            isInputFine = false;
                            windowCourse.close();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Text field with the duration of the semester exam is not a whole number", "Error", JOptionPane.ERROR_MESSAGE);
                            isInputFine = false;
                            windowCourse.close();
                        }
                    }
                    else if (noAboutOralExam.isSelected() && noAboutWrittenExam.isSelected() && noAboutSemesterExam.isSelected())
                    {
                        JOptionPane.showMessageDialog(null, "This course should have at least 1 exam", "Error", JOptionPane.ERROR_MESSAGE);
                        isInputFine = false;
                    }
                    int oralExamDuration = yesAboutOralExam.isSelected() ? Integer.parseInt(textFieldMinutesOralExam.getText()) : 0;
                    int writtenExamDuration = yesAboutWrittenExam.isSelected() ? Integer.parseInt(textFieldMinutesWrittenExam.getText()) : 0;
                    int semesterExamDuration = yesAboutSemesterExam.isSelected() ? Integer.parseInt(textFieldMinutesSemesterExam.getText()) : 0;
                    Course course = new Course(textFieldCourse.getText(), yesAboutOralExam.isSelected(), yesAboutWrittenExam.isSelected(), yesAboutSemesterExam.isSelected(), oralExamDuration, writtenExamDuration, semesterExamDuration);

                  if(isForChange)
                    {
                      if(isInputFine)
                        {
                            Course oldCourse = courseTable.getSelectionModel().getSelectedItem();
                            course.classes.add(oldCourse.classes.getList());
                            course.teachers.add(oldCourse.teachers.getList());
                          Course.allCourses.remove(oldCourse);
                          Course.allCourses.add(course);
                          Course.allCoursesIO.write(Course.allCourses);
                          courseTable.getItems().remove(oldCourse);
                            courseTable.getItems().add(course);
                            classTable.getItems().clear();
                            teachersTable.getItems().clear();
                            classTable.getItems().addAll(course.classes.getList());
                            teachersTable.getItems().addAll(course.teachers.getList());
                        }
                        isForChange = false;
                    }
                  else
                    {
                        boolean isUnique = true;
                        for (int i = 0; i < Course.allCourses.size(); i++) {
                            Course thisCourse = Course.allCourses.get(i);
                            if (thisCourse.getName().equals(course.getName())) {
                                isUnique = false;
                                break;
                            }
                        }

                        if (isUnique && isInputFine) {
                          System.out.println("OK");
                          Course.allCourses.add(course);
                          Course.allCoursesIO.write(Course.allCourses);
                          System.out.println(Course.allCoursesIO.read());
                            courseTable.getItems().add(course);
                            classTable.getItems().addAll(course.classes.getList());
                            teachersTable.getItems().addAll(course.teachers.getList());
                        } else {
                            if (!(isUnique))
                                JOptionPane.showMessageDialog(null, "The course " + course.getName() + " already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    windowCourse.close();
                }
        }
    }
    public class CourseMouseListener implements EventHandler <MouseEvent>
    {

        @Override
        public void handle(MouseEvent e)
        {
            if(e.getSource() == courseTable)
            {
                if(e.getClickCount() == 1)
                {
                    if(courseTable.getSelectionModel().getSelectedItem() != null)
                    {
                        classTable.getItems().clear();
                        teachersTable.getItems().clear();
                        Course course = courseTable.getSelectionModel().getSelectedItem();
                        classTable.getItems().addAll(course.classes.getList());
                        teachersTable.getItems().addAll(course.teachers.getList());
                    }

                }
            }
        }
    }
}
