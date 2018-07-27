/**
 * Ground is a FieldObject that is passable.
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
public class Ground extends FieldObject
{
    public Ground(int xPos, int yPos, BufferedImage ground, FieldObject[][] objectMatrix, BufferedImage[][] gridDisplay, Player player)
    {
        super(xPos, yPos, ground, true, ground, objectMatrix, gridDisplay, player);
    }
}

