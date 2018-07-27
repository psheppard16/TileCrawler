/**
 * Generates Feilds for gameplay
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
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
public class Field extends JFrame implements Runnable
{
    int xSize;
    int ySize;
    private ArrayList<Goblin> enemyList;
    private ArrayList<Coin> coinList;
    ArrayList<Bomb> bombList;
    private FieldObject[][] objectMatrix;
    private BufferedImage[][] gridDisplay;
    BufferedImage ground;
    BufferedImage basicWall;
    BufferedImage playerIcon;
    BufferedImage goblinIcon;
    BufferedImage coinIcon;
    BufferedImage easy;
    BufferedImage medium;
    BufferedImage hard;
    BufferedImage finished;
    BufferedImage finishedPlayer;
    BufferedImage empty;
    BufferedImage shop;
    BufferedImage sentinelIcon;
    BufferedImage berzerkerIcon;
    BufferedImage pyrophileIcon;
    private BufferedImage mapIcon;
    int xCo;
    int yCo;
    Player player;
    boolean completed;
    Thread thread;
    String difficulty;
    public Field(Field[][] map, int xCo, int yCo, Player player)
    {
        try
        {
            ground = ImageIO.read(new File("images/grass.jpg"));
            basicWall = ImageIO.read(new File("images/wall.jpg"));
            playerIcon = ImageIO.read(new File("images/knight1.jpg"));
            goblinIcon = ImageIO.read(new File("images/goblin.jpg"));
            coinIcon = ImageIO.read(new File("images/coin.jpg"));
            easy = ImageIO.read(new File("images/easy.jpg"));
            medium = ImageIO.read(new File("images/medium.jpg"));
            hard = ImageIO.read(new File("images/hard.jpg"));
            shop = ImageIO.read(new File("images/shop.jpg"));
            finished = ImageIO.read(new File("images/finished.jpg"));
            finishedPlayer = ImageIO.read(new File("images/finishedPlayer.jpg"));
            empty = ImageIO.read(new File("images/empty.jpg"));
            sentinelIcon = ImageIO.read(new File("images/sentinelImage.jpg"));
            berzerkerIcon = ImageIO.read(new File("images/berzerkerImage.jpg"));
            pyrophileIcon = ImageIO.read(new File("images/pyrophileImage.jpg"));
        }
        catch (IOException e)
        {
            System.out.println("File read error");  
        }
        this.xCo = xCo;
        this.yCo = yCo;
        this.player = player;
        completed = false;
        enemyList = new ArrayList<Goblin>(0);
        coinList = new ArrayList<Coin>(0);
        bombList = new ArrayList<Bomb>(0);
        mapIcon = empty;
        newLayout();
        thread = new Thread(this, "field thread");
        thread.start();
    }

    public void run()
    {
        /**
         * determines if a level has been completed
         * if so opens the level
         */
        while(!completed)
        {
            try
            {
                /**
                 * Checks if any enemies in enemyList are not alive
                 * if so they are removed from the arrayList.
                 */
                for (int i = 0; i < enemyList.size(); i ++)
                {
                    try
                    {
                        if (!enemyList.get(i).alive)
                        {
                            removeEnemyListElement(i);
                        }
                    }
                    catch (NullPointerException e)
                    {
                        System.out.println("enemy does not exist");
                    }
                }
                /** 
                 * Checks if any coins in coinList are pickedUp
                 * if so they are removed from the arrayList.
                 */ 
                for (int i = 0; i < coinList.size(); i ++)
                {
                    try
                    {
                        if (coinList.get(i).pickedUp)
                        {
                            removeCoinListElement(i);
                        }
                    }
                    catch (NullPointerException e)
                    {
                        System.out.println("coin does not exist");
                    }
                }
                /** 
                 * Checks if any bombs in bombList are exploded
                 * if so they are removed from the arrayList.
                 */ 
                for (int i = 0; i < bombList.size(); i ++)
                {
                    try
                    {
                        if (bombList.get(i).exploded)
                        {
                            removeBombListElement(i);
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                }
            }
            catch (NullPointerException b)
            {
            }
            if (enemyList.size() == 0 && bombList.size() == 0)
            {
                completed = true;
            }
        }
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e)
        {
        }
        if (!(xCo == 2 && yCo == 0))
        {
            openLevel();
        }
    }

    /**
     * spawns a field of the desired size
     */
    public void putGround(int x, int y)
    {
        this.xSize = x + 2;
        this.ySize = y + 2;
        objectMatrix = new FieldObject[xSize][ySize];
        gridDisplay = new BufferedImage[xSize][ySize];
        for (int j = 0; j < ySize; j++)
        {
            for(int i = 0; i < xSize; i++)
            {
                if(j == 0 || j == ySize - 1 || i == 0 || i == xSize - 1)
                {
                    new Wall(i, j, basicWall, ground, objectMatrix, gridDisplay, player);
                }
                else
                {
                    new Ground(i, j, ground, objectMatrix, gridDisplay, player);
                }
            }
        }
    }

    /**
     * opens up the entrances to adjacent fields
     * unless the field is on the edge of the map
     */
    public void openLevel()
    {
        if (yCo != 0) new Ground(xSize / 2, 0, ground, objectMatrix, gridDisplay, player);
        if (yCo != 6) new Ground(xSize / 2, ySize - 1, ground, objectMatrix, gridDisplay, player);      
        if (xCo != 6) new Ground(xSize - 1, ySize / 2, ground, objectMatrix, gridDisplay, player);     
        if (xCo != 0) new Ground(0, ySize / 2, ground, objectMatrix, gridDisplay, player);
    }

    /**
     * displays the player
     */
    public void showPlayer()
    {
        gridDisplay[player.xPos][player.yPos] = player.playerIcon;
    }

    /**
     * chooses the layout for the new field then spawns it
     */
    public void newLayout() 
    {
        try
        {
            if (yCo == 0)
            {
                if (xCo == 0)
                {
                    player.defaultSpawn = true;
                    putGround(7, 3);
                    enemyList.add(new Berzerker(1, 2, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "medium";
                }
                else if (xCo == 1)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Pyrophile(3, 3, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    difficulty = "easy";
                }
                else if (xCo == 2)
                {
                    difficulty = "shop";
                    player.defaultSpawn = false;
                }
                else if (xCo == 3)
                {               
                    player.defaultSpawn = true;
                    putGround(5, 7);
                    for (int j = 0; j < 8; j++)
                    {
                        for (int i = 0; i < 6; i++)
                        {
                            new  Wall(i, j, basicWall, ground, objectMatrix, gridDisplay, player);       
                        }
                    }
                    new Ground(1, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(1, 5, ground, objectMatrix, gridDisplay, player);
                    new Ground(1, 6, ground, objectMatrix, gridDisplay, player);
                    new Ground(1, 7, ground, objectMatrix, gridDisplay, player);
                    new Ground(2, 7, ground, objectMatrix, gridDisplay, player);
                    new Ground(3, 7, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 7, ground, objectMatrix, gridDisplay, player);
                    new Ground(5, 7, ground, objectMatrix, gridDisplay, player);
                    new Ground(5, 6, ground, objectMatrix, gridDisplay, player);
                    new Ground(5, 5, ground, objectMatrix, gridDisplay, player);
                    new Ground(5, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 4, basicWall, objectMatrix, gridDisplay, player);
                    new Ground(4, 3, basicWall, objectMatrix, gridDisplay, player);
                    new Ground(4, 2, basicWall, objectMatrix, gridDisplay, player);
                    new Ground(4, 1, ground, objectMatrix, gridDisplay, player);
                    new Ground(3, 1, ground, objectMatrix, gridDisplay, player);
                    new Ground(2, 1, ground, objectMatrix, gridDisplay, player);
                    new Coin(4, 1, coinIcon, ground, objectMatrix, gridDisplay, player);
                    new Coin(3, 1, coinIcon, ground, objectMatrix, gridDisplay, player);
                    new Coin(2, 1, coinIcon, ground, objectMatrix, gridDisplay, player);
                    difficulty = "easy";
                }
                else if (xCo == 4)
                {
                    putGround(6, 1);
                    player.defaultSpawn = false;
                    player.xPos = 1;
                    player.yPos = 1;
                    enemyList.add(new Grunt(5, 1, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "medium";
                }
            }
            else if (yCo == 1)
            {
                if (xCo == 0)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Sentinel(3, 3, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Berzerker(2, 2, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    new Wall(3, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(6, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    difficulty = "medium";
                }
                else if (xCo == 1)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Grunt(4, 4, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "easy";                        
                }
                else if (xCo == 2)
                {
                    player.defaultSpawn = false;
                    putGround(5, 5);
                    difficulty = "start";
                }
                else if (xCo == 3)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Berzerker(6, 6, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "easy"; 
                }
                else if (xCo == 4)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Pyrophile(5, 5, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    new Wall(5, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(2, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    difficulty = "medium";
                }
            }
            else if (yCo == 2)
            {
                if (xCo == 0)
                {
                    putGround(7, 3);
                    player.defaultSpawn = false;
                    player.xPos = 4;
                    player.yPos = 4;           
                    enemyList.add(new Grunt(1, 2, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Grunt(7, 2, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "hard";
                }
                else if (xCo == 1)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Pyrophile(4, 4, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    new Wall(2, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(6, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    difficulty = "medium";
                }
                else if (xCo == 2)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Sentinel(4, 4, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    difficulty = "easy";   
                }
                else if (xCo == 3)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Sentinel(4, 4, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Grunt(1, 1, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    new Wall(2, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(6, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    difficulty = "medium";
                }
                else if (xCo == 4)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Pyrophile(4, 4, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    new Wall(5, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(3, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(5, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(3, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    enemyList.add(new Berzerker(3, 4, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Berzerker(5, 4, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    new Wall(4, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    difficulty = "hard";
                }
            }
            else if (yCo == 3)
            {
                if (xCo == 0)
                {
                    putGround(7, 7);
                    player.defaultSpawn = false;
                    player.xPos = 7;
                    player.yPos = 4;
                    for (int j = 0; j < 8; j++)
                    {
                        for (int i = 0; i < 6; i++)
                        {
                            new  Wall(i, j, basicWall, ground, objectMatrix, gridDisplay, player);       
                        }
                    }
                    enemyList.add(new Pyrophile(4, 4, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(1, 4, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(4, 1, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(4, 7, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    new Ground(3, 3, ground, objectMatrix, gridDisplay, player);
                    new Ground(5, 5, ground, objectMatrix, gridDisplay, player);
                    new Ground(5, 3, ground, objectMatrix, gridDisplay, player);
                    new Ground(3, 5, ground, objectMatrix, gridDisplay, player);
                    new Ground(5, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(6, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(7, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(3, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(2, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(1, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 7, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 6, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 5, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 3, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 2, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 1, ground, objectMatrix, gridDisplay, player);
                    new Ground(9, 9, ground, objectMatrix, gridDisplay, player);
                    new Ground(1, 4, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 7, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 6, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 5, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 3, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 2, ground, objectMatrix, gridDisplay, player);
                    new Ground(4, 1, ground, objectMatrix, gridDisplay, player);
                    difficulty = "hard";
                }
                else if (xCo == 1)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    new Wall(4, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    enemyList.add(new Berzerker(2, 3, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Berzerker(2, 5, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "medium";
                }
                else if (xCo == 2)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Pyrophile(3, 4, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    new Wall(2, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(3, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(2, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(5, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(5, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(2, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(2, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    enemyList.add(new Berzerker(5, 6, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "medium";
                }
                else if (xCo == 3)
                {
                    putGround(7, 7);
                    player.defaultSpawn = false;
                    player.xPos = 4;
                    player.yPos = 4;           
                    enemyList.add(new Grunt(1, 3, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Grunt(3, 5, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Grunt(5, 3, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Grunt(3, 1, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "medium";
                }
                else if (xCo == 4)
                {
                    putGround(9, 3);
                    player.defaultSpawn = false;
                    player.xPos = 1;
                    player.yPos = 2;
                    enemyList.add(new Sentinel(9, 9, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Pyrophile(8, 8, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(7, 7, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Grunt(6, 3, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Grunt(6, 1, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "hard";
                }
            }
            else if (yCo == 4)
            {
                if (xCo == 0)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    enemyList.add(new Pyrophile(4, 4, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(2, 2, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(6, 6, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(2, 6, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(6, 2, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    new Wall(2, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(6, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    difficulty = "hard";
                }
                else if (xCo == 1)
                {
                    player.defaultSpawn = true;
                    putGround(5, 5);
                    enemyList.add(new Pyrophile(8, 2, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Berzerker(7, 1, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Grunt(9, 3, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Grunt(9, 1, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "hard";
                }
                else if (xCo == 2)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    int numG = Utility.random(0, 4);
                    int numB = Utility.random(0, 3);
                    int numW = Utility.random(0, 10);
                    for (int i = numW; i > 0; i --)
                    {
                        new Wall(Utility.random(1, 7), Utility.random(1, 7), basicWall, ground, objectMatrix, gridDisplay, player);
                    }
                    for (int i = numG; i > 0; i --)
                    {
                        enemyList.add(new Grunt(Utility.random(1, 7), Utility.random(3, 7), goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    }
                    for (int i = numB; i > 0; i --)
                    {
                        enemyList.add(new Berzerker(Utility.random(1, 7), Utility.random(4, 7), berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    }
                    difficulty = "medium";
                }
                else if (xCo == 3)
                {
                    putGround(9, 7);
                    player.defaultSpawn = false;
                    player.xPos = 1;
                    player.yPos = 1;
                    enemyList.add(new Pyrophile(5, 4, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    new Wall(5, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(5, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(3, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(3, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(3, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(3, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(7, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(7, 3, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(7, 5, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(7, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    enemyList.add(new Sentinel(1, 1, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(9, 1, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Sentinel(9, 7, sentinelIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Berzerker(3, 4, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Berzerker(7, 4, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    difficulty = "hard";
                }
                else if (xCo == 4)
                {
                    player.defaultSpawn = true;
                    putGround(7, 7);
                    putGround(7, 7);
                    enemyList.add(new Pyrophile(4, 4, pyrophileIcon, ground, objectMatrix, player, gridDisplay, bombList, this));
                    enemyList.add(new Grunt(1, 1, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Grunt(7, 7, goblinIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Berzerker(1, 7, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    enemyList.add(new Berzerker(7, 1, berzerkerIcon, ground, objectMatrix, player, gridDisplay, this));
                    new Wall(2, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 2, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(6, 4, basicWall, ground, objectMatrix, gridDisplay, player);
                    new Wall(4, 6, basicWall, ground, objectMatrix, gridDisplay, player);
                    difficulty = "hard";
                }
            }
            if (difficulty.equals("easy"))
            {
                mapIcon = easy;
            }
            else if (difficulty.equals("medium"))
            {
                mapIcon = medium;
            }
            else if (difficulty.equals("hard"))
            {
                mapIcon = hard;
            }
            else if (difficulty.equals("start"))
            {
                player.xPos = 3;
                player.yPos = 3;
                showPlayer();
                mapIcon = finishedPlayer;
            }
            else if (difficulty.equals("shop"))
            {
                mapIcon = shop;
            }
        }
        catch (NullPointerException e)
        {
            System.out.println("error in field generation");
        }
    }

    public void clearField()
    {
        try
        {
            for (int i = 0; i < enemyList.size(); i ++)
            {
                try
                {
                    enemyList.get(0).alive = false;
                    enemyList.remove(0);
                }
                catch (NullPointerException e)
                {
                    System.out.println("Coin does not exist");
                }
            }
            Thread.sleep(100);
            for (int i = 0; i < coinList.size(); i ++)
            {
                try
                {
                    coinList.get(0).pickedUp = true;
                    coinList.remove(0);
                }
                catch (NullPointerException e)
                {
                    System.out.println("Coin does not exist");
                }
            }
        }
        catch (InterruptedException e)
        {
        }
    }

    public BufferedImage getMapIcon()
    {
        return mapIcon;
    }

    public void setMapIcon(BufferedImage image)
    {
        mapIcon = image;
    }

    public FieldObject[][] getObjectMatrix()
    {
        return objectMatrix;
    }

    public void setObjectMatrix(int x, int y, FieldObject object)
    {
        objectMatrix[x][y] = object;
    }

    public BufferedImage[][] getGridDisplay()
    {
        return gridDisplay;
    }

    public void setGridDisplay(int x, int y, BufferedImage image)
    {
        gridDisplay[x][y] = image;
    }

    public ArrayList<Goblin> getEnemyList()
    {
        return enemyList;
    }

    public void removeEnemyListElement(int place)
    {
        enemyList.remove(place);
    }

    public ArrayList<Coin> getCoinList()
    {
        return coinList;
    }

    public void removeCoinListElement(int place)
    {
        coinList.remove(place);
    }

    public void addCoinListElement(int x, int y)
    {
        coinList.add(new Coin(x, y, coinIcon, ground, objectMatrix, gridDisplay, player));
    }

    public ArrayList<Bomb> getBombList()
    {
        return bombList;
    }

    public void removeBombListElement(int place)
    {
        bombList.remove(place);
    }
}

