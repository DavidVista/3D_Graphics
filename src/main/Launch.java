package main;

import geom.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

public final class Launch extends Application {

    private static final Logger logger = Logger.getLogger(Launch.class.getName());

    public static void main(String[] args) {
        setupLogger();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        final int SIZE_X = 640;
        final int SIZE_Y = 640;
        Point3D.MAX_X = SIZE_X;
        Point3D.MAX_Y = SIZE_Y;

        Canvas canvas = new Canvas();
        canvas.setWidth(SIZE_X);
        canvas.setHeight(SIZE_Y);

        float rotationX = 1f;
        float rotationY = 0f;

        Space room = new Space(rotationX, rotationY);
        List<Point3D> list = new ArrayList<>();
        for (float t = (float) -Math.PI / 2; t <= Math.PI / 2; t += 0.01f) {
            for (float k = 0; k <= Math.PI * 2; k += 0.01f) {
                Vector3D point = new Vector3D(t, k);
                point.scale(300);
                list.add(point);
            }
        }

        Group root = new Group();
        Scene scene = new Scene(root, SIZE_X, SIZE_Y);
        root.getChildren().add(canvas);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                logger.info("The key " + event.getCode() + " has been pressed!");
                switch (event.getCode()) {
                    case LEFT:
                        room.movePolar(-0.1f);
                        canvas.getGraphicsContext2D().clearRect(0, 0, SIZE_X, SIZE_Y);
                        render(canvas.getGraphicsContext2D(), room, list);
                        break;
                    case RIGHT:
                        room.movePolar(0.1f);
                        canvas.getGraphicsContext2D().clearRect(0, 0, SIZE_X, SIZE_Y);
                        render(canvas.getGraphicsContext2D(), room, list);
                        break;
                    case UP:
                        room.moveAzimuth(-0.1f);
                        canvas.getGraphicsContext2D().clearRect(0, 0, SIZE_X, SIZE_Y);
                        render(canvas.getGraphicsContext2D(), room, list);
                        break;
                    case DOWN:
                        room.moveAzimuth(0.1f);
                        canvas.getGraphicsContext2D().clearRect(0, 0, SIZE_X, SIZE_Y);
                        render(canvas.getGraphicsContext2D(), room, list);
                        break;
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
        logger.info("The scene has been set up.");

        render(canvas.getGraphicsContext2D(), room, list);
    }

    private static void render(GraphicsContext context, Space space, Collection<Point3D> collection) {
        context.setFill(Color.BLACK);
        for (Point3D p : collection) {
            if (space.getDisplayPlane().distanceToPoint(p) > 0) {
                Point3D q = space.pointProjection(p);
                context.fillRect(320f + q.getX(), 320f + q.getY(), 1, 1);
            }
        }
        logger.info("The canvas has been updated!");
    }

    private static void setupLogger() {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.SEVERE);
        ch.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });
        logger.addHandler(ch);

        try {
            FileHandler fh = new FileHandler(Launch.class.getName() + ".log", true);
            fh.setLevel(Level.FINE);
            fh.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$s] %3$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName().charAt(0),
                            lr.getMessage()
                    );
                }
            });
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}

