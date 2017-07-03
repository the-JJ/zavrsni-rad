package com.juricicjuraj.ai.planner.model.json;

import com.google.gson.*;
import com.juricicjuraj.ai.planner.model.calendar.Calendar;

import java.lang.reflect.Type;

public class CalendarAdapter implements JsonSerializer<Calendar> {

    @Override
    public JsonElement serialize(Calendar src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        src.forEach(evt -> {
            JsonElement serializedEvt = context.serialize(evt);
            array.add(serializedEvt);
        });

        return array;
    }
}
