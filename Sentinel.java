
/**
 * Sentinel is a Goblin which shoots bombs in random directions
 * unless player is in line of sight, then it shoots a bomb towards player
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
public class Sentinel extends Goblin implements Runnable
{
    Bomb bomb;
    int direction;
    ArrayList<Bomb> bombList;
    public Sentinel(int xPos, int yPos,  BufferedImage icon, BufferedImage ground, FieldObject[][] objectMatrix, Player player, BufferedImage[][] gridDisplay, ArrayList<Bomb> bombList, Field field)
    {
        super(xPos, yPos, icon, 3, ground, objectMatrix, player, gridDisplay, field);
        this.bombList = bombList;
        new Thread(this, "sentinel actions").start();
    }  

    /**
     * Thread checks to see if player is in line of sight
     * if not picks random direction and shoots a bomb.
     * In addition, checks if sentinel has died
     * if so, removes Sentinel from Field.
     */
    public void run() 
    {
        try
        {
            while(alive)
            {
                p: for (int i = 0; i < 15; i++)
                {
                    objectMatrix[xPos][yPos] = this;
                    gridDisplay[xPos][yPos] = icon;
                    Thread.sleep(100);
                    if (health < 1)
                    {
                        break p;
                    }
                }
                if(health < 1)
                {
                    alive = false;
                    killObject();
                }
                else
                {
                    if(yPos == player.yPos && xPos == player.xPos - 1)
                    {
                        direction = 1;
                    }
                    else if(yPos == player.yPos && xPos == player.xPos + 1)
                    {
                        direction = 3;
                    }
                    else if(xPos >= player.xPos - 1 && xPos <= player.xPos + 1)
                    {
                        if(yPos > player.yPos)
                        {
                            direction = 0;
                        }
                        else if(yPos < player.yPos)
                        {
                            direction = 2;
                        }
                    }
                    else if(yPos >= player.yPos - 1 && yPos <= player.yPos + 1)
                    {
                        if(xPos > player.xPos)
                        {
                            direction = 3;
                        }
                        else if(xPos < player.xPos)
                        {
                            direction = 1;
                        }
                    }
                    else
                    {
                        direction = Utility.random(0, 3);
                    }
                    try
                    {
                        bombList.add(new Bomb(xPos, yPos, ImageIO.read(new File("images/bomb.jpg")), direction, ground, objectMatrix, gridDisplay, player));
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
            if (icon != null)
            {
                killObject();
            }
        } catch (InterruptedException e)
        {
            System.out.println("Sentinel loop interrupted");
        }
    }
}
