import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Pomodoro extends JPanel{
  private Font mainFont;
  private boolean timerRunning = false;
  private Timer currentTimer;
  private int minutesLeft = 25;
  private int secondsLeft = 00;
  private JFrame frame;
  private MouseEvent pressed;
  private Point location;
  
  @Override
  public void paint(Graphics g){
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d.setFont(mainFont);
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0,0,getWidth(),getHeight());
    g2d.setColor(Color.BLACK);
    int width = g.getFontMetrics().stringWidth(minutesLeft + ":" + String.format("%02d", secondsLeft));
    g2d.drawString(minutesLeft + ":" + String.format("%02d", secondsLeft),200 - width/2,200);
    g2d.setColor(Color.RED);
    double progress = (1500-(minutesLeft*60 + secondsLeft))*0.2666666666666 + 0.0000000001;
    g2d.fillRect(0,396,(int)(progress),4);
  }
  
  
  public Pomodoro(JFrame frame){
    this.frame = frame;
    
    frame.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }
      @Override
      public void keyReleased(KeyEvent e) {
      }
      @Override
      public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Q){
          System.exit(0);
        }
      }
       });
    
    addMouseListener(new MouseListener(){
      
      @Override
      public void mouseClicked(MouseEvent e){
        if(timerRunning == false){
          if(secondsLeft == 0 && minutesLeft == 0){
            minutesLeft = 25;
          }
          timerRunning = true;
          currentTimer = new Timer();
          currentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
              secondsLeft--;
              if(secondsLeft == -1){
                minutesLeft--;
                secondsLeft = 59;
              }
              
              if(secondsLeft == 0 && minutesLeft == 0){
                currentTimer.cancel();
                currentTimer.purge();
                timerRunning = false;
                    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("notif.wav").getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch(Exception e) {
        System.out.println("Error with playing sound.");
    }
              }
            }
          }, 0, 1000);
        }
      }
      
      @Override
      public void mouseEntered(MouseEvent e) {}  
      @Override
      public void mouseExited(MouseEvent e) {}  
      @Override
      public void mousePressed(MouseEvent e) {
        pressed = e;
      }  
      @Override
      public void mouseReleased(MouseEvent e) {}  
    });
    
    addMouseMotionListener(new MouseMotionListener(){
      @Override
      public void mouseMoved(MouseEvent e){
        
      }
      @Override
      public void mouseDragged(MouseEvent e){

        location = frame.getLocation(location);
        int x = location.x - pressed.getX() + e.getX();
        int y = location.y - pressed.getY() + e.getY();
        frame.setLocation(x, y);
      }
    });
    
    
    try{
      Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File("Anonymous Pro B.ttf")).deriveFont(Font.PLAIN, 30);
      mainFont = newFont;
    }catch(Exception e){
      System.out.println("Font Loading Error");
    }
    
    setBackground(Color.WHITE);
    setFont(mainFont);
    
    setDoubleBuffered(true);
    
  }
  
  public static void main(String[] args){
    JFrame frame = new JFrame("pomodoro");
    Pomodoro p = new Pomodoro(frame);
    frame.add(p);
    frame.setPreferredSize(new Dimension(400,400));
    frame.setUndecorated(true);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setTitle("Pomodoro");
    frame.setVisible(true);  
    
    while(true){
      p.repaint();
      try{
       Thread.sleep(400);
      }catch(Exception e){
      }
    }
  }
}