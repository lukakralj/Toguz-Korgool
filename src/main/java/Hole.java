import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Hole extends OvalButton {

    private List<Korgool> korgools;


    public Hole() {
        korgools = new ArrayList<>();
        setLayout(null);
    }

    private Dimension calculateKorgoolSize() {
        double factor = 5;
        double diameter = (getSize().width < getSize().height) ? getSize().width / factor : getSize().height / factor;
        return new Dimension((int)diameter, (int)diameter);
    }

    private Point calculateKorgoolLocation() {
        return new Point(30, 30);
    }

    public void addKorgool(Korgool toAdd) {
        add(toAdd);
        korgools.add(toAdd);
        toAdd.setParent(this);
        toAdd.setSize(calculateKorgoolSize());
        toAdd.setLocation(calculateKorgoolLocation());
    }

    public int getKorgools() {
        return -1;
    }

    public void setKorgools() {

    }

    public void setTuz(boolean isTuz) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Korgool k : korgools) {
            k.setSize(calculateKorgoolSize());
            k.setLocation(calculateKorgoolLocation());
        }
    }
}
