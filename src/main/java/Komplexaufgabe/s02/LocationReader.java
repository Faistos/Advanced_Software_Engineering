package Komplexaufgabe.s02;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class LocationReader {
    public static LinkedList<Location> readPointsFromFile(String csvFile) {
        LinkedList<Location> locations = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                String id = data[0].trim();
                String type = data[1].trim();
                double longitude = Double.parseDouble(data[2].trim());
                double latitude = Double.parseDouble(data[3].trim());
                Location location = new Location(id, type, longitude, latitude);
                locations.add(location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locations;
    }
}