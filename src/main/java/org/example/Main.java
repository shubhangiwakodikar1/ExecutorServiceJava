package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    ExecutorService executorService;
    Runnable runnableTask;
    List<Callable<String>> callableTasks;
    static int timeout = 10;

    ScheduledExecutorService scheduledExecutorService;

    public static void main(String[] args) throws ExecutionException, InterruptedException, Exception {
        System.out.println("Hello world!");

        Main main = new Main();

        main.runnableTask = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(timeout);
                System.out.println("\nrunnableTask executed");
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        };

        Callable<String> callableTask1 = () -> {
            TimeUnit.MILLISECONDS.sleep(timeout);
            System.out.println("callableTask1 executed");
            return "string from callableTask1";
        };
        Callable<String> callableTask2 = () -> {
            TimeUnit.MILLISECONDS.sleep(timeout);
            System.out.println("callableTask2 executed");
            return "string from callableTask2";
        };
        Callable<String> callableTask3 = () -> {
            TimeUnit.MILLISECONDS.sleep(timeout);
            System.out.println("callableTask3 executed");
            return "string from callableTask3";
        };
        main.callableTasks = new ArrayList<>();
        main.callableTasks.add(callableTask1);
        main.callableTasks.add(callableTask2);
        main.callableTasks.add(callableTask3);

//        main.executorServiceFixedPool();
//        main.executorServiceThreadPoolExecutor();
//        main.runAnyTaskInCollection();
//        main.runAllTasksInCollection();
//        main.showdownExecutorService();

        main.createScheduledExecutorService();
        main.shutdownScheduledExecutorService();
    }

    private void showdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination((timeout * 5), TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException interruptedException) {
            executorService.shutdownNow();
        }
    }

    public void executorServiceFixedPool() {
        executorService = Executors.newFixedThreadPool(10);
        executorService.execute(runnableTask);
        System.out.println("After executing runnableTask");
    }

    public void executorServiceThreadPoolExecutor() {
        System.out.println();
        executorService = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        Future<String> stringFuture = executorService.submit(callableTasks.get(0));
        try {
            System.out.println("stringFuture: " + stringFuture.get());
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }
    }

    public void runAnyTaskInCollection() throws ExecutionException, InterruptedException {
        System.out.println("\nRunning any task in a collection...");
        String result = executorService.invokeAny(callableTasks);
        System.out.println("result of invokeAny of callableTasks: " + result);
    }

    public void runAllTasksInCollection() throws Exception {
        System.out.println("\nRunning all task in a collection...");
        List<Future<String>> listOfFutures = executorService.invokeAll(callableTasks);
        listOfFutures.stream().forEach((future) -> {
            try {
                //blocking call
                String result = future.get(100, TimeUnit.MILLISECONDS);
                System.out.println("stringFutureResult: " + result);
            } catch (InterruptedException | ExecutionException | TimeoutException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    private void createScheduledExecutorService() {
        scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.schedule(runnableTask, 1, TimeUnit.MILLISECONDS);

//        System.out.println("scheduling at fixed rate...");
//        scheduledExecutorService.scheduleAtFixedRate(runnableTask, 10, 1000, TimeUnit.MILLISECONDS);

        System.out.println("scheduling with fixed delay...");
        scheduledExecutorService.scheduleWithFixedDelay(runnableTask, 10, 1000, TimeUnit.MILLISECONDS);
    }

    private void shutdownScheduledExecutorService() {
        scheduledExecutorService.shutdown();
        try {
            if (!scheduledExecutorService.awaitTermination(240, TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException interruptedException) {
            scheduledExecutorService.shutdownNow();
        }
    }
}