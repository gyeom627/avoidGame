// 제작자: 고겸
// 위 게임은 바이브 코딩으로 제작되었습니다.

import javax.swing.JFrame;

public class AvoidGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("피하기 게임 (Avoid Game)");
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        gamePanel.startGameThread();
    }
}
