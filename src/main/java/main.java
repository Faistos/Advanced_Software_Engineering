import Basisaufgabe.VrpGlobalSpan;
import Komplexaufgabe.s01.BruteForce;
import Komplexaufgabe.s01.ImportDatensatz;

import java.util.Arrays;
import java.util.List;

import static Komplexaufgabe.s01.ImportDatensatz.distances;
import static Komplexaufgabe.s01.ImportDatensatz.parseCSV;

public class main {
    public static void main(String[] args) {
        ImportDatensatz.container cont = parseCSV("dataset.csv");
        double[][] dist = distances(cont);
        BruteForce bf = new BruteForce(dist, cont.discr, 4, 60);
        int[][] test = bf.getBestRoute();
        System.out.println(Arrays.deepToString(test));
    }
}
