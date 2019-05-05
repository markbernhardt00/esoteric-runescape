package utils;
import java.util.Random;

public class RandomUtils {

    private RandomUtils(){}

    public static int random(int min, int max) {
        Random RANDOM = new Random();
        return RANDOM.nextInt(max) + min;
    }
}
