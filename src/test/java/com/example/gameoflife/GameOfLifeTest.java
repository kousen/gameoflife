package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeTest {
    
    private GameOfLife game;
    
    @AfterEach
    void cleanup() {
        if (game != null) {
            game.close();
        }
    }
    
    @Test
    @DisplayName("Blinker pattern oscillates with period 2")
    void blinkerOscillates() throws Exception {
        var initial = Pattern.BLINKER.toGrid();
        game = new GameOfLife(initial);
        
        var gen0 = game.getCurrentGrid();
        game.evolve();
        var gen1 = game.getCurrentGrid();
        game.evolve();
        var gen2 = game.getCurrentGrid();
        
        assertNotEquals(gen0, gen1);
        assertEquals(gen0, gen2);
    }
    
    @Test
    @DisplayName("Block pattern remains stable")
    void blockStable() throws Exception {
        var initial = Pattern.BLOCK.toGrid();
        game = new GameOfLife(initial);
        
        var gen0 = game.getCurrentGrid();
        game.evolve();
        var gen1 = game.getCurrentGrid();
        
        assertEquals(gen0, gen1);
    }
    
    @Test
    @DisplayName("Empty grid remains empty")
    void emptyGridStaysEmpty() throws Exception {
        var empty = new Grid(5, 5);
        game = new GameOfLife(empty);
        
        game.evolve(10);
        
        var result = game.getCurrentGrid();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                assertFalse(result.getCellState(Cell.of(row, col)).isAlive());
            }
        }
    }
    
    @Test
    @DisplayName("Generation counter increments correctly")
    void generationCounter() throws Exception {
        game = new GameOfLife(new Grid(3, 3));
        
        assertEquals(0, game.getGeneration());
        game.evolve();
        assertEquals(1, game.getGeneration());
        game.evolve(5);
        assertEquals(6, game.getGeneration());
    }
    
    @Test
    @DisplayName("Glider moves diagonally")
    void gliderMoves() throws Exception {
        var initial = new Grid(10, 10);
        // Place glider at top-left
        initial.setCellState(Cell.of(1, 2), CellState.ALIVE);
        initial.setCellState(Cell.of(2, 3), CellState.ALIVE);
        initial.setCellState(Cell.of(3, 1), CellState.ALIVE);
        initial.setCellState(Cell.of(3, 2), CellState.ALIVE);
        initial.setCellState(Cell.of(3, 3), CellState.ALIVE);
        
        game = new GameOfLife(initial);
        
        var initialLiveCells = game.getCurrentGrid().getLiveCells();
        game.evolve(4); // Glider period is 4
        var afterFourGens = game.getCurrentGrid().getLiveCells();
        
        // Glider should have moved but maintain same pattern
        assertEquals(5, initialLiveCells.size());
        assertEquals(5, afterFourGens.size());
        
        // Check that it has moved (cells are not in same positions)
        assertNotEquals(initialLiveCells, afterFourGens);
    }
    
    @Test
    @DisplayName("Custom rules work correctly")
    void customRules() throws Exception {
        // Create a rule where cells always die
        GameRules alwaysDie = (_, _) -> CellState.DEAD;
        
        var initial = Pattern.BLOCK.toGrid();
        game = new GameOfLife(initial, alwaysDie);
        
        game.evolve();
        
        var result = game.getCurrentGrid();
        for (int row = 0; row < result.getRows(); row++) {
            for (int col = 0; col < result.getCols(); col++) {
                assertFalse(result.getCellState(Cell.of(row, col)).isAlive());
            }
        }
    }
    
    @Test
    @DisplayName("Simulation with observer works")
    void simulationWithObserver() throws Exception {
        var grid = new Grid(3, 3);
        game = new GameOfLife(grid);

        var generations = new java.util.ArrayList<Integer>();

        game.simulate(5, g -> generations.add(g.getGeneration()));

        assertEquals(6, generations.size()); // 0 through 5
        assertEquals(java.util.List.of(0, 1, 2, 3, 4, 5), generations);
    }

    @Test
    @DisplayName("HighLife rules work correctly")
    void highLifeRules() {
        // HighLife: Birth on 3 or 6, Survival on 2 or 3
        var rules = GameRules.highLife();

        // Test birth on 3 neighbors (same as Conway)
        assertEquals(CellState.ALIVE, rules.nextState(CellState.DEAD, 3));

        // Test birth on 6 neighbors (different from Conway)
        assertEquals(CellState.ALIVE, rules.nextState(CellState.DEAD, 6));

        // Test survival on 2 or 3 (same as Conway)
        assertEquals(CellState.ALIVE, rules.nextState(CellState.ALIVE, 2));
        assertEquals(CellState.ALIVE, rules.nextState(CellState.ALIVE, 3));

        // Test death scenarios
        assertEquals(CellState.DEAD, rules.nextState(CellState.ALIVE, 1));
        assertEquals(CellState.DEAD, rules.nextState(CellState.ALIVE, 4));
        assertEquals(CellState.DEAD, rules.nextState(CellState.DEAD, 2));
        assertEquals(CellState.DEAD, rules.nextState(CellState.DEAD, 5));
    }
}
