import Basisaufgabe.VrpGlobalSpan;
import Komplexaufgabe.s01.BruteForce;
import Komplexaufgabe.s01.ImportDatensatz;

import java.util.List;

import static Komplexaufgabe.s01.ImportDatensatz.distances;
import static Komplexaufgabe.s01.ImportDatensatz.parseCSV;

public class main {
    public static void main(String[] args) {
        VrpGlobalSpan.executeVRP();
        ImportDatensatz.container cont = parseCSV("dataset.csv");
        double[][] dist = distances(parseCSV("dataset.csv"));

        BruteForce vrp = new BruteForce(dist, cont.discr, 4, 60);
        vrp.solve();
    }
}
