/**
 * Wall is a field object which blocks player movement.
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
public class Wall extends FieldObject
{
    public Wall(int xPos, int yPos, BufferedImage icon, BufferedImage ground, FieldObject[][] objectMatrix, BufferedImage[][] gridDisplay, Player player)
    {
        super(xPos, yPos, icon, false, ground, objectMatrix, gridDisplay, player);
    }
}