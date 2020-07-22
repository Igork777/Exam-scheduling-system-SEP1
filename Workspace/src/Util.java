import javafx.scene.control.Alert;

import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Util
{
  public static Class<?>[] wrapperTypes = {Void.class, Double.class, Float.class, Long.class, Integer.class, Short.class, Byte.class, Character.class, Boolean.class};
  public static Class<?>[] primitiveTypes = {void.class, double.class, float.class, long.class, int.class, short.class, byte.class, char.class, boolean.class};

  public static Class<?> primitiveToWrapper(Class<?> type){
    if (type.isPrimitive()){
      return wrapperTypes[getIndex(primitiveTypes, type)];
    }else{
      return type;
    }
  }

  public static <T> int getIndex(T[] arr, T element){
    for (int i = 0; i < arr.length; i++) {
      if(arr[i].equals(element)){
        return i;
      }
    }
    return -1;
  }

  /**
   * Finds one method from the specified class, using its name and parameters types
   * @param classToSearch a Class object of the class to search in
   * @param methodName a case-sensitive string for the name of the method to find
   * @param paramTypes an array, or enumeration of Class objects containing the types of the parameters
   * @return the Method object found
   * @throws NoSuchMethodException
   */
  public static Method findMethod(Class<?> classToSearch, String methodName, Class<?>... paramTypes)
  {
    Method res = null;
    try
    {  classToSearch.getDeclaredMethod(methodName, paramTypes); // try to find the method having its name and parameters types
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return res;
  }
  /**
   * Finds the method(s) from the specified class having the specified name
   * @param classToSearch a Class object of the class to search in
   * @param methodName a case-sensitive string for the name of method(s) searched
   * @return an ArrayList of Methods found
   */
  public static ArrayList<Method> findMethods(Class<?> classToSearch, String methodName)
  {
    ArrayList<Method> res = new ArrayList<>();
    Method[] methods = classToSearch.getDeclaredMethods(); // try to find the method having its name
    for (int i = 0; i < methods.length; i++) {
      if(methods[i].getName().equals(methodName)){ res.add(methods[i]); }
    }
    return res;
  }

  /**
   * Returns an object if it is doesn't have a copy method inside or returns copy of the object if it has a copy method inside
   * @param elem an object, which has to be checked
   * @return the elem parameter or its copy
   */
  public static <T> T elementCopy(T elem)
  {
    try
    {
      Method method = elem.getClass().getMethod("copy");
      return (T)method.invoke(elem);
    }
    catch (IllegalAccessException | NoSuchMethodException e)
    {
      return elem;
    }
    catch (InvocationTargetException e){
      e.printStackTrace();
      System.exit(1);
      return null;
    }
  }

  /**
   * Transforms an ArrayList to Array
   * @param arr he ArrayList to be transformed
   * @return the transformed Array
   */
  public static <T> T[] arrayListToArr(ArrayList<T> arr, T[] toArr){
    for (int i = 0; i < arr.size(); i++)
      toArr[i] = elementCopy(arr.get(i));
    return toArr;
  }
  /**
   * Transforms an Array to ArrayList
   * @param arr the Array to transform to ArrayList
   * @return the transformed ArrayList
   */
  public static <T> ArrayList<T> arrToArrayList(T[] arr)
  {
    ArrayList<T> arrList = new ArrayList<T>();
    for (int i = 0; i < arr.length; i++)
      arrList.add(Util.elementCopy(arr[i]));
    return arrList;
  }

  /**
   * Copies the ArrayList
   * @param arr the ArrayList to be copied
   * @param <T> the generic type of the array to be copied
   * @return
   */
  public static <T> ArrayList<T> copyArrList(ArrayList<T> arr) { return copyArrList(arr, false); }
  /**
   * Returns a copy of the passed ArrayList
   * @param arr the ArrayList to be copied
   * @param unique a boolean - specifies whether the ArrayList should be unique
   * @return a copy of the ArrayList
   */
  public static <T> ArrayList<T> copyArrList(ArrayList<T> arr, boolean unique){
    ArrayList<T> newArray = new ArrayList<T>();
    for (int i = 0; i < arr.size(); i++) {
      T element = Util.elementCopy(arr.get(i));
      if (!unique || !newArray.contains(element)){
        newArray.add(element);
      }
    }
    return newArray;
  }
  public static void validIfPositive(double number)
  {
    if(number< 0)
      throw new IllegalArgumentException();
  }
}
