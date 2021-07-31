package Server;

/**
  * @ProjectName:    Escape
  * @ClassName:      CloseUtil
  * @Description:    服务器各需要关闭的方法
  * @Author:         Han Chubo
  */


import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
    public static void closeAll(Closeable... able) {
        for (Closeable c: able) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
