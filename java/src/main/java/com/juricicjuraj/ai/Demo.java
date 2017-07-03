package com.juricicjuraj.ai;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.Planner;
import com.juricicjuraj.ai.planner.model.GAParameters;
import com.juricicjuraj.ai.planner.model.Task;
import com.juricicjuraj.ai.planner.model.calendar.Calendar;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.model.calendar.EventType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
//        GAParameters parameters = new GAParameters(
//                0.76,
//                0d,
//                0d,
//                0.19,
//                9000,
//                new LinearRankSelector<>(0.2),
//                new TournamentSelector<>(3)
//        );
        Planner planner = new Planner(new GAParameters(), java.time.Duration.ofSeconds(60));

        Calendar calendar = planner.plan(
                demoTasks(),
                demoConstraints(),
                LocalDateTime.parse("2017-04-21T08:00")
        );

        System.out.println(calendar);
    }

    public static List<CalendarEvent> demoConstraints() {
        List<CalendarEvent> events = new ArrayList<>();

        events.add(new CalendarEvent(
                LocalDateTime.parse("2017-04-21T10:30"),
                LocalDateTime.parse("2017-04-21T12:00"),
                EventType.EXISTING,
                "fiksni task 10:30 - podne"
        ));

        return events;
    }

    public static List<Task> demoTasks() {
        List<Task> tasks = new ArrayList<>();

        tasks.add(
                new Task(
                        "1",
                        Duration.ofHours(4),
                        LocalDateTime.parse("2017-04-21T14:00"),
                        3
                )
        );
        tasks.add(
                new Task(
                        "2",
                        Duration.ofMinutes(90),
                        LocalDateTime.parse("2017-04-21T15:00"),
                        1
                )
        );
        tasks.add(
                new Task(
                        "3",
                        Duration.ofHours(1),
                        LocalDateTime.parse("2017-04-21T16:00"),
                        2
                )
        );
        tasks.add(
                new Task(
                        "4",
                        Duration.ofHours(1),
                        LocalDateTime.parse("2017-04-21T18:00")
                )
        );
        tasks.add(
                new Task(
                        "5",
                        Duration.ofHours(2),
                        LocalDateTime.parse("2017-04-21T19:00")
                )
        );
        tasks.add(
                new Task(
                        "6",
                        Duration.ofHours(10),
                        LocalDateTime.parse("2017-04-22T21:00")
                )
        );

        return tasks;
    }
}
