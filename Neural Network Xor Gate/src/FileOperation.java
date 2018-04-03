import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileOperation {

    private String filepath = "";

    public void writeToFile(Object oin, String filepath){
        this.filepath = filepath;
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(oin);
            objectOut.close();
            System.out.println("The Object  was succesfully written to file:" + filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object readFromFile(String filepath){
        this.filepath = filepath;
        Object out = null;
        try{
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            out = objectIn.readObject();
            objectIn.close();
            System.out.println("The Object  was succesfully read from file:" + filepath);
        }catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }

}
