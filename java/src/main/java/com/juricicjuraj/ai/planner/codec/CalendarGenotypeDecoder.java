package com.juricicjuraj.ai.planner.codec;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.juricicjuraj.ai.planner.model.calendar.Calendar;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.model.calendar.EventType;
import com.juricicjuraj.ai.planner.model.evolution.FixedMoment;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import com.juricicjuraj.ai.planner.model.evolution.TaskMoment;
import org.jenetics.EnumGene;
import org.jenetics.Genotype;

import java.time.LocalDateTime;
import java.util.function.Function;

public class CalendarGenotypeDecoder implements Function<Genotype<EnumGene<Moment>>, Calendar> {
    private final MomentSeqFactory msf;

    public CalendarGenotypeDecoder(MomentSeqFactory msf) {
        this.msf = msf;
    }

    @Override
    public Calendar apply(Genotype<EnumGene<Moment>> genotype) {
        Calendar calendar = new Calendar();

        Moment[] moments = msf.mergeConstraintsAndGenotype(genotype);

        int sequenceLength = 1;
        int sequenceStart = 0;
        Moment previous = null;
        int i = 0;
        for(Moment current : moments) {
            if (current.similar(previous)) {
                sequenceLength++;
            } else {
                if (previous != null) {
                    // upisi prethodni
                    if (previous instanceof TaskMoment) {
                        calendar.add(createCalendarEvent(
                                (TaskMoment) previous,
                                msf.interval.multipliedBy(sequenceStart),
                                msf.interval.multipliedBy(sequenceLength)
                        ));
                    } else if (previous instanceof FixedMoment) {
                        calendar.add(
                                ((FixedMoment) previous).event
                        );
                    }

                    sequenceLength = 1;
                }

                sequenceStart = i;
            }

            previous = current;
            i++;
        }
        if (previous instanceof TaskMoment) {
            calendar.add(createCalendarEvent(
                    (TaskMoment) previous,
                    msf.interval.multipliedBy(sequenceStart),
                    msf.interval.multipliedBy(sequenceLength)
            ));
        }

        return calendar;
    }

    private CalendarEvent createCalendarEvent(TaskMoment moment, Duration offset, Duration duration) {
        LocalDateTime eventStart = offset.addSelfToTemporal(msf.beginning);
        LocalDateTime eventEnd = duration.addSelfToTemporal(eventStart);

        return new CalendarEvent(
                eventStart,
                eventEnd,
                EventType.GENERATED,
                moment.task.name
        );
    }

}
