package simpleaoc;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class AoC {

    //declare game variables
    int numOfIslands = 11;
    int activePlayer = 0;
    int winPoints = 7;
    boolean win = false;
    int numOfPlayers;
    Player[] playerArray;
    Island[] islandArray;
    final double[] islandCostArray = {1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1};
    ArrayList<Integer> IntIslandDice;
    JFrame gameFrame = new JFrame();
    String playerNameEntry;
    JLabel islandLabel;
    DicePanel dicePanel;
    JPanel allIslandPanel;
    JPanel[] islandPanelArray;
    JPanel[] islandInfoPanel;
    JPanel[] islandBuildPanel;
    JPanel[] playerPanelArray;
    JPanel playerPanel;
    JPanel playerDicePanel;
    int buildLocation;
    boolean canBuild;
    boolean wantToBuild;

    public static void main(String[] args) {
        try {
        //create a new instance of the game, and instantiate it
        AoC game = new AoC();
        game.go();
        } catch (Exception e) {
            System.out.println("error: "+e);
        }
    } // close main method

    public void go() {
        //setup the game, then listen for ActionListeners in rollOutput and buildClicked
        displayInstrux();
        setupIslands();
        setupPlayers();
        setupGui();
        initialBuild();
    } // close go method

    public void displayInstrux() {
        JOptionPane.showMessageDialog(gameFrame, "Welcome to Archipelago of Catan!\n" +
                "- The objective of the game is to build 7 houses, each worth 1 point.\n" +
                "- Each player will be initially allocated 6 \"resources.\"\n" +
                "- The cost (in Resources) to build on an island is proportionate to the frequency\n" +
                "    with which the island's corresponding dice roll is rolled.\n" +
                "    - For example, an island with a dice roll of 7 will be more expensive than an island with a 12.\n" +
                "- An island's payout is inversely related to how many houses are on the island.\n" +
                "- The island's payout will always add up to ~1.00. If there are 3 players on an island,\n" +
                "    each will receive 0.33 resources.\n" +
                "- A player can only build a house on an island they either occupy or are a neighbor of.\n" +
                "- When building a house, the player's resources will be deducted from their total.\n\n" +
                "Good luck!");
    }

    public void setupIslands() {
        //setup Islands, along with corresponding dice roll and location
        IntIslandDice = new ArrayList<Integer>();
        ArrayList<Integer> setIslandDice = new ArrayList<Integer>();
        for (int i = 2; i < numOfIslands + 2; i++) {
            setIslandDice.add(new Integer(i));
        }
        /* assigns a unique random number (dice roll) to the island, by removing each number from setIslandDice,
        *  so that it is not used again.
        */
        while (!setIslandDice.isEmpty()) {
            int randomIndex = (int) (Math.random() * setIslandDice.size());
            IntIslandDice.add(setIslandDice.get(randomIndex));
            setIslandDice.remove(randomIndex);
        }
        //sets the dice roll and location of the island
        islandArray = new Island[numOfIslands];
        for (int i = 0; i < numOfIslands; i++) {
            int dice = IntIslandDice.get(i);
            islandArray[i] = new Island(islandCostArray[dice - 2], dice, this);
            islandArray[i].setLocation(i);
        }
    } // close setupIslands

    public void setupPlayers() {

        // setup players, and ask for names
        String stringNumOfPlayers = JOptionPane.showInputDialog(gameFrame, "How many players? (1-4)");
        numOfPlayers = Integer.parseInt(stringNumOfPlayers);
        playerArray = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            playerArray[i] = new Player(playerNameEntry);
            playerNameEntry = JOptionPane.showInputDialog(gameFrame, "What is player " + (i + 1) + "'s name?");
            playerArray[i].setName(playerNameEntry);
            playerArray[i].addResources(6);
        }
    } // close setupPlayers

    public void setupGui() {
        /* main panel is divided into 3 sections
         * 1) islands, with info about each island
         * 2) player info
         * 3) animation of dice roll
         */

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        setupIslandPanel();
        setupPlayerDicePanel();

        mainPanel.add(allIslandPanel, BorderLayout.CENTER);
        mainPanel.add(playerDicePanel, BorderLayout.EAST);
        gameFrame.add(mainPanel);
        gameFrame.setVisible(true);
        gameFrame.setSize(1250, 500);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } //close setupGui

    public void setupIslandPanel() {

        /*
         * add island info, as well as build buttons
         */

        allIslandPanel = new ImagePanel("/backgrounds/water.jpeg");
        allIslandPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
        allIslandPanel.setPreferredSize(new Dimension(1150, 500));

        islandPanelArray = new JPanel[numOfIslands];
        islandInfoPanel = new JPanel[numOfIslands];
        islandBuildPanel = new JPanel[numOfIslands];


        for (int i = 0; i < numOfIslands; i++) {
            islandPanelArray[i] = new ImagePanel("/backgrounds/sand.jpeg");
            islandPanelArray[i].setLayout(new BorderLayout());

            islandInfoPanel[i] = new ImagePanel("/backgrounds/sand.jpeg");
            islandInfoPanel[i].setLayout(new BoxLayout(islandInfoPanel[i], BoxLayout.Y_AXIS));
            islandLabel = new JLabel((new StringBuilder()).append("Island ").append(i + 1).toString());
            islandInfoPanel[i].add(islandLabel);
            islandInfoPanel[i].add(islandArray[i].diceLabel);
            islandInfoPanel[i].add(islandArray[i].costLabel);
            islandInfoPanel[i].add(islandArray[i].payoutLabel);
            islandInfoPanel[i].add(islandArray[i].occupLabel);
            islandInfoPanel[i].setPreferredSize(new Dimension(95, 195));

//           islandBuildPanel[i].add(islandArray[i].islandBuildButton);

            islandPanelArray[i].add(islandInfoPanel[i], BorderLayout.CENTER);
            islandPanelArray[i].add(islandArray[i].islandBuildButton, BorderLayout.SOUTH);

            allIslandPanel.add(islandPanelArray[i]);
        }
    }

    public void setupPlayerDicePanel() {

        /*
         * add player panels, with player info
         * add dice animation panel
         */

        playerDicePanel = new JPanel();
        playerDicePanel.setLayout(new BorderLayout());
        playerPanel = new JPanel();
        dicePanel = new DicePanel(this);
        playerPanelArray = new JPanel[numOfPlayers];

        playerDicePanel.add(playerPanel, BorderLayout.NORTH);
        playerDicePanel.add(dicePanel, BorderLayout.SOUTH);

        playerPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        playerPanel.setLayout(new GridLayout(numOfPlayers, 1));

        dicePanel.setBorder(BorderFactory.createLineBorder(Color.green));

        for (int i = 0; i < numOfPlayers; i++) {
            playerPanelArray[i] = new JPanel();
            playerPanelArray[i].setLayout(new BoxLayout(playerPanelArray[i], BoxLayout.Y_AXIS));
            playerPanelArray[i].setBorder(BorderFactory.createRaisedBevelBorder());
            // playerPanelArray[i].setPreferredSize(new Dimension(120,150));
            playerPanelArray[i].add(new JLabel(playerArray[i].getName()));
            playerPanelArray[i].add(playerArray[i].pointLabel);
            playerPanelArray[i].add(playerArray[i].resourceLabel);
            playerPanelArray[i].setBackground(playerArray[i].playerColor[i]);
            playerPanel.add(playerPanelArray[i]);
        }

    }

    public void initialBuild() {
        /* build initial house
         * similar to normal build, but does not check for beighbor,
         * since players do not have any houses established
         */
        for (int i = 0; i < playerArray.length; i++) {

            String strInitBuild = JOptionPane.showInputDialog(gameFrame, playerArray[i].getName() + ", where would you like to place your initial House?");
            int initialBuild = Integer.parseInt(strInitBuild);
            playerArray[i].pInitBuild(initialBuild, islandArray[initialBuild - 1].getCost());
            islandArray[initialBuild - 1].addOccup(playerArray[i].getName());
        }
    }// close initialBuild

    public void rollOutput() {
        /*
         * roll the dice, and use output to determine resource allocation
         * roll also invokes the build() method
         */
        
        int thisRoll = dicePanel.dRoll();
        System.out.println(thisRoll);
        Integer IntegerPaidIsland = (IntIslandDice.indexOf(thisRoll) + 1);
        int paidIsland = IntegerPaidIsland.intValue();
        JOptionPane.showMessageDialog(gameFrame, "Dice roll: " + thisRoll
                + "\nPaid Island: " + paidIsland);

        //allocate resoucres to the players, as a result of dice roll
        for (int i = 0; i < numOfPlayers; i++) {
            if (playerArray[i].findProperty(paidIsland) > 0) {
                JOptionPane.showMessageDialog(gameFrame, playerArray[i].getName() + " receives "
                        + ((playerArray[i].findProperty(paidIsland)) * (islandArray[paidIsland - 1].getPayOut()))
                        + " resource(s)");
                playerArray[i].addResources((playerArray[i].findProperty(paidIsland)) * (islandArray[paidIsland - 1].getPayOut()));
            }
        }
        //display allocation of resources
        for (int i = 0; i < numOfPlayers; i++) {
            System.out.println(playerArray[i].getName() + " has " + playerArray[i].getResources() + " resources");
            System.out.println(playerArray[i].getName() + " has " + playerArray[i].getPoints() + " points");
        }
        build();
//          } catch (Exception e) {}

    }//close rollOutput

    public void build() {

        /*
         * check if player has any resources available
         * ask player if he wants to build
         * if so, invoke buildClicked()
         */
        canBuild = true;
        while (canBuild == true) {
            wantToBuild = true;
            if ((playerArray[activePlayer].getResources() > 0) && (wantToBuild == true)) {
                int buildChoice = JOptionPane.showConfirmDialog(gameFrame, playerArray[activePlayer].getName() + ", do you want to build?",
                        null, JOptionPane.YES_NO_OPTION);
                if (buildChoice == JOptionPane.NO_OPTION) {
                    wantToBuild = false;
                    canBuild = false;
                    nextPlayer();

                } else {
                    for (int i = 0; i < numOfIslands; i++) {
                        if (((playerArray[activePlayer].isNeighbor(islandArray[i].getLocation())) == true)
                        && (playerArray[activePlayer].getResources() >= islandArray[i].getCost())) {
                        islandArray[i].islandBuildButton.setVisible(true);
                        }
                    }
                    break;
                }
            } else {
                System.out.println(playerArray[activePlayer].getName() + " has no resources");

                nextPlayer();
                break;
            }
            for (int i = 0; i < numOfIslands; i++) {
                islandArray[i].islandBuildButton.setVisible(false);
            }
        }//close canBuild

    } //close build

    public void buildClicked(int buildLocation) {
        /*
         * listen for ActionListener in Island
         * determine if player can build on island they selected
         * if they can build:
         *  add house to island
         *  subtract resources from player
         *  add points to player
         *  check if player wins
         *  loops back to build()
         */
        if ((playerArray[activePlayer].isNeighbor(buildLocation) == false)
                && (playerArray[activePlayer].getResources() >= islandArray[buildLocation].getCost())) {
            JOptionPane.showMessageDialog(gameFrame, "Sorry, you're not a neighbor.");
            build();
        } else if ((playerArray[activePlayer].isNeighbor(buildLocation) == true)
                && (playerArray[activePlayer].getResources() < islandArray[buildLocation].getCost())) {
            JOptionPane.showMessageDialog(gameFrame, "Sorry, you do not have enough resources.");
            build();
        } else if ((playerArray[activePlayer].isNeighbor(buildLocation) == false)
                && (playerArray[activePlayer].getResources() < islandArray[buildLocation].getCost())) {
            JOptionPane.showMessageDialog(gameFrame, "Sorry, you do not have enough resources and you're not a neighbor.");
            build();
        } else {//player is eligible to build
               /*
             * adds to playr's property
             * adds to player's points
             * deducts player's resources
             * adds player's name to island's occupant list
             */

            playerArray[activePlayer].pBuild(buildLocation, islandArray[buildLocation].getCost());
            islandArray[buildLocation].addOccup(playerArray[activePlayer].getName());

            for (int i = 0; i < numOfPlayers; i++) {
                System.out.println(playerArray[i].getName() + " has " + playerArray[i].getResources() + " resources");
                System.out.println(playerArray[i].getName() + " has " + playerArray[i].getPoints() + " points\n");
            }

            if (playerArray[activePlayer].getPoints() == winPoints) {
                JOptionPane.showMessageDialog(gameFrame, playerArray[activePlayer].getName() + " wins!");
            }
            build();

        }
    }

    public void nextPlayer() {
        //advances activePlayer to next player
        if (activePlayer < numOfPlayers - 1) {
            activePlayer++;
        } else {
            activePlayer = 0;
        }
    }
}// close AoC class
