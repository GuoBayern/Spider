import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {

    public AboutDialog()
    {
        setTitle("蜘蛛纸牌 Design By Guo");
        setSize(300,200);
        setResizable(false);
        setDefaultCloseOperation (WindowConstants.DISPOSE_ON_CLOSE);
        Container c = this.getContentPane();

        JTextArea jt1 = new JTextArea("将纸牌按照相同的花色从大到小的顺序排列；如果牌数不够请点击下方蜘蛛纸牌图案。");
        jt1.setSize(260,200);
        JTextArea jt2 = new JTextArea("纸牌的图片拷贝Windows系统的蜘蛛纸牌游戏！");
        jt2.setSize(260,200);

        jt1.setEditable(false);
        jt2.setEditable(false);

        jt1.setLineWrap(true);
        jt2.setLineWrap(true);

        jt1.setFont(new Font("楷体_GB2312", java.awt.Font.BOLD, 13));
        jt1.setForeground(Color.blue);

        jt2.setFont(new Font("楷体_GB2312", java.awt.Font.BOLD, 13));
        jt2.setForeground(Color.black);

        JPanel jPanel1 = new JPanel();
        jPanel1.add(jt1);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(jt2);

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setSize(300,200);
        jTabbedPane.addTab("规则", null, jPanel1, null);
        jTabbedPane.addTab("关于", null, jPanel2, null);

        JPanel jMainPane = new JPanel();
        jMainPane.add(jTabbedPane);
        c.add(jMainPane);

        pack();
        this.setVisible(true);
    }

}
