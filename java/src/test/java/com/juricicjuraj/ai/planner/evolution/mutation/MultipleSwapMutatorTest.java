package com.juricicjuraj.ai.planner.evolution.mutation;

import org.jenetics.Gene;
import org.jenetics.IntegerGene;
import org.jenetics.util.MSeq;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class MultipleSwapMutatorTest {
    private MSeq<IntegerGene> genes;

    private static final int MIN = 1;
    private static final int MAX = 7;

    @Before
    public void createSequence() throws Exception {
        genes = MSeq.empty();
        for(int i = MIN; i <= MAX; i++) {
            genes = genes.append(
                    IntegerGene.of(i, MIN, MAX)
            );
        }
    }

    private <T extends Gene<?, T>> void swapSequences(final MSeq<T> genes, int start1, int start2, int sequenceLength)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        MultipleSwapMutator<T, Integer> mutator = new MultipleSwapMutator<>(0.1);

        Method swapSequence = mutator.getClass().getDeclaredMethod
                ("swapSequences", MSeq.class, int.class, int.class, int.class);
        swapSequence.setAccessible(true);

        swapSequence.invoke(mutator, genes, start1, start2, sequenceLength);
    }

    @Test
    public void simpleSwapSequenceTest() throws Exception {
        // [1|2|3|4|5|6|7]
        //  ^ ^ <-> ^ ^
        // [5|6|3|4|1|2|7]
        int[] expected = {5,6,3,4,1,2,7};
        swapSequences(genes, 0, 4, 2);

        assertEquals(expected.length, genes.length());
        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], (int)genes.get(i).getAllele());
        }
    }

    @Test
    public void overlappingSwapSequenceTest() throws Exception {
        // [1|2|3|4|5|6|7]
        //  ^ ^^ ^^ ^
        // [3|4|5|2|1|6|7]
        int[] expected = {3,4,5,2,1,6,7};
        swapSequences(genes, 0, 2, 3);

        assertEquals(expected.length, genes.length());
        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], (int)genes.get(i).getAllele());
        }
    }

    @Test
    public void multipleSwapMutatorTest() throws Exception {
        MultipleSwapMutator<IntegerGene, Integer> mutator = new MultipleSwapMutator<>(0.1);

        mutator.mutate(genes, 0.2);
    }
}
