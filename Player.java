package simpleaoc;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class Player {

    private int points = 0;
    public double resources = 0;
    private String name;
    //playerProperty keeps track of which islands the player occupies
    public int[] playerProperty = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public JLabel pointLabel;
    public JLabel resourceLabel;
    public Color[] playerColor = new Color[]{Color.blue, Color.orange, Color.green, Color.red};

    public Player(String pName) {
        name = pName;
        pointLabel = new JLabel("Points: " + (points));
        resourceLabel = new JLabel("Resources: " + (roundResources(resources)));
    }

    double roundResources(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    /*
     * isNeighbor checks the playerProperty array for occupancy
     * the if statements control for the islands on the edges
     */
    public boolean isNeighbor(int n) {
        boolean boolIsNeighbor = false;
        if (n == 0) {
            if (playerProperty[n] > 0 || playerProperty[n + 1] > 0) {
                boolIsNeighbor = true;
            }
        } else if (n == 10) {
            if (playerProperty[n - 1] > 0 || playerProperty[n] > 0) {
                boolIsNeighbor = true;
            }
        } else {
            if (playerProperty[n] > 0 || playerProperty[n + 1] > 0 || playerProperty[n - 1] > 0) {
                boolIsNeighbor = true;
            }
        }
        return boolIsNeighbor;
    }

    public int getPoints() {
        return points;
    }

    void addPoints() {
        points++;
    }

    public void setPoints(int p) {
        points = points + p;
    }

    public double getResources() {
        return roundResources(resources);
    }

    public void addResources(double res) {
        resources += res;
        resourceLabel.setText("Resources: " + (roundResources(resources)));
    }

    public void subtractResouces(double res) {
        resources = resources - res;
        resourceLabel.setText("Resources: " + (roundResources(resources)));
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    void addProperty(int prop) {
        playerProperty[prop - 1]++;
    }

    public int findProperty(int pr) {
        return playerProperty[pr - 1];
    }


    public void pInitBuild(int prop, double res) {
    /*
     * adds the island to the player's playerProperty, by incrementing the
     * respective index of playerProperty
     * subtracts resources as a result of the build
     */
        playerProperty[prop - 1]++;
        resources = resources - res;
        resourceLabel.setText("Resources: " + (roundResources(resources)));
    }

    public void pBuild(int prop, double res) {
    /*
     * adds the island to the player's playerProperty, by incrementing the
     * respective index of playerProperty
     * subtracts resources as a result of the build
     * adds a point to the player's point total
     */
        playerProperty[prop]++;
        resources = resources - res;
        points++;
        resourceLabel.setText("Resources: " + (roundResources(resources)));
        pointLabel.setText("Points: " + (points));
    }

    public Color getColor(int c) {
        return playerColor[c];
    }
}


