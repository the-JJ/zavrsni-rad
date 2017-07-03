package com.juricicjuraj.ai.planner.model.calendar;

import com.google.common.base.Joiner;
import com.juricicjuraj.ai.fasttime.Duration;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Represents a calendar with all entries. Entries are represented by {@link CalendarEvent} class.
 */
public class Calendar implements Iterable<CalendarEvent> {
	private NavigableSet<CalendarEvent> eventSet;

	public Calendar(){
		eventSet = new TreeSet<>();
	}

	/**
	 * Initializes a new {@link Calendar} using the given event set.
	 * @param eventSet
	 */
	public Calendar(NavigableSet<CalendarEvent> eventSet){
		this.eventSet = eventSet;
	}

	public boolean add(CalendarEvent event){
		return eventSet.add(event);
	}

	public NavigableSet<CalendarEvent> getEventSet(){
		return eventSet;
	}

	public Iterator<CalendarEvent> iterator(){
		return eventSet.iterator();
	}

	/**
	 * Adds all events from the other calendar.
	 * @param other
	 */
	public void addAll(Calendar other){
		this.eventSet.addAll(other.eventSet);
	}

	/**
	 * Returns a view of the portion of this calendar whose events are present in this calendar and start
	 * after the given {@link LocalDateTime} value.
	 * @param from
	 * @param inclusive If true, will also include the events starting at the given {@link LocalDateTime} value.
	 * @return
	 */
	public Calendar from(LocalDateTime from, boolean inclusive){
		if (from == null) {
			return this;
		}
		NavigableSet<CalendarEvent> subSet = eventSet.tailSet(new CalendarEvent(from, Duration.ZERO), inclusive);

		return new Calendar(subSet);
	}

	/**
	 * Returns a live view of the portion of this calendar whose events are present in this calendar and start
	 * before the given {@link LocalDateTime} value.
	 * @param to
	 * @param inclusive If true, will also include the events starting at the given {@link LocalDateTime} value.
	 * @return
	 */
	public Calendar to(LocalDateTime to, boolean inclusive){
		if (to == null) {
			return this;
		}
		NavigableSet<CalendarEvent> subSet = eventSet.headSet(new CalendarEvent(to, Duration.ZERO), inclusive);

		return new Calendar(subSet);
	}

	/**
	 * Returns a liveview of the portion of this calendar whose events are present in this calendar and start
	 * between the given {@link LocalDateTime} values.
     * @param from
	 * @param to
	 * @param inclusive If true, will also include the events starting and ending at the given {@link LocalDateTime} value.
	 * @return
	 */
	public Calendar fromTo(LocalDateTime from, LocalDateTime to, boolean inclusive){
		if (from == null) {
			return this.to(to, inclusive);
		}
		if (to == null) {
			return this.from(from, inclusive);
		}
		NavigableSet<CalendarEvent> subSet = eventSet.subSet(new CalendarEvent(from, Duration.ZERO), inclusive, new CalendarEvent(to, Duration.ZERO), inclusive);

		return new Calendar(subSet);
	}

	public int size(){
		return eventSet.size();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Calendar)) return false;

        Calendar that = (Calendar) o;

        return eventSet.equals(that.eventSet);
    }

    @Override
    public int hashCode() {
        return eventSet.hashCode();
    }

    @Override
    public String toString() {
	    return Joiner.on(System.lineSeparator()).join(eventSet);
    }
}
