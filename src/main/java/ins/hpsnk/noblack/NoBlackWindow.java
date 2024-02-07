package ins.hpsnk.noblack;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoBlackWindow {
  private static final Logger logger = LogManager.getLogger(NoBlackRunnable.class);

  private final JFrame jFrame;
  private final JButton jButton;
  private boolean isRunning;
  private Thread thread;
  private NoBlackRunnable runnable;
  private SystemTray systemTray;

  public NoBlackWindow() {
    this.isRunning = false;

    this.jFrame = new JFrame();
    this.jFrame.setSize(300, 100);

    // this.jFrame.setLocation(100, 100);
    setLocation(jFrame);

    this.jFrame.setTitle("NoBlack");
    this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.jFrame.setResizable(false);

    this.jFrame.addWindowStateListener(event -> {
      if ((event.getNewState() & Frame.ICONIFIED) != 0) {
        // 窗体最小化时，隐藏窗口并添加到系统托盘
        jFrame.setVisible(false);
//        systemTray = addToSystemTray();
        if (runnable != null) {
          runnable.updateSystemTray(systemTray);
        }
      }
    });

    this.jButton = new JButton();
    this.jFrame.add(this.jButton);
    this.jButton.setText("Start");
    this.jButton.addActionListener(listener -> {
      if (!isRunning) {
        // start
        isRunning = true;
        jButton.setText("Stop");
        runnable = new NoBlackRunnable(
            jFrame, 60, systemTray);
        thread = new Thread(runnable);
        thread.start();
      } else {
        // stop
        isRunning = false;
        jButton.setText("Start");
        jFrame.setTitle("NoBlack");
        thread.stop();
        thread = null;
      }
    });

    systemTray = addToSystemTray();
//    System.out.println("SystemColor.activeCaption = " + SystemColor.activeCaption.brighter());
  }

  private static void setLocation(JFrame jFrame) {


    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;

    int posX = (screenWidth - jFrame.getWidth()) / 2;
    int posY = (screenHeight - jFrame.getHeight()) / 2;

    jFrame.setLocation(posX, posY);
  }

  public void display() {
    this.jFrame.setVisible(true);
  }

  private SystemTray addToSystemTray() {
    if (SystemTray.isSupported()) {
      SystemTray tray = SystemTray.getSystemTray();
      URL imageUrl = this.getClass().getResource("/noblack_icon.png");
      Image icon = Toolkit.getDefaultToolkit().getImage(imageUrl);

      // 创建弹出菜单
      PopupMenu popupMenu = new PopupMenu();

      // 创建菜单项，用于显示程序窗口
      MenuItem showItem = new MenuItem("Show");
      popupMenu.add(showItem);

      // 创建菜单项，用于退出程序
      MenuItem exitItem = new MenuItem("Exit");
      popupMenu.add(exitItem);

      // 创建托盘图标
      TrayIcon trayIcon = new TrayIcon(icon, "NoBlack", popupMenu);
      trayIcon.setToolTip("NoBlack");
      trayIcon.setImageAutoSize(true);

      try {
        tray.add(trayIcon);
      } catch (AWTException e) {
        logger.error("Failed to add tray icon.", e);
      }

      // 添加菜单项的监听器
      showItem.addActionListener(e -> {
        // 点击菜单项时
        // 显示窗体
        this.jFrame.setVisible(true);
        // 从系统托盘移除图标
//                tray.remove(trayIcon);
      });

      // 添加退出菜单项的监听器
      exitItem.addActionListener(e -> System.exit(0));

      // 托盘图标双击事件
      trayIcon.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            // 双击托盘图标时

            // 显示窗体
            jFrame.setVisible(true);
            // 从系统托盘移除图标
//                        tray.remove(trayIcon);
          }
        }
      });

      return tray;
    } else {
      logger.warn("SystemTray is not supported");
      return null;
    }
  }
}
