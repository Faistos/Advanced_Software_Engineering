package Komplexaufgabe.s02;


public class Location {
    final String ID;
    final String Type;
    final double Longitude;
    final double Latitude;


    public Location(String id, String type, double longitude, double latitude) {
        ID = id;
        Type = type;
        Longitude = longitude;
        Latitude = latitude;
    }

    public double getDistanceTo (Location p) {
        double lat1 = Latitude;
        double lat2 = p.Latitude;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(p.Longitude - Longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * 3963.19;
    }
}
