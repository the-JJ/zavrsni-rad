package com.juricicjuraj.ai.planner.evolution.fitness;

import com.juricicjuraj.ai.planner.model.evolution.Moment;
import com.juricicjuraj.ai.planner.model.evolution.TaskMoment;

import java.util.function.Function;

public class TasksSequenceFunction implements Function<Moment[], Double> {

    private final double param_n;
    private final int param_t;

    private final static double scaleFactor = 5;

    public TasksSequenceFunction(
            double param_n,
            int param_t
    ) {
        if (param_n < -1 || param_n > 1) {
            throw new IllegalArgumentException("param_n must be in range: [-1, 1]");
        }
        if (param_t < 0) {
            throw new IllegalArgumentException("param_t must be greater than or equal to 0.");
        }

        this.param_n = param_n;
        this.param_t = param_t;
    }

    @Override
    public Double apply(Moment[] moments) {
        if (moments.length == 1) {
            return sequence(moments[0], 1);
        }

        double score = 0;
        int sequenceLength = 1;
        Moment previous = moments[0];
        for (int i = 1; i < moments.length; i++) {
            Moment current = moments[i];

            if (current.similar(previous)) {
                sequenceLength++;
            } else {
                score += sequence(previous, sequenceLength);
                sequenceLength = 1;
                previous = current;
            }
        }
        score += sequence(previous, sequenceLength);

        return score;
    }

    private double sequence(Moment moment, int sequenceLength) {
        if (!(moment instanceof TaskMoment)) {
            return 0d;
        }

        double sgn = Math.signum(param_n);
        if (sequenceLength < param_t) {
            sgn *= -1;
        }

        return sgn * Math.pow(sequenceLength, Math.abs(this.param_n * this.scaleFactor));
    }
}
