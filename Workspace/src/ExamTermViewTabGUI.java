import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExamTermViewTabGUI extends Tab
{
  private BorderPane mainPane;

  private HBox startHBox, endHBox;

  private Label startLabel, endLabel;

  private TextField endYearField, endMonthField, endDayField;

  private ComboBox startYearCmb, startMonthCmb, startDayCmb, endYearCmb, endMonthCmb, endDayCmb;

  private Button saveBtn, exportBtn;

  private ExamTermListener startListener, endListener;

  private ExportXmlListener exportXmlListener;

  public ExamTermViewTabGUI(String title){
    super(title);
    // Creating
    mainPane = new BorderPane();

    HBox topHBox = new HBox();
    VBox examTermVBox = new VBox();
    HBox saveHBox = new HBox();
    saveBtn = new Button("Save");
    exportBtn = new Button("Export");

    FlowPane monthViewBox = new FlowPane();

    startHBox = new HBox();
    startLabel = new Label("Exam Term Start: ");
    startYearCmb = new ComboBox<Integer>();
    int thisYear = new MyDate().getYear();
    startYearCmb.getItems().addAll(thisYear - 3, thisYear - 2, thisYear - 1, thisYear, thisYear + 1, thisYear + 2, thisYear + 3, thisYear + 4);
    startMonthCmb = new ComboBox<String>();
    for (int i = 1; i <= 12; i++) { startMonthCmb.getItems().add(MyDate.getMonthName(i)); } // Filling the month comboBox
    startDayCmb = new ComboBox<Integer>();

    endHBox = new HBox();
    endLabel = new Label("Exam Term End: ");
    endYearCmb = new ComboBox<Integer>();
    endYearCmb.getItems().addAll(thisYear - 3, thisYear - 2, thisYear - 1, thisYear, thisYear + 1, thisYear + 2, thisYear + 3, thisYear + 4);
    endMonthCmb = new ComboBox<String>();
    for (int i = 1; i <= 12; i++) { endMonthCmb.getItems().add(MyDate.getMonthName(i)); } // Filling the month comboBox
    endDayCmb = new ComboBox<Integer>();

    // Set Listeners
    startListener = new ExamTermListener(startYearCmb, startMonthCmb, startDayCmb){
      public void assign(Object obj) { Main.examTerm.setStartDate((MyDate)obj); }
    };
    startYearCmb.setOnAction(startListener);
    startMonthCmb.setOnAction(startListener);
    startDayCmb.setOnAction(startListener);

    endListener = new ExamTermListener(endYearCmb, endMonthCmb, endDayCmb){
      public void assign(Object obj) { Main.examTerm.setEndDate((MyDate)obj); }
    };
    endYearCmb.setOnAction(endListener);
    endMonthCmb.setOnAction(endListener);
    endDayCmb.setOnAction(endListener);
    exportXmlListener = new ExportXmlListener();
    exportBtn.setOnAction(exportXmlListener);
    saveBtn.setOnAction(new saveBtnListener(Main.examTermIO, Main.examTerm){
      public void onSave() {
        startListener.sync(Main.examTerm.getStartDate());
        endListener.sync(Main.examTerm.getEndDate());
        showMonths(monthViewBox);
        mainPane.setCenter(monthViewBox);
      }

      public boolean valid(Object obj) {
        if (obj instanceof ExamTerm){
          ExamTerm eTerm = (ExamTerm)obj;
          boolean startBeforeEnd = eTerm.getStartDate().isBefore(eTerm.getEndDate());
          if (!eTerm.getStartDate().isBefore(eTerm.getEndDate())){
            Alert alert = new Alert(AlertType.ERROR, "Exam term start date must be before the End Date");
            alert.setHeaderText(null);
            alert.showAndWait();
          }

          return startBeforeEnd;
        }else{ return false; }
      }
    });

    // Styling
    mainPane.setPadding(new Insets(5));
    examTermVBox.setMaxWidth(600);
    examTermVBox.setAlignment(Pos.TOP_CENTER);
    examTermVBox.setSpacing(10);
    saveHBox.setAlignment(Pos.CENTER_LEFT);
    saveHBox.setSpacing(5);
    saveHBox.setPadding(new Insets(0,0,0,5));
    saveBtn.setFont(new Font("Arial", 13));
    exportBtn.setFont(new Font("Arial", 13));

    startHBox.setSpacing(5);
    startHBox.setAlignment(Pos.CENTER);
    startYearCmb.setPromptText("Year");
    startMonthCmb.setPromptText("Month");
    startDayCmb.setPromptText("Day");
    startDayCmb.setDisable(Main.examTerm.getStartDate() == null);
    startLabel.setFont(new Font("Arial", 15));

    endHBox.setSpacing(5);
    endHBox.setAlignment(Pos.CENTER);
    endYearCmb.setPromptText("Year");
    endMonthCmb.setPromptText("Month");
    endDayCmb.setPromptText("Day");
    endDayCmb.setDisable(Main.examTerm.getStartDate() == null);
    endLabel.setFont(new Font("Arial", 15));

    // Synchronizing
    endListener.sync(Main.examTerm.getEndDate());
    startListener.sync(Main.examTerm.getStartDate());
    showMonths(monthViewBox);

    saveBtn.setDisable( !(startListener.isGoodToSave() && endListener.isGoodToSave()) );

    // Assembling
    startHBox.getChildren().addAll(startLabel, startYearCmb, startMonthCmb, startDayCmb);
    endHBox.getChildren().addAll(endLabel, endYearCmb, endMonthCmb, endDayCmb);
    examTermVBox.getChildren().addAll(startHBox, endHBox);
    saveHBox.getChildren().addAll(saveBtn, exportBtn);

    topHBox.getChildren().addAll(examTermVBox,saveHBox);
    mainPane.setTop(topHBox);
    mainPane.setCenter(monthViewBox);
    super.setContent(mainPane);
  }

  private class ExportXmlListener implements EventHandler<ActionEvent>
  {
    public void handle(ActionEvent actionEvent)
    {
      if (actionEvent.getSource() == exportBtn)
      {
        Exam.allExams = Exam.allExamsIO.read();

        XMLConverter xmlConverter = new XMLConverter();
        ArrayList<Exam> scheduled = Exam.allExams.findBy("isScheduled", true);

        String xmlToBeExported = xmlConverter.XML_HEADER;
        String arrToXml = xmlConverter.toXML(scheduled);
        xmlToBeExported += arrToXml;
        System.out.println(xmlToBeExported);

        try
        {
          FileOutputStream fileOut = new FileOutputStream("saves/schedule.xml");
          PrintWriter write = new PrintWriter(fileOut);
          write.print(xmlToBeExported);
          write.close();
        }
        catch (FileNotFoundException e)
        {
          Alert notExported = new Alert(AlertType.INFORMATION);
          notExported.setTitle("Exams not exported.");
          notExported.setHeaderText(null);
          notExported.setContentText("XML file could not have been exported.");
          notExported.show();
        }
      }
    }
  }

  private abstract class ExamTermListener implements EventHandler<ActionEvent>
  {
    private ComboBox yearCmb;
    private ComboBox monthCmb;
    private ComboBox dayCmb;

    private boolean yearSelected;
    private boolean monthSelected;
    private boolean daySelected;
    private int year;
    private int month;
    private int day;

    public ExamTermListener(ComboBox yearCmb, ComboBox monthCmb, ComboBox dayCmb) {
      this.yearCmb = yearCmb;
      this.monthCmb = monthCmb;
      this.dayCmb = dayCmb;
    }

    public void sync(MyDate date){ // Sets the controls to reflect the value
      if (date != null){
        yearSelected = true;
        monthSelected = true;
        daySelected = true;
        year = date.getYear();
        month = date.getMonth();
        day = date.getDay();

        feedDaysCmb(year, month);

        yearCmb.setValue(year);
        monthCmb.setValue(MyDate.getMonthName(month));
        dayCmb.setValue(day);
      }
    }

    public boolean isGoodToSave() { return (daySelected && monthSelected && yearSelected); }

    private void feedDaysCmb(int year, int month){
      int days = MyDate.daysInMonth(year, month);
      dayCmb.getItems().clear();
      dayCmb.setDisable(false);
      for (int i = 1; i <= days; i++) { dayCmb.getItems().add(i); }
    }

    public void handle(ActionEvent e) {
      if (e.getSource() == yearCmb) {
        yearSelected = true;
        year = (int) yearCmb.getValue();
        if (monthSelected){
          feedDaysCmb(year, month);
          daySelected = false;
        }
      }else if (e.getSource() == monthCmb){
        monthSelected = true;
        month = MyDate.getMonthNumber( (String)monthCmb.getValue() );
        if (yearSelected) {
          feedDaysCmb(year, month);
          daySelected = false;
        }
      }else if (e.getSource() == dayCmb){
        daySelected = true;
        day = (int)dayCmb.getValue();
      }

      if (daySelected && monthSelected && yearSelected){ assign(new MyDate(day, month, year)); }
      saveBtn.setDisable( !(startListener.isGoodToSave() && endListener.isGoodToSave()) );
    }

    public abstract void assign(Object obj);

  }

  private void showMonths(FlowPane container){
    if (Main.examTerm.getStartDate() != null && Main.examTerm.getEndDate() != null){
      container.getChildren().clear();
      MyDate start = Main.examTerm.getStartDate().copy();
      MyDate end = Main.examTerm.getEndDate().copy();
      end.nextMonth(); // in order to cover the very month of endDate
      MyDate temp = start.copy();
      do{
        VBox monthBox = new MonthView(temp).show();
        container.getChildren().add(monthBox);
        temp.nextMonth();
      }while(!temp.sameMonth(end));

    }
  }
}
