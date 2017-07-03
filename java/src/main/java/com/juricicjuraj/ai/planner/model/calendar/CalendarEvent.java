package com.juricicjuraj.ai.planner.model.calendar;

import com.juricicjuraj.ai.fasttime.Duration;
import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

/**
 * Immutable calendar entry.
 *
 * Calendar entries can be compared. When comparing events, their start times are compared.
 */
final public class CalendarEvent implements Comparable<CalendarEvent> {
    private static final EventType DEFAULT_EVENT_TYPE = EventType.HARD_CONSTRAINT;

    @NotNull
    private String title;

    @NotNull
    private final LocalDateTime eventStart;

    @NotNull
    private final LocalDateTime eventEnd;

    @NotNull
    private EventType eventType;

    private final Duration duration;

    public static final Comparator<CalendarEvent> eventStartComparator =
            Comparator.comparing(o -> o.eventStart);

    public static final Comparator<CalendarEvent> eventEndComparator =
            Comparator.comparing(o -> o.eventEnd);

    public static final Comparator<CalendarEvent> fullComparator = eventStartComparator
            .thenComparing(eventEndComparator.reversed())
            .thenComparing(Comparator.comparing(o -> o.title)) // could be replaced by parent ID?
            .thenComparing((o1, o2) -> 1); // hack: duplicates

    public CalendarEvent(LocalDateTime eventStart, LocalDateTime eventEnd, EventType eventType, String title) {
        Objects.requireNonNull(eventStart);
        Objects.requireNonNull(eventEnd);

        if (eventType == null) {
            eventType = DEFAULT_EVENT_TYPE;
        }
        if (title == null) {
            title = "";
        }

        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventType = eventType;
        this.title = title;

        this.duration = Duration.between(eventStart, eventEnd);
    }

    public CalendarEvent(LocalDateTime eventStart, LocalDateTime eventEnd, EventType eventType) {
        this(eventStart, eventEnd, eventType, "");
    }

    public CalendarEvent(LocalDateTime eventStart, Duration duration, EventType eventType) {
        this(eventStart, duration.addSelfToTemporal(eventStart), eventType);
    }

    public CalendarEvent(LocalDateTime eventStart, LocalDateTime eventEnd) {
        this(eventStart, eventEnd, null);
    }

    public CalendarEvent(LocalDateTime eventStart, Duration duration) {
        this(eventStart, duration, null);
    }

    public LocalDateTime getEventStart() {
        return eventStart;
    }

    public LocalDateTime getEventEnd() {
        return eventEnd;
    }

    public Duration getDuration() {
        return duration;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(CalendarEvent o) {
        if (this.equals(o)) {
            return 0;
        }

        return fullComparator.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarEvent that = (CalendarEvent) o;

        if (!title.equals(that.title)) return false;
        if (!eventStart.equals(that.eventStart)) return false;
        if (!eventEnd.equals(that.eventEnd)) return false;
        return eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + eventStart.hashCode();
        result = 31 * result + eventEnd.hashCode();
        result = 31 * result + eventType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(eventStart);
        sb.append(" - ");
        sb.append(eventEnd);
        sb.append(" :: ");
        sb.append(title);
        return sb.toString();
    }
}
