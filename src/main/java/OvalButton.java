import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OvalButton extends JButton {

    private Color colorNormal;
    private Color colorHighlighted;
    private Color colorBorderNormal;
    private Color colorBorderHighlighted;

    public OvalButton() {
        this("");
    }

    public OvalButton(String text) {
        super(text);
        colorNormal = Color.WHITE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension d = getSize();

        g.fillOval(0, 0, d.width, d.height);
        g.setColor(colorNormal);
    }

    @Override
    public void addActionListener(ActionListener l) {
        super.addActionListener(e -> {
            Point p = MouseInfo.getPointerInfo().getLocation();
            if (isInRange(p.x, p.y)) {
                l.actionPerformed(e);
            }
        });
    }

    private boolean isInRange(int x, int y) {
        // Calculate centre of the ellipse.
        double s1 = getLocationOnScreen().x + getSize().width / 2;
        double s2 = getLocationOnScreen().y + getSize().height / 2;

        // Calculate semi-major and semi-minor axis
        double a = getSize().width / 2;
        double b = getSize().height / 2;

        // Check if the given point is withing the specified ellipse:
        return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;


    }



}
