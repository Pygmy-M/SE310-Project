import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TransientWhenSerializing implements Serializable {
    // Required for serialization implementation
    private static final long serialVersionUID = 1L;

    // BufferedReader that we don't want serialized, mark as transient
    private transient BufferedReader br = null;

    // The path to the file we do want to serialize
    public String pathToFile = null;

    // The lines of the file, we want to serialize them, the readLineByLine function
    // will check if this is null and *only* read the file again if this is null
    public List<String> fileLines = null;

    public TransientWhenSerializing(String path){
        this.pathToFile = path;
    }

    public List<String> readFileLineByLine(){
        // Return the existing lines if we already have them stored
        if(this.fileLines != null) return this.fileLines;

        // The BufferedReader should be null here regardless of constructor
        // instantiation or deserialization but it doesn't hurt to check
        if(this.br == null){
            try {
                this.br = Files.newBufferedReader(Paths.get(this.pathToFile));
            }
            catch(IOException e){
                e.printStackTrace();
                System.exit(2);
            }
        }

        // Instantiate the fileLines list
        this.fileLines = new ArrayList<>();

        try {
            String line = this.br.readLine();
            while(line != null){
                fileLines.add(line);
                line = br.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(2);
        }
        // Cleanup what we opened, finally blocks run regardless of an exception being
        // thrown
        finally{
            if(this.br != null){
                try{
                    br.close();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }
        // Return the lines of the file
        return this.fileLines;
    }

    // This function serializes this class and returns the path to where it can be
    // found on disk
    public static String serialize(Survey tws){
        // We're saving it to the local directory without any sub directories
        String path = tws.getName() + tws.getType();
        if (tws.getType().equals("Response")) {
            path = path + tws.getResponseNum();
        }
        path = path + ".ser";
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(tws);
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(2);
        }

        finally{
            try{
                if(fos != null)
                    fos.close();
                if(oos != null)
                    oos.close();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return path;
    }

    // This function deserializes a saved TransientWhenSerialing object based on the
    // path given. It returns the fully instantiated object
    public static Survey deserialize(String path){
        Survey deserializedObject = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            deserializedObject = (Survey) ois.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.exit(2);
        }
        finally{
            try{
                if(ois != null)
                    ois.close();
                if(fis != null)
                    fis.close();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return deserializedObject;
    }
}
