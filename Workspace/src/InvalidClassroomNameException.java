public class InvalidClassroomNameException extends IllegalArgumentException
{
  public InvalidClassroomNameException(){
    super("Invalid name");
  }

  public InvalidClassroomNameException(String m){
    super("Invalid name: "+m);
  }
}
