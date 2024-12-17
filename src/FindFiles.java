import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Paths;

public class FindFiles {

    public static ArrayList<File> findFiles(String key) throws IOException {
        File directory = new File(Paths.get(".").toFile().getCanonicalPath());
        ArrayList<File> result = new ArrayList<>();

        FilenameFilter extFilter = (dir, name) -> name.contains(key);
        File[] files = directory.listFiles(extFilter);

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    result.add(file);
                }
            }
        }

        return result;
    }
}
