package com.juricicjuraj.ai.planner.model.evolution;

import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.juricicjuraj.ai.planner.model.Task;
import com.sun.istack.internal.NotNull;

import java.util.Objects;

final public class TaskMoment extends Moment {
    @NotNull
    public final Task task;

    public final int partNumber;

    public TaskMoment(Task task, int partNumber, MomentSeqFactory creator) {
        super(creator);
        Objects.requireNonNull(task);

        this.task = task;
        this.partNumber = partNumber;
    }

    @Override
    public String toString() {
        return task.name
//                + "P" + partNumber
        ;
    }

    /**
     * Two TaskMoments are similar if their (parent) tasks are equal.
     * @param o other {@link Moment}
     * @return true if the other {@link Moment} has the same parent task as this one.
     */
    @Override
    public boolean similar(Moment o) {
        if (o == null) return false;
        if (!(o instanceof TaskMoment)) return false;

        return this.task.equals(((TaskMoment) o).task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskMoment)) return false;
        if (!super.equals(o)) return false;

        TaskMoment that = (TaskMoment) o;

        if (partNumber != that.partNumber) return false;
        return task.equals(that.task);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + task.hashCode();
        result = 31 * result + partNumber;
        return result;
    }
}
