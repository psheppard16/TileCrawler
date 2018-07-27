
/**
 * Coin is a FieldObject that can be collected by the player
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
public class Coin extends FieldObject implements Runnable
{
    boolean pickedUp;
    Thread thread;
    public Coin(int xPos, int yPos, BufferedImage icon, BufferedImage ground, FieldObject[][] objectMatrix, BufferedImage[][] gridDisplay, Player player)
    {
        super(xPos, yPos, icon, true, ground, objectMatrix, gridDisplay, player);
        pickedUp = false;
        thread = new Thread(this, "Coin thread");
        thread.start();
    }

    /**
     * Thread checks to see if a player occupies the same space
     * if so, it deletes itself and gives a coin to player
     */
    public void run()
    {
        while(!pickedUp)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
            }
        }
        deleteCoin();
    }

    /**
     * removes coin from the field
     */    
    public void deleteCoin()
    {
        new Ground(xPos, yPos, ground, objectMatrix, gridDisplay, player);
        if (xPos == player.xPos && yPos == player.yPos)
        {
            gridDisplay[xPos][yPos] = player.playerIcon;
        }
        else
        {
            gridDisplay[xPos][yPos] = ground;
        }
        xPos = 0;
        yPos = 0;
        icon = null;
        canMove = true;
        ground = null;
    }
}
