import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.*;

public class Main extends JPanel implements KeyListener {

    public JFrame frame;
    public int objectHeight = 20;
    public int windowSize = 500;
    public int playerX = 250,playerY=250;
    public boolean isRunning = true;

    public void renderPlayer(){
        frame.getGraphics().setColor(Color.GREEN);
        frame.getGraphics().fillRect(playerX, playerY, objectHeight, objectHeight);
    }

    public void renderMonster(int x,int y) {
        frame.getGraphics().setColor(Color.RED);
        frame.getGraphics().fillRect(x, y, objectHeight, objectHeight);

        try {
            Thread.sleep(250);
        }catch (InterruptedException e){

        }
        if (playerX < x + objectHeight &&
                playerX +objectHeight > x &&
                playerY < y + objectHeight &&
                objectHeight + playerY> y) {
            // collision detected!
            System.exit(0);
        }
    }

    public Main(){

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Oyun");
        frame.add(this);
        frame.addKeyListener(this);
        frame.setSize(windowSize, windowSize);
        frame.setLocation(400,200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Thread.sleep(250);
        }catch (InterruptedException e){

        }
        renderPlayer();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int oldx=playerX,oldy=playerY;
        switch (e.getKeyChar()){
            case ' ':
                isRunning = true;
                break;
            case 'a':
            case 'A':
                if(playerX - 10 >= 0){
                    playerX -=10;
                }
                break;
            case 'D':
            case 'd':
                if((playerX + objectHeight) + 10 <= windowSize){
                    playerX +=10;
                }
                break;
            case 'W':
            case 'w':
                if(playerY - 10 >=0){
                    playerY -=10;
                }
                break;
            case 'S':
            case 's':
                if((playerY + objectHeight) + 35 <= windowSize){
                    playerY +=10;
                }
                break;
        }
        frame.getGraphics().clearRect(oldx, oldy, objectHeight, objectHeight);
        renderPlayer();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public class Monster extends Thread
    {
        int x, y;
        public Monster(int x , int y)
        {
            this.x = x;
            this.y = y;
            renderMonster(x,y);
        }

        @Override
        public void run() {
            while(isRunning){
                int oldx=x,oldy=y;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Random random = new Random();
                if(random.nextBoolean()){
                    if(x < playerX){
                        x+=10;
                    }else if (x > playerX){
                        x-=10;
                    }
                    else{
                        continue;
                    }
                }else{
                    if(y < playerY){
                        y+=10;
                    }else if(y > playerY){
                        y-=10;
                    }else{
                        continue;
                    }
                }

                frame.getGraphics().clearRect(oldx, oldy, objectHeight, objectHeight);
                renderMonster(x,y);
            }
        }
    }

    public static void main(String[] args) {

        int number_of_monsters = Integer.parseInt(args[0]);
        System.out.println(number_of_monsters);
        Main m = new Main();

        Monster [] monsters = new Monster[number_of_monsters];

        Random r = new Random();

        for(int i=0;i<number_of_monsters;i++)
        {
            monsters[i] = m.new Monster(Math.abs(r.nextInt()%500),Math.abs(r.nextInt()%500));
        }

        for(int i=0;i<number_of_monsters;i++)
            monsters[i].start();

        try {
            for(int i=0;i<number_of_monsters;i++)
                monsters[i].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
