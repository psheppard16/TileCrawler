
/**
 * Holds information about objects that inhabit Fields
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
public class FieldObject
{
    int xPos;
    int yPos;
    BufferedImage icon;
    boolean canMove;
    BufferedImage ground;
    FieldObject[][] objectMatrix;
    BufferedImage[][] gridDisplay;
    Player player;
    public FieldObject(int xPos, int yPos, BufferedImage icon, boolean canMove, BufferedImage ground, FieldObject[][] objectMatrix, BufferedImage[][] gridDisplay, Player player)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.icon = icon;
        this.canMove = canMove;
        this.player = player;
        this.ground = ground;
        this.objectMatrix = objectMatrix;
        this.gridDisplay = gridDisplay;
        objectMatrix[xPos][yPos] = this;
        gridDisplay[xPos][yPos] = icon;
    }

    /**
     * moves the FieldObject in a desired direction until it hits an impassable object.
     */
    public void moveUntilStop(int direction)
    {
        try
        {
            if (direction == 0)
            {
                while (objectMatrix[xPos][yPos - 1].canMove)
                {
                    moveObject(0);
                    Thread.sleep(150);
                }
            }
            if (direction == 1)
            {
                while (objectMatrix[xPos + 1][yPos].canMove)
                {
                    moveObject(1);
                    Thread.sleep(150);
                }
            }
            if (direction == 2)
            {
                while (objectMatrix[xPos][yPos + 1].canMove)
                {
                    moveObject(2);
                    Thread.sleep(150);
                }
            }
            if (direction == 3)
            {
                while (objectMatrix[xPos - 1][yPos].canMove)
                {
                    moveObject(3);
                    Thread.sleep(150);
                }
            }
        }
        catch (InterruptedException e)
        {}
    }

    /**
     * Moves a FieldObject a certain number of squares or until it reaches an impassable object.
     */
    public void moveNumSquares(int direction, int numSquares)
    {
        try
        {
            if (direction == 0)
            {
                for(int i = 0; i < numSquares; i++)
                {
                    if (objectMatrix[xPos][yPos - 1].canMove)
                    {
                        moveObject(0);
                        Thread.sleep(120);
                    }
                }
            }
            else if (direction == 1)
            {
                for(int i = 0; i < numSquares; i++)
                {
                    if (objectMatrix[xPos][yPos + 1].canMove)
                    {
                        moveObject(1);
                        Thread.sleep(120);
                    }
                }
            }
            else if (direction == 2)
            {
                for(int i = 0; i < numSquares; i++)
                {
                    if (objectMatrix[xPos][yPos + 1].canMove)
                    {
                        moveObject(2);
                        Thread.sleep(120);
                    }
                }
            }
            else if (direction == 3)
            {
                for(int i = 0; i < numSquares; i++)
                {
                    if (objectMatrix[xPos][yPos - 1].canMove)
                    {
                        moveObject(3);
                        Thread.sleep(120);
                    }
                }
            }
        }
        catch (InterruptedException e)
        {}
    }

    /**
     * Moves a FieldObject one square in a desired direction.
     */
    public void moveObject(int direction)
    {
        try
        {
            if (xPos == player.xPos && yPos == player.yPos)
            {
                gridDisplay[xPos][yPos] = player.playerIcon;
            }
            if(direction == 0 && objectMatrix[xPos][yPos - 1].canMove)
            {
                objectMatrix[xPos][yPos] = new Ground(xPos, yPos, ground, objectMatrix, gridDisplay, player);
                yPos -= 1;
                objectMatrix[xPos][yPos] = this;
                gridDisplay[xPos][yPos] = icon;
            }
            else if(direction == 1 && objectMatrix[xPos + 1][yPos].canMove)
            {
                objectMatrix[xPos][yPos] = new Ground(xPos, yPos, ground, objectMatrix, gridDisplay, player);   
                xPos += 1;
                objectMatrix[xPos][yPos] = this;
                gridDisplay[xPos][yPos] = icon;
            }
            else if(direction == 2 && objectMatrix[xPos][yPos + 1].canMove)
            {
                objectMatrix[xPos][yPos] = new Ground(xPos, yPos, ground, objectMatrix, gridDisplay, player);   
                yPos += 1;
                objectMatrix[xPos][yPos] = this;
                gridDisplay[xPos][yPos] = icon;
            }
            else if(direction == 3 && objectMatrix[xPos - 1][yPos].canMove)
            {
                objectMatrix[xPos][yPos] = new Ground(xPos, yPos, ground, objectMatrix, gridDisplay, player);   
                xPos -= 1;
                objectMatrix[xPos][yPos] = this;
                gridDisplay[xPos][yPos] = icon;
            }
            if (xPos == player.xPos && yPos == player.yPos)
            {
                gridDisplay[xPos][yPos] = player.playerIcon;
            }
        }
        catch (IndexOutOfBoundsException g)
        {
            System.out.println("You can't move there");
            System.out.println(xPos);
            System.out.println(yPos);
        }
    }
}

