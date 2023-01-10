import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
public class GCell extends JComponent{
    Color m_color;
    boolean m_state;
    public GCell(boolean state) {
        m_state = state;
        if (state == true) {
            m_color = Color.GREEN;
        }
        else {
            m_color = Color.DARK_GRAY;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle r=getBounds();
        g.clearRect(0, 0, r.width, r.height);

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, r.width, r.height);

        g2.setColor(m_color);
        g2.fillRect(r.width/10, r.height/10, (int)Math.ceil(r.width * 0.9), (int)Math.ceil(r.height * 0.9));
    }

    public void changeState() {
        m_state = !m_state;
        if (m_state == true) {
            m_color = Color.GREEN;
        }
        else {
            m_color = Color.DARK_GRAY;
        }
        super.repaint();
    }

    public void setState(boolean state) {
        if(m_state != state) {
            changeState();
        }
    }
}
