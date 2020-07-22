import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MonthView extends VBox
{
  private MyDate date;
  private VBox mainPane;

  public MonthView(MyDate myDate){
    this.date = myDate.copy();

    // Create
    mainPane = new VBox();
    VBox monthNameBox = new VBox();
    Label monthNameLab = new Label(date.getMonthName()+", "+date.getYear());
    HBox daysOfWeek = new HBox();
    // Filling week days
    for (int i = 0; i < MyDate.week.length; i++) {
      Label weekDayLabel = new Label(MyDate.week[i].substring(0, 2));
      weekDayLabel.setAlignment(Pos.CENTER);
      weekDayLabel.setPrefWidth(40);
      weekDayLabel.setPadding(new Insets(0));
      weekDayLabel.setFont(new Font("Arial", 20));
      daysOfWeek.getChildren().add(weekDayLabel);
    }
    // Filling the month with days
    FlowPane daysInMonth = new FlowPane();
    for (MyDate day = date.startOfMonth().startOfWeek(); !day.isAfter(date.endOfMonth()); day.nextDay()) {
      Label dayLabel = new Label(""+day.getDay());
      dayLabel.setAlignment(Pos.CENTER);

      if (day.isBefore(date.startOfMonth())){
        dayLabel.setTextFill(Color.web("#F5F5F5"));
      }else if(day.isBefore(Main.examTerm.getStartDate()) || day.isAfter(Main.examTerm.getEndDate())){
        dayLabel.setTextFill(Color.web("#bfbfbf"));
      }else{
        DayViewTabGUI dayViewTab = new DayViewTabGUI(day);
        dayLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
          public void handle(MouseEvent mouseEvent) {
            dayViewTab.show();
            MainGUI.tabPane.getTabs().add(dayViewTab);
            MainGUI.tabPane.getSelectionModel().select(dayViewTab);
          }
        });
        dayLabel.setStyle("-fx-cursor: hand;");
        dayLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
          public void handle(MouseEvent t) {
            dayLabel.setTextFill(Color.web("#ff3300"));
          }
        });
        dayLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
          public void handle(MouseEvent t) {
            dayLabel.setTextFill(Color.web("#000000"));
          }
        });
      }

      dayLabel.setPrefWidth(40);
      dayLabel.setPadding(new Insets(1));
      dayLabel.setFont(new Font("Arial", 20));
      daysInMonth.getChildren().add(dayLabel);
    }
    // Set Listeners
    // Styling
    daysOfWeek.setPrefWidth(280);
    daysInMonth.setPrefWidth(280);
    mainPane.setPadding(new Insets(5));
    mainPane.setStyle( "-fx-border-color: black; -fx-border-insets: 10; -fx-border-width: 1; -fx-border-style: solid;");
    monthNameLab.setFont(new Font("Arial", 30));
    monthNameBox.setAlignment(Pos.TOP_CENTER);
    mainPane.setAlignment(Pos.TOP_CENTER);
    // Synchronizing
    // Assembling
    monthNameBox.getChildren().add(monthNameLab);
    mainPane.getChildren().addAll(monthNameBox, daysOfWeek, daysInMonth);
  }
  public VBox show(){
    return mainPane;
  }
}
