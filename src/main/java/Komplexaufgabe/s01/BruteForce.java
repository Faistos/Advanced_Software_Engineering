package Komplexaufgabe.s01;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Random;

public class BruteForce {
    private static final int MAX_ITERATIONS = 1000000;

    private final double[][] distanceMatrix;
    private final String[][] nodeTypes;
    private final double maxTankCapacity;
    private final double consumptionRate;
    private int bestRouteDistance;
    private final boolean[] mask;

    private final int numVehicles;
    int[][] routePerVehicle;

    public BruteForce(double[][] distanceMatrix, String[][] nodeTypes, int numVehicles, int maxTankCapacity) {
        this.distanceMatrix = distanceMatrix;
        this.nodeTypes = nodeTypes;
        this.maxTankCapacity = maxTankCapacity;
        this.mask = new boolean[nodeTypes.length];
        this.bestRouteDistance = Integer.MAX_VALUE;
        this.consumptionRate = 0.2;
        this.routePerVehicle = new int[numVehicles][nodeTypes.length];
        this.numVehicles = numVehicles;
    }

    private void findRoute() throws IOException {

        int[][] bestTempRoute = new int[this.numVehicles][this.nodeTypes.length];
        FileWriter fileWriter = new FileWriter("bruteforce_gvrp.log");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        double bestDis = 9999999999999.;
        double dist = 0;
        int currentPosition = 0;
        double currentFuel = this.maxTankCapacity;
        Random rand = new Random();
        int skip = generatemask();
        int vehicle;
        int upperbound = this.nodeTypes.length - skip;
        long starttime = System.currentTimeMillis();
        for(int i = 0; i < MAX_ITERATIONS; i++){
            int stops = 0;
            int[][] tempRoute = new int[this.numVehicles][Math.round((float) this.nodeTypes.length /(numVehicles-1))];
            for (int j=0; j < tempRoute.length; j++){
                int OOF =0;
                boolean firstIn = true;
                dist = 0;
                for (int k = 1; k < tempRoute[j].length; k++) {
                        tempRoute[j][k]=0;
                        int nextpos = skip  + rand.nextInt(upperbound);
                        if(currentPosition == 0 && !firstIn) break;
                        if(allVisited(skip)) break;
                        firstIn = false;
                        if(k == tempRoute[j].length-1) nextpos = 0;
                        if(currentPosition == nextpos){
                            k--;
                        }
                        else if(currentFuel - this.distanceMatrix[findNearestF(nextpos, skip)][nextpos]<= 0){
                            nextpos = findNearestF(currentPosition, skip);
                            dist = dist + this.distanceMatrix[currentPosition][nextpos];
                            currentFuel = maxTankCapacity;
                            tempRoute[j][k]=nextpos;
                            currentPosition = nextpos;
                        }
                        else if(currentFuel - findNearestF(nextpos, skip)> 0){
                            if(!willDuplicate(skip,nextpos, tempRoute)){
                                dist = dist + this.distanceMatrix[currentPosition][nextpos];
                                currentFuel = currentFuel - (this.distanceMatrix[currentPosition][nextpos]*consumptionRate);
                                tempRoute[j][k]=nextpos;
                                currentPosition = nextpos;
                                stops++;
                            }
                            else{
                                k--;
                                OOF++;
                                if(OOF == 20){
                                    dist = dist + this.distanceMatrix[currentPosition][0];
                                    currentFuel = currentFuel - (this.distanceMatrix[currentPosition][0]*consumptionRate);
                                    tempRoute[j][k]=0;
                                    currentPosition = 0;
                                }
                            }
                        }
                        else  {
                            dist = dist + this.distanceMatrix[currentPosition][0];
                            currentFuel = currentFuel - (this.distanceMatrix[currentPosition][0]*consumptionRate);
                            tempRoute[j][k]=0;
                            currentPosition = 0;
                        }


                }
                if(allVisited(skip)) break;
            }
            if ( dist < bestDis && dist != 0){
                bestDis = dist;
                bestTempRoute = tempRoute;

                String str = String.valueOf(System.currentTimeMillis())+"|"+i+"|"+ this.numVehicles + stops + "|" + (System.currentTimeMillis()-starttime)+"|"+ dist ;

                printWriter.printf(str + "\n");


            }


        }
        printWriter.close();
        this.routePerVehicle = bestTempRoute;

    }

    public int[][] getBestRoute() throws IOException {
        findRoute();
        return this.routePerVehicle;
    }
    private boolean allVisited(int skip){
        boolean all = true;
        boolean[] visited= new boolean[this.nodeTypes.length-skip];
        for(int i = skip; i< this.routePerVehicle.length; i++){
            for(int j = 0; j< this.routePerVehicle[i].length; j++){
                visited[this.routePerVehicle[i][j]]= true;
            }
        }
        for (boolean b : visited) {
            if (!b) {
                all = false;
                break;
            }
        }
        return all;

    }
    private boolean isDuplicated(int skip){
        for(int i = skip; i< this.routePerVehicle.length; i++){
            for(int j = 0; j< this.routePerVehicle[i].length; j++){
                int comp = this.routePerVehicle[i][j];
                for(int k = skip; k< this.routePerVehicle.length; k++){
                    for(int l = 0; l< this.routePerVehicle[l].length; l++){
                        if(comp == this.routePerVehicle[k][l]) return true;

                    }
                }
            }
        }
        return false;
    }
    private boolean willDuplicate(int skip, int next, int[][] tempRoute){
        for(int i = 0; i< tempRoute.length; i++){
            for(int j = 0; j< tempRoute[i].length; j++){
                if (next == tempRoute[i][j] && tempRoute[i][j] > skip) return true;
            }
        }
        return false;
    }

    private int generatemask(){
        int h = 1;
        for(int i = 0; i < this.nodeTypes.length; i++ ){
             if(Objects.equals(this.nodeTypes[i][1], "f")){
                 this.mask[i]= true;
                         h++;
            }

        }
        return h;
    }
    private int findNearestF(int start, int skip){
        double shortestDist = 3000;
        int index = -1;
        for(int i = 1; i < (skip+2); i++){
            if(this.distanceMatrix[start][i] < shortestDist){
                shortestDist = this.distanceMatrix[start][i];
                index = i;
            }
        }
        return index;
    }
}
