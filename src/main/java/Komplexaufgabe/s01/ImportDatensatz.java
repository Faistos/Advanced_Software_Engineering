package Komplexaufgabe.s01;

import Basisaufgabe.VrpGlobalSpan;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportDatensatz {
    private static final Character CSV_DELIMITER = ';';
    public static class container {
        public double[][] coords;
        public String[][] discr;
        public container(double[][] coords,String[][] discr){
            this.coords = coords;
            this.discr = discr;
        }
    }

    public static container parseCSV(String csvFilePath) {
        CSVFormat format = CSVFormat.newFormat(';');
        container container = null;
        try (Reader reader = new FileReader("src/main/java/Basisaufgabe/dataset.csv");

             CSVParser csvParser = new CSVParser(reader, format)) {
            List<double[]> coordinatesList = new ArrayList<>();
            List<String[]> descriptionList = new ArrayList<>();
            boolean first = true;
            for (CSVRecord csvRecord : csvParser) {
                if (first) {
                    first = false;
                } else {
                    String id = csvRecord.get(0);
                    String type = csvRecord.get(1);
                    double longitude = Double.parseDouble(csvRecord.get(2));
                    double latitude = Double.parseDouble(csvRecord.get(3));

                    double[] coordinates = {longitude, latitude};
                    coordinatesList.add(coordinates);
                    String[] description = {id, type};
                    descriptionList.add(description);
                }

            }
            double[][] coordinatesArray = new double[coordinatesList.size()][2];
            String[][] descriptionArray = new String[descriptionList.size()][2];
            for (int i = 0; i < coordinatesList.size(); i++) {
                coordinatesArray[i] = coordinatesList.get(i);
                descriptionArray[i] = descriptionList.get(i);

            }
            container = new container(coordinatesArray, descriptionArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return container;
    }

    public static double measure(double lat1,double  lon1,double  lat2,double  lon2){  // generally used geo measurement function
        var R = 6378.137; // Radius of earth in KM
        var dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        var dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        var d = R * c;
        return d * 0.621371; // miles
    }
    public static double[][] distances(container container) {
        List<Double> distanceList = new ArrayList<>();
        double[][] distanceArray = new double[container.coords.length][container.coords.length];
        for(int i = 0; i < container.coords.length; i++){
            for(int j = 0; j < container.coords.length; j++) {
                distanceArray[i][j] = measure(container.coords[i][0],container.coords[i][1],container.coords[j][0],container.coords[j][1]);
            }
        }

        for (double[] dist : distanceArray) {
            System.out.println(Arrays.toString(dist));
        }
        return distanceArray;
    }
}
