package main.managers.httpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.managers.adapters.GsonDurationAdapter;
import main.managers.adapters.GsonLocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskGson {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new GsonDurationAdapter())
            .create();
}
