import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class template extends Canvas implements Runnable {
    private BufferStrategy bs;

    private boolean running = false;
    private Thread thread;

    private BufferedImage roger;
    private int rogerX = 100;
    private int rogerY = 100;
    private int rogerVX = 0;
    private int rogerVY = 0;


    public template() {
        try {
            roger = ImageIO.read(new File("roger_png.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSize(600, 400);
        JFrame frame = new JFrame();
        frame.add(this);
        frame.addKeyListener(new MyKeyListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void render() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Rita ut den nya bilden
        draw(g);

        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawImage(roger, rogerX, rogerY, roger.getWidth() / 4, roger.getHeight() / 4, null);
    }



    public static void main(String[] args) {
        template minGrafik = new template();
        minGrafik.start();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        double ns = 1000000000.0 / 25.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med updaterad data
                render();
                delta--;
            }
        }
        stop();
    }

    private void update() {
        rogerX += rogerVX;
        rogerY += rogerVY;

    }

    public class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                rogerVX = -3;
            }
            if (e.getKeyChar() == 'd') {
                rogerVX = 3;
            }
            if (e.getKeyChar() == 'w') {
                rogerVY = -3;
            }
            if (e.getKeyChar() == 's') {
                rogerVY = 3;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                rogerVX = 0;
            }
            if (e.getKeyChar() == 'd') {
                rogerVX = 0;
            }
            if (e.getKeyChar() == 'w') {
                rogerVY = 0;
            }
            if (e.getKeyChar() == 's') {
                rogerVY = 0;
            }
        }
    }
}