package main.managers.httpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.managers.HttpTaskManager;
import main.managers.Managers;
import main.managers.adapters.GsonDurationAdapter;
import main.managers.adapters.GsonLocalDateTimeAdapter;
import main.taskCore.Epic;
import main.taskCore.Subtask;
import main.taskCore.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;


public class HttpTaskServer {
    private final HttpServer httpServer;
    private final int PORT;

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new GsonDurationAdapter())
            .create();

    private final HttpTaskManager tasksManager;

    public HttpTaskServer() {
        PORT = 8080;
        tasksManager = Managers.getHttpTaskManager(8078);

        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpServer.createContext("/tasks", new TasksHandler());
    }

    public HttpTaskServer(int httpTaskServer_PORT, int DATA_PORT) {
        this.PORT = httpTaskServer_PORT;
        tasksManager = Managers.getHttpTaskManager(DATA_PORT);

        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpServer.createContext("/tasks", new TasksHandler());
    }

    public void start() {
        System.out.println("Запускаем сервер для работы с задачами на порту " + PORT);
        System.out.println("URL http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }


     class TasksHandler implements HttpHandler {
        private final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента.");

            Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath()
                    , httpExchange.getRequestMethod());

            System.out.println("Эндпоинт " + endpoint.toString());
            switch (endpoint) {
                case PUT_TASK:
                    handlePutTask(httpExchange);
                    break;
                case PUT_SUBTASK:
                    handlePutSubtask(httpExchange);
                    break;
                case PUT_EPIC:
                    handlePutEpic(httpExchange);
                    break;
                case GET_TASK:
                    handleGetTask(httpExchange);
                    break;
                case GET_SUBTASK:
                    handleGetSubtask(httpExchange);
                    break;
                case GET_EPIC:
                    handleGetEpic(httpExchange);
                    break;
                case GET_TASKS:
                    handleGetTasks(httpExchange);
                    break;
                case GET_SUBTASKS:
                    handleGetSubtasks(httpExchange);
                    break;
                case GET_EPICS:
                    handleGetEpics(httpExchange);
                    break;
                case GET_SUBTASKS_FROM_EPIC:
                    handleGetSubtasksFromEpic(httpExchange);
                    break;
                case GET_PRIORITIZED_TASKS:
                    handleGetPrioritizedTasks(httpExchange);
                    break;
                case GET_HISTORY:
                    handleGetHistory(httpExchange);
                    break;
                case DELETE_TASK:
                    handleDeleteTask(httpExchange);
                    break;
                case DELETE_SUBTASK:
                    handleDeleteSubtask(httpExchange);
                    break;
                case DELETE_EPIC:
                    handleDeleteEpic(httpExchange);
                    break;
                case DELETE_TASKS:
                    handleDeleteTasks(httpExchange);
                    break;
                case DELETE_SUBTASKS:
                    handleDeleteSubtasks(httpExchange);
                    break;
                case DELETE_EPICS:
                    handleDeleteEpics(httpExchange);
                    break;
                default:
                    writeResponse(httpExchange,  "Такого эндпоинта не существует", 404);
            }
        }

        private void handlePutTask(HttpExchange httpExchange)  throws IOException {
            String body;
            try (InputStream os = httpExchange.getRequestBody()) {
                body = new String(os.readAllBytes(), DEFAULT_CHARSET);
            } catch (JsonSyntaxException exp) {
                writeResponse(httpExchange, "Получен некорректный JSON", 400);
                return;
            }

            Task task;
            try {
                task = gson.fromJson(body, Task.class);
            } catch (JsonSyntaxException ex) {
                writeResponse(httpExchange, "Получен некорректный JSON", 400);
                return;
            }

            if (task.getId() == 0) {
                tasksManager.createTask(task);
                writeResponse(httpExchange, "Задача создана", 201);
                System.out.println("Задача создана.");
            }
            else {
                tasksManager.updateTask(task);
                writeResponse(httpExchange, "Задача обновлена", 201);
                System.out.println("Задача обновлена.");
            }
        }

        private void handlePutSubtask(HttpExchange httpExchange) throws IOException {
            String body;
            try (InputStream os = httpExchange.getRequestBody()) {
                body = new String(os.readAllBytes(), DEFAULT_CHARSET);
            } catch (JsonSyntaxException exp) {
                writeResponse(httpExchange, "Получен некорректный JSON", 400);
                return;
            }

            Subtask subtask;
            try {
                subtask = gson.fromJson(body, Subtask.class);
            } catch (JsonSyntaxException ex) {
                writeResponse(httpExchange, "Получен некорректный JSON", 400);
                return;
            }

            if (subtask.getId() == 0) {
                tasksManager.createSubtask(subtask);
                writeResponse(httpExchange, "Подзадача создана", 201);
            }
            else {
                tasksManager.updateTask(subtask);
                writeResponse(httpExchange, "Подзадача обновлена", 201);
            }
        }

         private void handlePutEpic(HttpExchange httpExchange) throws IOException {
             String body;
             try (InputStream os = httpExchange.getRequestBody()) {
                 body = new String(os.readAllBytes(), DEFAULT_CHARSET);
             } catch (JsonSyntaxException exp) {
                 writeResponse(httpExchange, "Получен некорректный JSON", 400);
                 return;
             }

             Epic epic;
             try {
                 epic = gson.fromJson(body, Epic.class);
             } catch (JsonSyntaxException ex) {
                 writeResponse(httpExchange, "Получен некорректный JSON", 400);
                 return;
             }

             if (epic.getId() == 0) {
                 tasksManager.createEpic(epic);
                 writeResponse(httpExchange, "Эпик создана", 201);
             }
             else {
                 tasksManager.updateEpic(epic);
                 writeResponse(httpExchange, "Эпик обновлен", 201);
             }
         }

        private void handleGetTask(HttpExchange httpExchange) throws IOException {
            String pathId = httpExchange.getRequestURI().getRawQuery().replaceAll("id=", "");
            int id = Integer.parseInt(pathId);
            Task task = tasksManager.getTask(id);
            writeResponse(httpExchange, gson.toJson(task), 200);
            System.out.println("Задача успешно отправлена.");
        }

        private void handleGetSubtask(HttpExchange httpExchange) throws IOException {
            String pathId = httpExchange.getRequestURI().getRawQuery().replaceAll("id=", "");
            int id = Integer.parseInt(pathId);
            Subtask subtask = tasksManager.getSubtask(id);
            writeResponse(httpExchange, gson.toJson(subtask), 200);
            System.out.println("Подзадача успешно отправлена.");
        }

        private void handleGetEpic(HttpExchange httpExchange) throws IOException {
            String pathId = httpExchange.getRequestURI().getRawQuery().replaceAll("id=", "");
            int id = Integer.parseInt(pathId);
            Epic epic = tasksManager.getEpic (id);
            writeResponse(httpExchange, gson.toJson(epic), 200);
            System.out.println("Эпик успешно отправлен.");
        }

        private void handleGetTasks(HttpExchange httpExchange) throws IOException {
            writeResponse(httpExchange, gson.toJson(tasksManager.getTasks()), 200);
            System.out.println("Задачи успешно отправлены.");
        }

        private void handleGetSubtasks(HttpExchange httpExchange) throws IOException {
            writeResponse(httpExchange, gson.toJson(tasksManager.getSubtasks()), 200);
            System.out.println("Подзадачи успешно отправлены.");
        }

        private void handleGetEpics(HttpExchange httpExchange) throws IOException {
            writeResponse(httpExchange, gson.toJson(tasksManager.getEpics()), 200);
            System.out.println("Эпики успешно отправлены.");
        }

        private void handleGetSubtasksFromEpic(HttpExchange httpExchange) throws IOException {
            String pathId = httpExchange.getRequestURI().getRawQuery().replaceAll("id=", "");
            int id = Integer.parseInt(pathId);
            writeResponse(httpExchange, gson.toJson(tasksManager.getSubtasksFromEpic(id)), 200);
            System.out.println("Подзадачи эпика успешно отправлены.");
        }

        private void handleGetPrioritizedTasks(HttpExchange httpExchange) throws IOException {
            writeResponse(httpExchange, gson.toJson(tasksManager.getPrioritizedTasks()), 200);
            System.out.println("Отсортированные задачи успешно отправлены.");
        }

        private void handleGetHistory(HttpExchange httpExchange) throws IOException {
            writeResponse(httpExchange, gson.toJson(tasksManager.getHistory()), 200);
            System.out.println("История упешно отправлена.");
        }

        private void handleDeleteTask(HttpExchange httpExchange) throws IOException {
            String pathId = httpExchange.getRequestURI().getRawQuery().replaceAll("id=", "");
            int id = Integer.parseInt(pathId);
            tasksManager.deleteTask(id);
            writeResponse(httpExchange, "Задача успешно удалена.", 200);
            System.out.println("Задача успешно удалена.");
        }

        private void handleDeleteSubtask(HttpExchange httpExchange) throws IOException {
            String pathId = httpExchange.getRequestURI().getRawQuery().replaceAll("id=", "");
            int id = Integer.parseInt(pathId);
            tasksManager.deleteSubtask(id);
            writeResponse(httpExchange, "Подзадача успешно удалена.", 200);
            System.out.println("Подзадача успешно удалена.");
        }

        private void handleDeleteEpic(HttpExchange httpExchange) throws IOException {
            String pathId = httpExchange.getRequestURI().getRawQuery().replaceAll("id=", "");
            int id = Integer.parseInt(pathId);
            tasksManager.deleteEpic(id);
            writeResponse(httpExchange, "Эпик успешно удален.", 200);
            System.out.println("Эпик успешно удален.");
        }

        private void handleDeleteTasks(HttpExchange httpExchange) throws IOException {
            tasksManager.deleteAllTasks();
            writeResponse(httpExchange, "Все задачи успешно удалены.", 200);
            System.out.println("Все задачи успешно удалены.");
        }

        private void handleDeleteSubtasks(HttpExchange httpExchange) throws IOException {
            tasksManager.deleteAllSubtasks();
            writeResponse(httpExchange, "Все подзадачи успешно удалены.", 200);
            System.out.println("Все подзадачи успешно удалены.");
        }

        private void handleDeleteEpics(HttpExchange httpExchange) throws IOException {
            tasksManager.deleteAllEpics();
            writeResponse(httpExchange, "Все эпики успешно удалены.", 200);
            System.out.println("Все эпики успешно удалены.");
        }

        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if(responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }
    }



    public static Endpoint getEndpoint(String requestPath, String requestMethod) {
        try {
            switch (requestMethod) {
                case "GET":
                    if (Pattern.matches("^/tasks$", requestPath)) {
                        return Endpoint.GET_PRIORITIZED_TASKS;
                    }
                    if (Pattern.matches("^/tasks/history$", requestPath)) {
                        return Endpoint.GET_HISTORY;
                    }
                    if (Pattern.matches("^/tasks/task[\\s\\S]*", requestPath)) {
                        if (Pattern.matches("^/tasks/task$", requestPath)) {
                            return Endpoint.GET_TASKS;
                        }
                        else {
                            return Endpoint.GET_TASK;
                        }
                    } else if (Pattern.matches("^/tasks/subtask[\\s\\S]*", requestPath)) {
                        if (Pattern.matches("^/tasks/subtask$", requestPath)) {
                            return Endpoint.GET_SUBTASKS;
                        }
                        else if (Pattern.matches("^/tasks/subtask/epic[\\s\\S]*", requestPath)) {
                            return Endpoint.GET_SUBTASKS_FROM_EPIC;
                        }
                        else {
                            return Endpoint.GET_SUBTASK;
                        }
                    } else if (Pattern.matches("^/tasks/epic[\\s\\S]*", requestPath)) {
                        if (Pattern.matches("^/tasks/epic$", requestPath)) {
                            return Endpoint.GET_EPICS;
                        }
                        else {
                            return Endpoint.GET_EPIC;
                        }
                    } else {
                        return Endpoint.UNKNOWN;
                    }
                case "POST":
                    if (Pattern.matches("^/tasks/task$", requestPath)) {
                        return Endpoint.PUT_TASK;
                    } else if (Pattern.matches("^/tasks/subtask$", requestPath)) {
                        return Endpoint.PUT_SUBTASK;
                    } else if (Pattern.matches("^/tasks/epic$", requestPath)) {
                        return Endpoint.PUT_EPIC;
                    } else {
                        return Endpoint.UNKNOWN;
                    }
                case "DELETE":
                    if (Pattern.matches("^/tasks/task[\\s\\S]*", requestPath)) {
                        if (Pattern.matches("^/tasks/task$", requestPath)) {
                            return Endpoint.DELETE_TASKS;
                        }
                        else {
                            return Endpoint.DELETE_TASK;
                        }
                    } else if (Pattern.matches("^/tasks/subtask[\\s\\S]*", requestPath)) {
                        if (Pattern.matches("^/tasks/subtask$", requestPath)) {
                            return Endpoint.DELETE_SUBTASKS;
                        }
                        else {
                            return Endpoint.DELETE_SUBTASK;
                        }
                    } else if (Pattern.matches("^/tasks/epic[\\s\\S]*", requestPath)) {
                        if (Pattern.matches("^/tasks/epic$", requestPath)) {
                            return Endpoint.DELETE_EPICS;
                        }
                        else {
                            return Endpoint.DELETE_EPIC;
                        }
                    } else {
                        return Endpoint.UNKNOWN;
                    }
                default:
                    System.out.println("Неизвестный запрос");
                    return Endpoint.UNKNOWN;
            }
        } catch (JsonSyntaxException | NullPointerException ex) {
            System.out.println("Json объект передается в неверном формате.");
            return Endpoint.UNKNOWN;
        }
    }
}
