package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.util.Table;

public class Strsafe {

  private static Table<String, String> x1a;

  public static String safe(String y) {
    String z = find(y);
    if (z == null) {
      z = y;
      insert(z);
    }
    return z;
  }

  //char *Strsafe(y)
  //char *y;
  //{
  //  char *z;
  //
  //  z = Strsafe_find(y);
  //  if( z==0 && (z=malloc( strlen(y)+1 ))!=0 ){
  //    strcpy(z,y);
  //    Strsafe_insert(z);
  //  }
  //  MemoryCheck(z);
  //  return z;
  //}

  public static void init() {
    if (x1a != null) return;
    x1a = new Table<>();
    x1a.init(1024);
  }

  public static int insert(String data) {
    return x1a.insert(data, data);
  }

  public static String find(String data) {
    return x1a.find(data);
  }
}
