package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    
    @Test
    @DisplayName("Cell creation with valid coordinates succeeds")
    void validCellCreation() {
        var cell = Cell.of(5, 10);
        assertEquals(5, cell.row());
        assertEquals(10, cell.col());
    }
    
    @Test
    @DisplayName("Cell translation works correctly")
    void cellTranslation() {
        var cell = Cell.of(5, 5);
        var translated = cell.translate(2, -3);
        
        assertEquals(7, translated.row());
        assertEquals(2, translated.col());
    }
    
    @Test
    @DisplayName("Cell comparison orders by row then column")
    void cellComparison() {
        var cell1 = Cell.of(1, 5);
        var cell2 = Cell.of(2, 3);
        var cell3 = Cell.of(1, 7);
        
        assertTrue(cell1.compareTo(cell2) < 0);
        assertTrue(cell1.compareTo(cell3) < 0);
        assertTrue(cell2.compareTo(cell3) > 0);
    }
    
    @Test
    @DisplayName("Cell equality and hashCode work correctly")
    void cellEquality() {
        var cell1 = Cell.of(3, 4);
        var cell2 = Cell.of(3, 4);
        var cell3 = Cell.of(4, 3);
        
        assertEquals(cell1, cell2);
        assertEquals(cell1.hashCode(), cell2.hashCode());
        assertNotEquals(cell1, cell3);
    }
}
