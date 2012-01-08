package simpleaoc;

import java.util.ArrayList;
import javax.swing.*;
import java.text.DecimalFormat;
import java.awt.event.*;

public class Island {

    private int iLocation;
    private int iDice;
    public double islandCost;
    public ArrayList<String> islandOccupList = new ArrayList<String>();
    public JLabel occupLabel;
    public JLabel payoutLabel;
    public JLabel costLabel;
    public JLabel diceLabel;
    public JButton islandBuildButton;

    /*
     * creates a new island, with its cost and associated dice roll as parameters
     * allows it to call AoC, as well
     */

    public Island(double cost, int dice, final AoC aoc) {
        islandCost = cost;
        iDice = dice;
        costLabel = new JLabel("Cost: " + (cost));
        diceLabel = new JLabel("Dice: " + (dice));
        payoutLabel = new JLabel("Payout: 1");
        occupLabel = new JLabel("Occupants: " + islandOccupList);
        islandBuildButton = new JButton("Build");
        islandBuildButton.setVisible(false);
        islandBuildButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            aoc.buildClicked(iLocation);
            System.out.println("location: " + iLocation);
        }
        });
    }

    /*
     * payout is a function of how many occupants are on th island
     * since it is a double, we round it, to control for, e.g. 0.3333333...
     */
    public double getPayOut() {
        if (islandOccupList.size() == 0) {
            return 1;
        } else {
            return roundPayOut(1.0 / islandOccupList.size());
        }
    }

    double roundPayOut(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public double getCost() {
        return islandCost;
    }

    public void setLocation(int loc) {
        iLocation = loc;
    }

    public int getLocation() {
        return iLocation;
    }

    public int getDice() {
        return iDice;
    }

    public void addOccup(String name) {
        /*
         * each island has an ArrayList that stores its occupants
         * when an island is built on, this adds that player to list of occupants
         */
        islandOccupList.add(name);
        occupLabel.setText("<html>Occupants: <br>" + islandOccupList.toString().
                replaceAll(",", ",<br>") + "</html>");
        payoutLabel.setText("Payout: " + getPayOut());
    }
}
