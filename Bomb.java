/**
 * Bomb is a FieldObject which explodes after a period of time, possibly dealing damage to the player.
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
public class Bomb extends FieldObject implements Runnable
{
    BufferedImage[] explosion;
    Thread thread;
    int direction;
    int numSquares;
    boolean exploded;
    public Bomb(int xPos, int yPos, BufferedImage icon, int direction, BufferedImage ground, FieldObject[][] objectMatrix, BufferedImage[][] gridDisplay, Player player)
    {
        super(xPos, yPos, icon, true, ground, objectMatrix, gridDisplay, player);
        this.direction = direction;
        this.numSquares = numSquares;
        this.exploded = false;
        explosion = new BufferedImage[9];
        try
        {
            explosion[0] = ImageIO.read(new File("images/explosion0.jpg"));
            explosion[1] = ImageIO.read(new File("images/explosion1.jpg"));
            explosion[2] = ImageIO.read(new File("images/explosion2.jpg"));
            explosion[3] = ImageIO.read(new File("images/explosion3.jpg"));
            explosion[4] = ImageIO.read(new File("images/explosion4.jpg"));
            explosion[5] = ImageIO.read(new File("images/explosion5.jpg"));
            explosion[6] = ImageIO.read(new File("images/explosion6.jpg"));
            explosion[7] = ImageIO.read(new File("images/explosion7.jpg"));
            explosion[8] = ImageIO.read(new File("images/explosion8.jpg"));
        }
        catch (IOException e)
        {}
        thread = new Thread(this, "Bomb actions");
        thread.start();
    }

    /**
     * Thread moves the bomb until it hits an impassable object, at which point it explodes after 1.5 seconds
     * and hurts the player if they are within 3 x 3 around the bomb
     */
    public void run() 
    {
        try
        {
            moveUntilStop(direction);
            Thread.sleep(1500);
            gridDisplay[xPos - 1][yPos - 1] = explosion[0];
            gridDisplay[xPos][yPos - 1] = explosion[1];
            gridDisplay[xPos + 1][yPos - 1] = explosion[2];
            gridDisplay[xPos + 1][yPos] = explosion[3];
            gridDisplay[xPos + 1][yPos + 1] = explosion[4];
            gridDisplay[xPos][yPos + 1] = explosion[5];
            gridDisplay[xPos - 1][yPos + 1] = explosion[6];
            gridDisplay[xPos - 1][yPos] = explosion[7];
            gridDisplay[xPos][yPos] = explosion[8];
            if (player.xPos <= xPos + 1 && player.xPos >=xPos - 1 && player.yPos <= yPos + 1 && player.yPos >= yPos - 1)              
            {
                player.health -= 1;
            }
            Thread.sleep(300);
            gridDisplay[xPos - 1][yPos - 1] = objectMatrix[xPos - 1][yPos - 1].icon;
            gridDisplay[xPos][yPos - 1] = objectMatrix[xPos][yPos - 1].icon;
            gridDisplay[xPos + 1][yPos - 1] = objectMatrix[xPos + 1][yPos - 1].icon;
            gridDisplay[xPos + 1][yPos] = objectMatrix[xPos + 1][yPos].icon;
            gridDisplay[xPos + 1][yPos + 1] = objectMatrix[xPos + 1][yPos + 1].icon;
            gridDisplay[xPos][yPos + 1] = objectMatrix[xPos][yPos + 1].icon;
            gridDisplay[xPos - 1][yPos + 1] = objectMatrix[xPos - 1][yPos + 1].icon;
            gridDisplay[xPos - 1][yPos] = objectMatrix[xPos - 1][yPos].icon;
            new Ground(xPos, yPos, ground, objectMatrix, gridDisplay, player);
            exploded = true;
            xPos = 0;
            yPos = 0;
            direction = 0;
            numSquares = 0;
        }
        catch (InterruptedException e)
        {
            System.out.print("Bomb thread interrupted");
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }
    }
}
