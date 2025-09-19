package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class GridTest {
    
    private Grid grid;
    
    @BeforeEach
    void setup() {
        grid = new Grid(5, 5);
    }
    
    @Test
    @DisplayName("New grid is initialized with all dead cells")
    void newGridAllDead() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                assertFalse(grid.getCellState(Cell.of(row, col)).isAlive());
            }
        }
    }
    
    @Test
    @DisplayName("Setting cell state works correctly")
    void setCellState() {
        var cell = Cell.of(2, 3);
        grid.setCellState(cell, new CellState.Alive());
        
        assertTrue(grid.getCellState(cell).isAlive());
    }
    
    @Test
    @DisplayName("Grid creation from string pattern works")
    void gridFromString() {
        var pattern = """
            .O.
            ..O
            OOO
            """;
        var patternGrid = new Grid(pattern);
        
        assertTrue(patternGrid.getCellState(Cell.of(0, 1)).isAlive());
        assertTrue(patternGrid.getCellState(Cell.of(1, 2)).isAlive());
        assertTrue(patternGrid.getCellState(Cell.of(2, 0)).isAlive());
        assertFalse(patternGrid.getCellState(Cell.of(0, 0)).isAlive());
    }
    
    @Test
    @DisplayName("Neighbor counting works for corner cell")
    void cornerCellNeighbors() {
        grid.setCellState(Cell.of(0, 1), new CellState.Alive());
        grid.setCellState(Cell.of(1, 0), new CellState.Alive());
        grid.setCellState(Cell.of(1, 1), new CellState.Alive());
        
        assertEquals(3, grid.countLiveNeighbors(Cell.of(0, 0)));
    }
    
    @Test
    @DisplayName("Neighbor counting works for center cell")
    void centerCellNeighbors() {
        // Create a cross pattern around center
        grid.setCellState(Cell.of(1, 2), new CellState.Alive()); // North
        grid.setCellState(Cell.of(3, 2), new CellState.Alive()); // South
        grid.setCellState(Cell.of(2, 1), new CellState.Alive()); // West
        grid.setCellState(Cell.of(2, 3), new CellState.Alive()); // East
        
        assertEquals(4, grid.countLiveNeighbors(Cell.of(2, 2)));
    }
    
    @Test
    @DisplayName("Toroidal boundary wraps correctly")
    void toroidalBoundary() {
        var toroidalGrid = new Grid(3, 3, new BoundaryCondition.Toroidal());
        
        // Set cell at top-left
        toroidalGrid.setCellState(Cell.of(0, 0), new CellState.Alive());
        
        // Check wrapped neighbors
        var neighbors = toroidalGrid.getNeighbors(Cell.of(0, 0));
        assertEquals(8, neighbors.size()); // Should have all 8 neighbors in toroidal
        
        // Verify wrapping
        assertTrue(neighbors.contains(Cell.of(2, 2))); // Bottom-right wraps to top-left
    }
    
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    @DisplayName("Invalid grid dimensions throw exception")
    void invalidDimensionsThrow(int size) {
        assertThrows(IllegalArgumentException.class, () -> new Grid(size, 5));
        assertThrows(IllegalArgumentException.class, () -> new Grid(5, size));
    }
    
    @Test
    @DisplayName("Grid copy creates independent copy")
    void gridCopyIsIndependent() {
        grid.setCellState(Cell.of(2, 2), new CellState.Alive());
        var copy = grid.copy();
        
        // Modify original
        grid.setCellState(Cell.of(3, 3), new CellState.Alive());
        
        // Copy should not be affected
        assertTrue(copy.getCellState(Cell.of(2, 2)).isAlive());
        assertFalse(copy.getCellState(Cell.of(3, 3)).isAlive());
    }
    
    @Test
    @DisplayName("Grid equality works correctly")
    void gridEquality() {
        var grid1 = new Grid(3, 3);
        var grid2 = new Grid(3, 3);
        
        grid1.setCellState(Cell.of(1, 1), new CellState.Alive());
        grid2.setCellState(Cell.of(1, 1), new CellState.Alive());
        
        assertEquals(grid1, grid2);
        
        grid2.setCellState(Cell.of(2, 2), new CellState.Alive());
        assertNotEquals(grid1, grid2);
    }
}
