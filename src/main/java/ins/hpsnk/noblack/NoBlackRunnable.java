package ins.hpsnk.noblack;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NoBlackRunnable implements Runnable {
    private final JFrame jFrame;
    private final int interval;
    private SystemTray systemTray;
    private Robot robot;

    public NoBlackRunnable(JFrame frame, int sleepTime, SystemTray systemTray) {
        this.jFrame = frame;
        this.interval = sleepTime;
        this.systemTray = systemTray;

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void updateSystemTray(SystemTray st) {
        this.systemTray = st;
    }

    public void run() {
        try {
            for (; ; ) {
                for (int i = 1; i < this.interval; i++) {
                    this.jFrame.setTitle(String.valueOf(this.interval - i));

                    if (this.systemTray != null && systemTray.getTrayIcons().length > 0) {
                        systemTray.getTrayIcons()[0].setToolTip("Noblack : " + i);
                        systemTray.getTrayIcons()[0].setImage(createImage(i));
                    }
                    Thread.sleep(1000L);
                }
                Point currentPoint = MouseInfo.getPointerInfo().getLocation();
                robot.mouseMove(currentPoint.x - 1, currentPoint.y - 1);
                robot.mouseMove(currentPoint.x, currentPoint.y);
            }

        } catch (Exception e) {
            System.out.println("Exception in NoBlackRunnable.run");
            e.printStackTrace();
        }
    }

    private Image createImage(int index) {
        int width = 96;
        int height = 96;

        // 创建一个 BufferedImage 对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 获取图像的画笔对象
        Graphics2D graphics = image.createGraphics();


        // 图片背景
        // 任务栏的背景色
        // graphics.setColor(SystemColor.activeCaption.brighter());
        graphics.setColor(Color.blue);
        graphics.fillRect(0, 0, width, height);

        // 在边框显示进图条
        // 1像素的红色边框
        int precent = 100 * (this.interval - index) / this.interval;
        //    System.out.println("precent = " + precent);

        // 红色的进度线
        graphics.setColor(Color.RED);
        if (precent >= 75) {
            // 右
            int lineLength = (precent - 75) * (height - 8) / 25;
            graphics.fillRect(width - 8, 8, 8, lineLength);
        }
        if (precent >= 50) {
            // 上
            int lineLength = (precent - 50) * (height - 8) / 25;
            graphics.fillRect(0, 0, lineLength, 8);
        }
        if (precent >= 25) {
            // 左
            int lineLength = (precent - 25) * (height - 8) / 25;
            graphics.fillRect(0, height - lineLength, 8, lineLength);
        }
        if (precent >= 0) {
            // 下
            int lineLength = (precent - 0) * (height - 8) / 25;
            graphics.fillRect(width - lineLength, height - 8, lineLength, 8);
        }

        // 图像的水平垂直中央部位，描画文字
        // 设置字体样式和大小
        graphics.setFont(new Font("Arial", Font.BOLD, 80));
        String text = String.valueOf(this.interval - index);
        FontMetrics fm = graphics.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(text, graphics).getBounds();
        int textX = (width - rectText.width) / 2;
        int textY = (height - rectText.height) / 2 + fm.getMaxAscent();
        graphics.setColor(Color.green);
        graphics.drawString(text, textX, textY);

        // 释放画笔资源
        graphics.dispose();

        // 将 BufferedImage 转换为 Image 对象
        return (Image) image;

    }
}