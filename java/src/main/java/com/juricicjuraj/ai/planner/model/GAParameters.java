package com.juricicjuraj.ai.planner.model;

import com.juricicjuraj.ai.planner.evolution.mutation.MultipleSwapMutator;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import org.jenetics.EnumGene;
import org.jenetics.Selector;
import org.jenetics.StochasticUniversalSelector;
import org.jenetics.TournamentSelector;
import org.jenetics.engine.Engine;

import java.time.LocalDateTime;

public class GAParameters {
    // defaults
    private static final double DEFAULT_pmxProbability = 0.3;
    private static final double DEFAULT_multipleSwapMutatorLambda = 0.7;
    private static final double DEFAULT_multipleSwapMutatorAlterProbability = 0.2;
    private static final double DEFAULT_engineOffspringFraction = 0.65;
    private static final int DEFAULT_enginePopulationSize = 200;
    private static final Selector<EnumGene<Moment>, Double> DEFAULT_engineSurvivorsSelector
            = new TournamentSelector<>(3);
    private static final Selector<EnumGene<Moment>, Double> DEFAULT_engineOffspringSelector
            = new StochasticUniversalSelector<>();

    /**
     * {@link org.jenetics.PartiallyMatchedCrossover} parameter probability.
     * Defaults to 0.3
     */
    public double pmxProbability;

    /**
     * {@link MultipleSwapMutator} parameter lambda.
     * Defaults to 0.7.
     */
    public double multipleSwapMutatorLambda;

    /**
     * {@link MultipleSwapMutator} parameter alterProbability.
     * Defaults to 0.2.
     */
    public double multipleSwapMutatorAlterProbability;

    /**
     * {@link Engine.Builder} parameter offspringFraction.
     * Defaults to 0.65.
     */
    public double engineOffspringFraction;

    /**
     * {@link Engine.Builder} parameter populationSize.
     * Defaults to 200.
     */
    public int enginePopulationSize;

    /**
     * {@link Engine.Builder} parameter survivorsSelector.
     * Defaults to TournamentSelector(3).
     */
    public Selector<EnumGene<Moment>, Double> engineSurvivorsSelector;

    /**
     * {@link Engine.Builder} parameter offspringSelector.
     * Defaults to StochasticUniversalSelector().
     */
    public Selector<EnumGene<Moment>, Double> engineOffspringSelector;

    public LocalDateTime planningStart;

    public double deadlineFunctionWeight;
    public double sequenceFunctionWeight;
    public double priorityFunctionWeight;

    public double sequenceFunction_n;
    public int sequenceFunction_t;

    public double deadline_rLate;
    public double deadline_rEarly;

    public double priority_p;

    public GAParameters() {
        this.pmxProbability = DEFAULT_pmxProbability;

        this.multipleSwapMutatorLambda = DEFAULT_multipleSwapMutatorLambda;
        this.multipleSwapMutatorAlterProbability = DEFAULT_multipleSwapMutatorAlterProbability;

        this.engineOffspringFraction = DEFAULT_engineOffspringFraction;
        this.enginePopulationSize = DEFAULT_enginePopulationSize;

        this.engineSurvivorsSelector = DEFAULT_engineSurvivorsSelector;
        this.engineOffspringSelector = DEFAULT_engineOffspringSelector;

        this.planningStart = LocalDateTime.now();

        this.deadlineFunctionWeight = 1f;
        this.sequenceFunctionWeight = 1f;
        this.priorityFunctionWeight = 1f;

        this.sequenceFunction_n = 0.5;
        this.sequenceFunction_t = 3;

        this.priority_p = 50;
    }

    @Override
    public String toString() {
        return String.format(
                "[ " +
                "pmxProbability = %.3f; " +
                "multipleSwapMutatorLambda = %.3f; " +
                "multipleSwapMutatorAlterProbability = %.3f; " +
                "engineOffspringFraction = %.3f; " +
                "enginePopulationSize = %d; " +
                "engineSurvivorsSelector = %s; " +
                "engineOffspringSelector = %s" +
                " ]",
                pmxProbability,
                multipleSwapMutatorLambda,
                multipleSwapMutatorAlterProbability,
                engineOffspringFraction,
                enginePopulationSize,
                engineSurvivorsSelector.toString(),
                engineOffspringSelector.toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GAParameters)) return false;

        GAParameters that = (GAParameters) o;

        if (Double.compare(that.pmxProbability, pmxProbability) != 0) return false;
        if (Double.compare(that.multipleSwapMutatorLambda, multipleSwapMutatorLambda) != 0) return false;
        if (Double.compare(that.multipleSwapMutatorAlterProbability, multipleSwapMutatorAlterProbability) != 0)
            return false;
        if (Double.compare(that.engineOffspringFraction, engineOffspringFraction) != 0) return false;
        if (enginePopulationSize != that.enginePopulationSize) return false;
        if (!engineSurvivorsSelector.equals(that.engineSurvivorsSelector)) return false;
        return engineOffspringSelector.equals(that.engineOffspringSelector);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(pmxProbability);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(multipleSwapMutatorLambda);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(multipleSwapMutatorAlterProbability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(engineOffspringFraction);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + enginePopulationSize;
        result = 31 * result + engineSurvivorsSelector.hashCode();
        result = 31 * result + engineOffspringSelector.hashCode();
        return result;
    }
}
