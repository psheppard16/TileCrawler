
/**
 * Creates the UI for the game and runs commands that have to do with user input
 * 
 * @author Preston 
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
import sun.audio.*;
public class Window implements Runnable
{
    JPanel iconGrid;
    JPanel mapGrid;
    JFrame playScreen;
    JFrame mapScreen;
    JPanel input;  
    KeyListener keyInput;
    InputStream in;
    AudioStream audioStream;
    Thread thread;
    Thread songThread;
    Thread songTimer;
    Player player;
    BufferedImage empty; 
    BufferedImage a11;
    BufferedImage a10;
    BufferedImage a22;
    BufferedImage a21;
    BufferedImage a20;
    BufferedImage a33;
    BufferedImage a32;
    BufferedImage a31;
    BufferedImage a30;
    BufferedImage a44;
    BufferedImage a43;
    BufferedImage a42;
    BufferedImage a41;
    BufferedImage a40;
    BufferedImage coins0;
    BufferedImage coins1;
    BufferedImage coins2;
    BufferedImage coins3;
    BufferedImage coins4;
    BufferedImage coins5;
    BufferedImage coins6;
    BufferedImage coins7;
    BufferedImage coins8;
    BufferedImage coins9;
    BufferedImage coins10;
    BufferedImage coins10plus;
    BufferedImage swordStabReady;
    BufferedImage swordStabNotReady;
    BufferedImage whirlwindReady;
    BufferedImage whirlwindNotReady;
    BufferedImage easyPlayer;
    BufferedImage mediumPlayer;
    BufferedImage hardPlayer;
    BufferedImage finishedPlayer;
    BufferedImage shopPlayer;
    BufferedImage easy;
    BufferedImage medium;
    BufferedImage hard;
    BufferedImage finished;
    BufferedImage shop;
    File song1;
    String path;
    boolean append;
    String[] saveFile;
    String[] completeSave;
    boolean[] completed1;
    int startingLine;
    String name;
    String password;
    boolean timerStarted;
    public Window()
    {       
        path = "StatisticStorage.txt";
        append = true;
        timerStarted = false;
        completed1 = new boolean[25];
        try
        {
            empty = ImageIO.read(new File("images/empty.jpg"));
            a11 = ImageIO.read(new File("images/11.jpg"));
            a10 = ImageIO.read(new File("images/10.jpg"));
            a22 = ImageIO.read(new File("images/22.jpg"));
            a21 = ImageIO.read(new File("images/21.jpg"));
            a20 = ImageIO.read(new File("images/20.jpg"));
            a33 = ImageIO.read(new File("images/33.jpg"));
            a32 = ImageIO.read(new File("images/32.jpg"));
            a31 = ImageIO.read(new File("images/31.jpg"));
            a30 = ImageIO.read(new File("images/30.jpg"));
            a44 = ImageIO.read(new File("images/44.jpg"));
            a43 = ImageIO.read(new File("images/43.jpg"));
            a42 = ImageIO.read(new File("images/42.jpg"));
            a41 = ImageIO.read(new File("images/41.jpg"));
            a40 = ImageIO.read(new File("images/40.jpg"));
            swordStabReady = ImageIO.read(new File("images/swordStabReady.jpg"));
            swordStabNotReady = ImageIO.read(new File("images/swordStabNotReady.jpg"));
            whirlwindReady = ImageIO.read(new File("images/whirlwindReady.jpg"));
            whirlwindNotReady = ImageIO.read(new File("images/whirlwindNotReady.jpg"));
            coins0 = ImageIO.read(new File("images/coins0.jpg"));
            coins1 = ImageIO.read(new File("images/coins1.jpg"));
            coins2 = ImageIO.read(new File("images/coins2.jpg"));
            coins3 = ImageIO.read(new File("images/coins3.jpg"));
            coins4 = ImageIO.read(new File("images/coins4.jpg"));
            coins5 = ImageIO.read(new File("images/coins5.jpg"));
            coins6 = ImageIO.read(new File("images/coins6.jpg"));
            coins7 = ImageIO.read(new File("images/coins7.jpg"));
            coins8 = ImageIO.read(new File("images/coins8.jpg"));
            coins9 = ImageIO.read(new File("images/coins9.jpg"));
            coins10 = ImageIO.read(new File("images/coins10.jpg"));
            coins10plus = ImageIO.read(new File("images/coins10+.jpg"));
            shop = ImageIO.read(new File("images/shop.jpg"));
            easyPlayer = ImageIO.read(new File("images/easyPlayer.jpg"));
            mediumPlayer = ImageIO.read(new File("images/mediumPlayer.jpg"));
            hardPlayer = ImageIO.read(new File("images/hardPlayer.jpg"));
            finishedPlayer = ImageIO.read(new File("images/finishedPlayer.jpg"));
            shopPlayer = ImageIO.read(new File("images/shopPlayer.jpg"));
            easy = ImageIO.read(new File("images/easy.jpg"));
            medium = ImageIO.read(new File("images/medium.jpg"));
            hard = ImageIO.read(new File("images/hard.jpg"));
            finished = ImageIO.read(new File("images/finished.jpg"));
            empty = ImageIO.read(new File("images/empty.jpg"));
        }
        catch (IOException e)
        {
            System.out.println("File read error");  
        }

        /**
         * opens the start screen
         * reopens the startscreen until the player logs in
         */       
        while(saveFile == null)
        {
            startScreen();
        }

        /**
         * creates keylistener to detect user input
         */
        keyInput = new KeyListener()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e) 
            {
                keyAction(e);
            }

            @Override
            public void keyTyped(KeyEvent e) 
            {
            }
        };
        mapScreen = new JFrame("Map");
        mapGrid = new JPanel(new BorderLayout());
        playScreen = new JFrame("Playing field"); 
        iconGrid = new JPanel(new BorderLayout());  

        /**
         * Makes the playScreen visible.
         */
        playScreen.addKeyListener(keyInput);
        playScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playScreen.setLocationByPlatform(true);
        playScreen.setResizable(false);
        playScreen.pack();
        playScreen.setVisible(true);

        /**
         * Makes the mapScreen visible.
         */
        mapScreen.add(mapGrid);
        mapScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mapScreen.setLocationByPlatform(true);
        mapScreen.setResizable(false);
        mapScreen.pack();
        mapScreen.setVisible(true);

        /**
         * Loads player information.
         */
        player = new Player(saveFile);

        songTimer = new Thread() 
        {
            public void run() 
            {
                try
                {
                    if (player.map[player.mapX][player.mapY].difficulty.equals("easy") || player.map[player.mapX][player.mapY].difficulty.equals("shop") || player.map[player.mapX][player.mapY].difficulty.equals("start"))
                    {
                        timerStarted = true;
                        p: for (int i = 0; i < 65; i++)
                        {
                            Thread.sleep(1000);
                            if (!(player.map[player.mapX][player.mapY].difficulty.equals("easy") || player.map[player.mapX][player.mapY].difficulty.equals("shop") || player.map[player.mapX][player.mapY].difficulty.equals("start")))
                            {
                                break p;
                            }
                        }       
                    }
                    else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                    {
                        timerStarted = true;
                        p: for (int i = 0; i < 196; i++)
                        {
                            Thread.sleep(1000);
                            if (!player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                            {
                                break p;
                            }
                        }      
                    }
                    else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                    {
                        timerStarted = true;
                        p: for (int i = 0; i < 90; i++)
                        {
                            Thread.sleep(1000);
                            if (!player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                            {
                                break p;
                            }
                        }
                    }
                    AudioPlayer.player.stop(audioStream);
                    player.currentSong = 0;
                    timerStarted = false;
                }
                catch (InterruptedException e)
                {
                    System.out.println("Song timer interrupted");
                }
                catch (NullPointerException b)
                {
                }
            }
        };

        songThread = new Thread() 
        {
            public void run() 
            {
                while(player.alive)
                {
                    try
                    {
                        if (player.currentSong != 1 && (player.map[player.mapX][player.mapY].difficulty.equals("easy") || player.map[player.mapX][player.mapY].difficulty.equals("shop") || player.map[player.mapX][player.mapY].difficulty.equals("start")))
                        {
                            if (audioStream != null)
                            {
                                AudioPlayer.player.stop(audioStream);
                            }
                            playSong("easySong.au");
                            player.currentSong = 1;
                            if (timerStarted == false)
                            {
                                songTimer.start();
                            }
                        }
                        else if (player.currentSong != 2 && player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                        {
                            if (audioStream != null)
                            {
                                AudioPlayer.player.stop(audioStream);
                            }
                            AudioPlayer.player.stop(audioStream);
                            playSong("mediumSong.au");
                            player.currentSong = 2;
                            if (timerStarted == false)
                            {
                                songTimer.start();
                            }
                        }
                        else if (player.currentSong != 3 && player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                        {
                            if (audioStream != null)
                            {
                                AudioPlayer.player.stop(audioStream);
                            }
                            playSong("hardSong.au");
                            player.currentSong = 3;
                            if (timerStarted == false)
                            {
                                songTimer.start();
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    catch (IllegalThreadStateException b)
                    {
                        if (audioStream != null)
                        {
                            AudioPlayer.player.stop(audioStream);
                        }
                        player.currentSong = 0;
                        timerStarted = true;
                    }
                }
            }
        };
        songThread.start();

        thread = new Thread(this, "Refresh thread");
        thread.start();
    }

    public void playSong(String song)
    {
        try
        {
            in = new FileInputStream(song);
            audioStream = new AudioStream(in);
            AudioPlayer.player.start(audioStream);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Song file not found");
        }
        catch (IOException e)
        {
            System.out.println("IO song play error");
        }
    }

    /**
     * Displays the start screen to the user which includes
     * logins, account creation, instructions, and the credits.
     * If the player logs in, retrieves account information.
     */
    public void startScreen()
    {
        String[] possibilities = {"Login", "Create Account", "Instructions", "Credits", "Exit"};
        String option = (String)JOptionPane.showInputDialog(
                null,
                "Please log in:",
                "Goblin Quest: MADNESS v5.2",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0]);

        try
        {
            if (option.equals("Login"))
            {
                try 
                {
                    loginValidation();
                }
                catch(IOException e)
                {
                    System.out.println("File read error");
                }
            }
            else if (option.equals("Create Account"))
            {
                try 
                {
                    addAccount();
                    startScreen();         
                }                                       
                catch(IOException e) {}
            }
            else if (option.equals("Instructions"))
            {
                JOptionPane.showMessageDialog(null, "1) Create an account and then log in. All your progress is stored for your convenience" + "\n" 
                    + "2) move with WASD and use your sword attack with arrow keys" + "\n" 
                    + "3) press e to reopen the map" + "\n" 
                    + "4) press q while in a completed room to save and quit" + "\n" 
                    + "5) There are four different types of enemies, the grunt, the bezerker, the sentinel, and the pyrophile. Each enemy drops a coin on death" + "\n" 
                    + "-Grunt- has two health and follows you at a slow pace. If he lands on top of you you get damaged so be careful!" + "\n" 
                    + "-Berzerker- has one health and charges at you quickly when you are in range. He is quite dumb though, so try to get him caught in an infinite loop!" + "\n" 
                    + "-Sentinel- has three health and shoots bombs in random directions, unless you are in range, when he will fire towards your location." + "\n" 
                    + "-Pyrophile- has two health and really, REALLY, loves bombs" + "\n" 
                    + "6) The farther you travel from the starting location the harder it gets, you can check the difficulty on the map. Yellow is easy, orange is medium, and red is hard." + "\n" 
                    + "7) Bellow the map you can see your current health, how many coins you have, and whether your abilities are on cooldown."  + "\n" 
                    + "8) Try to beat all the levels and above all HAVE FUN!!!");
                startScreen();
            }
            else if (option.equals("Credits"))
            {
                JOptionPane.showMessageDialog(null, "Goblin Quest: MADNESS" + "\n" + "Version 5.2" + "\n" 
                    + "Created by: Preston Sheppard" + "\n" + "Produced: by Preston Sheppard" + "\n" 
                    + "Developed by: Preston Sheppard" +  "\n" + "Commented by: Preston Sheppard" + "\n" 
                    + "Consulted: Isaac Carlson" + "\n" + "Easy level music: FF7 by: JoJoBiBop" + "\n"
                    + "Medium level music: Demolition of Order by: Seprix" + "\n" + "Hard level music: Total Extermination by: Blackaux" + "\n"                  
                    + "Given a score of 100 by: Anthony Beckwith"
                );
                startScreen();
            }
            else if (option.equals("Exit"))
            {
                System.exit(0);
            }
        }
        catch (NullPointerException e)
        {
        }
    }

    /**
     * Allows user to add new accounts.
     */
    public void addAccount()
    throws IOException
    {
        try
        {
            String accountName = JOptionPane.showInputDialog("Please type your new username:").trim();
            String password = JOptionPane.showInputDialog("Please type your new password:").trim();
            if (!password.equals(null) && !accountName.equals(null) && !password.equals("") && !accountName.equals(""))
            {
                if (nameCheck(accountName))
                {
                    FileWriter statistics = new FileWriter(path, append);
                    PrintWriter stats = new PrintWriter(statistics);
                    writeToFile(accountName);
                    writeToFile(password);  
                    writeToFile("1");
                    writeToFile("2");
                    writeToFile("1");
                    writeToFile("0");
                    writeToFile("0");
                    writeToFile("2000");
                    writeToFile("3000");
                    for (int i = 0; i < 25; i++)
                    {
                        writeToFile("false");
                    }
                    stats.close();
                }
                else JOptionPane.showMessageDialog(null, "An account with that username already exists");
            }
            else JOptionPane.showMessageDialog(null, "You must input both a username and password");
        }
        catch (NullPointerException e) {}
    }

    /**
     * Verifies that the username and password the user entered belongs to an existing account.
     * @return The starting line of the data for that account if the log in is successful, and -1 if it is not.
     */
    public void loginValidation()
    throws IOException
    {
        startingLine = -1;
        try
        {
            String accountName = JOptionPane.showInputDialog("Account Name:");
            String password = JOptionPane.showInputDialog("Password:");
            FileReader statistics = new FileReader(path);
            BufferedReader statsReader = new BufferedReader(statistics);
            String[] fileContent = new String[numberLines()]; 
            fileContent = readFile(0, numberLines());
            p:for (int i = 0; i < numberLines(); i += 34)
            {
                if (accountName.equals(fileContent[i]) && password.equals(fileContent[(i + 1)]))
                {
                    startingLine = i;
                    break p;
                }
                else startingLine = -1;
            }
            statsReader.close();
            if (startingLine == -1)
            {
                JOptionPane.showMessageDialog(null, "Incorrect username or password");
            }
        }
        catch (NullPointerException e)
        {
            startingLine = -1;
        }
        if (startingLine != -1)
        {
            saveFile = readFile(startingLine, 34);
            completeSave = readFile(0, numberLines());
        }
    }

    /**
     * Checks to see if an account with a desired username already exists.
     * @return True if an account with that username exists, false if the username if free.
     */
    public boolean nameCheck(String username)
    throws IOException
    {
        boolean valid = true;
        String[] nameCheck = new String[numberLines()];
        nameCheck = readFile(0, numberLines());
        for (int i = 0; i < numberLines(); i += 34)
        {            
            if (nameCheck[i].equals(username))
            {
                valid = false;
            }
            else valid = true;
        }      
        return valid;
    }

    /**
     * Finds the number of lines that a file has.
     * @return the number of lines in the file.
     */
    public int numberLines() throws IOException
    {
        FileReader statistics = new FileReader(path);
        BufferedReader statsReader = new BufferedReader(statistics);
        int NumberOfLines = 0;
        while (statsReader.readLine() != null)
        {   
            NumberOfLines++;
        }
        statsReader.close();
        return NumberOfLines;
    }

    /**
     * Reads a file from a starting point for a desired number of lines.
     * @return the content of the file in an array.
     */
    public String[] readFile(int startingLine, int linesToRead) throws IOException
    {
        FileReader statistics = new FileReader(path);
        BufferedReader statsReader = new BufferedReader(statistics);
        String[] fileContent = new String[linesToRead];
        for (int i = 0; i < startingLine + linesToRead; i++)
        {            
            if (i >= startingLine)
            {
                fileContent[i - startingLine] = statsReader.readLine();
            }
            else 
            {
                statsReader.readLine();   
            }
        }
        statsReader.close();
        return fileContent;
    }

    /**
     * Adds one line of text onto the end of a file.
     */
    public void writeToFile(String text)
    throws IOException
    {
        FileWriter statistics = new FileWriter(path, append);
        PrintWriter stats = new PrintWriter(statistics);
        stats.printf("%s" + "%n", text);
        stats.close();
    }

    /**
     * Rewrites a file with the content of an array.
     */
    public void writeToFile(String[] file)
    throws IOException
    {
        FileWriter statistics = new FileWriter(path, false);
        PrintWriter stats = new PrintWriter(statistics);
        for (int i = 0; i < file.length; i++)
        {
            stats.printf("%s" + "%n", file[i]);
        }
        stats.close();
    }

    /**
     * Method is called when a key is pressed
     * Handles player movement
     * swordStab attack
     * whirlwind attack
     * saving and quitting
     * and opening the map
     */
    public void keyAction(KeyEvent e) 
    {   
        try
        {
            if (e.getKeyChar() == 'w' && player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos - 1].canMove)
            {
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos].icon); 
                player.yPos -= 1;
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.playerIcon);
            }
            else if (e.getKeyChar() == 'd' && player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos + 1][player.yPos].canMove)
            {
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos].icon);
                player.xPos += 1;
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.playerIcon);
            }
            else if (e.getKeyChar() == 's' && player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos + 1].canMove)
            {
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos].icon);
                player.yPos += 1;
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.playerIcon);
            }
            else if (e.getKeyChar() == 'a' && player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos - 1][player.yPos].canMove)
            {
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos].icon);
                player.xPos -= 1;
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.playerIcon);
            }
            else if (e.getKeyCode() == KeyEvent.VK_UP)
            {
                player.swordStab(0);
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                player.swordStab(1);
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                player.swordStab(2);
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                player.swordStab(3);
            }
            else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            {
                player.whirlWind();
            }
            else if (e.getKeyChar() == 'e')
            {
                mapScreen.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                mapScreen.setLocationByPlatform(true);
                mapScreen.setResizable(false);
                mapScreen.pack();
                mapScreen.setVisible(true);
            }
            else if (e.getKeyChar() == 'q' && player.map[player.mapX][player.mapY].completed)
            {
                if (JOptionPane.showConfirmDialog(null, "would you like to save and quit?", "", JOptionPane.YES_NO_OPTION) == 0)
                {
                    saveFile[0] = player.name;
                    saveFile[1] = player.password;
                    saveFile[2] = Integer.toString(player.health);
                    saveFile[3] = Integer.toString(player.coinAmount);
                    saveFile[4] = Integer.toString(player.maxHealth);
                    saveFile[5] = Integer.toString(player.stabLevel);
                    saveFile[6] = Integer.toString(player.whirlLevel);
                    saveFile[7] = Integer.toString(player.whirlCD);
                    saveFile[8] = Integer.toString(player.stabCD);
                    for (int j = 0; j < 5; j++)
                    {
                        for(int i = 0; i < 5; i++)
                        {
                            if (player.map[i][j] != null)
                            {
                                saveFile[9 + i + 4 * (j + 1)] = "true";
                            }
                            else 
                            {
                                saveFile[9 + i + 4 * (j + 1)] = "false";
                            }
                        }
                    }
                    for (int i = 0; i < 34; i++)
                    {
                        completeSave[startingLine + i] = saveFile[i];
                    }
                    try
                    {
                        writeToFile(completeSave);
                    }
                    catch (IOException b)
                    {
                        System.out.println("Error in saving");
                    }
                    playScreen.dispatchEvent(new WindowEvent(playScreen, WindowEvent.WINDOW_CLOSING));
                    mapScreen.dispatchEvent(new WindowEvent(playScreen, WindowEvent.WINDOW_CLOSING));
                    System.exit(0);
                }
            }
            if (player.xPos == 0 && player.yPos == player.map[player.mapX][player.mapY].ySize / 2)
            {
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos].ground);
                if (player.map[player.mapX][player.mapY].difficulty.equals("easy"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(easy);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(medium);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(hard);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("start"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(finished);
                }
                player.mapX -= 1;
                if (player.map[player.mapX][player.mapY] == null)
                {
                    player.map[player.mapX][player.mapY] = new Field(player.map, player.mapX, player.mapY, player);
                }
                if (player.mapX == 2 && player.mapY == 0)
                {
                    player.map[player.mapX][player.mapY].setMapIcon(shopPlayer);
                    player.openShop();
                    player.map[player.mapX][player.mapY].setMapIcon(shop);
                    player.mapX = 2;
                    player.mapY = 1;
                    player.xPos = 3;
                    player.yPos = 3;
                }
                else if (player.defaultSpawn || player.map[player.mapX][player.mapY].completed) 
                {
                    player.xPos = player.map[player.mapX][player.mapY].xSize - 2;
                    player.yPos = player.map[player.mapX][player.mapY].ySize / 2;
                }
                player.map[player.mapX][player.mapY].showPlayer();
                if (player.map[player.mapX][player.mapY].difficulty.equals("easy"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(easyPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(mediumPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(hardPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("start"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(finishedPlayer);
                }
            }
            else if (player.xPos == player.map[player.mapX][player.mapY].xSize  - 1 && player.yPos == player.map[player.mapX][player.mapY].ySize / 2)
            {
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos].ground);
                if (player.map[player.mapX][player.mapY].difficulty.equals("easy"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(easy);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(medium);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(hard);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("start"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(finished);
                }
                player.mapX += 1;
                if (player.map[player.mapX][player.mapY] == null)
                {
                    player.map[player.mapX][player.mapY] = new Field(player.map, player.mapX, player.mapY, player);
                }
                if (player.mapX == 2 && player.mapY == 0)
                {
                    player.map[player.mapX][player.mapY].setMapIcon(shopPlayer);
                    player.openShop();
                    player.map[player.mapX][player.mapY].setMapIcon(shop);
                    player.mapX = 2;
                    player.mapY = 1;
                    player.xPos = 3;
                    player.yPos = 3;
                }
                else if (player.defaultSpawn || player.map[player.mapX][player.mapY].completed)
                {
                    player.xPos = 1;    
                    player.yPos = player.map[player.mapX][player.mapY].ySize / 2;
                }  
                player.map[player.mapX][player.mapY].showPlayer();
                if (player.map[player.mapX][player.mapY].difficulty.equals("easy"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(easyPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(mediumPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(hardPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("start"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(finishedPlayer);
                }
            }
            else if (player.xPos == player.map[player.mapX][player.mapY].xSize / 2 && player.yPos == 0)
            {
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos].ground);
                if (player.map[player.mapX][player.mapY].difficulty.equals("easy"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(easy);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(medium);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(hard);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("start"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(finished);
                }
                player.mapY -= 1;
                if (player.map[player.mapX][player.mapY] == null)
                {
                    player.map[player.mapX][player.mapY] = new Field(player.map, player.mapX, player.mapY, player);
                }
                if (player.mapX == 2 && player.mapY == 0)
                {
                    player.map[player.mapX][player.mapY].setMapIcon(shopPlayer);
                    player.openShop();
                    player.map[player.mapX][player.mapY].setMapIcon(shop);
                    player.mapX = 2;
                    player.mapY = 1;
                    player.xPos = 3;
                    player.yPos = 3;
                }
                else if (player.defaultSpawn || player.map[player.mapX][player.mapY].completed)
                {
                    player.xPos = player.map[player.mapX][player.mapY].xSize / 2;
                    player.yPos = player.map[player.mapX][player.mapY].ySize - 2;  
                }
                player.map[player.mapX][player.mapY].showPlayer();
                if (player.map[player.mapX][player.mapY].difficulty.equals("easy"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(easyPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(mediumPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(hardPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("start"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(finishedPlayer);
                }
            }
            else if (player.xPos == player.map[player.mapX][player.mapY].xSize / 2 && player.yPos == player.map[player.mapX][player.mapY].ySize - 1)
            {
                player.map[player.mapX][player.mapY].setGridDisplay(player.xPos, player.yPos, player.map[player.mapX][player.mapY].getObjectMatrix()[player.xPos][player.yPos].ground);
                if (player.map[player.mapX][player.mapY].difficulty.equals("easy"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(easy);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(medium);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(hard);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("start"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(finished);
                }
                player.mapY += 1;
                if (player.map[player.mapX][player.mapY] == null)
                {
                    player.map[player.mapX][player.mapY] = new Field(player.map, player.mapX, player.mapY, player);
                }
                if (player.mapX == 2 && player.mapY == 0)
                {
                    player.map[player.mapX][player.mapY].setMapIcon(shopPlayer);
                    player.openShop();
                    player.map[player.mapX][player.mapY].setMapIcon(shop);
                    player.mapX = 2;
                    player.mapY = 1;
                    player.xPos = 3;
                    player.yPos = 3;
                }
                else if (player.defaultSpawn || player.map[player.mapX][player.mapY].completed)
                {
                    player.xPos = player.map[player.mapX][player.mapY].xSize / 2;
                    player.yPos = 1;
                }
                player.map[player.mapX][player.mapY].showPlayer();
                if (player.map[player.mapX][player.mapY].difficulty.equals("easy"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(easyPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("medium"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(mediumPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("hard"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(hardPlayer);
                }
                else if (player.map[player.mapX][player.mapY].difficulty.equals("start"))
                {
                    player.map[player.mapX][player.mapY].setMapIcon(finishedPlayer);
                }
            }
        }
        catch (NullPointerException h)
        {
            System.out.println("KeyPress error");  
        }
        catch (ArrayIndexOutOfBoundsException l)
        {
        }
    }

    /**
     * updates the playScreen
     */
    public void refreshPlayScreen()
    {
        try
        {
            playScreen.remove(iconGrid);    
            JPanel iconGrid1 = new JPanel(new BorderLayout());       
            iconGrid1.setBorder(new EmptyBorder(2, 3, 2, 3));
            iconGrid1.setBackground(Color.GREEN.darker().darker());
            iconGrid1.setLayout(new GridLayout(player.map[player.mapX][player.mapY].ySize ,player.map[player.mapX][player.mapY].xSize));
            for (int j = 0; j < player.map[player.mapX][player.mapY].ySize; j++)
            {
                for(int i = 0; i < player.map[player.mapX][player.mapY].xSize; i++)
                {
                    iconGrid1.add(new JLabel(new ImageIcon(player.map[player.mapX][player.mapY].getGridDisplay()[i][j])));
                }
            }
            iconGrid = iconGrid1;
            playScreen.add(iconGrid1);
            playScreen.pack();
            playScreen.validate();
            playScreen.repaint();
        }
        catch(NullPointerException e)
        {
        }
        catch(IllegalArgumentException b)
        {
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            player.mapX = 2;
            player.mapY = 1;
            player.xPos = 3;
            player.yPos = 3;
        }
    }

    /**
     * updates the mapScreen
     */
    public void refreshMapScreen()
    {
        try
        {
            mapScreen.remove(mapGrid);
            JPanel mapGrid1 = new JPanel(new BorderLayout());  
            mapGrid1.setBorder(new EmptyBorder(2, 3, 2, 3));
            mapGrid1.setBackground(Color.BLACK);
            mapGrid1.setLayout(new GridLayout(6, 5));
            for (int j = 0; j < 6; j++)
            {
                for(int i = 0; i < 5; i++)
                {
                    if (j == 5)
                    {
                        if (i == 0)
                        {
                            if (player.maxHealth == 1)
                            {
                                if (player.health == 0)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a10)));
                                }
                                else if (player.health == 1)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a11)));
                                }
                            }
                            else if (player.maxHealth == 2)
                            {
                                if (player.health == 0)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a20)));
                                }
                                else if (player.health == 1)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a21)));
                                }
                                else if (player.health == 2)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a22)));
                                }
                            }
                            else if (player.maxHealth == 3)
                            {
                                if (player.health == 0)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a30)));
                                }
                                else if (player.health == 1)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a31)));
                                }
                                else if (player.health == 2)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a32)));
                                }
                                else if (player.health == 3)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a33)));
                                }
                            }
                            else if (player.maxHealth == 4)
                            {
                                if (player.health == 0)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a40)));
                                }
                                else if (player.health == 1)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a41)));
                                }
                                else if (player.health == 2)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a42)));
                                }
                                else if (player.health == 3)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a43)));
                                }
                                else if (player.health == 4)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(a44)));
                                }
                            }
                        }
                        else if (i == 1)
                        {
                            if (player.coinAmount == 0)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins0))); 
                            }
                            else if (player.coinAmount == 1)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins1)));
                            }
                            else if (player.coinAmount == 2)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins2)));
                            }
                            else if (player.coinAmount == 3)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins3)));
                            }
                            else if (player.coinAmount == 4)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins4)));
                            }
                            else if (player.coinAmount == 5)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins5)));
                            }
                            else if (player.coinAmount == 6)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins6)));
                            }
                            else if (player.coinAmount == 7)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins7)));
                            }
                            else if (player.coinAmount == 8)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins8)));
                            }
                            else if (player.coinAmount == 9)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins9)));
                            }
                            else if (player.coinAmount == 10)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins10)));
                            }
                            else if (player.coinAmount > 10)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(coins10plus)));
                            }
                        }
                        else if (i == 2)
                        {
                            if (player.stabCoolDown == 0)
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(swordStabReady)));
                            }
                            else
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(swordStabNotReady)));
                            }
                        }
                        else if (i == 3)
                        {
                            if (player.whirlLevel > 0)
                            {
                                if (player.whirlCoolDown <= 0)
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(whirlwindReady)));
                                }
                                else
                                {
                                    mapGrid1.add(new JLabel(new ImageIcon(whirlwindNotReady)));
                                }
                            }
                            else
                            {
                                mapGrid1.add(new JLabel(new ImageIcon(empty)));
                            }
                        }
                        else if (i == 4)
                        {
                            mapGrid1.add(new JLabel(new ImageIcon(empty)));
                        }
                        else if (i == 5)
                        {
                            mapGrid1.add(new JLabel(new ImageIcon(empty)));
                        }
                        else if (i == 6)
                        {
                            mapGrid1.add(new JLabel(new ImageIcon(empty)));
                        }
                    }
                    else if (player.map[i][j] == null)
                    {             
                        mapGrid1.add(new JLabel(new ImageIcon(empty)));
                    }
                    else
                    {
                        mapGrid1.add(new JLabel(new ImageIcon(player.map[i][j].getMapIcon())));
                    }
                }
            }
            mapGrid = mapGrid1;
            mapScreen.add(mapGrid);
            mapScreen.pack();
            mapScreen.validate();
            mapScreen.repaint();
        }
        catch(NullPointerException e)
        {
            System.out.println("Error in map refresh");
        }
    }

    /**
     * continuously refreshes both the playScreen and mapScreen
     * until the player dies, when it exits the gamn
     */
    public void run() 
    {
        try
        {
            while(player.alive) 
            {
                refreshPlayScreen();
                refreshMapScreen();
                Thread.sleep(100);
            }
            playScreen.dispatchEvent(new WindowEvent(playScreen, WindowEvent.WINDOW_CLOSING));
            mapScreen.dispatchEvent(new WindowEvent(playScreen, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } catch (InterruptedException e)
        {
            System.out.println("Thread interrupted.");
        }
    }
}