import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

public class DayViewTabGUI extends Tab {
  private MyDate date;
  private BorderPane mainPane;
  private VBox leftPane;
  private HBox topPane;
  private VBox centerPane;
  private Button backBtn;
  private Label dateLab;
  private HBox examBtns;
  private ArrayList<Exam> dayExams;

  private TableView<Exam> examsTable;
  private TableColumn<Exam, String> examCourseCol;
  private TableColumn<Exam, String> examClassCol;
  private TableColumn<Exam, String> examTeacherCol;
  private TableColumn<Exam, String> examClassroomCol;
  private TableColumn<Exam, String> examTypeCol;

  private TableView<Exam> scheduleTable;
  private TableColumn<Exam, String> scheduleCourseCol;
  private TableColumn<Exam, String> scheduleClassCol;
  private TableColumn<Exam, String> scheduleTeacherCol;
  private TableColumn<Exam, String> scheduleClassroomCol;
  private TableColumn<Exam, String> scheduleTypeCol;
  private TableColumn<Exam, String> scheduleTimeCol;

  private Button newBtn, deleteBtn, scheduleBtn, unScheduleBtn;

  public DayViewTabGUI(MyDate date){
    super("Day view");
    this.date = date.copy();
    this.getStyleClass().add("dayView");
    generate();
  }

  public void show(){
    clearDayViews();
    sync();
    System.out.println(this.date);
    super.setContent(mainPane);
  }

  public void sync(){
    System.out.println("Syncing");
    MyList<Exam> fileExams = Exam.allExamsIO.read();
    if ((Exam.allExams.empty() || !Exam.allExams.equals(fileExams)) && fileExams != null){ Exam.allExams = fileExams; }
    examsTable.getItems().clear();
    scheduleTable.getItems().clear();
    ArrayList<Exam> scheduled = new ArrayList<>();
    ArrayList<Exam> notScheduled = new ArrayList<>();
    if (!Exam.allExams.empty()){
      scheduled = Exam.allExams.findBy("getStartDate", this.date);
      notScheduled = Exam.allExams.findBy("isScheduled", false);
      System.out.println(scheduled);
    }
    examsTable.getItems().addAll(notScheduled);
    scheduleTable.getItems().addAll(scheduled);
  }

  public BorderPane generate(){
    // Creating
    mainPane = new BorderPane();
    leftPane = new VBox();
    topPane = new HBox();
    centerPane = new VBox();
    backBtn = new Button("< Back");
    dateLab = new Label(date.dayOfWeek()+", "+date.getDay()+" "+date.getMonthName()+" "+date.getYear());

    examBtns = new HBox();
    HBox scheduleBtns = new HBox();
    newBtn = new Button("New");
    deleteBtn = new Button("Delete");
    scheduleBtn = new Button("Schedule >");
    unScheduleBtn = new Button("< Un-Schedule");
    // Table
    examsTable = new TableView();
    examCourseCol = new TableColumn<>("Course");
    examTypeCol = new TableColumn<>("Type");
    examClassCol = new TableColumn<>("Class");
    examTeacherCol = new TableColumn<>("Teacher");
    examClassroomCol = new TableColumn<>("Classroom");
    // Link cols to fields
    examCourseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
    examTypeCol.setCellValueFactory(new PropertyValueFactory<>("examType"));
    examClassCol.setCellValueFactory(new PropertyValueFactory<>("classAttending"));
    examTeacherCol.setCellValueFactory(new PropertyValueFactory<>("teacher"));
    examClassroomCol.setCellValueFactory(new PropertyValueFactory<>("classroom"));

    // Schedule Table
    scheduleTable = new TableView();
    scheduleCourseCol = new TableColumn<>("Course");
    scheduleTypeCol = new TableColumn<>("Type");
    scheduleClassCol = new TableColumn<>("Class");
    scheduleTeacherCol = new TableColumn<>("Teacher");
    scheduleClassroomCol = new TableColumn<>("Classroom");
    scheduleTimeCol = new TableColumn<>("Starting at");
    // Link cols to fields
    scheduleCourseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
    scheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("examType"));
    scheduleClassCol.setCellValueFactory(new PropertyValueFactory<>("classAttending"));
    scheduleTeacherCol.setCellValueFactory(new PropertyValueFactory<>("teacher"));
    scheduleClassroomCol.setCellValueFactory(new PropertyValueFactory<>("classroom"));
    scheduleTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

    // Styling
    dateLab.setFont(new Font("Arial", 20));
    topPane.setAlignment(Pos.CENTER_LEFT);
    topPane.setSpacing(5);
    examBtns.setSpacing(5);
    examBtns.setPadding(new Insets(5, 0, 5, 0));

    scheduleBtns.setSpacing(5);
    scheduleBtns.setPadding(new Insets(5, 0, 5, 0));
    centerPane.setPadding(new Insets(0,0,0,5));


    examsTable.setPrefHeight(500);
    examsTable.setMinWidth(513);
    examsTable.setMaxWidth(513);

    examCourseCol.setPrefWidth(90);
    examTypeCol.setPrefWidth(86);
    examClassCol.setPrefWidth(83);
    examTeacherCol.setPrefWidth(160);
    examClassroomCol.setPrefWidth(90);

    scheduleTable.setPrefHeight(500);
    scheduleClassroomCol.setMinWidth(105);
    scheduleClassroomCol.setMaxWidth(105);
    scheduleTable.setMinWidth(590);
    scheduleTable.setMaxWidth(590);

    scheduleTeacherCol.setPrefWidth(160);

    mainPane.setPadding(new Insets(5));
    DayViewTabGUI thisDayView = this;
    // Setting listeners
    scheduleBtn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent actionEvent) {
        Exam thisExam = examsTable.getSelectionModel().getSelectedItem();
        if (thisExam != null){ new ScheduleExamGUI(thisDayView, thisExam, date); }
      }
    });
    unScheduleBtn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent actionEvent) {
        Exam thisExam = scheduleTable.getSelectionModel().getSelectedItem();
        if (thisExam != null){
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("UnSchedule exam");
          alert.setHeaderText(null);
          alert.setContentText("Are you sure?");
          if (alert.showAndWait().get() == ButtonType.OK) {
            Exam.allExams.remove(thisExam);
            thisExam.unSchedule();
            Exam.allExams.add(thisExam);
            Exam.allExamsIO.write(Exam.allExams);
            thisDayView.sync();
          }
        }
      }
    });
    backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent mouseEvent) {
        MainGUI.tabPane.getSelectionModel().select(0);
      }
    });
    DayViewTabGUI newThis = this;
    newBtn.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent actionEvent) { new EditExamGUI(newThis); }
    });


    deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
      Method examEql = null;
        public void handle(ActionEvent actionEvent) {
          Exam selectedExam = examsTable.getSelectionModel().getSelectedItem();
          if (selectedExam != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?");
            alert.setTitle("Remove exam");
            alert.setHeaderText(null);
            if (alert.showAndWait().get() == ButtonType.OK) {
              Exam.allExams.remove(selectedExam);
              examsTable.getItems().remove(examsTable.getSelectionModel().getSelectedItem());
              Exam.allExamsIO.write(Exam.allExams);
            }
          }
        }
      });


    // Assembling
    // Table
    examsTable.getColumns().addAll(examCourseCol, examTypeCol, examClassCol, examTeacherCol, examClassroomCol);

    scheduleTable.getColumns().addAll(scheduleTimeCol, scheduleCourseCol, scheduleTypeCol, scheduleClassCol, scheduleTeacherCol, scheduleClassroomCol);

    topPane.getChildren().addAll(backBtn, dateLab);

    examBtns.getChildren().addAll(newBtn, deleteBtn, scheduleBtn);
    leftPane.getChildren().addAll(examBtns, examsTable);

    scheduleBtns.getChildren().addAll(unScheduleBtn);
    centerPane.getChildren().addAll(scheduleBtns, scheduleTable);


    mainPane.setTop(topPane);
    mainPane.setCenter(centerPane);
    mainPane.setLeft(leftPane);

    return mainPane;
  }

  public static void clearDayViews(){
    int count = MainGUI.tabPane.getTabs().size();
    for (int i = 0; i < count; i++) {
      Tab thisTab = MainGUI.tabPane.getTabs().get(i);
      if (thisTab.getStyleClass().contains("dayView")){
        MainGUI.tabPane.getTabs().remove(i);
      }
    }
  }
}
