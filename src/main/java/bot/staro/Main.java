package bot.staro;

import bot.staro.booleans.RemoteProcessBoolean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public final class Main {
    private static final int ITERATIONS = 1_000_000;
    private static final int WARMUP = 100_000;

    public static void main(String[] args) {
        String task = "";
        // determine what task to task
        // use provided arguments, or user input if none
        if (args.length == 0) {
            System.out.println("Enter a task to test: benchmark/remoteprocessboolean");
            task = getInput();
        } else {
            task = args[0];
        }

        // Run the given task
        switch (task.toLowerCase()) {
            case "benchmark" -> {
                benchmarkAll();
                System.exit(0);
            }
            case "remoteprocessboolean" -> {
                // test the remote process boolean
                System.out.println("Creating remote process boolean...");
                RemoteProcessBoolean remoteProcessBoolean = new RemoteProcessBoolean();
                System.out.println("====================================");
                System.out.println("Current value = " + remoteProcessBoolean.getValue());
                System.out.println("Expects = false");

                remoteProcessBoolean.setValue(true);
                System.out.println("====================================");
                System.out.println("Current value = " + remoteProcessBoolean.getValue());
                System.out.println("Expects = true");

                System.exit(0);
            }
            default -> {
                System.out.println("No tasks to run.");
                System.exit(0);
            }
        }
    }

    /**
     * @return the line provided by console input
     */
    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Runs a benchmark on (most) of the booleans
     */
    private static void benchmarkAll() {
        benchmark("java.lang.Boolean", "TRUE", 1000);
        benchmark("bot.staro.booleans.UnstableBoolean", null, 1000);
        benchmark("bot.staro.booleans.UncertainBoolean", null, 1000);
        benchmark("bot.staro.booleans.SchroedingerBoolean", null, 1000);
        benchmark("bot.staro.booleans.MemoryLeakBoolean", null, 1000);
        benchmark("bot.staro.booleans.MemorySafeBoolean", null, 1000);
        benchmark("bot.staro.booleans.RaceConditionBoolean", null, 1000);
        benchmark("bot.staro.booleans.OffHeapBoolean", null, 1000);
        benchmark("bot.staro.booleans.DiskBoolean", null, 1000);
        benchmark("bot.staro.booleans.ThreadLocalBoolean", null, 1000);
        benchmark("bot.staro.booleans.RogueBoolean", null, 1000);
        benchmark("bot.staro.booleans.QuantumFlipBoolean", null, 1000);
        benchmark("bot.staro.booleans.OptimizedMemoryLeakBoolean", null, 1000);
    }

    /**
     * Benchmarks a boolean class with the given number of iterations.
     *
     * @param className The fully qualified class name
     * @param staticField Optional static field to use (e.g., "TRUE" for Boolean.TRUE)
     * @param iterations Number of iterations to run
     */
    public static void benchmark(String className, String staticField, int iterations) {
        try {
            System.out.println("====================================");
            System.out.println("Benchmarking: " + className);
            System.out.println("====================================");

            Class<?> clazz = Class.forName(className);

            // Benchmark creation
            /*long createTime = benchmarkCreation(clazz, iterations);
            System.out.printf("Creation time: %.3f ms (%.3f ns per operation)%n",
                    createTime / 1_000_000.0, createTime / (double) iterations);*/

            // Get an instance for further benchmarks
            Object instance;
            if (staticField != null) {
                instance = clazz.getField(staticField).get(null);
            } else {
                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    // Try boolean constructor
                    instance = clazz.getDeclaredConstructor(boolean.class).newInstance(true);
                }
            }

            // Find and benchmark setter methods
            Map<String, Long> setTimes = benchmarkSetters(instance, iterations);
            for (Map.Entry<String, Long> entry : setTimes.entrySet()) {
                System.out.printf("%s time: %.3f ms (%.3f ns per operation)%n",
                        entry.getKey(), entry.getValue() / 1_000_000.0,
                        entry.getValue() / (double) iterations);
            }

            // Find and benchmark getter methods
            Map<String, Long> getTimes = benchmarkGetters(instance, iterations);
            for (Map.Entry<String, Long> entry : getTimes.entrySet()) {
                System.out.printf("%s time: %.3f ms (%.3f ns per operation)%n",
                        entry.getKey(), entry.getValue() / 1_000_000.0,
                        entry.getValue() / (double) iterations);
            }

            // Benchmark common Object methods
            benchmarkObjectMethods(instance, iterations);

            System.out.println();
        } catch (Exception e) {
            System.out.println("Error benchmarking " + className + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Benchmarks object creation time.
     */
    private static long benchmarkCreation(Class<?> clazz, int iterations) {
        // Find a suitable constructor
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> constructor = null;
        Object[] args = null;

        // Try no-arg constructor first
        try {
            constructor = clazz.getDeclaredConstructor();
            args = new Object[0];
        } catch (NoSuchMethodException e) {
            // Try boolean constructor
            try {
                constructor = clazz.getDeclaredConstructor(boolean.class);
                args = new Object[]{true};
            } catch (NoSuchMethodException e2) {
                // Try other constructors
                for (Constructor<?> c : constructors) {
                    constructor = c;
                    args = new Object[c.getParameterCount()];
                    for (int i = 0; i < args.length; i++) {
                        Class<?> type = c.getParameterTypes()[i];
                        if (type == boolean.class || type == Boolean.class) {
                            args[i] = true;
                        } else if (type == int.class || type == Integer.class) {
                            args[i] = 1;
                        } else if (type == String.class) {
                            args[i] = "";
                        }
                        // Add more types as needed
                    }
                    break;
                }
            }
        }

        if (constructor == null) {
            System.out.println("Could not find a suitable constructor for " + clazz.getName());
            return 0;
        }

        constructor.setAccessible(true);

        // Warmup
        try {
            for (int i = 0; i < 10000; i++) {
                constructor.newInstance(args);
            }
        } catch (Exception e) {
            System.out.println("Warmup error: " + e.getMessage());
        }

        // Actual benchmark
        long startTime = System.nanoTime();
        try {
            for (int i = 0; i < iterations; i++) {
                constructor.newInstance(args);
            }
        } catch (Exception e) {
            System.out.println("Benchmark error: " + e.getMessage());
        }
        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    /**
     * Benchmarks setter methods.
     */
    private static Map<String, Long> benchmarkSetters(Object instance, int iterations) {
        Map<String, Long> results = new HashMap<>();

        // List of common setter method names to try
        List<String> setterNames = new ArrayList<>();
        setterNames.add("set");
        setterNames.add("setValue");
        setterNames.add("setMutant");
        setterNames.add("reset");

        for (String setterName : setterNames) {
            try {
                // Try boolean parameter
                Method method = instance.getClass().getMethod(setterName, boolean.class);
                method.setAccessible(true);

                // Warmup
                for (int i = 0; i < 10000; i++) {
                    method.invoke(instance, i % 2 == 0);
                }

                // Actual benchmark
                long startTime = System.nanoTime();
                for (int i = 0; i < iterations; i++) {
                    method.invoke(instance, i % 2 == 0);
                }
                long endTime = System.nanoTime();

                results.put(setterName, endTime - startTime);
            } catch (NoSuchMethodException e) {
                // Try no parameter version
                try {
                    Method method = instance.getClass().getMethod(setterName);
                    method.setAccessible(true);

                    // Warmup
                    for (int i = 0; i < 10000; i++) {
                        method.invoke(instance);
                    }

                    // Actual benchmark
                    long startTime = System.nanoTime();
                    for (int i = 0; i < iterations; i++) {
                        method.invoke(instance);
                    }
                    long endTime = System.nanoTime();

                    results.put(setterName, endTime - startTime);
                } catch (Exception e2) {
                    // Method doesn't exist or can't be called
                }
            } catch (Exception e) {
                System.out.println("Error benchmarking " + setterName + ": " + e.getMessage());
            }
        }

        return results;
    }

    /**
     * Benchmarks getter methods.
     */
    private static Map<String, Long> benchmarkGetters(Object instance, int iterations) {
        Map<String, Long> results = new HashMap<>();

        // List of common getter method names to try
        List<String> getterNames = new ArrayList<>();
        getterNames.add("get");
        getterNames.add("getValue");
        getterNames.add("getMutantValue");
        getterNames.add("observe");

        for (String getterName : getterNames) {
            try {
                Method method = instance.getClass().getMethod(getterName);
                method.setAccessible(true);

                // Warmup
                for (int i = 0; i < 10000; i++) {
                    method.invoke(instance);
                }

                // Actual benchmark
                long startTime = System.nanoTime();
                for (int i = 0; i < iterations; i++) {
                    method.invoke(instance);
                }
                long endTime = System.nanoTime();

                results.put(getterName, endTime - startTime);
            } catch (Exception e) {
                // Method doesn't exist or can't be called
            }
        }

        return results;
    }

    /**
     * Benchmarks common Object methods.
     */
    private static void benchmarkObjectMethods(Object instance, int iterations) {
        // Benchmark toString
        long toStringTime = 0;
        try {
            // Warmup
            for (int i = 0; i < 10000; i++) {
                instance.toString();
            }

            // Actual benchmark
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                instance.toString();
            }
            long endTime = System.nanoTime();
            toStringTime = endTime - startTime;
        } catch (Exception e) {
            System.out.println("Error benchmarking toString: " + e.getMessage());
        }

        // Benchmark equals
        long equalsTime = 0;
        try {
            // Warmup
            for (int i = 0; i < 10000; i++) {
                instance.equals(instance);
            }

            // Actual benchmark
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                instance.equals(instance);
            }
            long endTime = System.nanoTime();
            equalsTime = endTime - startTime;
        } catch (Exception e) {
            System.out.println("Error benchmarking equals: " + e.getMessage());
        }

        // Benchmark hashCode
        long hashCodeTime = 0;
        try {
            // Warmup
            for (int i = 0; i < 10000; i++) {
                instance.hashCode();
            }

            // Actual benchmark
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                instance.hashCode();
            }

            long endTime = System.nanoTime();
            hashCodeTime = endTime - startTime;
        } catch (Exception e) {
            System.out.println("Error benchmarking hashCode: " + e.getMessage());
        }

        System.out.printf("toString time: %.3f ms (%.3f ns per operation)%n", toStringTime / 1_000_000.0, toStringTime / (double) iterations);
        System.out.printf("equals time: %.3f ms (%.3f ns per operation)%n", equalsTime / 1_000_000.0, equalsTime / (double) iterations);
        System.out.printf("hashCode time: %.3f ms (%.3f ns per operation)%n", hashCodeTime / 1_000_000.0, hashCodeTime / (double) iterations);
    }

}