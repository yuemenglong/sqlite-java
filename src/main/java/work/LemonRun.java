package work;

import io.github.yuemenglong.sqlite.lemon.Main;

import java.io.*;
import java.nio.file.Paths;

public class LemonRun {
  public static String getPath(String path) {
    return Paths.get(getDir(), path).toAbsolutePath().toString().replace("\\.\\", "\\").replaceAll("\\\\", "/");
  }

  public static String getDir() {
    return (new File(".")).getAbsolutePath();
  }

  public static void main(String[] args) throws IOException {
    System.out.println(getPath("work/lemon.y"));
    Main.main(new String[]{getPath("work/lemon.y")});
    FileInputStream is = new FileInputStream(getPath("work/lemon.java"));
    FileOutputStream os = new FileOutputStream(getPath("src/main/java/work/lemon.java"));
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line;
    while ((line = br.readLine()) != null) {
      os.write(line.getBytes());
      os.write("\n".getBytes());
    }
    is.close();
    os.close();
  }
}
