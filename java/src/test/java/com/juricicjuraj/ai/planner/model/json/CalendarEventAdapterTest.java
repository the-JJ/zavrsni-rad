package com.juricicjuraj.ai.planner.model.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.model.calendar.EventType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class CalendarEventAdapterTest {
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CalendarEvent.class, new CalendarEventAdapter());
        gson = gsonBuilder.create();
    }

    @Test
    public void calendarEventToJsonTest() throws Exception {
        CalendarEvent event = new CalendarEvent(
                LocalDateTime.parse("2017-01-12T00:00"),
                LocalDateTime.parse("2017-01-12T12:00"),
                EventType.EXISTING,
                "Neki event"
        );

        String json = gson.toJson(event);
        String expected = "{\"title\":\"Neki event\",\"start\":\"2017-01-12T00:00:00.000\",\"end\":\"2017-01-12T12:00:00.000\",\"color\":\"" + EventType.EXISTING.getColor() + "\"}";
        assertEquals(expected, json);
    }
}
