package com.example.gameoflife;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class GameOfLife implements AutoCloseable {
    private Grid currentGrid;
    private final GameRulesBase rules;
    private final ExecutorService executor;
    private int generation;

    public GameOfLife(Grid initialGrid) {
        this(initialGrid, GameRulesBase.conway());
    }

    public GameOfLife(Grid initialGrid, GameRulesBase rules) {
        this.currentGrid = Objects.requireNonNull(initialGrid, "Initial grid cannot be null").copy();
        this.rules = Objects.requireNonNull(rules, "Game rules cannot be null");
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
        this.generation = 0;
    }
    
    public void evolve() throws InterruptedException, ExecutionException, TimeoutException {
        currentGrid = currentGrid.evolveWith(rules, executor);
        generation++;
    }

    public void evolve(int steps) throws InterruptedException, ExecutionException, TimeoutException {
        for (int i = 0; i < steps; i++) {
            evolve();
        }
    }

    public void simulate(int generations, Consumer<GameOfLife> observer)
            throws InterruptedException, ExecutionException, TimeoutException {
        for (int i = 0; i <= generations; i++) {
            observer.accept(this);
            if (i < generations) {
                evolve();
            }
        }
    }
    
    public Grid getCurrentGrid() {
        return currentGrid.copy();
    }
    
    public int getGeneration() {
        return generation;
    }
    
    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
