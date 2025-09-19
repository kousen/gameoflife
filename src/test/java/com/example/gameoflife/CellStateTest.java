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
        var alive = new CellState.Alive();
        
        assertInstanceOf(CellState.Alive.class, alive.next(2));
        assertInstanceOf(CellState.Alive.class, alive.next(3));
    }
    
    @Test
    @DisplayName("Alive cell dies with fewer than 2 or more than 3 neighbors")
    void aliveCellDeathRule() {
        var alive = new CellState.Alive();
        
        assertInstanceOf(CellState.Dead.class, alive.next(0));
        assertInstanceOf(CellState.Dead.class, alive.next(1));
        assertInstanceOf(CellState.Dead.class, alive.next(4));
        assertInstanceOf(CellState.Dead.class, alive.next(8));
    }
    
    @Test
    @DisplayName("Dead cell becomes alive with exactly 3 neighbors")
    void deadCellBirthRule() {
        var dead = new CellState.Dead();
        
        assertInstanceOf(CellState.Alive.class, dead.next(3));
    }
    
    @Test
    @DisplayName("Dead cell stays dead with other neighbor counts")
    void deadCellStaysDeadRule() {
        var dead = new CellState.Dead();
        
        for (int i = 0; i <= 8; i++) {
            if (i != 3) {
                assertInstanceOf(CellState.Dead.class, dead.next(i),
                    "Dead cell should stay dead with %d neighbors".formatted(i));
            }
        }
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
