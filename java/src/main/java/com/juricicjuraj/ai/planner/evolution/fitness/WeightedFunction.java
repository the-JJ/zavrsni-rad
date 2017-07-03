package com.juricicjuraj.ai.planner.evolution.fitness;

import com.juricicjuraj.ai.planner.model.evolution.Moment;

import java.util.function.Function;

public class WeightedFunction {
    public final Function<Moment[], Double> function;
    public final double weight;

    public WeightedFunction(double weight, Function<Moment[], Double> function) {
        this.weight = weight;
        this.function = function;
    }
}
