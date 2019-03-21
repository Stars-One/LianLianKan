import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import wigget.GamePanel;

/**
 * @author StarsOne
 * @date Create in  2019-3-17 0017 09:05:15
 * @description
 */
class MainWindow extends JFrame {

    private GamePanel gamePanel;

    public MainWindow() throws HeadlessException {
        // 得到显示器屏幕的宽高
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        // 定义窗体的宽高
        int windowsWedth = 600;
        int windowsHeight = 600;
        setTitle("二次元连连看");
        // TODO: 2019-3-17 0017 设置标题头像
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds((width - windowsWedth) / 2, (height - windowsHeight) / 2, windowsWedth, windowsHeight);//设置窗口大小并将窗口大小移动到指定的x,y坐标
        setLayout(null);

        //图片路径
        List<String> imagePathList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            imagePathList.add("." + File.separator + "res" + File.separator + i + ".jpg");
        }

        // 获取res的图片资源，作为GamePanel的构造函数的参数传入

        gamePanel = new GamePanel(60, 60, imagePathList, 4, 4);//设置游戏画板的位置和数据，3行3
        System.out.println("gamepanel"+gamePanel.getHeight()+","+gamePanel.getWidth());
        gamePanel.setCenter(this);//居中
        this.add(gamePanel);

        setVisible(true);//设置窗口显示

    }





}
