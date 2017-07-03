package com.juricicjuraj.ai.planner.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juricicjuraj.ai.planner.model.calendar.Calendar;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.model.json.CalendarAdapter;
import com.juricicjuraj.ai.planner.model.json.CalendarEventAdapter;
import spark.Request;
import spark.Response;
import spark.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static spark.Spark.get;

/**
 * Provides JSON REST API methods and routes.
 */
public class RestRoutes {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(CalendarEvent.class, new CalendarEventAdapter())
            .registerTypeAdapter(Calendar.class, new CalendarAdapter())
            .create();

    public static void routes() {
        get("/events", new JsonRoute() {
            @Override
            protected Object execute(Request request, Response response) {
                Session session = request.session(true);

                String start = request.queryParams("start");
                String end = request.queryParams("end");

                LocalDateTime startInstant = null;
                LocalDateTime endInstant = null;
                if (start != null) {
                    startInstant = LocalDate.parse(start).atStartOfDay();
                }
                if (end != null) {
                    endInstant = LocalDate.parse(end).atStartOfDay();
                }

                Calendar fullCalendar = getSessionCalendar(session);
                return fullCalendar.fromTo(startInstant, endInstant, true);
            }
        }, gson::toJson);
    }

    private static Calendar getSessionCalendar(Session session) {
        Future<Calendar> future = session.attribute("futureResult");
        if (future == null) {
            throw new RestException("Calculation not requested.", 400);
        }

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RestException(e);
        }
    }
}
