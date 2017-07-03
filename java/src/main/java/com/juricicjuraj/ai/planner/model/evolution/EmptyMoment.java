package com.juricicjuraj.ai.planner.model.evolution;

import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;

import java.util.HashMap;

final public class EmptyMoment extends Moment {
    private static final HashMap<MomentSeqFactory, EmptyMoment> instances = new HashMap<>();

    public static EmptyMoment create(MomentSeqFactory factory) {
        return instances.computeIfAbsent(factory, EmptyMoment::new);
    }

    private EmptyMoment(MomentSeqFactory creator) {
        super(creator);
    }

    @Override
    public String toString() {
        return "0";
    }

    @Override
    public boolean similar(Moment o) {
        return o instanceof EmptyMoment;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }
}
