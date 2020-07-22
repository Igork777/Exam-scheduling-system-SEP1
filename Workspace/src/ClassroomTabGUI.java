import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

public class ClassroomTabGUI extends Tab
{
  private HBox mainPane;
  private VBox managePane;
  private BorderPane rightPane;

  //Classroom list table
  private TableView classroomListTableView;
    private TableColumn<String, Classroom> classroomListNameColumn;
    private TableColumn<Integer, Classroom> classroomListCapacityColumn;
    private TableColumn<String, Classroom> classroomListProjectorColumn;


  //Filter
  private TitledPane filterOutsideTitledPane;
  private GridPane filterInsideGridPane;
    private Label filterFloor;
      private HBox filterFloorCheckBoxPane;
      private CheckBox filterFloorBasement;
      private CheckBox filterFloorFirst;
      private CheckBox filterFloorSecond;
      private CheckBox filterFloorThird;
    private Label filterCapacity;
      private HBox filterCapacityBox;
      private Label filterCapacityMin;
      private TextField filterCapacityMinField;
      private Label filterCapacityMax;
      private TextField filterCapacityMaxField;
    private Label filterProjector;
      private HBox filterProjectorRadioButtons;
      private ToggleGroup filterProjectorGroup;
      private RadioButton filterProjectorAll;
      private RadioButton filterProjectorPresent;
      private RadioButton filterProjectorNotPresent;


  //Selected classroom info
  private VBox selectClassroomBox;
  //header
  private Label selectHeader;
  //wrap pane
  private GridPane selectClassroomGridPane;
    //name
    private Label selectName;
    private TextField selectNameField;
    //capacity
    private Label selectCapacity;
    private TextField selectCapacityField;
    //projector
    private Label selectProjector;
    private ComboBox<String> selectProjectorDropDownBox;


  //Buttons new,apply,remove,load from file
  private HBox buttonsManage;
    private Button buttonApply;
    private Button buttonRemove;
    private Button buttonAddNew;


  //Handlers
  private MyActionListener listener;
  private TableChangeListener changeListener;

  //= = = = = = = = = = = =
  //=  ONLY FOR TESTING   =
  //= = = = = = = = = = = =
  MyList<Classroom> filteredClassrooms = new MyList<Classroom>();


  public ClassroomTabGUI(String title)
  {
    super(title);
    MyList<Classroom> fileClassrooms = Classroom.allClassroomsIO.read();
    if (fileClassrooms == null){fileClassrooms = new MyList<Classroom>();}
    Classroom.allClassrooms.setList(fileClassrooms.getList());
    filteredClassrooms.setList(Classroom.allClassrooms.getList());

    listener = new MyActionListener();
    changeListener = new TableChangeListener();
    //SETUP OF LIST OF CLASSROOMS
      //INITIALIZE TABLEVIEW, COLUMNS (with column value getters)
      classroomListTableView = new TableView();
      classroomListNameColumn = new TableColumn<>("Name");
      classroomListNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
      classroomListCapacityColumn = new TableColumn<>("Capacity");
      classroomListCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
      classroomListProjectorColumn = new TableColumn<>("Projector");
      classroomListProjectorColumn.setCellValueFactory(new PropertyValueFactory<>("hasProjector"));
      //ADD COLUMNS TO TABLEVIEW
      classroomListTableView.getColumns().add(classroomListNameColumn);
      classroomListTableView.getColumns().add(classroomListCapacityColumn);
      classroomListTableView.getColumns().add(classroomListProjectorColumn);
      //TABLEVIEW SETTINGS
      classroomListTableView.setMinWidth(375);
      classroomListTableView.getSelectionModel().getSelectedItems().addListener(changeListener);
      //COLUMNS SETTINGS
      classroomListNameColumn.setPrefWidth(120);
      classroomListNameColumn.setResizable(false);
      classroomListNameColumn.setSortable(false);
      classroomListCapacityColumn.setPrefWidth(120);
      classroomListCapacityColumn.setResizable(false);
      classroomListCapacityColumn.setSortable(false);
      classroomListProjectorColumn.setPrefWidth(120);
      classroomListProjectorColumn.setResizable(false);
      classroomListProjectorColumn.setSortable(false);
    //\\SETUP OF LIST OF CLASSROOMS

    //SETUP OF FILTER FOR LIST
    filterOutsideTitledPane = new TitledPane();
    filterOutsideTitledPane.setText("Filters");
    filterOutsideTitledPane.setPadding(new Insets(10,30,10,0));
    filterInsideGridPane = new GridPane();
    filterInsideGridPane.setHgap(25);
    filterInsideGridPane.setVgap(20);
      //FILTER BY FLOOR
      filterFloorCheckBoxPane = new HBox();
      filterFloor = new Label("Floor:");
      filterFloorBasement = new CheckBox("Basement");
      filterFloorFirst = new CheckBox("1st floor");
      filterFloorSecond = new CheckBox("2nd floor");
      filterFloorThird = new CheckBox("3rd floor");
      //FILTER BY FLOOR SETTINGS
      filterFloorCheckBoxPane.setSpacing(40);
      filterFloorBasement.setOnAction(listener);
      filterFloorFirst.setOnAction(listener);
      filterFloorSecond.setOnAction(listener);
      filterFloorThird.setOnAction(listener);
      //FILTER BY CAPACITY
      filterCapacityBox = new HBox();
      filterCapacity = new Label("Capacity:");
      filterCapacityMin = new Label("Min:");
      filterCapacityMinField = new TextField();
      filterCapacityMax = new Label("Max:");
      filterCapacityMaxField = new TextField();
      //FILTER BY CAPACITY SETTINGS
      filterCapacityBox.setAlignment(Pos.CENTER_LEFT);
      filterCapacityBox.setSpacing(10);
      filterCapacityMinField.setPrefWidth(75);
      filterCapacityMaxField.setPrefWidth(75);
      filterCapacityMinField.textProperty().addListener(new FilterCapacityFieldsListener());
      filterCapacityMaxField.textProperty().addListener(new FilterCapacityFieldsListener());
      //FILTER BY PROJECTOR
      filterProjectorRadioButtons = new HBox();
      filterProjector = new Label("Projector:");
      filterProjectorAll = new RadioButton("All");
      filterProjectorPresent = new RadioButton("Present");
      filterProjectorNotPresent = new RadioButton("Not present");
      //FILTER BY PROJECTOR SETTINGS
      filterProjectorGroup = new ToggleGroup();
      filterProjectorPresent.setToggleGroup(filterProjectorGroup);
      filterProjectorNotPresent.setToggleGroup(filterProjectorGroup);
      filterProjectorAll.setToggleGroup(filterProjectorGroup);
      filterProjectorAll.setSelected(true);
      filterProjectorRadioButtons.setSpacing(20);
      filterProjectorPresent.setOnAction(listener);
      filterProjectorNotPresent.setOnAction(listener);
      filterProjectorAll.setOnAction(listener);
    //ADDING ALL CONTENT
      //ROW0
      filterFloorCheckBoxPane.getChildren().addAll(filterFloorBasement,filterFloorFirst,filterFloorSecond,filterFloorThird);
      filterInsideGridPane.addRow(0, filterFloor, filterFloorCheckBoxPane);
      //ROW1
      filterCapacityBox.getChildren().addAll(filterCapacityMin,filterCapacityMinField,filterCapacityMax,filterCapacityMaxField);
      filterInsideGridPane.addRow(1, filterCapacity, filterCapacityBox);
      //ROW2
      filterProjectorRadioButtons.getChildren().addAll(filterProjectorPresent, filterProjectorNotPresent, filterProjectorAll);
      filterInsideGridPane.addRow(2, filterProjector, filterProjectorRadioButtons);

      filterOutsideTitledPane.setContent(filterInsideGridPane);
    //\\SETUP OF FILTER FOR LIST

    //SETUP OF SELECTED CLASSROOM
    selectClassroomBox = new VBox();
    selectHeader = new Label("Selected classroom:");
    selectHeader.setFont(new Font("Arial", 20));
      //GRIDPANE SETUP FOR SELECTED CLASSROOM
      selectClassroomGridPane = new GridPane();
      selectClassroomGridPane.setHgap(15);
      selectClassroomGridPane.setVgap(10);
      selectName = new Label("Name:");
      selectNameField = new TextField();
      selectCapacity = new Label("Capacity:");
      selectCapacityField = new TextField();
      selectProjector = new Label("Projector:");
      selectProjectorDropDownBox = new ComboBox<>();
      selectProjectorDropDownBox.getItems().addAll("Present", "Not present");
      //ADD CONTENT TO GRIDPANE
      selectClassroomGridPane.addRow(0, selectName, selectNameField);
      selectClassroomGridPane.addRow(1, selectCapacity, selectCapacityField);
      selectClassroomGridPane.addRow(2, selectProjector, selectProjectorDropDownBox);

      selectClassroomBox.setSpacing(15);
      selectClassroomBox.getChildren().addAll(selectHeader,selectClassroomGridPane);
    //\\SETUP OF SELECTED CLASSROOM

    //SETUP OF BUTTONS
    buttonsManage = new HBox();
    buttonsManage.setAlignment(Pos.BOTTOM_RIGHT);
    buttonAddNew = new Button("Add new");
    buttonApply = new Button("Apply");
    buttonRemove = new Button("Remove");
    buttonRemove.setOnAction(listener);
    buttonAddNew.setOnAction(listener);
    buttonApply.setOnAction(listener);
    //ADD TO HBOX OF BUTTONS
    buttonsManage.getChildren().addAll(buttonApply, buttonRemove, buttonAddNew);
    buttonsManage.setSpacing(10);
    buttonsManage.setPadding(new Insets(20,20,10,20));
    //\\SETUP OF BUTTONS

    //RIGHT-HAND MANAGE CONTENT PANE
    managePane = new VBox(50, filterOutsideTitledPane, selectClassroomBox);
//    managePane.setSpacing(50);
    managePane.setPadding(new Insets(25, 40, 30, 40));
//    managePane.getChildren().add(filterOutsideTitledPane);
//    managePane.getChildren().add(selectClassroomBox);

    //RIGHT PANE
    rightPane = new BorderPane();
    rightPane.setCenter(managePane);
    rightPane.setBottom(buttonsManage);
    rightPane.setPrefWidth(800);

    //MAIN CLASSROOM PANE(HBOX)
    mainPane = new HBox();
    mainPane.getChildren().add(classroomListTableView);
    mainPane.getChildren().add(rightPane);

    super.setContent(mainPane);
  }

  public void updateClassroomList()
  {
    classroomListTableView.getItems().clear();
    filteredClassrooms.setList(Classroom.allClassrooms.getList());

    ArrayList<Classroom> basementFloor;
    ArrayList<Classroom> firstFloor;
    ArrayList<Classroom> secondFloor;
    ArrayList<Classroom> thirdFloor;
    int minCap = filterCapacityMinField.getText().equals("") ? 0 : Integer.parseInt(filterCapacityMinField.getText());
    int maxCap = filterCapacityMaxField.getText().equals("") ? Integer.MAX_VALUE : Integer.parseInt(filterCapacityMaxField.getText());
    ArrayList<Classroom> classroomsNotWithin;
    ArrayList<Classroom> haveProjector;
    ArrayList<Classroom> doNotHaveProjector;


    try
    {
      //COLLECTING FILTERED DATA
      basementFloor = Classroom.allClassrooms.findBy("getFloor", 0);
      firstFloor = Classroom.allClassrooms.findBy("getFloor", 1);
      secondFloor = Classroom.allClassrooms.findBy("getFloor", 2);
      thirdFloor = Classroom.allClassrooms.findBy("getFloor", 3);

      Method isWithin = Classroom.inspect.getMethod("isCapacityWithin", int.class, int.class);
      classroomsNotWithin = Classroom.allClassrooms.findBy(isWithin, false, minCap, maxCap);

      haveProjector = Classroom.allClassrooms.findBy("getHasProjector", true);
      doNotHaveProjector = Classroom.allClassrooms.findBy("getHasProjector", false);

      //FILTERING DATA
      if (filterFloorBasement.isSelected() || filterFloorFirst.isSelected() || filterFloorSecond.isSelected() || filterFloorThird.isSelected())
      {
        if (!filterFloorBasement.isSelected())
          filteredClassrooms.remove(basementFloor);
        if (!filterFloorFirst.isSelected())
          filteredClassrooms.remove(firstFloor);
        if (!filterFloorSecond.isSelected())
          filteredClassrooms.remove(secondFloor);
        if (!filterFloorThird.isSelected())
          filteredClassrooms.remove(thirdFloor);
      }
      filteredClassrooms.remove(classroomsNotWithin);
      if (filterProjectorPresent.isSelected())
        filteredClassrooms.remove(doNotHaveProjector);
      else if (filterProjectorNotPresent.isSelected())
        filteredClassrooms.remove(haveProjector);
    }
    catch (Throwable throwable)
    {
      System.out.println("Could not filter.");
    }

    classroomListTableView.getItems().addAll(filteredClassrooms.getList());
  }

  private class FilterCapacityFieldsListener implements ChangeListener<String>
  {
    public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
    {
      updateClassroomList();
    }
  }

  private class TableChangeListener implements ListChangeListener<Classroom>
  {
    public void onChanged(Change<? extends Classroom> change)
    {
      {
        Classroom selectedClassroom = ((Classroom)(classroomListTableView.getSelectionModel().getSelectedItem()));
        if (selectedClassroom != null)
        {
          String name = selectedClassroom.getName();
          selectNameField.setText(name);
          String capacity = Integer.toString(selectedClassroom.getCapacity());
          selectCapacityField.setText(capacity);
          int projector = selectedClassroom.getHasProjector() ? 0 : 1;
          selectProjectorDropDownBox.getSelectionModel().select(projector);

        }
      }
    }
  }

  private class MyActionListener implements EventHandler<ActionEvent>
  {
    public void handle(ActionEvent actionEvent)
    {
      if ((actionEvent.getSource() == buttonApply))
      {
        Classroom selectedClassroom = ((Classroom) (classroomListTableView.getSelectionModel().getSelectedItem()));
        int selectedClassroomIndex = classroomListTableView.getSelectionModel().getSelectedIndex();
        if (selectedClassroom != null)
        {
          String newName = selectNameField.getText();
          int newCapacity = Integer.parseInt(selectCapacityField.getText());
          boolean newProjector = selectProjectorDropDownBox.getValue().equals("Present");

          Classroom classroom = new Classroom(newName, newCapacity, newProjector);
          if (!selectedClassroom.equals(classroom))
          {
            if (!filteredClassrooms.contains(classroom))
            {
              Classroom.allClassrooms.setAt(Classroom.allClassrooms.getIndexOf(selectedClassroom), classroom);
              Classroom.allClassroomsIO.write(Classroom.allClassrooms);
              updateClassroomList();
            }
            else
            {
              Alert alreadyExists = new Alert(Alert.AlertType.INFORMATION);
              alreadyExists.setTitle("Already exists.");
              alreadyExists.setHeaderText(null);
              alreadyExists.setContentText("Classroom '" + classroom.getName() + "' already exists.");
              alreadyExists.show();
            }
          }
          classroomListTableView.getSelectionModel().select(selectedClassroomIndex);
        }
        sync();
      }
      else if (actionEvent.getSource() == buttonRemove)
      {
        String newName = selectNameField.getText();
        int newCapacity = Integer.parseInt(selectCapacityField.getText());
        boolean newProjector = selectProjectorDropDownBox.getValue().equals("Present");
        Classroom classroom = new Classroom(newName, newCapacity, newProjector);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove classroom");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete a classroom:\n"
            + classroom);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
          Classroom.allClassrooms.remove(classroom);
          Classroom.allClassroomsIO.write(Classroom.allClassrooms);
        }
        updateClassroomList();
      }
      else if (actionEvent.getSource() == buttonAddNew)
      {
        String newName = selectNameField.getText();
        int newCapacity = Integer.parseInt(selectCapacityField.getText());
        boolean newProjector = selectProjectorDropDownBox.getValue().equals("Present");
        Classroom classroom = new Classroom(newName, newCapacity, newProjector);

        if (Classroom.allClassrooms.add(classroom) <= filteredClassrooms.size())
        {
          Alert alreadyExists = new Alert(Alert.AlertType.INFORMATION);
          alreadyExists.setTitle("Already exists.");
          alreadyExists.setHeaderText(null);
          alreadyExists.setContentText("Classroom '" + classroom.getName() + "' already exists.");
          alreadyExists.show();
        }
        else

          if(Classroom.allClassrooms.findBy("getName", classroom.getName()).size() > 1)
          {
            Alert sameName = new Alert(Alert.AlertType.CONFIRMATION);
            sameName.setTitle("Match in name");
            sameName.setHeaderText(null);
            sameName.setContentText("Classroom with the same name already exists.\n"
                + "Do you want to add anyways?");
            Optional<ButtonType> resultSure = sameName.showAndWait();
            if (resultSure.get() != ButtonType.OK)
            {
              Classroom.allClassrooms.remove(classroom);
            }
          }

        updateClassroomList();
        classroomListTableView.getSelectionModel().select(Classroom.allClassrooms.size()-1);
        Classroom.allClassroomsIO.write(Classroom.allClassrooms);
      }
      else if (actionEvent.getSource() == filterFloorBasement || actionEvent.getSource() == filterFloorFirst
          || actionEvent.getSource() == filterFloorSecond || actionEvent.getSource() == filterFloorThird
          || actionEvent.getSource() == filterProjectorPresent || actionEvent.getSource() == filterProjectorNotPresent
          || actionEvent.getSource() == filterProjectorAll)
      {
        updateClassroomList();
      }
    }
  }

  public void sync(){
    Classroom.allClassroomsIO.write(Classroom.allClassrooms);
  }

}
