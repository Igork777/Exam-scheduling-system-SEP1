public class IllegalDateException extends RuntimeException
{
  public IllegalDateException(){
    super("Invalid date");
  }

  public IllegalDateException(String m){
    super("Invalid date: "+m);
  }
}
