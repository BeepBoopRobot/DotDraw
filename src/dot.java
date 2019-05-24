public class dot {
    private double x, y, dx, dy, mass;
    private dot[] neighbours;

    dot(double x, double y, double dx, double dy, double mass) {
        this.dx = dx;
        this.dy = dy;
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.neighbours = new dot[3];
    }

    void addNeighbours(dot n1, dot n2, dot n3) {
        neighbours[0] = n1;
        neighbours[1] = n2;
        neighbours[2] = n3;
    }

    dot[] getNeighbours() {
        return neighbours;
    }

    void update(int maxWidth, int maxHeight) {
        double a = x + dx;
        double b = y + dy;
        int oob = 10;
        if (a > maxWidth + oob) {
            x = -oob;
        } else if (a < -oob) {
            x = maxWidth + oob;
        } else {
            x = a;
        }

        if (b > maxHeight + oob) {
            y = -oob;
        } else if (b < -oob) {
            y = maxHeight + oob;
        } else {
            y = b;
        }
    }

    double getX() {
        return x;
    }

    double getMass() {
        return mass;
    }

    void setMass(double mass) {
        this.mass = mass;
    }

    public void setX(double x) {
        this.x = x;
    }

    double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    double getDx() {
        return dx;
    }

    void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }
}
