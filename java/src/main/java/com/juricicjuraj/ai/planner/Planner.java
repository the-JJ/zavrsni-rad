package com.juricicjuraj.ai.planner;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.codec.CalendarGenotypeDecoder;
import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.juricicjuraj.ai.planner.evolution.fitness.*;
import com.juricicjuraj.ai.planner.evolution.mutation.MultipleSwapMutator;
import com.juricicjuraj.ai.planner.model.GAParameters;
import com.juricicjuraj.ai.planner.model.Task;
import com.juricicjuraj.ai.planner.model.calendar.Calendar;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.limit;
import org.jenetics.util.Factory;
import org.jenetics.util.ISeq;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Planner {
    private static final double EMPTY_MOMENT_FILL_FACTOR = 0.25;

    private static double debugger_best;

    private static void debug(EvolutionResult<EnumGene<Moment>, Double> result) {
        Phenotype<EnumGene<Moment>, Double> phenotype = result.getBestPhenotype();
        if (Math.abs(debugger_best - phenotype.getFitness()) > 1e-6) {
            debugger_best = phenotype.getFitness();
            System.out.printf("%d. %.2f %s%n", result.getGeneration(), phenotype.getFitness(), phenotype);
        }
    }

    private GAParameters parameters;

    private Crossover<EnumGene<Moment>, Double> crossover;
    private Mutator<EnumGene<Moment>, Double> mutator;
    private Function<Genotype<EnumGene<Moment>>, Double> fitnessFunction;

    private final java.time.Duration timeLimit;

    public Planner(GAParameters parameters, java.time.Duration timeLimit) {
        Objects.requireNonNull(parameters);

        this.parameters = parameters;
        this.timeLimit = timeLimit;

        loadParameters(parameters);
        loadFitnessFunction();
    }

    private Engine.Builder<EnumGene<Moment>, Double> builder(Factory<Genotype<EnumGene<Moment>>> gtf) {
        return Engine.builder(fitnessFunction, gtf)
                .populationSize(parameters.enginePopulationSize)
                .offspringFraction(parameters.engineOffspringFraction)
                .alterers(crossover, mutator)
                .survivorsSelector(parameters.engineSurvivorsSelector)
                .offspringSelector(parameters.engineOffspringSelector);
    }

    private void loadFitnessFunction() {
        // fitness
        DeadlineFunction deadlineFitness = new DeadlineFunction(parameters.deadline_rLate, parameters.deadline_rEarly);
        TasksSequenceFunction sequenceFitness = new TasksSequenceFunction(parameters.sequenceFunction_n, parameters.sequenceFunction_t);
        PriorityFunction priorityFunction = new PriorityFunction(parameters.priority_p);

        fitnessFunction =
                new FitnessFunction(
                        new WeightedFunction(parameters.sequenceFunctionWeight, sequenceFitness),
                        new WeightedFunction(parameters.deadlineFunctionWeight, deadlineFitness),
                        new WeightedFunction(parameters.priorityFunctionWeight, priorityFunction)
                );
    }

    private void loadParameters(GAParameters parameters) {
        // crossover and mutator
        this.crossover = new PartiallyMatchedCrossover<>(parameters.pmxProbability);
        this.mutator = new MultipleSwapMutator<>(
                parameters.multipleSwapMutatorLambda,
                parameters.multipleSwapMutatorAlterProbability
        );
    }

    public Calendar plan(
            List<Task> tasks,
            List<CalendarEvent> fixedEvents,
            LocalDateTime startMoment
    ) {
        final MomentSeqFactory momentSeqFactory = new MomentSeqFactory(
                Duration.ofMinutes(30),
                EMPTY_MOMENT_FILL_FACTOR,
                fixedEvents,
                startMoment
        );

        final ISeq<Moment> alleles = momentSeqFactory.of(tasks);
        final Factory<Genotype<EnumGene<Moment>>> gtf = Genotype.of(
                PermutationChromosome.of(alleles)
        );

        Engine<EnumGene<Moment>, Double> engine = builder(gtf).build();

        try {
            Phenotype<EnumGene<Moment>, Double> phenotype = engine.stream()
                    .limit(limit.byExecutionTime(timeLimit))
//                    .peek(EvolutionStatistics.ofNumber())
//                    .peek(a -> {
//                        System.out.println(a.getBestPhenotype());
//                    })
                    .collect(EvolutionResult.toBestPhenotype());

            return decode(phenotype);
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Calendar decode(Phenotype<EnumGene<Moment>, Double> phenotype) {
        MomentSeqFactory msf = phenotype.getGenotype().getChromosome().getGene().getAllele().creator;

        Function<Genotype<EnumGene<Moment>>, Calendar> decoder = new CalendarGenotypeDecoder(msf);
        return decoder.apply(phenotype.getGenotype());
    }
}
