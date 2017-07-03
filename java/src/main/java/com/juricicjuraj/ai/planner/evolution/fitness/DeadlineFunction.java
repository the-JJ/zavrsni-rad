package com.juricicjuraj.ai.planner.evolution.fitness;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import com.juricicjuraj.ai.planner.model.evolution.TaskMoment;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * A fitness function that determines a schedule's fitness by checking task deadlines and individual {@link TaskMoment}s.
 * It is actually implemented as a cost function, but its result is negated (multiplied by -1), so that the "fitter"
 * schedule will have a higher score.
 */
public class DeadlineFunction implements Function<Moment[], Double> {

    private final double r_early;
    private final double r_late;

    /**
     * Instantiates a new deadline function with given minute mappers and finish-early promotion flag.
     */
    public DeadlineFunction(
            double r_late,
            double r_early
    ) {
        if (r_late < 0) {
            throw new IllegalArgumentException("r_late must be greater than or equal to 0.");
        }
        if (r_early < 0) {
            throw new IllegalArgumentException("r_early must be greater than or equal to 0.");
        }

        this.r_late = r_late;
        this.r_early = r_early;
    }

    @Override
    public Double apply(Moment[] moments) {
        double score = 0;
        for (int i = 0; i < moments.length; i++) {
            if (moments[i] instanceof TaskMoment) {
                TaskMoment taskMoment = (TaskMoment) moments[i];

                LocalDateTime point = taskMoment.creator.mapTimePoint(i);

                Duration late = Duration.between(taskMoment.task.deadline, point);
                if (!late.isNegative() && r_late != 0) {
                    score -= Math.pow(late.toMinutes(), r_late);
                } else if (r_early != 0) {
                    score += Math.pow(late.toMinutes(), r_early);
                }
            }
        }

        return score;
    }

}
