package Komplexaufgabe.s02;

import java.util.LinkedList;
import java.util.*;

public class AntColonyOptimization {
    int vehicles = 10;
    int fuelUsage = 2;
    int tankSize = 60;
    LinkedList<Location> locations;

    AntColony antHill;

    public AntColonyOptimization(){
        this.locations = LocationReader.readPointsFromFile("src/main/java/Basisaufgabe/dataset.csv");
        Google.DataModel data = new Google.DataModel(this.locations,vehicles);
        this.antHill = new AntColony(data, locations, tankSize, fuelUsage);

    }

    public void exec(){
        this.antHill.startACO(10);

        for (double[] Pphero: antHill.pheromoneMatrix ) {
            System.out.println(Arrays.toString(Pphero));
        }

    }
}

