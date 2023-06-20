package Komplexaufgabe.s02;

import java.util.LinkedList;
import java.util.List;

public class AntColony {
    public final Google.DataModel data;
    public final LinkedList<Location> locations;
    public final long tankSize;
    public final double fuelUsage;

    public double[][] pheromoneMatrix;
    public final long maxDistancePerTank;

    public AntColony(Google.DataModel d, LinkedList<Location> locations, int tankSize, int usage100km) {
        data = d;
        this.locations = locations;
        this.tankSize = tankSize;
        fuelUsage = ((double) usage100km) / ((double) 100);
        maxDistancePerTank = (long) ((double) tankSize / fuelUsage);
        pheromoneMatrix = new double[locations.size()][locations.size()];
        for (int i = 0; i < pheromoneMatrix.length; i++) {
            for (int j = 0; j < pheromoneMatrix.length; j++) {
                pheromoneMatrix[i][j] = 0.01;
            }
        }
    }

    public List<Integer> createTHE_LIST() {
        List<Integer> a = new LinkedList<>();
        for (int i = 0; i < locations.size(); i++) {
            if (i == data.depot) {
                continue;
            }
            a.add(i);
        }
        return a;
    }

    public void startACO(long rounds) {
        for (long countdown = 0; countdown < rounds; countdown++) {
            Ant[] ants = new Ant[data.vehicleNumber * 2];
            for (int i = 0; i < ants.length; i++) {
                ants[i] = new Ant(pheromoneMatrix, 1, 1, maxDistancePerTank);
                ants[i].findPath(data.depot, createTHE_LIST(), maxDistancePerTank, data.distanceMatrix);
            }
            for (int i = 0; i < pheromoneMatrix.length; i++) {
                for (int j = 0; j < pheromoneMatrix.length; j++) {
                    pheromoneMatrix[i][j] /= 1.5;
                }
            }
            for (Ant ant : ants) {
                List<Integer> list = ant.getPath();
                for (int j = 1; j < list.size(); j++) {
                    pheromoneMatrix[list.get(j - 1)][list.get(j)] += 0.2;
                }
            }
        }
    }
}