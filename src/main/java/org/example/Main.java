package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    ExecutorService executorService;
    Runnable runnableTask;
    List<Callable<String>> callableTasks;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Hello world!");

        Main main = new Main();

        main.runnableTask = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
                System.out.println("\nrunnableTask executed");
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        };

        Callable<String> callableTask1 = () -> {
            TimeUnit.MILLISECONDS.sleep(300);
            System.out.println("callableTask1 executed");
            return "string from callableTask1";
        };
        Callable<String> callableTask2 = () -> {
            TimeUnit.MILLISECONDS.sleep(300);
            System.out.println("callableTask2 executed");
            return "string from callableTask2";
        };
        Callable<String> callableTask3 = () -> {
            TimeUnit.MILLISECONDS.sleep(300);
            System.out.println("callableTask3 executed");
            return "string from callableTask3";
        };
        main.callableTasks = new ArrayList<>();
        main.callableTasks.add(callableTask1);
        main.callableTasks.add(callableTask2);
        main.callableTasks.add(callableTask3);

        main.executorServiceFixedPool();
        main.executorServiceThreadPoolExecutor();

        main.runAnyTaskInCollection();

        main.runAllTasksInCollection();
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
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void runAnyTaskInCollection() throws ExecutionException, InterruptedException {
        System.out.println("\nRunning any task in a collection...");
        String result = executorService.invokeAny(callableTasks);
        System.out.println("result of invokeAny of callableTasks: " + result);
    }

    public void runAllTasksInCollection() throws ExecutionException, InterruptedException {
        System.out.println("\nRunning all task in a collection...");
        List<Future<String>> listOfFutures = executorService.invokeAll(callableTasks);
        listOfFutures.stream().forEach((future) -> {
            try {
                String result = future.get();
                System.out.println("stringFutureResult: " + result);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}