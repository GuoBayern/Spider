import javax.swing.*;

@SuppressWarnings("serial")
class SpiderMenuBar extends JMenuBar {

    private Spider main = null;

    private JMenuItem jItemPlayAgain = new JMenuItem("重新发牌");

    SpiderMenuBar(Spider spider){

        this.main = spider;

        JMenu jNewGame = new JMenu("游戏");
        JMenuItem jItemOpen = new JMenuItem("开局");
        jNewGame.add(jItemOpen);
        jNewGame.add(jItemPlayAgain);
        JMenuItem jItemValid = new JMenuItem("显示可行操作");
        jNewGame.add(jItemValid);

        jNewGame.addSeparator();
        JRadioButtonMenuItem jRMItemEasy = new JRadioButtonMenuItem("简单：单一花色");
        jNewGame.add(jRMItemEasy);
        JRadioButtonMenuItem jRMItemNormal = new JRadioButtonMenuItem("中级：双花色");
        jNewGame.add(jRMItemNormal);
        JRadioButtonMenuItem jRMItemHard = new JRadioButtonMenuItem("高级：四花色");
        jNewGame.add(jRMItemHard);

        jNewGame.addSeparator();

        JMenuItem jItemExit = new JMenuItem("退出");
        jNewGame.add(jItemExit);

        ButtonGroup group = new ButtonGroup();
        group.add(jRMItemEasy);
        group.add(jRMItemNormal);
        group.add(jRMItemHard);

        JMenu jHelp = new JMenu("帮助");
        JMenuItem jItemAbout = new JMenuItem("关于");
        jHelp.add(jItemAbout);

        this.add(jNewGame);
        this.add(jHelp);

        jItemOpen.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                main.newGame();
            }
        });

        jItemPlayAgain.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if(main.getC() < 60)
                {
                    main.deal();
                }
            }
        });

        jItemValid.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                new Show().start();
            }
        });

        jItemExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                main.dispose();
                System.exit(0);
            }
        });

        jRMItemEasy.setSelected(true);

        jRMItemEasy.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                main.setGrade(Spider.EASY);
                main.initCards();
                main.newGame();
            }
        });

        jRMItemNormal.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                main.setGrade(Spider.NATURAL);
                main.initCards();
                main.newGame();
            }
        });

        jRMItemHard.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                main.setGrade(Spider.HARD);
                main.initCards();
                main.newGame();
            }
        });

        jNewGame.addMenuListener(new javax.swing.event.MenuListener()
        {
            public void menuSelected(javax.swing.event.MenuEvent e)
            {
                if(main.getC() < 60)
                {
                    jItemPlayAgain.setEnabled(true);
                }
                else
                {
                    jItemPlayAgain.setEnabled(false);
                }
            }
            public void menuDeselected(javax.swing.event.MenuEvent e)
            {
            }
            public void menuCanceled(javax.swing.event.MenuEvent e)
            {
            }
        });

        jItemAbout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                new AboutDialog();
            }
        });
    }

    class Show extends Thread
    {
        public void run()
        {
            main.showEnableOperator();
        }
    }

}
