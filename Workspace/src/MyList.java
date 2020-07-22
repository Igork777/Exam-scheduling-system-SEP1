import java.io.Serializable;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MyList<T>  implements Serializable{

  private ArrayList<T> list;
  private boolean unique;

  // = = = = = = = = = = = = =
  // =     CONSTRUCTORS      =
  // = = = = = = = = = = = = =
  /**
   * No-argument constructor will create an empty and unique MyList
   */
  public MyList(){
    this.list = new ArrayList<T>();
    this.unique = true;
  }

  /**
   * One-argument constructor will create an empty MyList with specified unique setting
   * @param unique specifies whether the list should be unique
   */
  public MyList(boolean unique){
    this.list = new ArrayList<T>();
    this.unique = unique;
  }
  /**
   * One-argument constructor creates an unique MyList from an ArrayList
   * @param arr the ArrayList used to fetch Mylist
   */
  public MyList(ArrayList<T> arr){
    this.unique = true;
    this.list = Util.copyArrList(arr, true);
  }
  /**
   * One-argument constructor creates an unique MyList from an Array
   * @param arr the Array used to fetch Mylist
   */
  public MyList(T[] arr){
    this.unique = true;
    this.list = Util.copyArrList(Util.arrToArrayList(arr), true);
  }
  /**
   * Two-argument constructor creates a MyList instance from an ArrayList
   * @param arr the ArrayList used to fetch MyList
   * @param unique specifies whether the list should be unique
   */
  public MyList(ArrayList<T> arr, boolean unique){
    this.unique = unique;
    this.list = Util.copyArrList(arr, this.unique);
  }
  /**
   * Two-argument constructor creates a MyList instance from an Array
   * @param arr the array used to fetch MyList
   * @param unique specifies whether the list should be unique
   */
  public MyList(T[] arr, boolean unique){
    this.unique = unique;
    this.list = Util.arrToArrayList(arr);
    this.list = Util.copyArrList(this.list, this.unique);
  }

  // = = = = = = = = = = = = =
  // =        SETTERS        =
  // = = = = = = = = = = = = =
  /**
   * Removes the element by specified index as a parameter, if its present.
   * Adds specified as a parameter element on the index position
   * @param index index of the element, which has to be replaced
   * @param elem the object, which has to be placed on the index position instead of the old one
   * @return true if element was replaced, false otherwise
   */
  public boolean setAt(int index, T elem) {
      if(index >= 0 && index < this.list.size()){
          list.set(index, Util.elementCopy(elem));
          return true;
      }
      return false;
  }
  /**
   * Sets the list to be equal to an unique ArrayList
   * @param list the ArrayList to be assigned
   */
  public void setList(ArrayList<T> list) { this.list = Util.copyArrList(list, true); }
  /**
   * Sets the list to be equal to an unique Array
   * @param list the Array to be assigned
   */
  public void setList(T[] list) { this.list = Util.copyArrList(Util.arrToArrayList(list), true); }
  /**
   * Sets the list to be equal to an ArrayList
   * @param list the ArrayList to be assigned
   * @param unique specifies whether the list should be unique
   */
  public void setList(ArrayList<T> list, boolean unique) {
    this.unique = unique;
    this.list = Util.copyArrList(list, unique);
  }
  /**
   * Sets the list to be equal to an Array
   * @param list the Array to be assigned
   * @param unique specifies whether the list should be unique
   */
  public void setList(T[] list, boolean unique) {
    ArrayList<T> newList = Util.arrToArrayList(list);
    this.list = Util.copyArrList(newList, unique);
    this.unique = unique;
  }
  /**
   * Updates the unique field of the MyList instance. If set to true, deletes the duplicates from the list
   * @param unique true for a unique list, false for a non-unique list
   */
  public void setUnique(boolean unique) {
    this.unique = unique;
    this.list = Util.copyArrList(this.list, unique);
  }

  // = = = = = = = = = = = = =
  // =        GETTERS        =
  // = = = = = = = = = = = = =
  /**
   * Gets the ArrayList associated with MyList instance
   * @return the ArrayList contained in the MyList instance
   */
  public ArrayList<T> getList() { return Util.copyArrList(this.list, this.unique); }

  /**
   * Gets the element at a certain index
   * @param index index of element
   * @return the element at the given index
   */
  public T get(int index){return this.list.get(index);}
  /**
   * Returns the index of an element if present. Returns -1 if no such element found
   * @param obj the element to search for
   * @return the index of the element or -1 if no such element found
   */
  public int getIndexOf(T obj){
      return this.list.indexOf(obj);
  }
  /**
   * Says whether the list is unique or not
   * @return true if is unique, false if non-unique
   */
  public boolean isUnique() { return unique; }

  // = = = = = = = = = = = = =
  // =         UTILS         =
  // = = = = = = = = = = = = =
  /**
   * Says whether the Object is present in the list
   * @param obj the Object searched
   * @return true if is present, false if not
   */
  public boolean contains(T obj){ return this.list.contains(obj); }
  /**
   * Appends an element to the end of the list
   * @param elem the element to append
   * @return the new size of the list
   */
  public int add(T elem){
    if(!this.unique || !this.contains(elem)){
        this.list.add(Util.elementCopy(elem));
    }
    return this.list.size();
  }
  /**
   * Appends an array of elements to the end of the list
   * @param elements an Array of elements to be appended to the list
   * @return the new size of the list
   */
  public int add(T... elements){
    for (int i = 0; i < elements.length; i++) { this.add(elements[i]); }
    return this.list.size();
  }
  /**
   * Appends an ArrayList of elements to the end of the list
   * @param elements an ArrayList of elements to be appended to the list
   * @return the new size of the list
   */
  public int add(ArrayList<T> elements){
      for (int i = 0; i < elements.size(); i++) {
          this.add(elements.get(i));
      }
    return this.list.size();
  }
  /**
   * Removes the element at the index specified as a parameter, if its valid
   * @param index the index to be deleted
   * @return true if an element was removed, false otherwise
   */
  public int removeAt(int index){
    if (index < this.list.size() && index >= 0){
      this.list.remove(index);
    }
    return this.size();
  }
  /**
   * Removes an ArrayList of elements from the original MyList
   * @param elements an ArrayList of elements to be removed from the list
   * @return the new size of the list
   */
  public int remove(ArrayList<T> elements){
    for (int i = 0; i < elements.size(); i++) {
      this.remove(elements.get(i));
    }
    return this.list.size();
  }
  /**
   * Removes the element specified as a parameter, if its present
   * @param obj the object to be deleted
   * @return true if an element was removed, false otherwise
   */
  public int remove(T obj){
    int index;
    do {
      index = this.list.indexOf(obj);
      if (index >= 0 && index < this.list.size()){
        this.list.remove(index);
      }
    }while (index >= 0);
    return this.size();
  }
  /**
   * Says whether the list is empty or not
   * @return boolean. True if is empty, false otherwise.
   */
  public boolean empty(){
    return this.size() == 0;
  }
  /**
   * Returns amount of elements of the list
   * @return number of elements in the list
   */
  public int size() {return list.size();}
  /**
   * Creates and returns copy of the list
   * @return copy of the list
   */
  public MyList<T> copy() { return new MyList<>(list); }
  /**
   * Compares two objects
   * @param obj object that has to be compared with "this" object
   * @return true if objects are equal and false when objects are not equal or it is impossible to detect
   */
  public boolean equals (Object obj) {
    if(obj instanceof MyList){
      MyList newObject = (MyList)obj;
      if (newObject.size() == this.size()) {
          return this.list.equals(newObject.list);
      }
    }
    return false;
  }
  /**
   * Prints all the information about MyList
   * @return String with all the information about MyListClass
   */
  public String toString()
  {
    StringBuilder result = new StringBuilder();
    result.append(unique ? "The List is unique " : "The List is not unique");
    for (int i = 0; i < list.size() ; i++)
        result.append(list.get(i));
    return result.toString();
  }
  //- - - - - FINDERS - - - - -
  /**
   * This method finds all the entries of this list satisfying the condition `methodForSearch(params).equals(value)`.
   * @param methodForSearch the Method object used to search. Must be present in the generic class of the list
   * @param params the array of parameters
   * @param value the expected result of the `methodForSearch`
   * @param <E> the expected return type of `methodForSearch`
   * @return an ArrayList of the objects form the list satisfying the condition.
   * @throws Throwable any exception thrown by `methodForSearch`
   */
  public <E> ArrayList<T> findBy(Method methodForSearch, E value, Object... params)
  {
    ArrayList<T> theFinalList = new ArrayList<T>();

    if (list.size() > 0){
      Class<?> listGenClass = list.get(0).getClass();
      Method[] genClassMethods = listGenClass.getMethods();

      for (int i = 0; i < genClassMethods.length; i++) {
        if (methodForSearch.equals(genClassMethods[i])){ // Method found!
          for (int j = 0; j < list.size(); j++) { // Iterate over the list
              try {
              Class<?> resClass = Util.primitiveToWrapper(methodForSearch.getReturnType()); // Get the expected return type
              Class<?> valClass = Util.primitiveToWrapper(value.getClass()); // Get the value type
              if (resClass == valClass){ // Check if it is equal to the value type
                @SuppressWarnings("unchecked") // Now I am sure the cast won't fail
                E result = (E)resClass.cast(methodForSearch.invoke(list.get(j), params)); // Call the method specified as a parameter
                if(result != null){
                  if (result.equals(value)){theFinalList.add(list.get(j));} // Add to the result list if the results match
                }else if (value == null){
                  theFinalList.add(list.get(j));
                }
              }
            }
            catch (IllegalAccessException e) { e.printStackTrace(); } // Catch an attempt to invoke a private method
            catch (InvocationTargetException e) { // Catch any possible exceptions thrown by the invoked method
              e.printStackTrace();
              if (e.getCause() != null) {
                try {
                  throw (e.getCause()); // Throw the genuine exception further
                }
                catch (Throwable throwable) {
                  throwable.printStackTrace();
                  System.exit(1);
                }
              }
            }
          }
        }
      }
    }
    return theFinalList;
  }
  /**
   * This method finds all the entries of this list satisfying the condition `methodForSearch().equals(value)`.
   * @param methodName the no-argument method name used to search. Must be present in the generic class of the list
   * @param value the expected result of the `methodForSearch`
   * @param <E> the expected return type of `methodForSearch`
   * @return an ArrayList of the objects form the list satisfying the condition.
   * @throws Throwable any exception thrown by `methodForSearch`
   */
  public <E> ArrayList<T> findBy(String methodName, E value)
  {
    ArrayList<T> theFinalList = new ArrayList<T>();
    if (list.size() > 0){
      Class<?> listGenClass = list.get(0).getClass();
      Method[] genClassMethods = listGenClass.getMethods();

      Method methodForSearch = Util.findMethods(listGenClass, methodName).get(0);

      for (int i = 0; i < genClassMethods.length; i++) {
        if (methodForSearch.equals(genClassMethods[i])){ // Method found!
          for (int j = 0; j < list.size(); j++) { // Iterate over the list
            try {
              Class<?> resClass = Util.primitiveToWrapper(methodForSearch.getReturnType()); // Get the expected return type
              Class<?> valClass = Util.primitiveToWrapper(value.getClass()); // Get the value type
              if (resClass == valClass){ // Check if it is equal to the value type
                @SuppressWarnings("unchecked") // Now I am sure the cast won't fail
                    E result = (E)resClass.cast(methodForSearch.invoke(list.get(j))); // Call the method specified as a parameter
                if(result != null){
                  if (result.equals(value)){theFinalList.add(list.get(j));} // Add to the result list if the results match
                }else if (value == null){
                  theFinalList.add(list.get(j));
                }
              }
            }
            catch (IllegalAccessException e) { e.printStackTrace(); } // Catch an attempt to invoke a private method
            catch (InvocationTargetException e) { // Catch any possible exceptions thrown by the invoked method
              e.printStackTrace();
              if (e.getCause() != null) {
                try { throw (e.getCause()); }catch (Throwable err){
                  err.printStackTrace();
                  System.exit(1);
                }
              }
            }
          }
        }
      }
    }
    return theFinalList;
  }

}
