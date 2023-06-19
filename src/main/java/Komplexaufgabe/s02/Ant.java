package Komplexaufgabe.s02;
import java.util.*;

public class Ant {
    private final List<Integer> path;
    private final double[][] pheromoneMatrix;
    private final double alpha;
    private final double beta;
    private final long tankFill;

    public Ant(double[][] pheromoneMatrix, double alpha, double beta, long tankFill) {
        this.pheromoneMatrix = pheromoneMatrix;
        this.alpha = alpha;
        this.beta = beta;
        this.path = new ArrayList<>();
        this.tankFill = tankFill;
    }

    public void findPath(int currentCity, List<Integer> allowedCities, long tankSize, long[][] distanceM) {
        path.add(currentCity);
        if (currentCity == path.get(0)) {
            tankSize = tankFill;
        }

        if (allowedCities.isEmpty()) {
            path.add(path.get(0));
        } else {
            double[] probabilities = calculateProbabilities(currentCity, allowedCities, distanceM, tankSize,path.get(0));
            int nextCity = selectNextCity(allowedCities, probabilities,path.get(0));
            allowedCities.remove(Integer.valueOf(nextCity));
            tankSize -= distanceM[currentCity][nextCity];
            findPath(nextCity, allowedCities,tankSize,distanceM);
        }
    }

    private double[] calculateProbabilities(int currentCity, List<Integer> allowedCities, long[][] distanceM,long tankSize,int depot) {
        int numCities = pheromoneMatrix.length;
        double[] probabilities = new double[numCities];
        double total = 0.0;

        for (int city : allowedCities) {
            long toPointAndDepot = distanceM[currentCity][city] + distanceM[city][depot];
            if (toPointAndDepot > tankSize) {
                probabilities[city] = 0;
                continue;
            }
            probabilities[city] = Math.pow(pheromoneMatrix[currentCity][city], alpha) *
                    Math.pow(1.0 / ((double) distanceM[currentCity][city]), beta);

            total += probabilities[city];
        }

        if (total == 0) {
            probabilities[0] = -1;
            return probabilities;
        }

        for (int city : allowedCities) {
            probabilities[city] /= total;
        }

        return probabilities;
    }

    private int selectNextCity(List<Integer> allowedCities, double[] probabilities, int depot) {
        if (probabilities[0] == -1) {
            return depot;
        }

        double random = Math.random();
        double cumulativeProbability = 0.0;

        for (int city : allowedCities) {
            cumulativeProbability += probabilities[city];
            if (random <= cumulativeProbability) {
                return city;
            }
        }
        return allowedCities.get(0);
    }

    public List<Integer> getPath() {
        return path;
    }
}
