package com.juricicjuraj.ai.planner.web.rest;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

abstract class JsonRoute implements Route {
    protected abstract Object execute(Request request, Response respone);

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");

        try {
            Object executed = this.execute(request, response);
            return executed;
        } catch(RestException e) {
            response.status(e.getStatus());

            Map<String, String> map = new HashMap<>();
            map.put("error", e.getMessage());
            return map;
        }
    }
}
