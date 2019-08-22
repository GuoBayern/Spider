import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class Spider extends JFrame {

    static final int EASY = 1;
    static final int NATURAL = 2;
    static final int HARD = 3;

    private int grade = Spider.EASY;
    private Container pane = null;
    private PKCard[] cards = new PKCard[104];
    private int c = 0;
    private int n = 0;
    private int a = 0;
    private int finish = 0;
    @SuppressWarnings("rawtypes")
    Hashtable table = null;
    private JLabel[] groundLabel = null;

    public static void main(String[] args)
    {
        Spider spider = new Spider();
        spider.setVisible(true);
    }

    @SuppressWarnings("rawtypes")
    private Spider()
    {
        Font font = new Font("Dialog", Font.PLAIN, 12);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
            {
                UIManager.put(key, font);
            }
        }
        setTitle("蜘蛛纸牌 Design By Guo");
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setSize(1024, 742);
        setJMenuBar(new SpiderMenuBar(this));
        pane = this.getContentPane();
        pane.setBackground(new Color(0, 112, 26));
        pane.setLayout(null);
        JLabel clickLabel = new JLabel();
        clickLabel.setBounds(883, 606, 121, 96);
        pane.add(clickLabel);
        clickLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseReleased(MouseEvent me)
            {
                if (c < 60)
                {
                    Spider.this.deal();
                }
            }
        });
        this.initCards();
        this.randomCards();
        this.setCardsLocation();
        groundLabel = new JLabel[10];
        int x = 20;
        for (int i = 0; i < 10; i++)
        {
            groundLabel[i] = new JLabel();
            groundLabel[i].setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            groundLabel[i].setBounds(x, 25, 71, 96);
            x += 101;
            this.pane.add(groundLabel[i]);
        }
        this.setVisible(true);
        this.deal();
        this.addKeyListener(new KeyAdapter()
        {
            class Show extends Thread
            {
                public void run()
                {
                    Spider.this.showEnableOperator();
                }
            }
            public void keyPressed(KeyEvent e)
            {
                if (finish != 8) if (e.getKeyCode() == KeyEvent.VK_D && c < 60)
                {
                    Spider.this.deal();
                }
                else if (e.getKeyCode() == KeyEvent.VK_M)
                {
                    new Show().start();
                }
            }
        });
    }

    void newGame()
    {
        this.randomCards();
        this.setCardsLocation();
        this.setGroundLabelZOrder();
        this.deal();
    }

    int getC()
    {
        return c;
    }

    void setGrade(int grade)
    {
        this.grade = grade;
    }

    void initCards()
    {
        if (cards[0] != null)
        {
            for (int i = 0; i < 104; i++)
            {
                pane.remove(cards[i]);
            }
        }
        int n = 0;
        if (this.grade == Spider.EASY)
        {
            n = 1;
        }
        else if (this.grade == Spider.NATURAL)
        {
            n = 2;
        }
        else
        {
            n = 4;
        }
        for (int i = 1; i <= 8; i++)
        {
            for (int j = 1; j <= 13; j++)
            {
                cards[(i - 1) * 13 + j - 1] = new PKCard((i % n + 1) + "-" + j,this);
            }
        }
        this.randomCards();
    }

    private void randomCards()
    {
        PKCard temp = null;
        for (int i = 0; i < 52; i++)
        {
            int a = (int) (Math.random() * 104);
            int b = (int) (Math.random() * 104);
            temp = cards[a];
            cards[a] = cards[b];
            cards[b] = temp;
        }
    }

    void setNA()
    {
        a = 0;
        n = 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setCardsLocation()
    {
        table = new Hashtable();
        c = 0;
        finish = 0;
        n = 0;
        a = 0;
        int x = 883;
        int y = 580;
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                int n = i * 10 + j;
                pane.add(cards[n]);
                cards[n].turnRear();
                cards[n].moveto(new Point(x, y));
                table.put(new Point(x, y), cards[n]);
            }
            x += 10;
        }
        x = 20;
        y = 45;
        for (int i = 10; i > 5; i--)
        {
            for (int j = 0; j < 10; j++)
            {
                int n = i * 10 + j;
                if (n >= 104) continue;
                pane.add(cards[n]);
                cards[n].turnRear();
                cards[n].moveto(new Point(x, y));
                table.put(new Point(x, y), cards[n]);
                x += 101;
            }
            x = 20;
            y -= 5;
        }
    }

    void showEnableOperator()
    {
        int x = 0;
        out: while (true)
        {
            Point point = null;
            PKCard card = null;
            do{
                if (point != null)
                {
                    n++;
                }
                point = this.getLastCardLocation(n);
                while (point == null)
                {
                    point = this.getLastCardLocation(++n);
                    if (n == 10) n = 0;
                    x++;
                    if (x == 10) break out;
                }
                card = (PKCard) this.table.get(point);
            }
            while (!card.isCardCanMove());
            while (this.getPreviousCard(card)!= null && this.getPreviousCard(card).isCardCanMove())
            {
                card = this.getPreviousCard(card);
            }
            if (a == 10)
            {
                a = 0;
            }
            for (; a < 10; a++)
            {
                if (a != n)
                {
                    Point p = null;
                    PKCard c = null;
                    do
                    {
                        if (p != null)
                        {
                            a++;
                        }
                        p = this.getLastCardLocation(a);
                        int z = 0;
                        while (p == null)
                        {
                            p = this.getLastCardLocation(++a);
                            if (a == 10) a = 0;
                            if (a == n) a++;
                            z++;
                            if (z == 10) break out;
                        }
                        c = (PKCard) this.table.get(p);
                    }
                    while (!c.isCardCanMove());
                    if (c.getCardValue() == card.getCardValue() + 1)
                    {
                        card.flashCard(card);
                        try
                        {
                            Thread.sleep(800);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        c.flashCard(c);
                        a++;
                        if (a == 10)
                        {
                            n++;
                        }
                        break out;
                    }
                }
            }
            n++;
            if (n == 10)
            {
                n = 0;
            }
            x++;
            if (x == 10)
            {
                break;
            }
        }
    }

    @SuppressWarnings({ "unused", "unchecked" })
    void deal()
    {
        this.setNA();
        for (int i = 0; i < 10; i++)
        {
            if (this.getLastCardLocation(i) == null)
            {
                JOptionPane.showMessageDialog(this, "有空位不能发牌！", "错误",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        int x = 20;
        for (int i = 0; i < 10; i++)
        {
            Point lastPoint = this.getLastCardLocation(i);
            if (c == 0)
            {
                lastPoint.y += 5;
            }
            else
            {
                lastPoint.y += 20;
            }
            table.remove(cards[c + i].getLocation());
            cards[c + i].moveto(lastPoint);
            table.put(new Point(lastPoint), cards[c + i]);
            cards[c + i].turnFront();
            cards[c + i].setCanMove(true);
            this.pane.setComponentZOrder(cards[c + i], 1);
            Point point = new Point(lastPoint);
            if (cards[c + i].getCardValue() == 1)
            {
                int n = cards[c + i].whichColumnAvailable(point);
                point.y -= 240;
                PKCard card = (PKCard) this.table.get(point);
                if (card != null && card.isCardCanMove())
                {
                    this.haveFinish(n);
                }
            }
            x += 101;
        }
        c += 10;
    }

    PKCard getPreviousCard(PKCard card)
    {
        Point point = new Point(card.getLocation());
        point.y -= 5;
        card = (PKCard) table.get(point);
        if (card != null)
        {
            return card;
        }
        point.y -= 15;
        card = (PKCard) table.get(point);
        return card;
    }

    PKCard getNextCard(PKCard card)
    {
        Point point = new Point(card.getLocation());
        point.y += 5;
        card = (PKCard) table.get(point);
        if (card != null)
        {
            return card;
        }
        point.y += 15;
        card = (PKCard) table.get(point);
        return card;
    }

    Point getLastCardLocation(int column)
    {
        Point point = new Point(20 + column * 101, 25);
        PKCard card = (PKCard) this.table.get(point);
        if (card == null) return null;
        while (card != null)
        {
            point = card.getLocation();
            card = this.getNextCard(card);
        }
        return point;
    }

    Point getGroundLabelLocation(int column)
    {
        return new Point(groundLabel[column].getLocation());
    }

    private void setGroundLabelZOrder()
    {
        for (int i = 0; i < 10; i++)
        {
            pane.setComponentZOrder(groundLabel[i], 105 + i);
        }
    }

    @SuppressWarnings("unchecked")
    void haveFinish(int column)
    {
        Point point = this.getLastCardLocation(column);
        PKCard card = (PKCard) this.table.get(point);
        do
        {
            this.table.remove(point);
            card.moveto(new Point(20 + finish * 10, 580));
            pane.setComponentZOrder(card, 1);
            this.table.put(card.getLocation(), card);
            card.setCanMove(false);
            point = this.getLastCardLocation(column);
            if (point == null)
            {
                card = null;
            }
            else
            {
                card = (PKCard) this.table.get(point);
            }
        }
        while (card != null && card.isCardCanMove());
        finish++;
        if (finish == 8)
        {
            JOptionPane.showMessageDialog(this, "通关！Design By Guo", "成功",
                    JOptionPane.PLAIN_MESSAGE);
        }
        if (card != null)
        {
            card.turnFront();
            card.setCanMove(true);
        }
    }

}
