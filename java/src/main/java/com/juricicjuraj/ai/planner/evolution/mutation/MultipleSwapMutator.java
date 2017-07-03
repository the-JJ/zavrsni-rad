package com.juricicjuraj.ai.planner.evolution.mutation;

import org.jenetics.Gene;
import org.jenetics.SwapMutator;
import org.jenetics.util.MSeq;
import org.jenetics.util.RandomRegistry;

import java.util.Random;

import static org.jenetics.internal.math.random.indexes;

public class MultipleSwapMutator<
        G extends Gene<?, G>,
        C extends Comparable<? super C>
        > extends SwapMutator<G, C> {
    private static final double DEFAULT_LAMBDA = 0.7;

    private final double lambda;

    public MultipleSwapMutator(double lambda, double alterProbability) {
        super(alterProbability);
        this.lambda = lambda;
    }

    public MultipleSwapMutator(double lambda) {
        this(lambda, DEFAULT_ALTER_PROBABILITY);
    }

    public MultipleSwapMutator() {
        this(DEFAULT_LAMBDA);
    }

    /**
     * Swaps the sequences of genes in the given array, with the mutation probability of this
     * mutation.
     */
    @Override
    protected int mutate(final MSeq<G> genes, final double p) {
        final Random random = RandomRegistry.getRandom();

        if (genes.length() <= 1) {
            return 0;
        }

        int length = genes.length();

        return indexes(random, length, p).map(thisStart -> {
                final int otherStart = random.nextInt(length);
                final double nextDouble = random.nextDouble();

                int sequenceLength = 1;
                if (nextDouble > 0.5) {
                    final int maxSequenceLength = Math.min(length - thisStart, length - otherStart);

                    sequenceLength = Math.min(
                            (int) (Math.log(1 - nextDouble) / (-lambda)) + 1,
                            Math.abs(otherStart - thisStart)
                    );

                    sequenceLength = Math.min(sequenceLength, maxSequenceLength);
                }

                swapSequences(
                        genes,
                        thisStart,
                        otherStart,
                        sequenceLength
                );

                return sequenceLength;
            })
            .sum();
    }

    private void swapSequences(final MSeq<G> genes, int start1, int start2, int sequenceLength)
            throws ArrayIndexOutOfBoundsException {
        for(int i = 0; i < sequenceLength; i++) {
            genes.swap(start1+i, start2+i);
        }
    }

}
