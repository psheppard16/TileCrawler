
/**
 * Grunt is a Goblin that moves towards the player.
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
public class Grunt extends Goblin implements Runnable
{
    Thread thread;
    Player player;
    public Grunt(int xPos, int yPos, BufferedImage icon, BufferedImage ground, FieldObject[][] objectMatrix, Player player, BufferedImage[][] gridDisplay, Field field)
    {
        super(xPos, yPos, icon, 2, ground, objectMatrix, player, gridDisplay, field);
        this.player = player;
        thread = new Thread(this, "Grunt action");
        thread.start();
    }

    /**
     * Thread checks players position and then moves towards player
     */
    public void run() 
    {
        try 
        {
            while(alive)
            {
                Thread.sleep(1200);
                if(health < 1)
                {
                    alive = false;
                    killObject();
                }
                else
                {
                    if(yPos == player.yPos && xPos == player.xPos - 1)
                    {
                        moveObject(1);
                    }
                    else if(yPos == player.yPos && xPos == player.xPos + 1)
                    {
                        moveObject(3);
                    }
                    else if(xPos >= player.xPos - 1 && xPos <= player.xPos + 1)
                    {
                        if(yPos > player.yPos)
                        {
                            moveObject(0);
                        }
                        else if(yPos < player.yPos)
                        {
                            moveObject(2);
                        }
                    }
                    else if(yPos >= player.yPos - 1 && yPos <= player.yPos + 1)
                    {
                        if(xPos > player.xPos)
                        {
                            moveObject(3);
                        }
                        else if(xPos < player.xPos)
                        {
                            moveObject(1);
                        }
                    }
                    else
                    {
                        if(yPos > player.yPos - 1 && xPos < player.xPos - 1)
                        {
                            moveObject(0);
                            moveObject(1);
                        }
                        else if(yPos > player.yPos - 1 && xPos > player.xPos + 1)
                        {
                            moveObject(0);
                            moveObject(3);
                        }
                        else if(yPos < player.yPos + 1 && xPos < player.xPos - 1)
                        {
                            moveObject(2);
                            moveObject(1);
                        }
                        else if(yPos < player.yPos + 1 && xPos > player.xPos + 1)
                        {
                            moveObject(2);
                            moveObject(3);
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

