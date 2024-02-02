import ins.hpsnk.noblack.MouseTool;

public class NoBlackTest {

  public static void main(String[] args) {

    int screenWidth = (int) MouseTool.getScreenSize().getWidth();
    int screenHeight = (int) MouseTool.getScreenSize().getHeight();

    System.out.println(screenWidth);

    for (int i = 0; i < screenWidth; i++) {

      MouseTool.move(i, (screenHeight / 2));

      try {
        Thread.sleep(1);

      } catch (Exception e) {

      }

    }

  }
}
