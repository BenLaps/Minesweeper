import javax.swing.*;
import java.awt.*;

public class MenuBar {
    private final Minesweeper game;

    public MenuBar(Minesweeper game) {
        this.game = game;
    }

    public void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> game.resetGame());
        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(e -> openSettingsDialog());
        JMenuItem statisticItem = new JMenuItem("Statistic");
        statisticItem.addActionListener(e -> game.openStatistic());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(settingsItem);
        gameMenu.add(statisticItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        JMenu levelMenu = new JMenu("Level");
        JMenuItem beginnerItem = new JMenuItem("Beginner");
        beginnerItem.addActionListener(e -> game.resetGame(8, 10));
        JMenuItem intermediateItem = new JMenuItem("Intermediate");
        intermediateItem.addActionListener(e -> game.resetGame(16, 40));
        JMenuItem expertItem = new JMenuItem("Expert");
        expertItem.addActionListener(e -> game.resetGame(30, 16, 99));

        JMenuItem customLevelItem = new JMenuItem("Custom Level");
        customLevelItem.addActionListener(e -> openSettingsDialog());
        levelMenu.add(beginnerItem);
        levelMenu.add(intermediateItem);
        levelMenu.add(expertItem);
        levelMenu.addSeparator();
        levelMenu.add(customLevelItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        JMenuItem ruleItem = new JMenuItem("Rule");
        ruleItem.addActionListener(e -> showRuleDialog());
        helpMenu.add(aboutItem);
        helpMenu.add(ruleItem);

        menuBar.add(gameMenu);
        menuBar.add(levelMenu);
        menuBar.add(helpMenu);

        game.setJMenuBar(menuBar);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(game,
                "Minesweeper\nVersion 1.0\nDeveloped by Protsak Roman (Ben_Laps)",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRuleDialog() {
        JOptionPane.showMessageDialog(game,
                "Minesweeper is a game where mines are hidden in a grid of squares. " +
                        "Safe squares have numbers telling you how many mines touch the square." +
                        "\nYou can use the number clues to solve the game by opening all of the " +
                        "safe squares. If you click on a mine you lose the game!\n\n\t" +
                        "You open squares with the left mouse button and put flags on mines " +
                        "with the right mouse button. Pressing the right mouse button again changes " +
                        "your flag into a question mark.\nWhen you open a square that does not touch " +
                        "any mines, it will be empty and the adjacent squares will automatically open " +
                        "in all directions until reaching squares that contain numbers.\n\n" +
                        "The three difficulty levels are Beginner (8x8 or 9x9 with 10 mines), " +
                        "Intermediate (16x16 with 40 mines) and Expert (30x16 with 99 mines). " +
                        "The game ends when all safe squares have been opened.\n" +
                        "A counter shows the number of mines without flags, " +
                        "and a clock shows your time in seconds. Minesweeper saves your " +
                        "best time for each difficulty level.", "Rule",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSettingsDialog() {
        JDialog settingsDialog = new JDialog(game, "Settings", true);
        settingsDialog.setSize(300, 200);
        settingsDialog.setLayout(new GridLayout(5, 2));
        JCheckBoxMenuItem hintsItem = new JCheckBoxMenuItem("Enable Hints");
        hintsItem.setSelected(game.isHintsEnabled());
        hintsItem.addActionListener(e -> game.setHintsEnabled(hintsItem.isSelected()));

        JLabel sizeXLabel = new JLabel("Field width:");
        JTextField sizeXField = new JTextField(String.valueOf(game.getSizeX()));
        JLabel sizeYLabel = new JLabel("Field height:");
        JTextField sizeYField = new JTextField(String.valueOf(game.getSizeY()));
        JLabel minesLabel = new JLabel("Number of mines:");
        JTextField minesField = new JTextField(String.valueOf(game.getMines()));
        JLabel countHitsLabel = new JLabel("Count available hits in game:");
        JTextField countHitsField = new JTextField(String.valueOf(game.getRemainingHints()));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            try {
                int newSizeX = Integer.parseInt(sizeXField.getText());
                int newSizeY = Integer.parseInt(sizeYField.getText());
                int newMines = Integer.parseInt(minesField.getText());
                int newHits = Integer.parseInt(countHitsField.getText());
                if (newSizeX > 0 && newSizeY > 0 && newMines > 0 && newMines < newSizeX * newSizeY && newHits >= 0) {
                    game.resetGame(newSizeX, newSizeY, newMines,newHits);
                    settingsDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(settingsDialog, "Invalid input. Please check the values.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(settingsDialog, "Invalid input. Please enter numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        settingsDialog.add(sizeXLabel);
        settingsDialog.add(sizeXField);
        settingsDialog.add(sizeYLabel);
        settingsDialog.add(sizeYField);
        settingsDialog.add(minesLabel);
        settingsDialog.add(minesField);
        settingsDialog.add(countHitsLabel);
        settingsDialog.add(countHitsField);
        settingsDialog.add(hintsItem);
        settingsDialog.add(okButton);

        settingsDialog.setVisible(true);
    }
}
