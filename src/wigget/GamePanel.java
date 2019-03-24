package wigget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import util.ArrayUtils;

/**
 * 游戏画板，连连看图片放在这里
 *
 * @author StarsOne
 * @date Create in  2019-3-17 0017 10:57:29
 * @description
 */
public class GamePanel extends JPanel {
    private int x, y;//Jpanel左上角坐标，相对于Jframe
    private int imgWidth, imgHeight;
    private List<wigget.ImageButton> imageButtons = new ArrayList<>();
    private int rows, cols;//列，行
    List<wigget.ImageButton> clickImageButtons = new ArrayList<>();//存放两个点击的图片

    /**
     * @param x            左上角x坐标
     * @param y            左上角y坐标
     * @param imageButtons 图片列表
     */
    public GamePanel(int x, int y, List<wigget.ImageButton> imageButtons) {
        this.x = x;
        this.y = y;
        this.imageButtons = imageButtons;
        init();
    }


    /**
     * @param x             左上角x坐标
     * @param y             左上角y坐标
     * @param imagePathList 图片路径list
     * @param rows          列数
     * @param cols          行数
     */
    public GamePanel(int x, int y, List<String> imagePathList, int rows, int cols) {
        this.x = x;
        this.y = y;

        this.rows = rows;
        this.cols = cols;

        // 接收图片资源List，并依次随机获得图片，两个List，两者的size等于col*row,一个是随机获得的，另外一个是反转得到的

        //获取图片
        int size = (rows * cols) / 2;
        int[] flags = new int[size];//之后可以保证生成新的对象

        //随机取图片
        for (int i = 0; i < size; i++) {
            flags[i] = (int) (Math.random() * imagePathList.size());
        }
        ArrayUtils.random(flags);//把数组打乱
        for (int i = 0; i < size; i++) {
            int flag = flags[i];
            wigget.ImageButton imageButton = new wigget.ImageButton(imagePathList.get(flag));//从24张图片随机取9张图
            imageButton.setPictureFlag(flag);//设置图片的标志
            imageButtons.add(imageButton);
        }
        ArrayUtils.random(flags);//把数组打乱
        //逆序
        for (int i = size - 1; i >= 0; i--) {
            int flag = flags[i];
            wigget.ImageButton imageButton = new wigget.ImageButton(imagePathList.get(flag));//从24张图片随机取9张图
            imageButton.setPictureFlag(flag);//设置图片的标志
            imageButtons.add(imageButton);
        }

        imgWidth = imgHeight = imageButtons.get(0).getHeight();

        init();
    }

    //设置
    private void init() {
        this.setBounds(x, y, cols * imgWidth, rows * imgHeight);
        setLayout(null);//不需要布局
        addButtons();//将图片画在画板上
    }

    /**
     * 设置居中
     */
    public void setCenter(JFrame jFrame) {
        setLocation(jFrame.getWidth() / 2 - getWidth() / 2, jFrame.getHeight() / 2 - getHeight() / 2);
    }

    //添加图片按钮
    private void addButtons() {
        int k = 0;
        // 需要随机生成图片的位置以及设置图片的flag
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                wigget.ImageButton imageButton = imageButtons.get(k);
                imageButton.setBounds(j * imgWidth, i * imgWidth, imgWidth, imgHeight);
                imageButton.setIndex(i, j);
                imageButton.addActionListener(e -> {
                    if (clickImageButtons.size() == 2) {
                        //设置边框消失
                        for (wigget.ImageButton clickImageButton : clickImageButtons) {
                            clickImageButton.setBorder(null);
                        }
                        clickImageButtons.clear();//清空选中的图片
                    }

                    if (clickImageButtons.size() != 2) {
                        clickImageButtons.add(imageButton);
                        playClickWav();//播放点击音效
                        imageButton.repaintBorder();
                        //判断是否满足条件
                        if (isCanDelete(clickImageButtons)) {
                            hideImageButton(clickImageButtons);//清除
                            imageButtons.clear();//清空选中的图片
                        }
                    }

                });
                add(imageButton);//如果要添加的对象已存在，则不会添加进去
                k++;

            }
        }
        setOpaque(false);
    }

    private void playClickWav() {
        new Thread(() -> {
            try {
                AudioStream audioStream = new AudioStream(new FileInputStream("." + File.separator + "res" + File.separator + "music" + File.separator + "clickMusic.wav"));
                AudioPlayer.player.start(audioStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //播放消除的爆炸音效
    private void playBoomWav() {
        new Thread(() -> {
            try {
                AudioStream audioStream = new AudioStream(new FileInputStream("." + File.separator + "res" + File.separator + "music" + File.separator + "boom.wav"));
                AudioPlayer.player.start(audioStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    /**
     * 消除图片
     *
     * @param clickImageButtons
     */
    private void hideImageButton(List<wigget.ImageButton> clickImageButtons) {
        ImageButton imageButton1 = clickImageButtons.get(0);
        ImageButton imageButton2 = clickImageButtons.get(1);
        playBoomWav();//播放消除的爆炸音效
        try {
            imageButton1.imageBoom();
            imageButton2.imageBoom();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //是否满足条件
    private boolean isCanDelete(List<wigget.ImageButton> clickImageButtons) {
        //选中的图片有两个以及这两个图片都是相同的
        if (clickImageButtons.size() == 2 && isPictureSame(clickImageButtons)) {
            return lineHorizontal(clickImageButtons) || lineVertical(clickImageButtons) || turnOnce(clickImageButtons) || turnTwice(clickImageButtons);
        }
        return false;
    }

    //两张图片是否相同
    private boolean isPictureSame(List<wigget.ImageButton> clickImageButtons) {
        return clickImageButtons.get(0).getPictureFlag() == clickImageButtons.get(1).getPictureFlag();
    }

    /**
     * 水平方向是否能够直接连接
     *
     * @param clickImageButtons 已经点击的两个图片
     * @return 结果
     */
    private boolean lineHorizontal(List<wigget.ImageButton> clickImageButtons) {

        wigget.ImageButton imageButtonA = clickImageButtons.get(0);
        wigget.ImageButton imageButtonB = clickImageButtons.get(1);
        int rowIndexA = imageButtonA.getRowIndex();
        int colIndexA = imageButtonA.getColIndex();
        int rowIndexB = imageButtonB.getRowIndex();
        int colIndexB = imageButtonB.getColIndex();

        return lineVertical(rowIndexA, colIndexA, rowIndexB, colIndexB);
    }

    /**
     * 垂直方向是否能够直接连接
     *
     * @param clickImageButtons
     * @return
     */
    private boolean lineVertical(List<wigget.ImageButton> clickImageButtons) {
        wigget.ImageButton imageButtonA = clickImageButtons.get(0);
        wigget.ImageButton imageButtonB = clickImageButtons.get(1);
        int rowIndexA = imageButtonA.getRowIndex();
        int colIndexA = imageButtonA.getColIndex();
        int rowIndexB = imageButtonB.getRowIndex();
        int colIndexB = imageButtonB.getColIndex();

        return lineHorizontal(rowIndexA, colIndexA, rowIndexB, colIndexB);
    }

    /**
     * 两个点水平方向是否可以直接连接
     *
     * @param rowIndexA x1
     * @param colIndexA y1
     * @param rowIndexB x2
     * @param colIndexB y2
     * @return
     */
    private boolean lineHorizontal(int rowIndexA, int colIndexA, int rowIndexB, int colIndexB) {
        if (rowIndexA == rowIndexB && colIndexA == colIndexB) {
            return false;
        }

        if (colIndexA != colIndexB) {
            return false;
        }

        int start_x = Math.min(rowIndexA, rowIndexB);
        int end_x = Math.max(rowIndexA, rowIndexB);

        for (int i = start_x; i < end_x; i++) {
            if (isPictureExist(i, colIndexA)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 两个点垂直方向是否可以直接连接
     *
     * @param rowIndexA
     * @param colIndexA
     * @param rowIndexB
     * @param colIndexB
     * @return
     */
    private boolean lineVertical(int rowIndexA, int colIndexA, int rowIndexB, int colIndexB) {
        if (rowIndexA == rowIndexB && colIndexA == colIndexB) {
            return false;
        }
        if (rowIndexA != rowIndexB) {
            return false;
        }
        int start_y = Math.min(colIndexA, colIndexB);
        int end_y = Math.max(colIndexA, colIndexB);

        for (int j = start_y; j < end_y; j++) {
            //如果存在，直接返回false
            if (isPictureExist(rowIndexA, j)) {
                return false;
            }
        }
        return true;

    }

    /**
     * 两个点在一个拐点能够连接
     *
     * @param clickImageButtons
     * @return
     */
    private boolean turnOnce(List<wigget.ImageButton> clickImageButtons) {
        wigget.ImageButton imageButtonA = clickImageButtons.get(0);
        wigget.ImageButton imageButtonB = clickImageButtons.get(1);
        int rowIndexA = imageButtonA.getRowIndex();
        int colIndexA = imageButtonA.getColIndex();
        int rowIndexB = imageButtonB.getRowIndex();
        int colIndexB = imageButtonB.getColIndex();
        return turnOnce(rowIndexA, colIndexA, rowIndexB, colIndexB);

    }

    /**
     * 判断两个点是否在一个拐点就能够直接连接
     *
     * @param rowIndexA
     * @param colIndexA
     * @param rowIndexB
     * @param colIndexB
     * @return
     */
    private boolean turnOnce(int rowIndexA, int colIndexA, int rowIndexB, int colIndexB) {
        if (rowIndexA == rowIndexB && colIndexA == colIndexB) {
            return false;
        }

        int c_x = rowIndexA, c_y = colIndexB;
        int d_x = rowIndexB, d_y = colIndexA;

        boolean ret = false;
        //如果没有障碍物
        if (!isPictureExist(c_x, c_y)) {
            ret = lineHorizontal(rowIndexA, colIndexA, c_x, c_y) || lineVertical(c_x, c_y, rowIndexB, colIndexB);
        }
        if (!isPictureExist(d_x, d_y)) {
            ret = lineHorizontal(rowIndexA, colIndexA, d_x, d_y) || lineVertical(d_x, d_y, rowIndexB, colIndexB);

        }
        return ret;
    }

    /**
     * 坐标x，坐标y是否还存在有图片，行坐标和纵坐标
     *
     * @param x
     * @param y
     * @return 结果
     */
    private boolean isPictureExist(int x, int y) {
        for (wigget.ImageButton imageButton : imageButtons) {
            if (imageButton.getX() == x && imageButton.getY() == y) {
                return imageButton.isExist();//存在true
            }
        }
        return false;//这里可忽略
    }

    /**
     * 两个拐点，图片是否能够直接相连
     *
     * @param clickImageButtons
     * @return
     */
    private boolean turnTwice(List<wigget.ImageButton> clickImageButtons) {
        wigget.ImageButton imageButtonA = clickImageButtons.get(0);
        wigget.ImageButton imageButtonB = clickImageButtons.get(1);
        int rowIndexA = imageButtonA.getRowIndex();
        int colIndexA = imageButtonA.getColIndex();
        int rowIndexB = imageButtonB.getRowIndex();
        int colIndexB = imageButtonB.getColIndex();
        return turnTwice(rowIndexA, colIndexA, rowIndexB, colIndexB);

    }

    /**
     * 两个点在两个拐点是否能够直接相连
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private boolean turnTwice(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) {
            return false;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i != x1 && i != x2 && j != y1 && j != y2) {
                    continue;
                }

                if ((i == x1 && j == y1) || (i == x2 && j == y2)) {
                    continue;
                }

                if (isPictureExist(i, j)) {
                    continue;
                }

                if (turnOnce(x1, y1, i, j) && (lineHorizontal(i, j, x2, y2) || lineVertical(i, j, x2, y2))) {
                    return true;
                }
                if (turnOnce(i, j, x2, y2) && (lineHorizontal(x1, y1, i, j) || lineVertical(x1, y1, i, j))) {
                    return true;
                }

            }
        }

        return false;

    }

    public List<wigget.ImageButton> getImageButtons() {
        return imageButtons;
    }

    public void setImageButtons(List<wigget.ImageButton> imageButtons) {
        this.imageButtons = imageButtons;
    }
}
