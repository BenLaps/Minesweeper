public class Main {
    public static void main(String[] args) {
        System.out.println("Start program!");

        javax.swing.SwingUtilities.invokeLater(() -> {
            Minesweeper game = new Minesweeper();
            game.setVisible(true);
        });
    }
}

