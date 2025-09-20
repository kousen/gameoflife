package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    
    @Test
    @DisplayName("Cell creation with valid coordinates succeeds")
    void validCellCreation() {
        var cell = Cell.of(5, 10);

        assertAll("Cell coordinates",
            () -> assertEquals(5, cell.row(), "Row should be 5"),
            () -> assertEquals(10, cell.col(), "Column should be 10")
        );
    }
    
    @Test
    @DisplayName("Cell offset calculation works correctly")
    void cellWithOffset() {
        var cell = Cell.of(5, 5);
        var offsetCell = cell.withOffset(2, -3);

        assertAll("Cell offset calculation",
            () -> assertEquals(7, offsetCell.row(), "Row should be 5 + 2 = 7"),
            () -> assertEquals(2, offsetCell.col(), "Column should be 5 - 3 = 2")
        );
    }
    
    @Test
    @DisplayName("Cell comparison orders by row then column")
    void cellComparison() {
        var cell1 = Cell.of(1, 5);
        var cell2 = Cell.of(2, 3);
        var cell3 = Cell.of(1, 7);

        assertAll("Cell ordering",
            () -> assertTrue(cell1.compareTo(cell2) < 0,
                "Cell(1,5) should come before Cell(2,3) - row 1 < row 2"),
            () -> assertTrue(cell1.compareTo(cell3) < 0,
                "Cell(1,5) should come before Cell(1,7) - same row, col 5 < col 7"),
            () -> assertTrue(cell2.compareTo(cell3) > 0,
                "Cell(2,3) should come after Cell(1,7) - row 2 > row 1")
        );
    }
    
    @Test
    @DisplayName("Cell equality and hashCode work correctly")
    void cellEquality() {
        var cell1 = Cell.of(3, 4);
        var cell2 = Cell.of(3, 4);
        var cell3 = Cell.of(4, 3);

        assertAll("Cell equality and hashCode",
            () -> assertEquals(cell1, cell2,
                "Cells with same coordinates should be equal"),
            () -> assertEquals(cell1.hashCode(), cell2.hashCode(),
                "Equal cells should have same hashCode"),
            () -> assertNotEquals(cell1, cell3,
                "Cells with different coordinates should not be equal"),
            () -> assertNotEquals(null, cell1,
                "Cell should not equal null")
        );
    }
}
