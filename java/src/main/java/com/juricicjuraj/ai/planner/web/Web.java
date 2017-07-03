package com.juricicjuraj.ai.planner.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juricicjuraj.ai.planner.Planner;
import com.juricicjuraj.ai.planner.model.GAParameters;
import com.juricicjuraj.ai.planner.model.Task;
import com.juricicjuraj.ai.planner.model.calendar.Calendar;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.web.rest.RestRoutes;
import spark.ModelAndView;
import spark.Session;
import spark.TemplateEngine;
import spark.template.pebble.PebbleTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static spark.Spark.*;

public class Web {
    public static void main(String[] args) {
        Web web = new Web();

        web.initRoutes();
    }

    private ExecutorService executor;

    private Web() {
        initPublic(true);
        threadPool(4);

        executor = Executors.newWorkStealingPool();
    }

    private void initPublic(boolean debug) {
        if (debug) {
            // debug mode - doesn't cache static files
            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources/public";
            staticFiles.externalLocation(projectDir + staticDir);
        } else {
            staticFiles.location("/public");
        }
    }

    private void initRoutes() {
        TemplateEngine templateEngine = new PebbleTemplateEngine();

        get("/", (req, res) -> {
            return new ModelAndView(null, "templates/index.pebble");
        }, templateEngine);

        post("/", (req, res) -> {
            Session session = req.session(true);

            // POST data
            Map<String, Object> requestData = (new Gson()).fromJson(
                    req.body(), new TypeToken<Map<String, Object>>(){}.getType());

            // start calculation on another thread
            try {
                calculateCalendar(session, requestData);
            } catch(Exception e) {
                res.status(500);
            }

            return res;
        });

        get("/calendar", (req, res) -> {
            Session session = req.session(true);

            Map<String, String> map = new HashMap<>();
            map.put("name", req.session().attribute("name"));

            return new ModelAndView(map, "templates/calendar.pebble");
        }, templateEngine);

        // REST API
        RestRoutes.routes();
    }

    private void calculateCalendar(Session session, Map<String, Object> requestData) {
        // parse request data
        RequestDecoder decoder = new RequestDecoder(requestData);

        List<CalendarEvent> fixedEvents = decoder.getFixedEvents();
        List<Task> tasks = decoder.getTasks();
        GAParameters parameters = decoder.getParameters();

        Future<Calendar> future = session.attribute("futureResult");
        if (future != null) {
            future.cancel(true);
        }

        Callable<Calendar> task = () -> {
            Planner planner = new Planner(new GAParameters(), java.time.Duration.ofSeconds(10)); //TODO params

            return planner.plan(
                    tasks,
                    fixedEvents,
                    parameters.planningStart
            );
        };

        future = executor.submit(task);
        session.attribute("futureResult", future);
    }
}
