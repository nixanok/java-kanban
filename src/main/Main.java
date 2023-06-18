package main;

import main.managers.HttpTaskManager;
import main.managers.Managers;
import main.managers.httpServer.HttpTaskServer;
import main.managers.httpServer.KVServer;
import main.taskCore.Epic;
import main.taskCore.Status;
import main.taskCore.Task;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

import static main.managers.httpServer.HttpTaskServer.gson;


public class Main {
    public static void main(String[] args) throws IOException {

//        Task task1 = new Task("task1", "taskDescription1", Status.NEW
//                , LocalDateTime.parse("2021-06-03T13:54"), Duration.ofMinutes(100));
//        Epic epic1 = new Epic("epic1", "d");
//        System.out.println(gson.toJson(task1));
//        System.out.println(gson.toJson(epic1));
//
//
//        KVServer kvServer = new KVServer(8083);
//        kvServer.start();
//        HttpTaskServer httpTaskServer = new HttpTaskServer(8081, 8083);
//        httpTaskServer.start();
    }
}

