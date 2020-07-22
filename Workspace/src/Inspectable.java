import java.lang.Class;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Inspectable
{
  private Class<?> subclass;

  /**
   * method with 1 unknown Class type  parameter, which returns the instance of Inspectable class for the given subclass
   * @param subclass unknown Class type  parameter, representing the subclass to make inspectable
   * @return Inspectable - the object used to inspect the given class
   */
  public Inspectable makeInspectable(Class<?> subclass){
    this.subclass = subclass;
    return this;
  }

  /**
   * method with 1 String parameter and 1 var arg parameter of the unknown Class type which is used to get the method with the given name and parameter signature
   * @param methodName String representing the name of the method to find
   * @param paramTypes an var arg of an unknown type class, representing the parameter signature
   * @return the method found according to give parameters
   * @throws NoSuchMethodException when there are no methods found
   */
  public Method getMethod(String methodName, Class<?>... paramTypes)
  {
    try { return this.subclass.getDeclaredMethod(methodName,paramTypes); }
    catch (NoSuchMethodException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }

  /**
   * method with the 1 String parameter, which returns all the methods with the given name
   * @param methodName String parameter, which helps to find method with the same name
   * @return ArrayList of methods, which have the same name, as methodName argument
   */
  public ArrayList<Method> getMethods(String methodName)
  {
    ArrayList<Method> res = new ArrayList<>();
    Method[] methods = this.subclass.getDeclaredMethods(); // try to find the method having its name
    for (int i = 0; i < methods.length; i++) {
      if(methods[i].getName().equals(methodName)){ res.add(methods[i]); }
    }
    return res;
  }
}
