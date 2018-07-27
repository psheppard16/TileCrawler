/**
 * Pyrophile is a goblin which shoots bombs in all directions
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
public class Pyrophile extends Goblin implements Runnable
{
    Thread thread;
    ArrayList<Bomb> bombList;
    public Pyrophile(int xPos, int yPos, BufferedImage icon, BufferedImage ground, FieldObject[][] objectMatrix, Player player, BufferedImage[][] gridDisplay, ArrayList<Bomb> bombList, Field field)
    {
        super(xPos, yPos, icon, 2, ground, objectMatrix, player, gridDisplay, field);
        this.bombList = bombList;
        thread = new Thread(this, "Sentinel actions");
        thread.start();
    }

    /**
     * creates bombs moving in 4 different directions
     */
    public void newBombSet()
    {
        try
        {
            bombList.add(new Bomb(xPos, yPos, ImageIO.read(new File("images/bomb.jpg")), 0, ground, objectMatrix, gridDisplay, player));
            gridDisplay[xPos][yPos] = icon;
            Thread.sleep(5);
            bombList.add(new Bomb(xPos, yPos, ImageIO.read(new File("images/bomb.jpg")), 1, ground, objectMatrix, gridDisplay, player));
            gridDisplay[xPos][yPos] = icon;
            Thread.sleep(5);
            bombList.add(new Bomb(xPos, yPos, ImageIO.read(new File("images/bomb.jpg")), 2, ground, objectMatrix, gridDisplay, player));
            gridDisplay[xPos][yPos] = icon;
            Thread.sleep(5);
            bombList.add(new Bomb(xPos, yPos, ImageIO.read(new File("images/bomb.jpg")), 3, ground, objectMatrix, gridDisplay, player));
            gridDisplay[xPos][yPos] = icon;
            Thread.sleep(5);
        }
        catch (IOException e)
        {
            System.out.println("File read error");  
        }
        catch (InterruptedException e)
        {
        }
    }

    /**
     * Thread creates a set of bombs every 2 seconds and checks to see if it has died
     * if it has died it removes itself from the field
     */
    public void run() 
    {
        try
        {
            while(alive)
            {
                p: for (int i = 0; i < 20; i++)
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
                    newBombSet();
                    Thread.sleep(100);
                    gridDisplay[xPos][yPos] = icon;
                    objectMatrix[xPos][yPos] = this;
                }
            }
            if (icon != null)
            {
                killObject();
            }
        } catch (InterruptedException e)
        {
        }
    }
}
