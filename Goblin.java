
/**
 * Goblin is a Field object that is an enemy of the player
 * 
 * @author (your name) 
 * @version (a version number or a date)
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
public class Goblin extends FieldObject
{
    boolean alive;
    int health;
    BufferedImage coinIcon;
    Field field;
    public Goblin(int xPos, int yPos, BufferedImage icon, int health, BufferedImage ground, FieldObject[][] objectMatrix, Player player, BufferedImage[][] gridDisplay, Field field)
    {
        super(xPos, yPos, icon, false, ground, objectMatrix, gridDisplay, player);
        alive = true;
        this.field = field;
        try
        {
            coinIcon = ImageIO.read(new File("images/coin.jpg"));
        }
        catch (IOException e)
        {
            System.out.println("File read error");  
        }
        this.health = health;
    }

    /**
     * Removes the object from the field and sets variables to null.
     */
    public void killObject()
    {
        field.addCoinListElement(xPos, yPos);
        xPos = 0;
        yPos = 0;
        icon = null;
        canMove = true;
        ground = null;
    }
}