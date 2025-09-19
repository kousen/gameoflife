package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {
    
    @Test
    @DisplayName("Direction application works correctly")
    void directionApplication() {
        var center = Cell.of(5, 5);
        
        assertEquals(Cell.of(4, 5), Direction.NORTH.apply(center));
        assertEquals(Cell.of(6, 5), Direction.SOUTH.apply(center));
        assertEquals(Cell.of(5, 6), Direction.EAST.apply(center));
        assertEquals(Cell.of(5, 4), Direction.WEST.apply(center));
        assertEquals(Cell.of(4, 6), Direction.NORTHEAST.apply(center));
    }
    
    @Test
    @DisplayName("All neighbors returns 8 cells")
    void allNeighborsReturnsEight() {
        var center = Cell.of(10, 10);
        var neighbors = Direction.getAllNeighbors(center);
        
        assertEquals(8, neighbors.size());
        
        // Check all neighbors are adjacent
        neighbors.forEach(neighbor -> {
            var rowDiff = Math.abs(neighbor.row() - center.row());
            var colDiff = Math.abs(neighbor.col() - center.col());
            
            assertTrue(rowDiff <= 1);
            assertTrue(colDiff <= 1);
            assertFalse(rowDiff == 0 && colDiff == 0);
        });
    }
}
