package com.juricicjuraj.ai.planner.model.evolution;

import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;

import java.util.Objects;

final public class FixedMoment extends Moment {
    /**
     * Position
     */
    public final int partNumber;

    public final CalendarEvent event;

    public FixedMoment(int partNumber, CalendarEvent event, MomentSeqFactory creator) {
        super(creator);

        this.partNumber = partNumber;
        this.event = Objects.requireNonNull(event);;
    }

    @Override
    public String toString() {
        return event.getTitle()
//                + "P" + partNumber
        ;
    }

    @Override
    public boolean similar(Moment o) {
        if (o == null) return false;
        if (!(o instanceof FixedMoment)) return false;

        return this.event.equals(((FixedMoment) o).event);
    }
}
