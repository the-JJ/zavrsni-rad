package com.juricicjuraj.ai.planner.evolution.fitness;

import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import org.jenetics.EnumGene;
import org.jenetics.Genotype;

import java.util.Objects;
import java.util.function.Function;

public class FitnessFunction implements Function<Genotype<EnumGene<Moment>>, Double> {
    private final WeightedFunction[] functions;

    @SuppressWarnings("unchecked")
    public FitnessFunction(WeightedFunction... functions) {
        this.functions = Objects.requireNonNull(functions);
    }

    @Override
    public Double apply(Genotype<EnumGene<Moment>> genotype) {
        MomentSeqFactory msf = genotype.getChromosome().getGene().getAllele().creator;
        Moment[] moments = msf.mergeConstraintsAndGenotype(genotype);

        double score = 0;
        for(WeightedFunction weightedFunction : functions) {
            score += weightedFunction.weight * weightedFunction.function.apply(moments);
        }

        return score;
    }
}
