package wigget;

import java.awt.*;
import java.io.File;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author StarsOne
 * @date Create in  2019-3-17 0017 09:43:20
 * @description
 */
public class ImageButton extends JButton {
    private String imagePath;
    private int width, height;
    private ImageIcon imageIcon;
    private int rowIndex, colIndex;//行下标，纵坐标
    private int pictureFlag;//图片标识
    private boolean exist;//当前图片是否存在

    public ImageButton() {
        super();
    }

    /**
     * @param imagePath 图片名.统一为png格式
     */
    public ImageButton(String imagePath) {
        width = height = 60;
        this.imagePath = imagePath;
        dealImageIcon();//对图片进行处理
        setIcon(imageIcon);//设置图片
        setContentAreaFilled(false);//设置按钮内容区域不可见，使得按钮与我们图片大小相同
        setBorder(null);//消除按钮边界
        this.exist = true;//初始化的时候存在
        rowIndex = 0;
        colIndex = 0;
    }

    private void dealImageIcon() {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(width, height, 1));//按照指定宽高进行图片的缩放
        this.imageIcon = imageIcon;
    }

    /**
     * 图片消除时的爆炸效果
     * @throws InterruptedException
     */
    public void imageBoom() throws InterruptedException {
        setBorder(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 15; i++) {
                    ImageIcon imageIcon = new ImageIcon("." + File.separator + "res" + File.separator + "boom" + File.separator + "boom_" + i + ".png");
                    setIcon(imageIcon);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    repaint();
                }
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setVisible(false);
                setExist(false);//删除，将当前图片设置为不存在
            }
        }).start();

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public void repaintBorder() {
        setBorder(BorderFactory.createLineBorder(Color.RED));

    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setIndex(int rowIndex, int colIndex) {
        setRowIndex(rowIndex);
        setColIndex(colIndex);
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public int getPictureFlag() {
        return pictureFlag;
    }

    public void setPictureFlag(int pictureFlag) {
        this.pictureFlag = pictureFlag;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ImageButton that = (ImageButton) o;
        return width == that.width &&
                height == that.height &&
                rowIndex == that.rowIndex &&
                colIndex == that.colIndex &&
                pictureFlag == that.pictureFlag &&
                exist == that.exist &&
                Objects.equals(imagePath, that.imagePath) &&
                Objects.equals(imageIcon, that.imageIcon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imagePath, width, height, imageIcon, rowIndex, colIndex, pictureFlag, exist);
    }
}
