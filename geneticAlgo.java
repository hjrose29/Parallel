import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class geneticAlgo {
    public static void main(String[] args) {
        String[] availableTypes = {"x", "y", "z"};
        int[] availableQuantities = {30, 21, 30};
        int numThreads = 2; // Adjust this to your desired number of threads
        int numGenerations = 1000; // Adjust the number of generations as needed
        int eliminationRate = 95; // Adjust this to set the elimination rate (percentage)
    
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
    
        // Initialize the population of floor plans
        Machine[][][][] population = new Machine[numGenerations][numThreads][9][9];
        for (int generation = 0; generation < numGenerations; generation++) {
            for (int threadIndex = 0; threadIndex < numThreads; threadIndex++) {
                population[generation][threadIndex] = randomFloorPlan(9, 9, availableTypes, availableQuantities);
            }
        }
    
        for (int generation = 0; generation < numGenerations; generation++) {
            List<Callable<Machine[][]>> geneticTasks = new ArrayList<>();
    
            for (int threadIndex = 0; threadIndex < numThreads; threadIndex++) {
                final int currentGeneration = generation;
                final int currentThreadIndex = threadIndex;
                geneticTasks.add(() -> {
                    Machine[][] child = crossover(population[currentGeneration][currentThreadIndex],
                            population[currentGeneration][(currentThreadIndex + 1) % numThreads]);

                    int childFitness = calculateAffinity(child);
    
                    if (childFitness > calculateAffinity(population[currentGeneration][currentThreadIndex])) {
                        population[currentGeneration][currentThreadIndex] = child;
                    }
    
                    return population[currentGeneration][currentThreadIndex];
                });
            }
    
            try {
                List<Future<Machine[][]>> geneticResults = executorService.invokeAll(geneticTasks);
    
                int maxFitness = Integer.MIN_VALUE;
                Machine[][] bestFloor = null;
    
                for (Future<Machine[][]> result : geneticResults) {
                    Machine[][] floor = result.get();
                    int fitness = calculateAffinity(floor);
    
                    if (fitness > maxFitness) {
                        maxFitness = fitness;
                        bestFloor = floor;
                    }
                }
    
                // Print the best floor plan found in this generation
                printFloor(bestFloor);
    
                // Eliminate the least fit individuals (floor plans)
                int numToEliminate = (eliminationRate * population[generation].length) / 100;
                eliminateLeastFit(population[generation], numToEliminate, availableTypes, availableQuantities);
    
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    
        executorService.shutdown();
    }
    
    private static void eliminateLeastFit(Machine[][][] generation, int numToEliminate, String[]availableTypes, int[]availableQnt) {
        for (int i = 0; i < numToEliminate; i++) {
            int minFitnessIndex = 0;
            int minFitness = calculateAffinity(generation[0]);
            for (int threadIndex = 0; threadIndex < generation.length; threadIndex++) {
                int fitness = calculateAffinity(generation[threadIndex]);
                if (fitness < minFitness) {
                    minFitness = fitness;
                    minFitnessIndex = threadIndex;
                }
            }
            // Replace the least fit individual with a new random floor plan
            generation[minFitnessIndex] = randomFloorPlan(9, 9, availableTypes, availableQnt);
        }
    }
    
    

    public static Machine[][] createFloor(int x, int y){
        if(x <= 0 || y <= 0){
            return null;
        }
        Machine[][] floor = new Machine[x][y];
        for(int i = 0; i < floor.length; i++){
            for(int j = 0; j < floor[0].length; j++){
                floor[i][j] = new Machine("0", floor, i, j);
            }
        }
        return floor;
    }

    public static void printFloor(Machine[][] in){
        System.out.println("\nAffinity:" + calculateAffinity(in));
        for(int i = 0; i < in.length; i++){
            for(int j = 0; j < in[0].length; j++){
                System.out.print(in[i][j].type);
            }
                System.out.print("\n");
            
        }
    }
    public static Machine[][] crossover(Machine[][] parent1, Machine[][] parent2) {
        int numRows = parent1.length;
        int numCols = parent1[0].length;
    
        Machine[][] child = new Machine[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                child[i][j] = parent1[i][j];
            }
        }
    
        int crossoverPoint = getRandomCrossoverPoint(numCols);
    
        for (int i = 0; i < numRows; i++) {
            for (int j = crossoverPoint; j < numCols; j++) {
                child[i][j] = parent2[i][j];
            }
        }
    
        return child;
    }
    public static int getRandomCrossoverPoint(int numCols) {
        Random random = new Random();
        return random.nextInt(numCols);
    }
    public static int calculateAffinity(Machine[][] floor){
        int out = 0;
        for(int i = 0; i < floor.length;i++){
            for(int j = 0; j < floor[0].length;j++){
                floor[i][j].getAdjacencyList(floor);
                out += floor[i][j].getMachineAffinity(floor[i][j].getAdjacencyList(floor));
            }
        }
        return out;
    }

    public static Machine[][] randomFloorPlan(int x, int y, String[] types, int[] qnt){
        int[] q = qnt;
        Machine[][] floor = createFloor(x,y);

        for(int i = 0; i < y; i++){
            for(int j = 0; j < x;j++){
                int rand_y = Math.round(Math.round(Math.random() * (types.length-1)+0));
                if(qnt[rand_y] > 0){
                    floor[i][j] = new Machine(types[rand_y], floor, i, j);
                }
            }
        }
        

        return floor;
    }


}
