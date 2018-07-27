
/**
 * Holds information about player and player related methods such as attacks
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
public class Player implements Runnable
{
    boolean alive;
    int health;
    int coinAmount;
    int mapX;
    int mapY;
    int xPos;
    int yPos;
    Field[][] map;
    Thread thread;
    Thread stabThread;
    Thread whirlThread;
    BufferedImage playerIcon;
    BufferedImage hurtPicture;
    int maxHealth;
    int stabLevel;
    int whirlLevel;
    int whirlCD;
    int stabCD;
    int whirlCoolDown;
    int stabCoolDown;
    String name;
    String password;
    String[] saveFile;
    Boolean[][] completed;
    int currentSong;
    boolean defaultSpawn;
    public Player(String[] saveFile)
    {
        currentSong = 0;
        this.saveFile = saveFile;
        completed = new Boolean[5][5];
        name = saveFile[0];
        password = saveFile[1];
        health = Integer.parseInt(saveFile[2]);
        coinAmount = Integer.parseInt(saveFile[3]);
        maxHealth = Integer.parseInt(saveFile[4]);
        stabLevel = Integer.parseInt(saveFile[5]);
        whirlLevel = Integer.parseInt(saveFile[6]);
        stabCD = Integer.parseInt(saveFile[7]);
        whirlCD = Integer.parseInt(saveFile[8]);
        alive = true;
        try
        {
            playerIcon = ImageIO.read(new File("images/knight1.jpg"));
            hurtPicture = ImageIO.read(new File("images/hurtPicture.jpg"));
        }
        catch (IOException e)
        {
            System.out.println("File read error");  
        }
        /**
         * Loads completed levels
         */
        mapX = 2;
        mapY = 1;
        xPos = 0;
        yPos = 0;
        map = new Field[5][5];
        for (int j = 0; j < 5; j ++)
        {
            for (int i = 0; i <5; i ++)
            {
                completed[i][j] = Boolean.parseBoolean(saveFile[9 + i + 4 * (j + 1)]);
            }
        }
        for (int j = 0; j < 5; j ++)
        {
            for (int i = 0; i <5; i ++)
            {
                if (completed[i][j])
                {
                    map[i][j] = new Field(map, i, j, this);
                    xPos = 0;
                    yPos = 0;
                    map[i][j].clearField();
                }
            }
        }

        map[mapX][mapY] = new Field(map, mapX, mapY, this);

        /**
         * handles the cooldowns of swordStab and whirlwind
         */
        stabThread = new Thread() 
        {
            public void run() 
            {
                for (stabCoolDown = stabCD; stabCoolDown > 0; stabCoolDown -= 50)
                {
                    try
                    {
                        Thread.sleep(50);
                        if (stabCoolDown < 0)
                        {
                            stabCoolDown = 0;
                        }
                    }
                    catch(InterruptedException e)
                    {
                    }
                } 
            }
        };
        whirlThread = new Thread() 
        {
            public void run() 
            {
                for (whirlCoolDown = whirlCD; whirlCoolDown > 0; whirlCoolDown -= 50)
                {
                    try
                    {
                        Thread.sleep(50);
                        if (whirlCoolDown < 0)
                        {
                            whirlCoolDown = 0;
                        }
                    }
                    catch(InterruptedException e)
                    {
                        System.out.println("whirl CD interrupted");
                    }
                }
            }
        };

        thread = new Thread(this, "Player actions");
        thread.start();
    }

    /**
     * while the player is alive, checks to see if enemies are overlapping with the player
     * if an enemy is, subtracts one health from player. 
     */
    public void run() 
    {
        while(alive)
        {
            try
            {
                Thread.sleep(20);
                for (int i = 0; i < map[mapX][mapY].getEnemyList().size(); i++)
                {
                    if (map[mapX][mapY].getEnemyList().get(i).alive && xPos == map[mapX][mapY].getEnemyList().get(i).xPos && yPos == map[mapX][mapY].getEnemyList().get(i).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(i).alive = false;
                        health -= 1;
                    }

                }
                for (int i = 0; i < map[mapX][mapY].getCoinList().size(); i++)
                {
                    if (!map[mapX][mapY].getCoinList().get(i).pickedUp && xPos == map[mapX][mapY].getCoinList().get(i).xPos && yPos == map[mapX][mapY].getCoinList().get(i).yPos)
                    {
                        map[mapX][mapY].getCoinList().get(i).pickedUp = true;
                        coinAmount += 1;
                    }
                }
                if (map[mapX][mapY].getGridDisplay()[xPos][yPos] != playerIcon)
                {
                    map[mapX][mapY].setGridDisplay(xPos, yPos, playerIcon);
                }
            }
            catch (NullPointerException e)
            {
            }
            catch (IndexOutOfBoundsException o)
            {
            }
            catch (InterruptedException t)
            {
            }
            if(health < 1)
            {
                alive = false;
            }
        }
    }

    /**
     * Subtracts one health from enemies in the given direction
     * then starts the cooldown timer.
     */
    public void swordStab(int direction)
    {
        if (stabCoolDown == 0)
        {
            if (direction == 0)
            {
                for (int i = 0; i < map[mapX][mapY].getEnemyList().size(); i ++)
                {
                    try
                    {
                        if (xPos == map[mapX][mapY].getEnemyList().get(i).xPos && yPos - 1 == map[mapX][mapY].getEnemyList().get(i).yPos)
                        {
                            map[mapX][mapY].getEnemyList().get(i).health -= 1;
                            Thread.sleep(10);
                            if (map[mapX][mapY].getEnemyList().get(i).alive)
                            {
                                map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, hurtPicture);
                                Thread.sleep(200);
                                map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, map[mapX][mapY].getEnemyList().get(i).icon);
                            }
                        }
                        if (stabLevel >= 3)
                        {
                            if (xPos == map[mapX][mapY].getEnemyList().get(i).xPos && yPos - 2 == map[mapX][mapY].getEnemyList().get(i).yPos)
                            {
                                map[mapX][mapY].getEnemyList().get(i).health -= 1;
                                Thread.sleep(10);
                                if (map[mapX][mapY].getEnemyList().get(i).alive)
                                {
                                    map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, hurtPicture);
                                    Thread.sleep(200);
                                    map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, map[mapX][mapY].getEnemyList().get(i).icon);
                                }
                            }  
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    catch (InterruptedException g)
                    {
                    }
                }      
            }
            else if (direction == 1)
            {
                for (int i = 0; i < map[mapX][mapY].getEnemyList().size(); i ++)
                {
                    try
                    {
                        if (xPos + 1 == map[mapX][mapY].getEnemyList().get(i).xPos && yPos == map[mapX][mapY].getEnemyList().get(i).yPos)
                        {
                            map[mapX][mapY].getEnemyList().get(i).health -= 1;
                            Thread.sleep(10);
                            if (map[mapX][mapY].getEnemyList().get(i).alive)
                            {
                                map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, hurtPicture);
                                Thread.sleep(200);
                                map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, map[mapX][mapY].getEnemyList().get(i).icon);
                            }
                        }
                        if (stabLevel >= 3)
                        {
                            if (xPos + 2 == map[mapX][mapY].getEnemyList().get(i).xPos && yPos == map[mapX][mapY].getEnemyList().get(i).yPos)
                            {
                                map[mapX][mapY].getEnemyList().get(i).health -= 1;
                                Thread.sleep(10);
                                if (map[mapX][mapY].getEnemyList().get(i).alive)
                                {
                                    map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, hurtPicture);
                                    Thread.sleep(200);
                                    map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, map[mapX][mapY].getEnemyList().get(i).icon);
                                }
                            }  
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    catch (InterruptedException g)
                    {
                    }
                }        
            }
            else if (direction == 2)
            {
                for (int i = 0; i < map[mapX][mapY].getEnemyList().size(); i ++)
                {
                    try
                    {
                        if (xPos == map[mapX][mapY].getEnemyList().get(i).xPos && yPos + 1 == map[mapX][mapY].getEnemyList().get(i).yPos)
                        {
                            map[mapX][mapY].getEnemyList().get(i).health -= 1;
                            Thread.sleep(10);
                            if (map[mapX][mapY].getEnemyList().get(i).alive)
                            {
                                map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, hurtPicture);
                                Thread.sleep(200);
                                map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, map[mapX][mapY].getEnemyList().get(i).icon);
                            }
                        }
                        if (stabLevel >= 3)
                        {
                            if (xPos == map[mapX][mapY].getEnemyList().get(i).xPos && yPos + 2 == map[mapX][mapY].getEnemyList().get(i).yPos)
                            {
                                map[mapX][mapY].getEnemyList().get(i).health -= 1;
                                Thread.sleep(10);
                                if (map[mapX][mapY].getEnemyList().get(i).alive)
                                {
                                    map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, hurtPicture);
                                    Thread.sleep(200);
                                    map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, map[mapX][mapY].getEnemyList().get(i).icon);
                                }
                            }  
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    catch (InterruptedException g)
                    {
                    }
                }      
            }
            else if (direction == 3)
            {
                for (int i = 0; i < map[mapX][mapY].getEnemyList().size(); i ++)
                {
                    try
                    {
                        if (xPos - 1 == map[mapX][mapY].getEnemyList().get(i).xPos && yPos == map[mapX][mapY].getEnemyList().get(i).yPos)
                        {
                            map[mapX][mapY].getEnemyList().get(i).health -= 1;
                            Thread.sleep(10);
                            if (map[mapX][mapY].getEnemyList().get(i).alive)
                            {
                                map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, hurtPicture);
                                Thread.sleep(200);
                                map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, map[mapX][mapY].getEnemyList().get(i).icon);
                            }
                        }
                        if (stabLevel >= 3)
                        {
                            if (xPos - 2 == map[mapX][mapY].getEnemyList().get(i).xPos && yPos == map[mapX][mapY].getEnemyList().get(i).yPos)
                            {
                                map[mapX][mapY].getEnemyList().get(i).health -= 1;
                                Thread.sleep(10);
                                if (map[mapX][mapY].getEnemyList().get(i).alive)
                                {
                                    map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, hurtPicture);
                                    Thread.sleep(200);
                                    map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(i).xPos, map[mapX][mapY].getEnemyList().get(i).yPos, map[mapX][mapY].getEnemyList().get(i).icon);
                                }
                            }  
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    catch (InterruptedException g)
                    {
                    }
                }  
            }
            new Thread(stabThread).start();
        }
    }

    /**
     * Subtracts one health from enemies in the squares surrounding the player
     * then starts a cooldown timer.
     */
    public void whirlWind()
    {        
        if (whirlLevel > 0 && whirlCoolDown <= 0)
        {
            for (int k = 0; k < map[mapX][mapY].getEnemyList().size(); k ++)
            {
                try
                {
                    if (xPos + 1 == map[mapX][mapY].getEnemyList().get(k).xPos && yPos  + 1 == map[mapX][mapY].getEnemyList().get(k).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(k).health -= 1;
                        Thread.sleep(10);
                        if (map[mapX][mapY].getEnemyList().get(k).alive)
                        {
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, hurtPicture);
                            Thread.sleep(200);
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, map[mapX][mapY].getEnemyList().get(k).icon);
                        }
                    }
                    if (xPos + 1 == map[mapX][mapY].getEnemyList().get(k).xPos && yPos == map[mapX][mapY].getEnemyList().get(k).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(k).health -= 1;
                        Thread.sleep(10);
                        if (map[mapX][mapY].getEnemyList().get(k).alive)
                        {
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, hurtPicture);
                            Thread.sleep(200);
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, map[mapX][mapY].getEnemyList().get(k).icon);
                        }
                    }
                    if (xPos + 1 == map[mapX][mapY].getEnemyList().get(k).xPos && yPos - 1 == map[mapX][mapY].getEnemyList().get(k).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(k).health -= 1;
                        Thread.sleep(10);
                        if (map[mapX][mapY].getEnemyList().get(k).alive)
                        {
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, hurtPicture);
                            Thread.sleep(200);
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, map[mapX][mapY].getEnemyList().get(k).icon);
                        }
                    }
                    if (xPos - 1 == map[mapX][mapY].getEnemyList().get(k).xPos && yPos + 1 == map[mapX][mapY].getEnemyList().get(k).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(k).health -= 1;
                        Thread.sleep(10);
                        if (map[mapX][mapY].getEnemyList().get(k).alive)
                        {
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, hurtPicture);
                            Thread.sleep(200);
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, map[mapX][mapY].getEnemyList().get(k).icon);
                        }
                    }
                    if (xPos - 1 == map[mapX][mapY].getEnemyList().get(k).xPos && yPos - 1 == map[mapX][mapY].getEnemyList().get(k).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(k).health -= 1;
                        Thread.sleep(10);
                        if (map[mapX][mapY].getEnemyList().get(k).alive)
                        {
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, hurtPicture);
                            Thread.sleep(200);
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, map[mapX][mapY].getEnemyList().get(k).icon);
                        }
                    }
                    if (xPos - 1 == map[mapX][mapY].getEnemyList().get(k).xPos && yPos == map[mapX][mapY].getEnemyList().get(k).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(k).health -= 1;
                        Thread.sleep(10);
                        if (map[mapX][mapY].getEnemyList().get(k).alive)
                        {
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, hurtPicture);
                            Thread.sleep(200);
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, map[mapX][mapY].getEnemyList().get(k).icon);
                        }
                    }
                    if (xPos == map[mapX][mapY].getEnemyList().get(k).xPos && yPos  + 1 == map[mapX][mapY].getEnemyList().get(k).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(k).health -= 1;
                        Thread.sleep(10);
                        if (map[mapX][mapY].getEnemyList().get(k).alive)
                        {
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, hurtPicture);
                            Thread.sleep(200);
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, map[mapX][mapY].getEnemyList().get(k).icon);
                        }
                    }
                    if (xPos == map[mapX][mapY].getEnemyList().get(k).xPos && yPos - 1 == map[mapX][mapY].getEnemyList().get(k).yPos)
                    {
                        map[mapX][mapY].getEnemyList().get(k).health -= 1;
                        Thread.sleep(10);
                        if (map[mapX][mapY].getEnemyList().get(k).alive)
                        {
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, hurtPicture);
                            Thread.sleep(200);
                            map[mapX][mapY].setGridDisplay(map[mapX][mapY].getEnemyList().get(k).xPos, map[mapX][mapY].getEnemyList().get(k).yPos, map[mapX][mapY].getEnemyList().get(k).icon);
                        }
                    }
                }
                catch (NullPointerException e)
                {
                }  
                catch (InterruptedException g)
                {
                }
            } 
            new Thread(whirlThread).start();
        }
    }

    /**
     * Opens sub-menu in the shop which allows user to buy the various items
     */
    public void openShop()
    {
        String item0;
        String item1;
        String item2;
        String item3;
        if (stabLevel < 6)
        {
            item2 = "Upgrade sword stab";
        }
        else item2 = "Stab is max level";
        if (whirlLevel < 6)
        {
            item3 = "Upgrade whirlwind";
        }
        else item3 = "Wirlwind is max level";
        if (maxHealth < 4)
        {
            item0 = "Increase max health";
        }
        else item0 = "You cannot buy more health";
        if (health < maxHealth)
        {
            item1 = "Heal";
        }
        else item1 = "You already have maximum health";

        String[] possibilities = {item0, item1, item2, item3};
        String item = (String)JOptionPane.showInputDialog(
                null,
                "What would you like to buy?",
                "Items:",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0]);
        if(item == "Increase max health") 
        {
            int cost = (int)Math.pow(maxHealth + 1, 1.5);
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you would like to buy more max health?" + "\n"
                    + "Costs: " + cost + " coins" + "\n" + "Increases the amount of heart containers you have by one", "", JOptionPane.YES_NO_OPTION);
            if (confirm == 0)
            {
                if (coinAmount >= cost)
                {
                    maxHealth += 1;
                    health += 1;
                    coinAmount -= cost;
                }
                else JOptionPane.showMessageDialog(null, "You do not have enough coins");
            }
        }
        else if(item == "Heal") 
        {
            int cost = 1;
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you would like heal?" + "\n"
                    + "Costs: " + cost + " coins" + "\n" + "Heals one heart container", "", JOptionPane.YES_NO_OPTION);
            if (confirm == 0)
            {
                if (coinAmount >= cost)
                {
                    health += 1;
                    coinAmount -= cost;
                }
                else JOptionPane.showMessageDialog(null, "You do not have enough coins");
            }
        }
        else if(item == "Upgrade sword stab") 
        {
            int cost = stabLevel + 2;
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you would like to upgrade the stab ability?" + "\n"
                    + "Costs: " + cost + " coins" + "\n" + "Increases the range of sword stab upon the third purchase" + "\n" + "subsequent purchases reduce the cooldown by .2 seconds", "", JOptionPane.YES_NO_OPTION);
            if (confirm == 0)
            {
                if (coinAmount >= cost)
                {
                    stabLevel += 1;
                    stabCD -= 200;
                    coinAmount -= cost;
                }
                else JOptionPane.showMessageDialog(null, "You do not have enough coins");
            }
        }
        else if(item == "Upgrade whirlwind") 
        {
            int cost = whirlLevel + 3;
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you would like to upgrade the whirlwind ability?" + "\n"
                    + "Costs: " + cost + " coins" + "\n" + "Buys whirlwind upon the first purchase, an attack that hits 8 squares around your character" + "\n" + "all purchases reduce the cooldown by .25 seconds", "", JOptionPane.YES_NO_OPTION);
            if (confirm == 0)
            {
                if (coinAmount >= cost)
                {
                    whirlLevel += 1;
                    whirlCD -= 250;
                    coinAmount -= cost;
                }
                else JOptionPane.showMessageDialog(null, "You do not have enough coins");
            }
        }
    }
}
