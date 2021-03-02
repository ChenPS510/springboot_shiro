package com.mk.shiro.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author mk.chen
 */
public class VerificationCode {

    private static final Random RANDOM = new Random();
    /**
     * 字体Verdana
     */
    private Font font = new Font("dejavu", Font.BOLD, 32);
    /**
     * 验证码显示宽度
     */
    private int width = 130;
    /**
     * 验证码显示高度
     */
    private int height = 48;

    public void graphicsImage(int width, int height, String code, OutputStream out) {
        this.width = width;
        this.height = height;
        graphicsImage(code, out);
    }

    /**
     * 生成验证码图形
     *
     * @param code 验证码
     * @param out  输出流
     * @return boolean
     */
    public void graphicsImage(String code, OutputStream out) {
        char[] strs = code.toCharArray();
        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bi.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // 抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            // 随机画干扰线
            drawLine(3, null, g);
            // 随机画干扰圆
            drawOval(5, g);
            // 画字符串,指定透明度
            AlphaComposite ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
            g.setComposite(ac3);
            int hp = (height - font.getSize()) >> 1;
            int h = height - hp;
            int w = width / strs.length;
            int sp = (w - font.getSize()) / 2;
            for (int i = 0; i < strs.length; i++) {
                g.setColor(new Color(20 + randomNum(110), 20 + randomNum(110), 20 + randomNum(110)));
                // 计算坐标
                int x = i * w + sp + randomNum(3);
                int y = h - randomNum(3, 6);
                if (x < 8) {
                    x = 8;
                }
                if (x + font.getSize() > width) {
                    x = width - font.getSize();
                }
                if (y > height) {
                    y = height;
                }
                if (y - font.getSize() < 0) {
                    y = font.getSize();
                }
                g.setFont(font.deriveFont(randomNum(2) == 0 ? Font.PLAIN : Font.ITALIC));
                g.drawString(String.valueOf(strs[i]), x, y);
            }
            ImageIO.write(bi, "png", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawLine(int num, Color color, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? getRandomColor(150, 250) : color);
            int x1 = randomNum(-10, width - 10);
            int y1 = randomNum(5, height - 5);
            int x2 = randomNum(10, width + 10);
            int y2 = randomNum(2, height - 2);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawOval(int num, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(getRandomColor(100, 250));
            g.drawOval(randomNum(width), randomNum(height), 10 + randomNum(20), 10 + randomNum(20));
        }
    }

    private static int randomNum(int min, int max) {
        return min + RANDOM.nextInt(max - min);
    }

    private static int randomNum(int num) {
        return RANDOM.nextInt(num);
    }

    /**
     * 随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandomColor(int fc, int bc) {
        int maxRgbNum = 225;
        if (fc > maxRgbNum) {
            fc = maxRgbNum;
        }
        if (bc > maxRgbNum) {
            bc = maxRgbNum;
        }
        int r = fc + randomNum(bc - fc);
        int g = fc + randomNum(bc - fc);
        int b = fc + randomNum(bc - fc);
        return new Color(r, g, b);
    }
}