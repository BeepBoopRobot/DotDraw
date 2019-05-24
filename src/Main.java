import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        new JFXPanel();
        Platform.runLater(Main::launch);
        // Platform.runLater(Main::control);
        DotMath.setDots(numDots);
    }

    private static void close() {
        System.exit(0);
    }

    private static int windowWidth = 1000, windowHeight = 1000;
    private static ArrayList<dot> al;
    private static boolean connect = false;
    private static boolean monoTriangulate = true;
    private static boolean delauneyTriangulate = false;
    private static int numDots = 50;

    private static void launch() {
        Stage stage = new Stage();
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
        stage.setResizable(false);
         stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        Group group = new Group();
        Scene scene = new Scene(group);
        stage.setScene(scene);

        scene.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.ESCAPE) {
                close();
            } else if(ke.getCode() == KeyCode.R && !monoTriangulate && !delauneyTriangulate) {
                connect = !connect;
            } else if(ke.getCode() == KeyCode.T && !connect && !delauneyTriangulate) {
                monoTriangulate = !monoTriangulate;
            } else if(ke.getCode() == KeyCode.Y && !connect && !monoTriangulate) {
                delauneyTriangulate = !delauneyTriangulate;
            }
        });

        Canvas canvas = new Canvas();
        canvas.setWidth(windowWidth);
        canvas.setHeight(windowHeight);
        group.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        al = new ArrayList<>();
        for (int i = 0; i < numDots; i++) {
            al.add(new dot(
                    Math.ceil(Math.random() * windowWidth),
                    Math.ceil(Math.random() * windowHeight),
                    Math.random() * 4 - 2,
                    Math.random() * 4 - 2,
                    100.0));
        }

        int framerate = 60;
        AnimationTimer at = new AnimationTimer() {
            private long last;
            private double frametime = (1.0 / framerate);

            @Override
            public void handle(long now) {
                if (now - last >= (frametime * 1_000_000)) {
                    draw(gc, al, frametime);
                    update(al);
                    last = now;
                }
            }
        };

        at.start();

        scene.setOnMousePressed(me -> at.stop());
        scene.setOnMouseReleased(me -> at.start());
    }

    private static void update(ArrayList<dot> al) {
        for (dot d : al) {
            d.update(windowWidth, windowHeight);
        }
    }

    private static void draw(GraphicsContext gc, ArrayList<dot> al, double time) {
        gc.clearRect(0, 0, windowWidth, windowHeight);
        drawDots(al, gc);
        if(connect) DotMath.connect(al, gc);
        if(monoTriangulate) DotMath.monoTriangulate(al, gc);
        if(delauneyTriangulate) DotMath.delauneyTriangulate(al, gc);
        DotMath.calculate(al, time);
    }

    private static void drawDots(ArrayList<dot> al, GraphicsContext gc) {
        for (dot d : al) {
            gc.fillRect(d.getX() - 2, d.getY() - 2, 4, 4);
        }
    }

    private static double constant = 20;


    private static void control() {
        Stage stage = new Stage();
        stage.setWidth(200);
        stage.setHeight(300);
        stage.setResizable(false);
        stage.setOnCloseRequest(we -> close());
        stage.show();

        Group group = new Group();
        Scene scene = new Scene(group);
        stage.setScene(scene);

        VBox vb = new VBox();
        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 10, 10, 10));
        group.getChildren().add(vb);

        TextField constantText = new TextField();
        constantText.setMaxWidth(180);
        constantText.setPromptText("constant");

        Button applyConst = new Button("Apply Const");
        applyConst.setOnAction((ActionEvent se) -> {
            if (constantText.getText().length() != 0) {
                setConstant(Double.valueOf(constantText.getText()));
                constantText.clear();
            }
        });

        TextField massText = new TextField();
        massText.setMaxWidth(180);
        massText.setPromptText("mass");

        Button applyMass = new Button("Apply Mass");
        applyMass.setOnAction((ActionEvent se) -> {
            if (massText.getText().length() != 0) {
                applyMass(al, Double.valueOf(massText.getText()));
                massText.clear();
            }
        });

        vb.getChildren().addAll(constantText, applyConst, massText, applyMass);
    }

    private static void setConstant(double newConstant) {
        constant = newConstant;
        System.out.println(constant);
    }

    static double getA(double mass, double distance) {
        double top = constant * mass * -1;
        double bottom = distance * distance;
        return top / bottom;
    }

    private static void applyMass(ArrayList<dot> al, double mass) {
        for (dot d : al) {
            d.setMass(mass);
            System.out.println(d.getMass());
        }
    }
}
