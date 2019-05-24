import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

class DotMath {
    private static int numDots;

    static void setDots(int num) {
        numDots = num;
    }

    static void delauneyTriangulate(ArrayList<dot> al, GraphicsContext gc) {

    }

    static void monoTriangulate(ArrayList<dot> al, GraphicsContext gc) {
        HashMap<dot, Double> numb = new HashMap<>();
        for (dot a : al) {
            numb.put(a, a.getX());
        }

        numb = sortMap(numb);
        dot[] dots = numb.keySet().toArray(new dot[numDots]);
        ArrayList<dot> T = new ArrayList<>();

        dot[] L = new dot[numDots];
        int l = 0;
        for (int i = 0; i < numDots; ++i) {
            while (l >= 2 && cross(L[l - 2], L[l - 1], dots[i]) <= 0) {
                T.add(dots[i]);
                T.add(L[l - 1]);
                T.add(L[l - 2]);
                l--;
            }
            L[l++] = dots[i];
        }

        dot[] U = new dot[numDots];
        int u = 0;
        for (int j = numDots - 2; j > 0; --j) {
            while (u >= 2 && cross(U[u - 2], U[u - 1], dots[j]) <= 0) {
                T.add(dots[j]);
                T.add(U[u - 1]);
                T.add(U[u - 2]);
                u--;
            }
            U[u++] = dots[j];
        }

        dot[] triples = new dot[T.size()];
        T.toArray(triples);
        for (int i = 2; i < triples.length; i += 3) {
            triangle(gc, triples[i], triples[i - 1], triples[i - 2]);
        }

    }

    private static void triangle(GraphicsContext gc, dot a, dot b, dot c) {
        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
        gc.strokeLine(a.getX(), a.getY(), c.getX(), c.getY());
        gc.strokeLine(b.getX(), b.getY(), c.getX(), c.getY());
    }

    private static double cross(dot O, dot A, dot B) {
        return (A.getX() - O.getX()) * (B.getY() - O.getY()) - (A.getY() - O.getY()) * (B.getX() - O.getX());
    }

    static void connect(ArrayList<dot> al, GraphicsContext gc) {
        HashMap<dot, Double> dist = new HashMap<>();

        for (dot d : al) {
            double x1 = d.getX();
            double y1 = d.getY();

            for (dot a : al) {
                if (a == d) continue;
                double x2 = a.getX();
                double y2 = a.getY();
                double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
                dist.put(a, distance);
            }

            dist = sortMap(dist);
            dot[] dots = dist.keySet().toArray(new dot[30]);
            d.addNeighbours(dots[0], dots[1], dots[2]);
            gc.fillRect(d.getX() - 1, d.getY() - 1, 2, 2);
            gc.strokeLine(d.getX() - 1,
                    d.getY() - 1,
                    d.getNeighbours()[0].getX() - 1,
                    d.getNeighbours()[0].getY() - 1);
            gc.strokeLine(d.getX() - 1,
                    d.getY() - 1,
                    d.getNeighbours()[1].getX() - 1,
                    d.getNeighbours()[1].getY() - 1);
            gc.strokeLine(d.getX() - 1,
                    d.getY() - 1,
                    d.getNeighbours()[2].getX() - 1,
                    d.getNeighbours()[2].getY() - 1);
            dist.clear();
        }
    }

    private static HashMap<dot, Double> sortMap(HashMap<dot, Double> map) {
        return map
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    static void calculate(ArrayList<dot> al, double time) {
        for (dot d : al) {
            double x1 = d.getX();
            double y1 = d.getY();

            for (dot a : al) {
                double x2 = a.getX();
                double y2 = a.getY();
                double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

                double A = Main.getA(a.getMass(), distance);
                if (distance > 10) {
                    if (x1 > x2) {
                        double newDx = d.getDx() - (A * time * (Math.abs(x2 - x1) / distance));
                        d.setDx(newDx);
                    } else {
                        double newDx = d.getDx() + (A * time * (Math.abs(x2 - x1) / distance));
                        d.setDx(newDx);
                    }
                    if (y1 > y2) {
                        double newDy = d.getDy() - (A * time * (Math.abs(y2 - y1) / distance));
                        d.setDy(newDy);
                    } else {
                        double newDy = d.getDy() + (A * time * (Math.abs(y2 - y1) / distance));
                        d.setDy(newDy);
                    }
                }
            }
        }
    }

}
