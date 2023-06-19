package Komplexaufgabe.s02;

import java.util.LinkedList;
import java.util.*;

public class ACO {
    int vehicles = 10;
    int fuelUsage = 2;
    int tankSize = 60;
    LinkedList<Point> points;
    Google.DataModel data = new Google.DataModel(points,vehicles);
    AntHill antHill;

    public ACO(){
        this.antHill = new AntHill(data, points, tankSize, fuelUsage);
    }

    public void exec(){
        this.antHill.startACO(10);
    }
}

