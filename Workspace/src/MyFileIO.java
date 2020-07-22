import java.io.*;

public class MyFileIO<T>
{
  private String path;

  /**
   * 1 argument String constructor
   * @param filename String value, representing the name of the file to work, without any extensions
   */
  public MyFileIO(String filename)
  {
    this.path = "saves/"+filename+".bin";
  }

  /**
   * method with 1 generic parameter, which writes it to the binary file
   * @param obj the object, that has to be written in the file
   */
  public void write(T obj){
    FileOutputStream fileOut = null;
    ObjectOutputStream binWrite = null;

    try { fileOut = new FileOutputStream(this.path); }
    catch (FileNotFoundException e) { e.printStackTrace(); }

    try { binWrite = new ObjectOutputStream(fileOut); }
    catch (IOException e) { e.printStackTrace(); }

    try{
      binWrite.writeObject(obj);
    }catch(IOException e) {
      System.out.println("IO Error writing to file ");
      e.printStackTrace();
      System.exit(1);
    }finally {
      try {
        if (fileOut != null){fileOut.close();}
        if (binWrite != null){binWrite.close();}
      } catch (IOException e) { e.printStackTrace(); }
    }
  }

  /**
   * method with no parameters, which reads any information from the file
   * @return generic variable, which will be read form the file
   */
  public T read() {
    T res = null;
    FileInputStream fileIn = null;
    ObjectInputStream binRead = null;
    // fileInStream
    for (int i = 0; i < 2; i++) {
      try {
        fileIn = new FileInputStream(this.path);
        break; // success!
      }catch (FileNotFoundException e){
        System.out.println("File "+this.path+" not found, or could not be opened. Trying to create.");
        FileOutputStream tryFileOut = null;
        ObjectOutputStream tryBinWrite = null;
        try {
          tryFileOut = new FileOutputStream(this.path);
          tryBinWrite = new ObjectOutputStream(tryFileOut);
          System.out.println("Created!");
        } catch (IOException ex) { ex.printStackTrace(); }
        finally {
          try {
            if (tryFileOut != null){tryFileOut.close();}
            if (tryBinWrite != null){tryBinWrite.close();}
          }
          catch (IOException ex) { ex.printStackTrace(); }
        }
      }
    }
    // binRead
    try { binRead = new ObjectInputStream(fileIn); }
    catch (IOException e) { e.printStackTrace(); }
    // Reading
    while (true) {
      try {
        res = (T)binRead.readObject();
      }catch (EOFException eof) {
        break; // Done!
      }catch (IOException e) {
        System.out.println("IO Error reading from file");
        e.printStackTrace();
        System.exit(1);
      }catch (ClassNotFoundException e) {
        System.out.println("Failed to cast");
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      if (binRead != null){binRead.close();}
      if (fileIn != null){fileIn.close();}
    }catch (IOException e) { e.printStackTrace(); }

    return res;
  }
}