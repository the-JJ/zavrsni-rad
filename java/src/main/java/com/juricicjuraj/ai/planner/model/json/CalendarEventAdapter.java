package com.juricicjuraj.ai.planner.model.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class CalendarEventAdapter implements JsonSerializer<CalendarEvent> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(CalendarEvent src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("title", src.getTitle());
        obj.addProperty("start", FORMATTER.format(src.getEventStart()));
        obj.addProperty("end", FORMATTER.format(src.getEventEnd()));
        obj.addProperty("color", src.getEventType().getColor());

        return obj;
    }
}
