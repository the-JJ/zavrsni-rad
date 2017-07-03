package com.juricicjuraj.ai.planner.web;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.model.GAParameters;
import com.juricicjuraj.ai.planner.model.Task;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.model.calendar.EventType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestDecoder {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm");

    private Map<String, Object> requestData;

    public RequestDecoder(Map<String, Object> requestData) {
        this.requestData = requestData;
    }

    public List<CalendarEvent> getFixedEvents() {
        List<CalendarEvent> events = new ArrayList<>();

        ((List<Object>) requestData.get("fixedEvents")).forEach(eventObject -> {
            Map<String, Object> eventMap = (Map<String, Object>) eventObject;

            String title = (String) eventMap.getOrDefault("title", "");

            String startString = (String) eventMap.getOrDefault("start", "");
            if (startString.isEmpty()) {
                return;
            }
            LocalDateTime start = LocalDateTime.parse(startString, FORMATTER);

            String endString = (String) eventMap.getOrDefault("end", "");
            if (endString.isEmpty()) {
                return;
            }
            LocalDateTime end = LocalDateTime.parse(endString, FORMATTER);

            events.add(new CalendarEvent(start, end, EventType.EXISTING, title));
        });

        return events;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        ((List<Object>) requestData.get("tasks")).forEach(taskObject -> {
            Map<String, Object> taskMap = (Map<String, Object>) taskObject;

            String title = (String) taskMap.getOrDefault("title", "");

            String priorityString = (String) taskMap.getOrDefault("priority", "");
            Integer priority = null;
            if (!priorityString.isEmpty()) {
                priority = Integer.valueOf(priorityString);
            }

            int durationMinutes = Integer.valueOf(
                    (String) taskMap.getOrDefault("duration", "0")
            );
            Duration duration = Duration.ofMinutes(durationMinutes);

            String deadlineString = (String) taskMap.getOrDefault("deadline", null);
            LocalDateTime deadline = LocalDateTime.now();
            if (deadlineString != null) {
                deadline = LocalDateTime.parse(deadlineString, FORMATTER);
            }

            tasks.add(new Task(title, duration, deadline, priority));
        });

        return tasks;
    }

    public GAParameters getParameters() {
        GAParameters parameters = new GAParameters();

        Map<String, Object> params = (Map<String, Object>) requestData.get("params");

        String planningStartString = (String) params.getOrDefault("planning_start", "");
        LocalDateTime planningStart = LocalDateTime.now();
        if (!planningStartString.isEmpty()) {
            planningStart = LocalDateTime.parse(planningStartString, FORMATTER);
        }

        parameters.planningStart = planningStart;

        // numeric values
        parameters.deadlineFunctionWeight = Double.valueOf((String)params.getOrDefault("weight_deadline", "1"));
        parameters.sequenceFunctionWeight = Double.valueOf((String)params.getOrDefault("weight_similartask", "1"));
        parameters.priorityFunctionWeight = Double.valueOf((String)params.getOrDefault("weight_priority", "1"));

        parameters.sequenceFunction_n = Double.valueOf((String)params.getOrDefault("similartask_n", "0.5"));
        parameters.sequenceFunction_t = Integer.valueOf((String)params.getOrDefault("similartask_t", "3"));

        parameters.deadline_rLate = Double.valueOf((String)params.getOrDefault("deadline_r_late", "1.4"));
        parameters.deadline_rEarly = Double.valueOf((String)params.getOrDefault("deadline_r_early", "0"));

        parameters.priority_p = Double.valueOf((String)params.getOrDefault("priority_p", "50"));

        return parameters;
    }
}
