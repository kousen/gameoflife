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

    @Test
    @DisplayName("All patterns load without errors")
    void allPatternsLoad() {
        // Test that all patterns can be converted to grids
        for (Pattern pattern : Pattern.values()) {
            var grid = pattern.toGrid();
            assertNotNull(grid, "Pattern " + pattern + " should create a non-null grid");
            assertTrue(grid.getRows() > 0, "Pattern " + pattern + " should have rows");
            assertTrue(grid.getCols() > 0, "Pattern " + pattern + " should have columns");

            // Each pattern should have at least some live cells
            assertFalse(grid.getLiveCells().isEmpty(),
                "Pattern " + pattern + " should have live cells");
        }
    }

    @Test
    @DisplayName("BEACON pattern loads correctly")
    void beaconPattern() {
        var grid = Pattern.BEACON.toGrid();
        assertEquals(4, grid.getRows());
        assertEquals(4, grid.getCols());
        // Beacon has 8 live cells (2 2x2 blocks diagonally)
        assertEquals(8, grid.getLiveCells().size());
    }

    @Test
    @DisplayName("TOAD pattern loads correctly")
    void toadPattern() {
        var grid = Pattern.TOAD.toGrid();
        assertEquals(2, grid.getRows());
        assertEquals(4, grid.getCols());
        // Toad has 6 live cells
        assertEquals(6, grid.getLiveCells().size());
    }

    @Test
    @DisplayName("GOSPER_GLIDER_GUN pattern loads correctly")
    void gosperGliderGunPattern() {
        var grid = Pattern.GOSPER_GLIDER_GUN.toGrid();
        assertEquals(9, grid.getRows());
        assertEquals(36, grid.getCols());
        // Gosper Glider Gun has 36 live cells in initial configuration
        assertTrue(grid.getLiveCells().size() > 30,
            "Gosper Glider Gun should have many live cells");
    }

    @Test
    @DisplayName("BLOCK pattern is stable")
    void blockPatternStable() {
        var grid = Pattern.BLOCK.toGrid();
        assertEquals(2, grid.getRows());
        assertEquals(2, grid.getCols());
        // Block has 4 live cells (2x2 square)
        assertEquals(4, grid.getLiveCells().size());
    }
}
