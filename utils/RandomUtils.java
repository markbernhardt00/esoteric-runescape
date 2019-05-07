package utils;
import java.util.Random;

public class RandomUtils {

    private RandomUtils(){}

    //Makes a new random int with min value min and max value max
    public static int random(int min, int max) {
        Random RANDOM = new Random();
        return RANDOM.nextInt(max) + min;
    }
}
