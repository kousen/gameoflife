package com.example.gameoflife;

public class Main {
    static void main() {
        try {
            // Example 1: Using a predefined pattern
            System.out.println("=== Glider Pattern ===");
            var gliderGrid = Pattern.GLIDER.toGrid(new BoundaryCondition.Fixed());
            
            try (var gliderGame = new GameOfLife(gliderGrid)) {
                gliderGame.simulate(5, game -> {
                    System.out.println("Generation " + game.getGeneration());
                    System.out.println(game.getCurrentGrid());
                    System.out.println();
                });
            }
            
            // Example 2: Custom initial configuration
            System.out.println("=== Custom Configuration ===");
            var customGrid = new Grid(8, 8);
            customGrid.setCellState(Cell.of(2, 3), new CellState.Alive());
            customGrid.setCellState(Cell.of(3, 3), new CellState.Alive());
            customGrid.setCellState(Cell.of(4, 3), new CellState.Alive());
            customGrid.setCellState(Cell.of(4, 2), new CellState.Alive());
            customGrid.setCellState(Cell.of(3, 1), new CellState.Alive());
            
            try (var customGame = new GameOfLife(customGrid)) {
                for (int i = 0; i < 3; i++) {
                    System.out.println("Generation " + customGame.getGeneration());
                    System.out.println(customGame.getCurrentGrid());
                    customGame.evolve();
                }
            }
            
            // Example 3: Toroidal boundary
            System.out.println("=== Toroidal Boundary ===");
            var toroidalGrid = Pattern.BLINKER.toGrid(new BoundaryCondition.Toroidal());
            
            try (var toroidalGame = new GameOfLife(toroidalGrid)) {
                toroidalGame.simulate(2, game -> {
                    System.out.println("Generation " + game.getGeneration());
                    System.out.println(game.getCurrentGrid());
                });
            }
            
            // Example 4: High Life rules
            System.out.println("=== High Life Rules ===");
            var highLifeGrid = new Grid(10, 10);
            highLifeGrid.setCellState(Cell.of(4, 4), new CellState.Alive());
            highLifeGrid.setCellState(Cell.of(4, 5), new CellState.Alive());
            highLifeGrid.setCellState(Cell.of(4, 6), new CellState.Alive());
            
            try (var highLifeGame = new GameOfLife(highLifeGrid, GameRules.highLife())) {
                highLifeGame.simulate(3, game -> {
                    System.out.println("Generation " + game.getGeneration() + " (High Life)");
                    System.out.println(game.getCurrentGrid());
                });
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
