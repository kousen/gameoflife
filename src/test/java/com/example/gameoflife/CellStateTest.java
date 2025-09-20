package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class CellStateTest {
    
    @Test
    @DisplayName("Alive cell with 2 or 3 neighbors survives")
    void aliveCellSurvivalRule() {
        var alive = CellState.ALIVE;

        assertAll("Alive cell survival conditions",
            () -> assertInstanceOf(CellState.Alive.class, alive.next(2),
                "Alive cell with 2 neighbors should survive"),
            () -> assertInstanceOf(CellState.Alive.class, alive.next(3),
                "Alive cell with 3 neighbors should survive")
        );
    }
    
    @Test
    @DisplayName("Alive cell dies with fewer than 2 or more than 3 neighbors")
    void aliveCellDeathRule() {
        var alive = CellState.ALIVE;

        assertAll("Alive cell death conditions",
            () -> assertInstanceOf(CellState.Dead.class, alive.next(0),
                "Alive cell with 0 neighbors should die (loneliness)"),
            () -> assertInstanceOf(CellState.Dead.class, alive.next(1),
                "Alive cell with 1 neighbor should die (loneliness)"),
            () -> assertInstanceOf(CellState.Dead.class, alive.next(4),
                "Alive cell with 4 neighbors should die (overcrowding)"),
            () -> assertInstanceOf(CellState.Dead.class, alive.next(5),
                "Alive cell with 5 neighbors should die (overcrowding)"),
            () -> assertInstanceOf(CellState.Dead.class, alive.next(8),
                "Alive cell with 8 neighbors should die (overcrowding)")
        );
    }
    
    @Test
    @DisplayName("Dead cell becomes alive with exactly 3 neighbors")
    void deadCellBirthRule() {
        var dead = CellState.DEAD;

        assertInstanceOf(CellState.Alive.class, dead.next(3),
            "Dead cell with exactly 3 neighbors should become alive");
    }
    
    @Test
    @DisplayName("Dead cell stays dead with other neighbor counts")
    void deadCellStaysDeadRule() {
        var dead = CellState.DEAD;

        assertAll("Dead cell remains dead except with 3 neighbors",
            () -> assertInstanceOf(CellState.Dead.class, dead.next(0),
                "Dead cell with 0 neighbors should stay dead"),
            () -> assertInstanceOf(CellState.Dead.class, dead.next(1),
                "Dead cell with 1 neighbor should stay dead"),
            () -> assertInstanceOf(CellState.Dead.class, dead.next(2),
                "Dead cell with 2 neighbors should stay dead"),
            () -> assertInstanceOf(CellState.Dead.class, dead.next(4),
                "Dead cell with 4 neighbors should stay dead"),
            () -> assertInstanceOf(CellState.Dead.class, dead.next(5),
                "Dead cell with 5 neighbors should stay dead"),
            () -> assertInstanceOf(CellState.Dead.class, dead.next(6),
                "Dead cell with 6 neighbors should stay dead"),
            () -> assertInstanceOf(CellState.Dead.class, dead.next(7),
                "Dead cell with 7 neighbors should stay dead"),
            () -> assertInstanceOf(CellState.Dead.class, dead.next(8),
                "Dead cell with 8 neighbors should stay dead")
        );
    }
    
    @ParameterizedTest
    @CsvSource({
        "O, true", "*, true", "█, true", "1, true",
        "., false", "' ', false", "░, false", "0, false"
    })
    @DisplayName("Character parsing creates correct state")
    void fromCharCreatesCorrectState(char input, boolean expectedAlive) {
        var state = CellState.fromChar(input);
        assertEquals(expectedAlive, state.isAlive());
    }
    
    @Test
    @DisplayName("Invalid character throws exception")
    void invalidCharThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> CellState.fromChar('X'));
    }
}
