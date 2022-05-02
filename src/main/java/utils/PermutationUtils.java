package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PermutationUtils {
    private static boolean isDerangement(List<Integer> permutation, List<Integer> original) {
        for (int i = 0; i < permutation.size(); i++) {
            if (permutation.get(i).equals(original.get(i))) return false;
        }

        return true;
    }

    /* https://en.wikipedia.org/wiki/Derangement
     * P(A) = 1/Math.E = 0.3678 - limit of the probability that a randomly selected permutation
     * of a large number of objects is a derangement
     *
     * 99% of time we need at most 10 permutations to get a valid result
     */
    public static List<Integer> derangementFrom(List<Integer> ids) {
        List<Integer> permutation;
        int threshold = 1000;

        do {
            // Extremely low probability of happening
            // P((1 - A)^1000) = (1 - 1/Math.E)^1000 = 1e-200
            if (--threshold <= 0) throw new RuntimeException("Congratulations! Someone won a lottery and broke the bot");

            permutation = new ArrayList<>(ids);
            Collections.shuffle(permutation);
        } while (!isDerangement(permutation, ids));

        return permutation;
    }
}
