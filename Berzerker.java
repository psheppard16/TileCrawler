
/**
 * Bezerker is a Goblin that charges at the player
 * 
 * @author Preston Sheppard
 * @version 6.0
 */
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
public class Berzerker extends Goblin implements Runnable
{
    Thread thread;
    Player player;
    public Berzerker(int xPos, int yPos, BufferedImage icon, BufferedImage ground, FieldObject[][] objectMatrix, Player player, BufferedImage[][] gridDisplay, Field field)
    {
        super(xPos, yPos, icon, 1, ground, objectMatrix, player, gridDisplay, field);
        this.player = player;
        thread = new Thread(this, "Berzerker action");
        thread.start();
    }

    /**
     * Thread checks player position
     * if player is in range the bezerker charges
     * if bezerker health goes bellow 1 bezerker deletes itself from Field
     */
    public void run() 
    {
        try 
        {
            while(alive)
            {
                if(health < 1)
                {
                    alive = false;
                    killObject();
                }
                else
                {
                    Thread.sleep(500);
                    if(xPos >= player.xPos - 1 && xPos <= player.xPos + 1 && Math.abs(player.yPos - yPos) < 6)
                    {
                        if(yPos > player.yPos)
                        {
                            moveNumSquares(0, 6);
                            moveObject(Utility.random(0,3));
                        }
                        else if(yPos < player.yPos)
                        {
                            moveNumSquares(2, 6);
                            moveObject(Utility.random(0,3));
                        }
                    }
                    else if(yPos >= player.yPos - 1 && yPos <= player.yPos + 1 && Math.abs(player.xPos - xPos) < 6)
                    {
                        if(xPos > player.xPos)
                        {
                            moveNumSquares(3, 7);
                            moveObject(Utility.random(0,3));
                        }
                        else if(xPos < player.xPos)
                        {
                            moveNumSquares(1, 7);
                            moveObject(Utility.random(0,3));
                        }
                    }      
                }
            }
            if (icon != null)
            {
                killObject();
            }
        }
        catch (InterruptedException e)
        {}
    }
}
