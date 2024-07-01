import Configuration.ConfigManager;
import Configuration.eConfigPart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minesweeper extends JFrame {
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_MINES = 10;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int DEFAULT_COUNT_FLAGS = 0;
    private static final int DEFAULT_BTN_MIN_WIDTH = 50;
    private static final int DEFAULT_BTN_MIN_HEIGHT = 50;
    private static final int DEFAULT_HINTS = 3;

    private int sizeX;
    private int sizeY;
    private int mines;
    private int hits;
    private int countFlags;
    private int elapsedTime;
    private int remainingHints;
    private boolean hintsEnabled;
    private Timer timer;
    private JButton[][] buttons;
    private boolean[][] mineField;
    private boolean[][] revealed;
    private int[][] flagged; // 0: not flagged, 1: flagged, 2: question mark
    private int[][] bonus;

    private JLabel mineCountLabel;
    private JLabel timerLabel;
    private JLabel hintLabel;
    private JPanel topPanel;
    private JButton hintButton;

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getMines() {
        return mines;
    }

    public int getRemainingHints() {
        return remainingHints;
    }

    public boolean isHintsEnabled() {
        return hintsEnabled;
    }

    public void setHintsEnabled(boolean hintsEnabled) {
        this.hintsEnabled = hintsEnabled;
    }

    public Minesweeper() {
        this.sizeX = DEFAULT_SIZE;
        this.sizeY = DEFAULT_SIZE;
        this.mines = DEFAULT_MINES;
        this.countFlags = DEFAULT_COUNT_FLAGS;
        this.elapsedTime = 0;
        this.remainingHints = DEFAULT_HINTS;
        this.hintsEnabled = true;
        initUI();
    }

    private void initUI() {
        setTitle("Minesweeper");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        mineCountLabel = new JLabel("Mines: " + (mines - countFlags));
        timerLabel = new JLabel("Time: " + elapsedTime + "s");
        hintLabel = new JLabel("Hints: " + remainingHints);

        leftPanel.add(mineCountLabel);
        leftPanel.add(timerLabel);
        leftPanel.add(hintLabel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        hintButton = new JButton("Hint");
        hintButton.addActionListener(e -> provideHint());
        JButton resizeButton = new JButton("Resize");
        resizeButton.addActionListener(e -> resizeToFit());

        rightPanel.add(hintButton);
        rightPanel.add(resizeButton);



        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        timer = new Timer(1000, e -> {
            elapsedTime++;
            timerLabel.setText("Time: " + elapsedTime + "s");
        });

        initializeGame();
        new MenuBar(this).createMenuBar();
    }

    private void initializeGame() {
        buttons = new JButton[sizeX][sizeY];
        mineField = new boolean[sizeX][sizeY];
        revealed = new boolean[sizeX][sizeY];
        flagged = new int[sizeX][sizeY];
        bonus = new  int[sizeX][sizeY];
        hits = remainingHints;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(sizeX, sizeY));

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(40, 40));
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 14));
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    boolean isPressed = false;

                    @Override
                    public void mousePressed(MouseEvent e) {
                        isPressed = true;
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (isPressed) {
                            JButton button = (JButton) e.getSource();
                            int x = -1, y = -1;
                            for (int i = 0; i < sizeX; i++) {
                                for (int j = 0; j < sizeY; j++) {
                                    if (buttons[i][j] == button) {
                                        x = i;
                                        y = j;
                                    }
                                }
                            }
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                revealCell(x, y);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                flagCell(x, y);
                            }
                            checkWin();
                        }
                        isPressed = false;
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isPressed = false;
                    }
                });
                panel.add(buttons[i][j]);
            }
        }

        add(panel, BorderLayout.CENTER);

        if (isHintsEnabled())
            hintLabel.setText("Hints: " + hits);
        hintLabel.setVisible(isHintsEnabled());
        hintButton.setVisible(isHintsEnabled());
        placeMines();
        resetTimer();
        resizeToFit();
    }

    public void resetGame() {
        getContentPane().removeAll();
        add(topPanel, BorderLayout.NORTH);
        countFlags = 0;
        initializeGame();
        revalidate();
        repaint();
    }

    public void resetGame(int size, int mines) {
        this.sizeX = size;
        this.sizeY = size;
        this.mines = mines;
        resetGame();
    }


    public void resetGame(int sizeX, int sizeY, int mines,int hits) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.mines = mines;
        this.remainingHints = hits;
        resetGame();
    }

    public void resetGame(int size, int mines, int hits) {
        this.sizeX = size;
        this.sizeY = size;
        this.mines = mines;
        this.remainingHints = hits;
        resetGame();
    }

    private void placeMines() {
        Random rand = new Random();
        int placedMines = 0;
        while (placedMines < mines) {
            int x = rand.nextInt(sizeX);
            int y = rand.nextInt(sizeY);
            if (!mineField[x][y] ) {
                mineField[x][y] = true;
                placedMines++;

            }
        }
        for (int i = 0;i<mines;i++){
            if (rand.nextInt ( 1000 )>=970){
                int x =rand.nextInt(sizeX);
                int y = rand.nextInt(sizeY);
                if (!mineField[x][y]){
                    bonus[x][y]=1;
                    System.out.println ( x +" "+y );
                }
            }
        }
        mineCountLabel.setText("Mines: " + (mines - countFlags));
    }

    private void revealCell(int x, int y) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY || revealed[x][y] || flagged[x][y] == 1) {
            return;
        }
        if(bonus[x][y]==1)
            buttons[x][y].setText ( buttons[x][y].getText ()+"B" );
        revealed[x][y] = true;
        buttons[x][y].setEnabled(false);

        if (mineField[x][y]) {
            buttons[x][y].setText(buttons[x][y].getText ()+"M");
            buttons[x][y].setBackground(Color.RED);
            gameOver();
        } else {
            int mineCount = countAdjacentMines(x, y);
            if (mineCount > 0) {
                buttons[x][y].setText(String.valueOf(mineCount));

            } else {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        revealCell(x + i, y + j);
                    }
                }
            }
        }

    }

    private void flagCell(int x, int y) {
        if (revealed[x][y]) {
            return;
        }

        if (flagged[x][y] == 0) {
            flagged[x][y] = 1;
            buttons[x][y].setText("F");
            countFlags++;
        } else if (flagged[x][y] == 1) {
            flagged[x][y] = 2;
            buttons[x][y].setText("?");
            countFlags--; // Question marks do not count as flags
        } else {
            flagged[x][y] = 0;
            buttons[x][y].setText("");
        }
        mineCountLabel.setText("Mines: " + (mines - countFlags));
    }

    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < sizeX && ny >= 0 && ny < sizeY && mineField[nx][ny]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void checkWin() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (!mineField[i][j] && !revealed[i][j]) {
                    return;
                }
            }
        }
        timer.stop();
        String playerName = JOptionPane.showInputDialog(this, "You Win!\nYour time: " + elapsedTime + "s\nEnter your name:");
        ConfigManager.addByPart ( eConfigPart.gold,mines );
        if (playerName != null && !playerName.trim().isEmpty()) {

            StatisticsManager.saveStatistics(playerName, sizeX, sizeY, mines, elapsedTime);

        }
    }

    private void gameOver() {
        timer.stop();
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (mineField[i][j]) {
                    if (buttons[i][j].getText ()=="F")
                        ConfigManager.addByPart ( eConfigPart.gold, 1 );
                    buttons[i][j].setText(buttons[i][j].getText ()+"M");

                }
                if(bonus[i][j]==1)
                    buttons[i][j].setText ( buttons[i][j].getText ()+"B" );
                buttons[i][j].setEnabled(false);
            }
        }

        JOptionPane.showMessageDialog(this, "Game Over!");
        resetGame();
    }

    private void resetTimer() {
        elapsedTime = 0;
        timerLabel.setText("Time: " + elapsedTime + "s");
        timer.restart();
    }

    public void openStatistic() {
        List<String> statistics = StatisticsManager.loadStatistics();
        StringBuilder statsText = new StringBuilder("Player, Width, Height, Mines, Time\n");
        for (String stat : statistics) {
            statsText.append(stat).append("\n");
        }
        JOptionPane.showMessageDialog(this, statsText.toString(), "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resizeToFit() {
        int preferredWidth = getWidth();
        int preferredHeight = getHeight();
        int totalButtonWidth = DEFAULT_BTN_MIN_WIDTH * sizeX;
        int totalButtonHeight = DEFAULT_BTN_MIN_HEIGHT * sizeY;
        preferredWidth = totalButtonWidth;
        preferredHeight = totalButtonHeight;
        setSize(preferredWidth, preferredHeight);
    }

    private void provideHint() {
        if (!hintsEnabled || hits <= 0) {
            JOptionPane.showMessageDialog(this, "No more hints available or hints are disabled.");
            return;
        }

        List<Point> potentialHints = new ArrayList<>();
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (!revealed[i][j] && flagged[i][j] == 2 && buttons[i][j].getBackground()!=Color.GREEN && buttons[i][j].getBackground()!=Color.RED) {
                    potentialHints.add(new Point(i, j));
                }
            }
        }

        if (potentialHints.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No selected cells available for hint.\nFor hit use \"?\"");
            return;
        }

        Random rand = new Random();
        Point hintPoint = potentialHints.get(rand.nextInt(potentialHints.size()));
        if(mineField[hintPoint.x][hintPoint.y])
        {
            buttons[hintPoint.x][hintPoint.y].setBackground(Color.RED);
        }
        if(!mineField[hintPoint.x][hintPoint.y]&&!revealed[hintPoint.x][hintPoint.y])
        {
            buttons[hintPoint.x][hintPoint.y].setBackground(Color.GREEN);
        }
//        revealCell(hintPoint.x, hintPoint.y);
        hits--;
        hintLabel.setText("Hints: " + hits);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Minesweeper game = new Minesweeper();
            game.setVisible(true);
        });
    }
}
