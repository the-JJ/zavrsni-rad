package com.juricicjuraj.ai.planner.evolution.fitness;

import com.juricicjuraj.ai.planner.model.Task;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import com.juricicjuraj.ai.planner.model.evolution.TaskMoment;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * A fitness function that determines a schedule's fitness by checking task priority.
 */
public class PriorityFunction implements Function<Moment[], Double> {

    private double parameter;

    public PriorityFunction(
            double parameter
    ) {
        this.parameter = parameter;
    }

    @Override
    public Double apply(Moment[] moments) {
        SortedMap<Task, Integer> lastOccurence = new TreeMap<>((a, b) -> {
            if (a.equals(b)) return 0;
            return (a.priority.getAsInt() >= b.priority.getAsInt()) ? 1 : -1;
        });
        for (int i = 0; i < moments.length; i++) {
            if (moments[i] instanceof TaskMoment) {
                TaskMoment taskMoment = (TaskMoment) moments[i];
                if (!taskMoment.task.priority.isPresent()) {
                    continue;
                }
        
                lastOccurence.put(taskMoment.task, i);
            }
        }

        int weight = lastOccurence.size();
        int previousPriority = Integer.MIN_VALUE;
        int previousIndex = Integer.MAX_VALUE;
        double score = 0;
        for(Map.Entry<Task, Integer> entry : lastOccurence.entrySet()) {
            if (entry.getKey().priority.orElse(Integer.MIN_VALUE) == previousPriority) continue;
            if (entry.getValue() > previousIndex) {
                score += parameter * weight;
            } else {
                score -= parameter * weight;
            }
            weight--;
            previousPriority = entry.getKey().priority.orElse(Integer.MIN_VALUE);
        }

        return score;
    }

}
