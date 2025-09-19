package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class PatternTest {
    
    @Test
    @DisplayName("Pattern converts to grid correctly")
    void patternToGrid() {
        var grid = Pattern.GLIDER.toGrid();
        
        assertTrue(grid.getCellState(Cell.of(0, 1)).isAlive());
        assertTrue(grid.getCellState(Cell.of(1, 2)).isAlive());
        assertTrue(grid.getCellState(Cell.of(2, 0)).isAlive());
    }
    
    @Test
    @DisplayName("Pattern respects boundary condition")
    void patternWithBoundary() {
        var toroidal = Pattern.BLINKER.toGrid(new BoundaryCondition.Toroidal());
        var fixed = Pattern.BLINKER.toGrid(new BoundaryCondition.Fixed());
        
        assertEquals(BoundaryCondition.Toroidal.class, toroidal.getBoundary().getClass());
        assertEquals(BoundaryCondition.Fixed.class, fixed.getBoundary().getClass());
    }
    
    @Test
    @DisplayName("Complex pattern loads correctly")
    void complexPattern() {
        var grid = Pattern.PULSAR.toGrid();
        
        // Pulsar is 13x13
        assertEquals(13, grid.getRows());
        assertEquals(13, grid.getCols());
        
        // Check a few specific cells that should be alive
        assertTrue(grid.getCellState(Cell.of(0, 2)).isAlive());
        assertTrue(grid.getCellState(Cell.of(0, 3)).isAlive());
        assertTrue(grid.getCellState(Cell.of(0, 4)).isAlive());
    }
}
