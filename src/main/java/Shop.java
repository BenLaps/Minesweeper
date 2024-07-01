import Configuration.ConfigManager;
import Configuration.eConfigPart;

import javax.swing.*;
import java.awt.*;

public class Shop {
    private final Minesweeper game;
    private int currentGold;

    private JLabel goldLabel;

    public Shop(Minesweeper game) {
        this.game = game;
        currentGold = ConfigManager.getPart ( eConfigPart.gold.getKey ( ) );
        openShop();
    }

    private void openShop() {

        JDialog shopDialog = new JDialog(game, "Shop", true);
        shopDialog.setSize(500, 300);
        shopDialog.setLayout(new GridLayout(10, 1));

        goldLabel = new JLabel("Gold: " + currentGold);

        JButton buyOneHitsButton = new JButton("Buy 1 hint for 10g");
        buyOneHitsButton.addActionListener(e -> {
            bySomePart(eConfigPart.hint,10,1);
        });

        JButton buyTenHitsButton = new JButton("Buy 10 hint for 100g");
        buyTenHitsButton.addActionListener(e -> {
            bySomePart(eConfigPart.hint,10,10);
        });

        JButton buyOneAntButton = new JButton("Buy 1 ant for 100g");
        buyOneAntButton.addActionListener(e -> {
            bySomePart(eConfigPart.ant,100,1);
        });

        JButton addGoldButton = new JButton("Add 10 gold");
        addGoldButton.addActionListener(e -> {
            currentGold += 10;
            UpdateGold();
            ConfigManager.setPart ( eConfigPart.gold.getKey(),String.valueOf (currentGold));
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            shopDialog.dispose();
        });

        shopDialog.add(goldLabel);
        shopDialog.add(buyOneHitsButton);
        shopDialog.add(buyTenHitsButton);
        shopDialog.add(buyOneAntButton);

        shopDialog.add(addGoldButton);
        shopDialog.add(closeButton);

        shopDialog.setVisible(true);
    }
    private void UpdateGold(){
        goldLabel.setText("Gold: " + currentGold);
    }

    private void bySomePart(eConfigPart configPart,int priceByOne, int count){
        if (currentGold >= priceByOne*count ) {
            currentGold -= priceByOne*count;
            ConfigManager.addByPart ( configPart,count );
            ConfigManager.setPart (eConfigPart.gold.getKey(),String.valueOf (currentGold));
            UpdateGold();
        } else {
            System.out.println("Not enough gold");
        }
    }
}
