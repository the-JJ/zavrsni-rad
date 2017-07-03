package com.juricicjuraj.ai.planner.evolution;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.model.Task;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.model.evolution.EmptyMoment;
import com.juricicjuraj.ai.planner.model.evolution.FixedMoment;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import com.juricicjuraj.ai.planner.model.evolution.TaskMoment;
import org.jenetics.Chromosome;
import org.jenetics.EnumGene;
import org.jenetics.Genotype;
import org.jenetics.util.ISeq;

import java.time.LocalDateTime;
import java.util.*;

/**
 * A factory used to create ISeq objects of task parts and empty moments.
 *
 * @see Moment
 * @see ISeq
 */
public class MomentSeqFactory {
    public final Duration interval;
    public final LocalDateTime beginning;
    public final FixedMoment[] fixedMoments;

    private double emptyMomentFactor;

    private List<LocalDateTime> mappedTimePoints;

    /**
     * @param interval          The atom interval, the duration of a single Moment
     * @param emptyMomentFactor The ratio of number of empty moments and task moments.
     *                          For example, 1 means that there are as much empty moments as task ones.
     *                          There will always be one more empty moment than specified, for better
     *                          organization purposes.
     */
    public MomentSeqFactory(
            Duration interval,
            double emptyMomentFactor,
            List<CalendarEvent> fixedEvents,
            LocalDateTime beginning
    ) {
        if (emptyMomentFactor < 0) {
            throw new IllegalArgumentException("Empty moment factor must be positive.");
        }
        Objects.requireNonNull(beginning);

        this.interval = interval;
        this.emptyMomentFactor = emptyMomentFactor;
        this.beginning = beginning;

        this.fixedMoments = parseConstraints(fixedEvents);
        mappedTimePoints = new ArrayList<>(this.fixedMoments.length);
    }

    public ISeq<Moment> of(Iterable<Task> tasks) {
        List<Moment> parts = new LinkedList<>();
        tasks.forEach(t -> splitTask(t, parts));

        int emptyMomentCount = (int) (parts.size() * emptyMomentFactor) + 1;
        ISeq<Moment> iSeq = ISeq.of(() -> EmptyMoment.create(this), emptyMomentCount);
        iSeq = iSeq.append(parts);

        // init mapping - memoization
        mapTimePoints(iSeq.size() + fixedMoments.length - 1);

        return iSeq;
    }

    private void splitTask(Task task, Collection<Moment> target) {
        Duration duration = task.duration;

        double count = (double) duration.toMinutes() / interval.toMinutes();

        for (int i = 0; i < count; i++) {
            target.add(new TaskMoment(task, i, this));
        }
    }

    private FixedMoment[] parseConstraints(List<CalendarEvent> constraints) {
        ArrayList<FixedMoment> fixedMoments = new ArrayList<>();
        constraints.sort(CalendarEvent.eventStartComparator);

        for(CalendarEvent constraint : constraints) {
            int intervals = (int) constraint.getDuration().dividedBy(interval, true);

            Duration offsetDuration = Duration.between(beginning, constraint.getEventStart());
            int offset = (int) offsetDuration.dividedBy(interval, false);
            for(int i = 0; i < intervals; i++) {
                fixedMoments.add(
                        new FixedMoment(
                                offset + i,
                                constraint,
                                this
                        )
                );
            }
        }

        return fixedMoments.toArray(new FixedMoment[fixedMoments.size()]);
    }

    public Moment[] mergeConstraintsAndGenotype(Genotype<EnumGene<Moment>> genotype) {
        Chromosome<EnumGene<Moment>> chromosome = genotype.getChromosome();
        int chromosomeSize = chromosome.length();

        Moment[] moments = new Moment[chromosomeSize + fixedMoments.length];

        int fixedPointer = 0;
        int chromosomePointer = 0;
        for(int i = 0; i < moments.length; i++) {
            if (fixedPointer < fixedMoments.length && fixedMoments[fixedPointer].partNumber == i) {
                moments[i] = fixedMoments[fixedPointer++];
            } else if(chromosomePointer < chromosomeSize) {
                moments[i] = chromosome.getGene(chromosomePointer++).getAllele();
            } else {
                moments[i] = EmptyMoment.create(this);
            }
        }

        return moments;
    }

    /**
     * Maps all time points up to and including the given index.
     *
     * @param index inclusive
     */
    private synchronized void mapTimePoints(int index) {
        int start = mappedTimePoints.size();
        if (start > index) {
            return;
        }
        for (int i = start; i <= index; i++) {
            mappedTimePoints.add(
                    interval.multipliedBy(i).addSelfToTemporal(beginning)
            );
        }
    }

    public LocalDateTime mapTimePoint(int index) {
        // uses memoization; lazy loads map
        if (index >= mappedTimePoints.size()) {
            mapTimePoints(index);
        }

        return mappedTimePoints.get(index);
    }
}
