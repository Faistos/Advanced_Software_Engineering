import Basisaufgabe.VrpGlobalSpan;
import Komplexaufgabe.s01.BruteForce;
import Komplexaufgabe.s01.ImportDatensatz;
import Komplexaufgabe.s02.AntHill;
import Komplexaufgabe.s02.Google;
import Komplexaufgabe.s02.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static Komplexaufgabe.s01.ImportDatensatz.distances;
import static Komplexaufgabe.s01.ImportDatensatz.parseCSV;

public class main {
    public static void main(String[] args) throws IOException {
        VrpGlobalSpan vrpGlobalSpan= new VrpGlobalSpan();

        vrpGlobalSpan.executeVRP();
        ImportDatensatz.container cont = parseCSV("dataset.csv");
        double[][] dist = distances(cont);
        vrpGlobalSpan.dataModel.setDistanceMatrix(dist);
        BruteForce bf = new BruteForce(dist, cont.discr, 4, 60);
        int[][] test = bf.getBestRoute();
        System.out.println(Arrays.deepToString(test));
        AntColonyOptimization aco = new AntColonyOptimization();

        aco.exec();
    }
}
