package ins.hpsnk.noblack;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;

public class MouseTool {

  public static Dimension getScreenSize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // System.out.print("Screen size:");
    // System.out.print(screenSize.getWidth());
    // System.out.print("x");
    // System.out.print(screenSize.getHeight());
    // System.out.println();

    return screenSize;
  }

  public static void move(Point point) {
    try {
      Robot robot = new Robot();

      robot.mouseMove(point.x, point.y);

    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  public static void move(int x, int y) {
    Point p = new Point(x, y);

    move(p);
  }

  public static void shake() {
    Point currPos = getCurrentLocation();

    try {
      Robot robot = new Robot();

      robot.mouseMove(200, 200);

    } catch (AWTException e) {
      e.printStackTrace();
    }

  }


  public static Point getCurrentLocation() {
    return MouseInfo.getPointerInfo().getLocation();
  }
}
