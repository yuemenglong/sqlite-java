package work;

import io.github.yuemenglong.sqlite.common.CharPtr;
import io.github.yuemenglong.sqlite.common.Ptr;
import io.github.yuemenglong.sqlite.core.shell;

import java.io.File;
import java.util.Arrays;

public class ExecSqlite {
  public static void main(String[] args) {
    CharPtr[] as = Arrays.stream(new String[]{
            (new File(".")).getAbsolutePath(),
            "Test"
    }).map(CharPtr::new).toArray(CharPtr[]::new);
    shell.main(as.length, new Ptr<>(as));
  }
}
