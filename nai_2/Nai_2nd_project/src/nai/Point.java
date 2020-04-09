

import java.util.Arrays;

public class Point {
    public final IrisType type;
    public   final double [] coordinates;


    public Point(double [] coordinates, IrisType type){
        this.coordinates = coordinates;
        this.type = type;
    }
    public Point(double[] coordinates){
        this.coordinates = coordinates;
        type = null;
    }


    public static IrisType getType(String type) {
        return switch (type.toLowerCase()) {
            case "iris-setosa" -> IrisType.SETOSA;
            case "iris-versicolor" -> IrisType.VERSICOLOR;
            default -> null;
        };



    }

    @Override
    public String toString() {
        return "Point{" +
                "type=" + type +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}

