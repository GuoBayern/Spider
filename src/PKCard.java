import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@SuppressWarnings("serial")
public class PKCard extends JLabel implements MouseListener, MouseMotionListener {

    private Point point = null;
    private Point initPoint = null;
    private int value = 0;
    private int type = 0;
    private String name = null;
    private Container pane = null;
    private Spider main = null;
    private boolean canMove = false;
    private boolean isFront = false;
    private PKCard previousCard = null;

    public void mouseClicked(MouseEvent arg0)
    {
    }

    void flashCard(PKCard card)
    {
        new Flash(card).start();
        if(main.getNextCard(card) != null)
        {
            card.flashCard(main.getNextCard(card));
        }
    }

    static class Flash extends Thread
    {
        private PKCard card = null;

        Flash(PKCard card)
        {
            this.card = card;
        }

        public void run()
        {
            boolean is = false;
            ImageIcon icon = new ImageIcon("images/white.gif");
            for (int i = 0; i < 4; i++){
                try
                {
                    Thread.sleep(200);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                if (is)
                {
                    this.card.turnFront();
                    is = false;
                }
                else
                {
                    this.card.setIcon(icon);
                    is = true;
                }
                card.updateUI();
            }
        }
    }

    public void mousePressed(MouseEvent mp)
    {
        point = mp.getPoint();
        main.setNA();
        this.previousCard = main.getPreviousCard(this);
    }

    @SuppressWarnings("unchecked")
    public void mouseReleased(MouseEvent mr){
        Point point = ((JLabel) mr.getSource()).getLocation();
        int n = this.whichColumnAvailable(point);
        if (n == -1 || n == this.whichColumnAvailable(this.initPoint))
        {
            this.setNextCardLocation(null);
            main.table.remove(this.getLocation());
            this.setLocation(this.initPoint);
            main.table.put(this.initPoint, this);
            return;
        }
        point = main.getLastCardLocation(n);
        boolean isEmpty = false;
        PKCard card = null;
        if (point == null)
        {
            point = main.getGroundLabelLocation(n);
            isEmpty = true;
        }
        else
        {
            card = (PKCard) main.table.get(point);
        }

        if (isEmpty || (this.value + 1 == card.getCardValue()))
        {
            point.y += 40;
            if (isEmpty) point.y -= 20;
            this.setNextCardLocation(point);
            main.table.remove(this.getLocation());
            point.y -= 20;
            this.setLocation(point);
            main.table.put(point, this);
            this.initPoint = point;
            if (this.previousCard != null)
            {
                this.previousCard.turnFront();
                this.previousCard.setCanMove(true);
            }

            this.setCanMove(true);
        }
        else
        {
            this.setNextCardLocation(null);
            main.table.remove(this.getLocation());
            this.setLocation(this.initPoint);
            main.table.put(this.initPoint, this);
            return;
        }
        point = main.getLastCardLocation(n);
        card = (PKCard) main.table.get(point);
        if (card.getCardValue() == 1)
        {
            point.y -= 240;
            card = (PKCard) main.table.get(point);
            if (card != null && card.isCardCanMove())
            {
                main.haveFinish(n);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setNextCardLocation(Point point)
    {
        PKCard card = main.getNextCard(this);
        if (card != null)
        {
            if (point == null)
            {
                card.setNextCardLocation(null);
                main.table.remove(card.getLocation());
                card.setLocation(card.initPoint);
                main.table.put(card.initPoint, card);
            }
            else
            {
                point = new Point(point);
                point.y += 20;
                card.setNextCardLocation(point);
                point.y -= 20;
                main.table.remove(card.getLocation());
                card.setLocation(point);
                main.table.put(card.getLocation(), card);
                card.initPoint = card.getLocation();
            }
        }
    }

    int whichColumnAvailable(Point point)
    {
        int x = point.x;
        int y = point.y;
        int a = (x - 20) / 101;
        int b = (x - 20) % 101;
        if (a != 9)
        {
            if (b > 30 && b <= 71)
            {
                a = -1;
            }
            else if (b > 71)
            {
                a++;
            }
        }
        else if (b > 71)
        {
            a = -1;
        }
        if (a != -1)
        {
            Point p = main.getLastCardLocation(a);
            if (p == null) p = main.getGroundLabelLocation(a);
            b = y - p.y;
            if (b <= -96 || b >= 96)
            {
                a = -1;
            }
        }
        return a;
    }

    public void mouseEntered(MouseEvent arg0)
    {
    }

    public void mouseExited(MouseEvent arg0)
    {
    }

    public void mouseDragged(MouseEvent arg0)
    {
        if (canMove)
        {
            int x = 0;
            int y = 0;
            Point p = arg0.getPoint();
            x = p.x - point.x;
            y = p.y - point.y;
            this.moving(x, y);
        }
    }

    @SuppressWarnings("unchecked")
    private void moving(int x, int y)
    {
        PKCard card = main.getNextCard(this);
        Point p = this.getLocation();
        pane.setComponentZOrder(this, 1);
        main.table.remove(p);
        p.x += x;
        p.y += y;
        this.setLocation(p);
        main.table.put(p, this);
        if (card != null) card.moving(x, y);
    }

    public void mouseMoved(MouseEvent arg0)
    {
    }

    PKCard(String name, Spider spider)
    {
        super();
        this.type = Integer.parseInt(name.substring(0, 1));
        this.value = Integer.parseInt(name.substring(2));
        this.name = name;
        this.main = spider;
        this.pane = this.main.getContentPane();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setIcon(new ImageIcon("images/rear.gif"));
        this.setSize(71, 96);
        this.setVisible(true);
    }

    void turnFront()
    {
        this.setIcon(new ImageIcon("images/" + name + ".gif"));
        this.isFront = true;
    }

    void turnRear()
    {
        this.setIcon(new ImageIcon("images/rear.gif"));
        this.isFront = false;
        this.canMove = false;
    }

    void moveto(Point point)
    {
        this.setLocation(point);
        this.initPoint = point;
    }

    void setCanMove(boolean can)
    {
        this.canMove = can;
        PKCard card = main.getPreviousCard(this);
        if (card != null && card.isCardFront())
        {
            if (!can)
            {
                if (!card.isCardCanMove())
                {
                }
                else
                {
                    card.setCanMove(can);
                }
            }
            else
            {
                if (this.value + 1 == card.getCardValue()
                        && this.type == card.getCardType())
                {
                    card.setCanMove(can);
                }
                else
                {
                    card.setCanMove(false);
                }
            }
        }
    }

    private boolean isCardFront()
    {
        return this.isFront;
    }

    boolean isCardCanMove()
    {
        return this.canMove;
    }

    int getCardValue()
    {
        return value;
    }

    private int getCardType()
    {
        return type;
    }

}
