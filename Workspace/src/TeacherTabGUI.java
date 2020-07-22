
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

public class TeacherTabGUI extends Tab
{
    private TableView<Teacher> table = new TableView<>();
    private TableView<DateInterval> table2 = new TableView<>();
    private TableColumn<DateInterval, String>  subcolumn1, subcolumn2;
    private TableColumn<Teacher, String> nameColumn;

    private HBox tempBox, firstNameHBox, lastNameHBox,buttonsBox, iconsBox, mainHbox;
    private VBox mediumPart, lastPart;
    private BorderPane borderPane;
    private Label label, label2, label3, labelYearStart, labelMonthStart, labelDayStart, labelYearEnd,labelMonthEnd, labelDayEnd, labelStart, labelEnd  ;
    private TextField firstNameField, lastNameField, textFieldYearStart, textFieldMonthStart, textFieldDayStart, textFieldYearEnd, textFieldMonthEnd, textFieldDayEnd;
    private Button buttonIconAdd, buttonIconEdit, buttonIconRemove, buttonSave, buttonRemove, buttonAddNew, buttonSaveAddingOfDateInterval, buttonSaveEditingOfDateInterval;
    private MyListener myListener;
    private MyMouseListener myMouseListener;
    private TitledPane titledPane;
    private Stage newWindow;
    private MyListenerForEditingDateIntervals myListenerForEditingDateIntervals;
    private MyListenerForAddingDateIntervals myListenerForAddingDateIntervals;

    public void sync(MyList<Teacher> teachers){
        table.getItems().clear();
        table.getItems().addAll(teachers.getList());
    }

    public TeacherTabGUI(String title)
    {
        super(title);
        // Sync with files
        MyList<Teacher> fileTeachers = Teacher.allTeachersIO.read();
        if (fileTeachers == null){fileTeachers = new MyList<>();}
        Teacher.allTeachers.setList(fileTeachers.getList());
        sync(Teacher.allTeachers);

        //creating a listener
        myListener = new MyListener();
        myMouseListener = new MyMouseListener();


        //Creating the first table with the names of the teachers
        nameColumn = new TableColumn<>("Name");
        //Setting the height of this table
        table.setPrefHeight(543);
        table.getSelectionModel().setCellSelectionEnabled(true);
        //Putting the name of the column in center
        nameColumn.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        //Setting the width of this table
        nameColumn.setPrefWidth(500);
        //settings for the equality of the table
        table.prefWidthProperty().bind(nameColumn.widthProperty());
        //cells of this column
        nameColumn.setCellValueFactory(new PropertyValueFactory<Teacher, String>("fullName"));
        //adding the column to the table
        table.getColumns().add(nameColumn);
        //initializing of horizontal box and adding table to it
        tempBox = new HBox(table);
        //creating a vertical box
        mediumPart = new VBox();
        //creating a border pane
        borderPane = new BorderPane();

        //Creating a middle block
        label = new Label("Selected teacher:");
        //setting the proper insets for the all elements inside borderPane
        borderPane.setPadding(new Insets(15));


        //creating a horizontal box
        firstNameHBox = new HBox();
        //creating a label for the first textField
        label2 = new Label("First name:");
        //creating a textField for First Name label
        firstNameField = new TextField();
        //add label "First name" and textField in the same horizontal box
        firstNameHBox.getChildren().addAll(label2, firstNameField);
        //adding the proper insets from left
        firstNameHBox.setPadding(new Insets(0,0,0,35));
        //setting the space between label "First name" and textField
        firstNameHBox.setSpacing(20);


        //creating of the horizontal box
        lastNameHBox = new HBox();
        //setting some insets for the horizontal box form left side
        lastNameHBox.setPadding(new Insets(0, 0,0,35));
        //creating the label "Last name"
        label3 = new Label("Last name:");
        //creating its TextField
        lastNameField = new TextField();
        //adding a label "Last name" and its textField to the same horizontal box
        lastNameHBox.getChildren().addAll(label3, lastNameField);
        //adding the space between label "Last name" and its textField
        lastNameHBox.setSpacing(20);







        //adding label and two horizontal boxes to vertical box
        mediumPart.getChildren().addAll(label, firstNameHBox, lastNameHBox);
        //adding the space between elements in the mediumPart box
        mediumPart.setSpacing(20);
        mediumPart.setPadding(new Insets(0,10,0,0));
        //adding mediumPart vbox to left of the borderPane
        borderPane.setLeft(mediumPart);



        //Adding the last block
        //adding the borderPane
        titledPane = new TitledPane();

        titledPane.setText("Not available on");

        //creating the column
        //setting the height of the table


        table2.setPrefHeight(400);
        //creating the first subColumn
        subcolumn1 = new TableColumn<>("Start");
        //creating the secondColumn
        subcolumn2 =new TableColumn<>("End");
        //putting the name of the first subColumn to the center
        subcolumn1.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        //putting the name of the second subColumn to the center
        subcolumn2.getStyleClass().add("-fx-alignment: CENTER-RIGHT;");
        //setting the width of the first subColumn
        subcolumn1.setPrefWidth(200);
        //setting the width of the second subColumn
        subcolumn2.setPrefWidth(200);
        table2.setPrefWidth(400);


        //cells of the first subColumn
        subcolumn1.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        //cells of the second subColumn
        subcolumn2.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        //adding to subColumns to the column
        table2.getColumns().addAll(subcolumn1,subcolumn2);



        //creating the "Edit" image
        Image image1 = new Image(getClass().getResourceAsStream("images/free-edit-icon-1.png"));
        //creating the button with the "Edit" image
        buttonIconEdit = new Button("Edit", new ImageView(image1));
        //creating the "Add" image
        Image image2 = new Image(getClass().getResourceAsStream("images/plusSmall-512.png"));
        //creating the button with the "Add" image
        buttonIconAdd = new Button("Add", new ImageView(image2));
        //creating the "Remove " image
        Image image3 = new Image(getClass().getResourceAsStream("images/remove_small_interface_minus-512.png"));
        //creating the button with the "Remove" image
        buttonIconRemove = new Button("Remove", new ImageView(image3));
        //adding the horizontal box with the 3 button with the space between them
        iconsBox = new HBox(10,buttonIconEdit,buttonIconAdd, buttonIconRemove);
        //alignment the horizontal box to the right
        iconsBox.setAlignment(Pos.TOP_RIGHT);
        //set some insets for horizontal box. Top changed
        iconsBox.setPadding(new Insets(10,0,0,0));


        //to create a button "Apply"
        buttonSave = new Button("Apply");
        buttonSave.setOnAction(new saveBtnListener(Teacher.allTeachersIO, Teacher.allTeachers) {
            public void onSave() {

            }

            public boolean valid(Object obj) {
                sync(Teacher.allTeachers);
                return !Teacher.allTeachers.empty();
            }
        });
        //to create a button "Remove"
        buttonRemove = new Button("Remove");
        //to create a button "Add new"
        buttonAddNew = new Button("Add new");
        //to create horizontal box and to add 3 buttons with the spacing
        buttonsBox = new HBox(10, buttonSave, buttonRemove, buttonAddNew);
        //to position buttons to the right
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        //to set a table to the top of the border pane
        lastPart = new VBox(table2, iconsBox);
        titledPane.setContent(lastPart);

        borderPane.setBottom(buttonsBox);
        borderPane.setCenter(titledPane);
        //to create horizontal box and to add 1 horizontal box and two borderPanes
        HBox hbox1 = new HBox(tempBox, borderPane);
        //to add an eventListener
        buttonAddNew.setOnAction(myListener);
        buttonRemove.setOnAction(myListener);
        table.setOnMouseClicked(myMouseListener);
        buttonIconAdd.setOnAction(myListener);
        buttonIconRemove.setOnAction(myListener);
        buttonIconEdit.setOnAction(myListener);
        super.setContent(hbox1);
    }

    public class MyListener implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent e)
        {
            //if user pushes the Add new button
            if(e.getSource() == buttonAddNew)
            {
                //check if the program throws NullPointerException
                if(firstNameField.getText() == null || lastNameField.getText() == null)
                {
                    //Creating a window, which informs about the error
                    JOptionPane.showMessageDialog(null, (firstNameField.getText() == null && lastNameField.getText() == null) ? "First name and last name are equal to null" : (firstNameField.getText() == null) ? "First name is equal to null" : "Last name is equal with null", "Error",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    //checks if at least one textfield is empty
                    if(firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty())
                    {
                        //Creating a window, wich informs about the error
                        JOptionPane.showMessageDialog(null, (firstNameField.getText().trim().isEmpty() && lastNameField.getText().trim().isEmpty()) ? "First name and last name are empty!" : (firstNameField.getText().trim().isEmpty()) ? "First name is empty" : "Last name is empty", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        //variable, which changes if the teacher already exists in the arrayList of teachers
                        boolean isTeacherUnique = true;
                        //creating a teacher, on the basis of information form tex fields
                        Teacher teacher = new Teacher(firstNameField.getText(), lastNameField.getText());
                        //check if such teacher exists
                        for (int i = 0; i <Teacher.allTeachers.getList().size() ; i++)
                        {
                            if(teacher.getFirstName().equals(Teacher.allTeachers.getList().get(i).getFirstName()) && teacher.getLastName().equals(Teacher.allTeachers.getList().get(i).getLastName()))
                            {
                                //if two similar teachers are found, the boolean variable changes
                                isTeacherUnique = false;
                                //exit from the loop
                                break;
                            }
                        }
                        //if teacher is not unique, the massage with the error will be returned
                        if(!(isTeacherUnique))
                        JOptionPane.showMessageDialog(null, "Such teacher already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        //if teacher is unique than it will be added to the teacher's list , to the table and the message with the information will occur
                        else
                            {

                                Teacher.allTeachers.add(teacher.copy());
                            table.getItems().add(teacher.copy());

                        }
                    }
                }
            }
            //if the button remove is pushed
            else if(e.getSource() == buttonRemove)
            {
                Teacher teacher = table.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove the teacher");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Are you sure you want to delete a teacher:\n" + teacher);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    Method isTeacherAttending = Course.inspect.getMethod("isTeacherAttending", Teacher.class);
                    ArrayList<Course> coursesAttended = Course.allCourses.findBy(isTeacherAttending, true, teacher);
                    for (int i = 0; i < coursesAttended.size(); i++) {
                        coursesAttended.get(i).getTeachers().remove(teacher);
                    }
                    Course.allCoursesIO.write(Course.allCourses);
                    Teacher.allTeachers.remove(teacher);
                    table.getItems().remove(teacher);
                    table2.getItems().clear();
                }
                //loop where the proper information will be extracted from the chosen cell
                //After that the proper object from arrayList of teachers will be deleted


            }
         else if(e.getSource() == buttonIconAdd)
     {
         //creating of a new window
         mainHbox = new HBox();
         buttonSaveAddingOfDateInterval = new Button("Apply");
         labelYearStart = new Label("Year:");
         labelMonthStart = new Label("Month:");
         labelDayStart = new Label("Day:");
         labelYearEnd = new Label("Year:");
         labelMonthEnd = new Label("Month:");
         labelDayEnd = new Label("Day:");
         labelStart = new Label("Start date:");
         labelEnd = new Label("End date:");
         labelStart.setPadding(new Insets(0,0,30,85));
         labelEnd.setPadding(new Insets(0,0,30,95));
         textFieldYearStart = new TextField();
         textFieldMonthStart = new TextField();
         textFieldDayStart = new TextField();
         textFieldYearEnd = new TextField();
         textFieldMonthEnd = new TextField();
         textFieldDayEnd = new TextField();
         labelYearStart.setPadding(new Insets(5,0,0,0));
         labelMonthStart.setPadding(new Insets(5,0,0,0));
         labelDayStart.setPadding(new Insets(5,0,0,0));
         labelYearEnd.setPadding(new Insets(5,0,0,0));
         labelMonthEnd.setPadding(new Insets(5,0,0,0));
         labelDayEnd.setPadding(new Insets(5,0,0,0));
         HBox hBox1 = new HBox(22, labelYearStart, textFieldYearStart), hBox2 = new HBox(10, labelMonthStart, textFieldMonthStart), hBox3 = new HBox(26, labelDayStart, textFieldDayStart), hBox4 = new HBox(22, labelYearEnd, textFieldYearEnd), hBox5 = new HBox(10, labelMonthEnd,textFieldMonthEnd), hBox6 = new HBox(26, labelDayEnd, textFieldDayEnd);
         VBox vBox1 = new VBox(20,labelStart,hBox1,hBox2,hBox3), vBox2 = new VBox(20,labelEnd, hBox4, hBox5, hBox6), vBoxButton = new VBox();
         vBox1.setPadding(new Insets(10,0,0,10));
         vBox2.setPadding(new Insets(10,0,0,20));
         buttonSaveAddingOfDateInterval.setPrefSize(60, 30);
         vBoxButton.getChildren().add(buttonSaveAddingOfDateInterval);
         vBoxButton.setPadding(new Insets(240, 0,0,10));
         vBox1.setPrefWidth(250);
         vBoxButton.setPrefWidth(100);
         vBox2.setPrefWidth(250);
         mainHbox.getChildren().addAll(vBox1,vBoxButton,vBox2);
         Scene secondScene = new Scene(mainHbox, 600, 300);
          newWindow = new Stage();
          //if user didn't choose to which teacher it should assigns dateInterval
         try
         {
             //creating a scene if everything is fine
             newWindow.setTitle("Adding unavailability of " + table.getSelectionModel().getSelectedItem().getFullName());
             newWindow.setScene(secondScene);
             newWindow.show();
         }
         //error window if everything is bad
         catch (NullPointerException ex)
         {
             JOptionPane.showMessageDialog(null, "In order to add an unavailability, you should choose a teacher", "Error", JOptionPane.ERROR_MESSAGE);
         }
         //listener for changing the table, when user clicked to a teacher
       myListenerForAddingDateIntervals = new MyListenerForAddingDateIntervals();
         buttonSaveAddingOfDateInterval.setOnAction(myListenerForAddingDateIntervals);
     }
         else if (e.getSource() == buttonIconRemove)
            {
                if(table2.getSelectionModel().getSelectedItem() == null)
                    JOptionPane.showMessageDialog(null, "In order to remove an unavailability, you should choose a date interval", "Error", JOptionPane.ERROR_MESSAGE);
                else
                {
                    DateInterval dateInterval = table2.getSelectionModel().getSelectedItem();
                    table.getSelectionModel().getSelectedItem().removeNotAvailableInterval(dateInterval);
                    Teacher.allTeachers.findBy("getFullName", table.getSelectionModel().getSelectedItem().getFullName()).get(0).removeNotAvailableInterval(dateInterval);
                    table2.getItems().remove(dateInterval);
                }
            }
         else if (e.getSource() == buttonIconEdit)
            {
                if(table2.getSelectionModel().getSelectedItem() == null)
                    JOptionPane.showMessageDialog(null, "In order to edit an unavailability, you should choose a date interval", "Error", JOptionPane.ERROR_MESSAGE);
                else
                {
                    mainHbox = new HBox();
                    buttonSaveEditingOfDateInterval = new Button("Apply");
                    labelYearStart = new Label("Year:");
                    labelMonthStart = new Label("Month:");
                    labelDayStart = new Label("Day:");
                    labelYearEnd = new Label("Year:");
                    labelMonthEnd = new Label("Month:");
                    labelDayEnd = new Label("Day:");
                    labelStart = new Label("Start date:");
                    labelEnd = new Label("End date:");
                    labelStart.setPadding(new Insets(0,0,30,85));
                    labelEnd.setPadding(new Insets(0,0,30,95));
                    textFieldYearStart = new TextField();
                    textFieldMonthStart = new TextField();
                    textFieldDayStart = new TextField();
                    textFieldYearEnd = new TextField();
                    textFieldMonthEnd = new TextField();
                    textFieldDayEnd = new TextField();
                    labelYearStart.setPadding(new Insets(5,0,0,0));
                    labelMonthStart.setPadding(new Insets(5,0,0,0));
                    labelDayStart.setPadding(new Insets(5,0,0,0));
                    labelYearEnd.setPadding(new Insets(5,0,0,0));
                    labelMonthEnd.setPadding(new Insets(5,0,0,0));
                    labelDayEnd.setPadding(new Insets(5,0,0,0));
                    HBox hBox1 = new HBox(22, labelYearStart, textFieldYearStart), hBox2 = new HBox(10, labelMonthStart, textFieldMonthStart), hBox3 = new HBox(26, labelDayStart, textFieldDayStart), hBox4 = new HBox(22, labelYearEnd, textFieldYearEnd), hBox5 = new HBox(10, labelMonthEnd,textFieldMonthEnd), hBox6 = new HBox(26, labelDayEnd, textFieldDayEnd);
                    VBox vBox1 = new VBox(20,labelStart,hBox1,hBox2,hBox3), vBox2 = new VBox(20,labelEnd, hBox4, hBox5, hBox6), vBoxButton = new VBox();
                    vBox1.setPadding(new Insets(10,0,0,10));
                    vBox2.setPadding(new Insets(10,0,0,20));
                    buttonSaveEditingOfDateInterval.setPrefSize(60, 30);
                    vBoxButton.getChildren().add(buttonSaveEditingOfDateInterval);
                    vBoxButton.setPadding(new Insets(240, 0,0,10));
                    vBox1.setPrefWidth(250);
                    vBoxButton.setPrefWidth(100);
                    vBox2.setPrefWidth(250);
                    mainHbox.getChildren().addAll(vBox1,vBoxButton,vBox2);
                    Scene secondScene = new Scene(mainHbox, 600, 300);
                    newWindow = new Stage();
                    newWindow.setTitle("Editing unavailability of " + table.getSelectionModel().getSelectedItem().getFullName());
                    newWindow.setScene(secondScene);
                    newWindow.show();
                    myListenerForEditingDateIntervals = new MyListenerForEditingDateIntervals();
                    buttonSaveEditingOfDateInterval.setOnAction(myListenerForEditingDateIntervals);
                }
            }
        }

    }

    //changes of the table depending on which teacher user clicked
    public class MyMouseListener implements EventHandler <MouseEvent> {
        @Override
        public void handle(MouseEvent e) {

   try
            {
                if(e.getSource() == table)
                {
                    if (e.getClickCount() == 1) {
                        //clears the table
                        table2.getItems().clear();
                        //if teacher has not an empty notAvailable list
                        if (table.getSelectionModel().getSelectedItem().getNotAvailable().size() != 0) {
                            //than we add all of its DateIntervals to table
                            for (DateInterval dateInterval : table.getSelectionModel().getSelectedItem().getNotAvailable().getList()) {
                                //if user clicks on the same teacher, data doesn't duplicate
                                if (table2.getItems().contains(dateInterval.copy()))
                                    continue;
                                //if this data wasn't met before - than add it
                                table2.getItems().add(dateInterval.copy());
                            }
                        }
                        //closing the window
                        newWindow.close();
                    }
                }
        }
   //catches some of the ecxeptions, where user could create something wrong
         catch (NullPointerException ex)
           {

           }
        }
    }
    public class MyListenerForEditingDateIntervals implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent e)
        {
            if(e.getSource() == buttonSaveEditingOfDateInterval)
            {
                if(textFieldDayEnd.getText().trim().isEmpty() && textFieldDayStart.getText().trim().isEmpty() && textFieldMonthEnd.getText().trim().isEmpty() && textFieldMonthStart.getText().trim().isEmpty() && textFieldYearEnd.getText().trim().isEmpty() && textFieldYearStart.getText().trim().isEmpty())
                    newWindow.close();
                else
                {
                    DateInterval dateInterval = table2.getSelectionModel().getSelectedItem();
                    MyDate startDateOrig = dateInterval.getStartDate(), endDateOrig = dateInterval.getEndDate();
                    try
                    {
                        MyDate startDate = new MyDate(textFieldDayStart.getText().trim().isEmpty() ? startDateOrig.getDay() : Integer.parseInt(textFieldDayStart.getText()), textFieldMonthStart.getText().trim().isEmpty() ? startDateOrig.getMonth() : Integer.parseInt(textFieldMonthStart.getText()), textFieldYearStart.getText().trim().isEmpty() ? startDateOrig.getYear() : Integer.parseInt(textFieldYearStart.getText()));

                        MyDate endDate = new MyDate(textFieldDayEnd.getText().trim().isEmpty() ? endDateOrig.getDay() : Integer.parseInt(textFieldDayEnd.getText()), textFieldMonthEnd.getText().trim().isEmpty() ? endDateOrig.getMonth() : Integer.parseInt(textFieldMonthEnd.getText()), textFieldYearEnd.getText().trim().isEmpty() ? endDateOrig.getYear() : Integer.parseInt(textFieldYearEnd.getText()));
                        if (endDate.isBefore(startDate))
                            JOptionPane.showMessageDialog(null, "Start date can't be before than end date", "Error", JOptionPane.ERROR_MESSAGE);
                        else {
                            table.getSelectionModel().getSelectedItem().removeNotAvailableInterval(dateInterval);
                            Teacher.allTeachers.findBy("getFullName", table.getSelectionModel().getSelectedItem().getFullName()).get(0).removeNotAvailableInterval(dateInterval);
                            table2.getItems().remove(dateInterval);
                            table.getSelectionModel().getSelectedItem().addNotAvailableInterval(new DateInterval(startDate, endDate));
                            table2.getItems().add(new DateInterval(startDate, endDate));
                            //??
                            try {
                                Teacher.allTeachers.findBy("getFullName", table.getSelectionModel().getSelectedItem().getFullName()).get(0).addNotAvailableInterval(new DateInterval(startDate, endDate));
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                newWindow.close();
                            }

                        }
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, "New data is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                        newWindow.close();
                    }
                    newWindow.close();
                }
            }
            newWindow.close();
        }
    }
   public class MyListenerForAddingDateIntervals implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent actionEvent) {
            //check if the program throws NullPointerException
            if (textFieldYearStart.getText() == null || textFieldMonthStart.getText() == null || textFieldDayStart == null || textFieldYearEnd == null || textFieldMonthEnd == null || textFieldDayEnd == null)
            {
                //Creating a window, which informs about the error
                JOptionPane.showMessageDialog(null, "Some of the text fields is empty", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
                {
                if (textFieldYearStart.getText().trim().isEmpty() || textFieldMonthStart.getText().trim().isEmpty() || textFieldDayStart.getText().trim().isEmpty() || textFieldYearEnd.getText().trim().isEmpty() || textFieldMonthEnd.getText().trim().isEmpty() || textFieldDayEnd.getText().trim().isEmpty()) {
                    //Creating a window, which informs about the error
                    JOptionPane.showMessageDialog(null, "At least one field is empty", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    try
                    {
                    MyDate startDate = new MyDate(Integer.parseInt(textFieldDayStart.getText()), Integer.parseInt(textFieldMonthStart.getText()),  Integer.parseInt(textFieldYearStart.getText()));
                    MyDate endDate = new MyDate(Integer.parseInt(textFieldDayEnd.getText()), Integer.parseInt(textFieldMonthEnd.getText()), Integer.parseInt(textFieldYearEnd.getText()));
                    if(endDate.isBefore(startDate))
                        JOptionPane.showMessageDialog(null, "End date is before start date", "Error", JOptionPane.ERROR_MESSAGE);
                    else
                    {
                        table.getSelectionModel().getSelectedItem().addNotAvailableInterval(new DateInterval(startDate, endDate));
                        //??
                        Teacher.allTeachers.findBy("getFullName", table.getSelectionModel().getSelectedItem().getFullName()).get(0).addNotAvailableInterval(new DateInterval(startDate, endDate));
                        table2.getItems().clear();
                        table2.getItems().addAll(table.getSelectionModel().getSelectedItem().getNotAvailable().getList());
                    }
                    }
                    catch (NumberFormatException | IllegalDateException ex)
                    {
                        JOptionPane.showMessageDialog(null, "You inputted incorrect date","Error",JOptionPane.ERROR_MESSAGE);
                        newWindow.close();
                    }
                    catch (Throwable throwable) {
                        throwable.printStackTrace();
                        newWindow.close();
                    }
                }

            }
            newWindow.close();
        }

    }
}
