import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class saveBtnListener implements EventHandler<ActionEvent>
{
  private MyFileIO fileIO;
  private Object obj;

  public saveBtnListener(MyFileIO fileIO,  Object obj){
    this.fileIO = fileIO;
    this.obj = obj;
  }

  public abstract void onSave();

  public abstract boolean valid(Object obj);

  public void handle(ActionEvent actionEvent) {
    if (valid(obj)) {
      fileIO.write(obj);
      System.out.println(fileIO.read());
      onSave();
    }
  }
}
