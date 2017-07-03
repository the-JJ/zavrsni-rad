package com.juricicjuraj.ai.planner.model;

import com.juricicjuraj.ai.fasttime.Duration;
import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.OptionalInt;

public class Task {
	@NotNull
	public final String name;

	/**
	 * Task duration, in minutes
	 */
	public final Duration duration;

	@NotNull
	public final LocalDateTime deadline;

	public final OptionalInt priority;

	public Task(String name, Duration duration, LocalDateTime deadline) {
		this(name, duration, deadline, null);
	}

	public Task(String name, Duration duration, LocalDateTime deadline, Integer priority) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(deadline);

		this.name = name;
		this.duration = duration;
		this.deadline = deadline;

		if (priority == null) {
			this.priority = OptionalInt.empty();
		} else {
			this.priority = OptionalInt.of(priority);
		}
	}
}
