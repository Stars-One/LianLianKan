import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * @author StarsOne
 * @date Create in  2019-3-17 0017 09:08:55
 * @description
 */
class Main {
    public static void main(String[] args) throws IOException {

        MainWindow mainWindow = new MainWindow(1000,600);//设置宽高
        mainWindow.addGamePanel();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
        //播放背景音乐
        FileInputStream fileInputStream = new FileInputStream("." + File.separator + "res" + File.separator + "music" + File.separator + "bg.mp3");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        try {
            Player player = new Player(bufferedInputStream);
            player.play();
            if (player.isComplete()) {
                player.play();
            }
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }


    }
}
