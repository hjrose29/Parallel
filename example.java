import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class example {

    private static final int POPULATION_SIZE = 100;
    private static final int NUM_GENERATIONS = 100;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Create an ExecutorService with a fixed number of threads
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        // A list to store the results (maximum fitness values) from each thread
        List<Future<Integer>> results = new ArrayList<>();

        // Loop through the threads
        for (int i = 0; i < NUM_THREADS; i++) {
            // Create a Callable task for each thread
            Callable<Integer> task = () -> {
                // Initialize a random number generator for this thread
                Random random = new Random();

                // Initialize the population for this thread
                int[] population = new int[POPULATION_SIZE];

                // Initialize the maximum fitness value found by this thread
                int maxFitness = Integer.MIN_VALUE;

                // Iterate through the generations
                for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
                    // Initialize the population with random values
                    for (int j = 0; j < POPULATION_SIZE; j++) {
                        population[j] = random.nextInt(1000); // Adjust the range as needed
                    }

                    // Evaluate fitness and find the maximum value in this population
                    for (int value : population) {
                        maxFitness = Math.max(maxFitness, value);
                    }
                }

                // Return the maximum fitness value found by this thread
                return maxFitness;
            };

            // Submit the Callable task to the executor and store the Future object
            results.add(executorService.submit(task));
        }

        // Shutdown the executor service and wait for all tasks to complete
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // Find the overall maximum fitness value by examining results from all threads
        int overallMax = Integer.MIN_VALUE;
        for (Future<Integer> result : results) {
            overallMax = Math.max(overallMax, result.get());
        }

        // Print the overall maximum fitness found
        System.out.println("Overall maximum fitness found: " + overallMax);
    }
}
