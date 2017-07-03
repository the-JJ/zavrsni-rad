package com.juricicjuraj.ai.planner.model.evolution;

import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.sun.istack.internal.NotNull;

import java.util.Objects;

public abstract class Moment {
    @NotNull
    public final MomentSeqFactory creator;

    protected Moment(MomentSeqFactory creator) {
        Objects.requireNonNull(creator);
        this.creator = creator;
    }

    @Override
    public abstract String toString();

    public abstract boolean similar(Moment o);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Moment)) return false;

        Moment moment = (Moment) o;

        return creator.equals(moment.creator);
    }

    @Override
    public int hashCode() {
        return creator.hashCode();
    }
}
