package Komplexaufgabe.s02;
import Basisaufgabe.VrpGlobalSpan;
import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;

import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Logger;

public class Google {
    private static final Logger logger = Logger.getLogger(VrpGlobalSpan.class.getName());

    static class DataModel {

        public final int vehicleNumber;
        public final int depot;
        public final long[][] distanceMatrix;
        public DataModel(LinkedList<Location> locations, int vehicles) {
            vehicleNumber = vehicles;
            int dept = 0;
            long[][] d = new long[locations.size()][locations.size()];
            for (int i = 0; i < locations.size(); i++) {
                d[i] = getPointDistanceTo(locations, locations.get(i));
                if (Objects.equals(locations.get(i).Type, "d")) {
                    dept = i;
                }
            }
            depot = dept;
            distanceMatrix = d;
        }

        private long[] getPointDistanceTo(LinkedList<Location> locations, Location self) {
            long[] dist = new long[locations.size()];
            for (int i = 0; i < locations.size(); i++) {
                if (locations.get(i).ID.equals(self.ID)) {
                    dist[i] = 0;
                    continue;
                }
                dist[i] = (long) self.getDistanceTo(locations.get(i));
            }
            return dist;
        }
    }

    /// @brief Print the solution.
    static void printSolution(
            DataModel data, RoutingModel routing, RoutingIndexManager manager, Assignment solution) {
        // Solution cost.
        logger.info("Objective : " + solution.objectiveValue());
        // Inspect solution.
        long maxRouteDistance = 0;
        for (int i = 0; i < data.vehicleNumber; ++i) {
            long index = routing.start(i);
            logger.info("Route for Vehicle " + i + ":");
            long routeDistance = 0;
            String route = "";
            while (!routing.isEnd(index)) {
                route += manager.indexToNode(index) + " -> ";
                long previousIndex = index;
                index = solution.value(routing.nextVar(index));
                routeDistance += routing.getArcCostForVehicle(previousIndex, index, i);
            }
            logger.info(route + manager.indexToNode(index));
            logger.info("Distance of the route: " + routeDistance + "m");
            maxRouteDistance = Math.max(routeDistance, maxRouteDistance);
        }
        logger.info("Maximum of the route distances: " + maxRouteDistance + "m");
    }

    public static void startProcess(LinkedList<Location> locations, int vehicles) throws Exception {
        Loader.loadNativeLibraries();
        // Instantiate the data problem.
        final DataModel data = new DataModel(locations,vehicles);

        // Create Routing Index Manager
        RoutingIndexManager manager =
                new RoutingIndexManager(data.distanceMatrix.length, data.vehicleNumber, data.depot);

        // Create Routing Model.
        RoutingModel routing = new RoutingModel(manager);

        // Create and register a transit callback.
        final int transitCallbackIndex =
                routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                    // Convert from routing variable Index to user NodeIndex.
                    int fromNode = manager.indexToNode(fromIndex);
                    int toNode = manager.indexToNode(toIndex);
                    return data.distanceMatrix[fromNode][toNode];
                });

        // Define cost of each arc.
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Add Distance constraint.
        routing.addDimension(transitCallbackIndex, 0, 3000,
                true, // start cumul to zero
                "Distance");
        RoutingDimension distanceDimension = routing.getMutableDimension("Distance");
        distanceDimension.setGlobalSpanCostCoefficient(100);

        // Setting first solution heuristic.
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .build();

        // Solve the problem.
        Assignment solution = routing.solveWithParameters(searchParameters);

        // Print solution on console.
        printSolution(data, routing, manager, solution);
    }
}
