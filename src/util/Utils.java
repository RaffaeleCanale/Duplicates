package util;

import java.util.Collection;
import java.util.List;

/**
 * Created on 29/02/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Utils {

    public static void trimResults(Collection<? extends List<?>> result, int minGroupSize) {
        if (minGroupSize > 0) {
            result.removeIf(group -> group.size() < minGroupSize);
        }
    }

    public static boolean matches(long positiveValue, long min, long max) {
        return (min <= 0 || positiveValue >= min)
                && (max <= 0 || positiveValue <= max);
    }
}
