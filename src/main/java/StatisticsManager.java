import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticsManager {
    private static final String STATISTICS_FILE = "statistics.txt";

    public static void saveStatistics(String playerName, int sizeX, int sizeY, int mines, int elapsedTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATISTICS_FILE, true))) {
            writer.write(playerName + ",\t" + sizeX + ",\t" + sizeY + ",\t" + mines + ",\t" + elapsedTime);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadStatistics() {
        List<String> statistics = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STATISTICS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                statistics.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statistics;
    }
}
