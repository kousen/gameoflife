package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BoundaryConditionTest {
    
    @Test
    @DisplayName("Fixed boundary rejects out-of-bounds cells")
    void fixedBoundaryRejects() {
        var fixed = new BoundaryCondition.Fixed();

        assertAll("Fixed boundary validation",
            () -> assertTrue(fixed.wrap(Cell.of(2, 3), 5, 5).isPresent(),
                "Valid cell (2,3) should be present in 5x5 grid"),
            () -> assertFalse(fixed.wrap(Cell.of(5, 3), 5, 5).isPresent(),
                "Cell with row=5 should be rejected in 5x5 grid"),
            () -> assertFalse(fixed.wrap(Cell.of(2, -1), 5, 5).isPresent(),
                "Cell with negative column should be rejected"),
            () -> assertFalse(fixed.wrap(Cell.of(-1, 2), 5, 5).isPresent(),
                "Cell with negative row should be rejected"),
            () -> assertTrue(fixed.wrap(Cell.of(0, 0), 5, 5).isPresent(),
                "Corner cell (0,0) should be valid"),
            () -> assertTrue(fixed.wrap(Cell.of(4, 4), 5, 5).isPresent(),
                "Corner cell (4,4) should be valid in 5x5 grid")
        );
    }
    
    @Test
    @DisplayName("Toroidal boundary wraps around edges")
    void toroidalBoundaryWraps() {
        var toroidal = new BoundaryCondition.Toroidal();

        assertAll("Toroidal wrapping in all directions",
            () -> {
                var wrapped = toroidal.wrap(Cell.of(-1, -1), 5, 5);
                assertTrue(wrapped.isPresent(), "Wrapped cell should always be present");
                assertEquals(Cell.of(4, 4), wrapped.get(), "(-1,-1) should wrap to (4,4)");
            },
            () -> {
                var wrapped = toroidal.wrap(Cell.of(6, 7), 5, 5);
                assertTrue(wrapped.isPresent(), "Wrapped cell should always be present");
                assertEquals(Cell.of(1, 2), wrapped.get(), "(6,7) should wrap to (1,2)");
            },
            () -> {
                var wrapped = toroidal.wrap(Cell.of(0, -1), 5, 5);
                assertTrue(wrapped.isPresent(), "Toroidal wrap should always return a cell");
                assertEquals(Cell.of(0, 4), wrapped.orElseThrow(), "Left edge wraps to right");
            },
            () -> {
                var wrapped = toroidal.wrap(Cell.of(-1, 0), 5, 5);
                assertTrue(wrapped.isPresent(), "Toroidal wrap should always return a cell");
                assertEquals(Cell.of(4, 0), wrapped.orElseThrow(), "Top edge wraps to bottom");
            },
            () -> {
                var wrapped = toroidal.wrap(Cell.of(5, 2), 5, 5);
                assertTrue(wrapped.isPresent(), "Toroidal wrap should always return a cell");
                assertEquals(Cell.of(0, 2), wrapped.orElseThrow(), "Bottom edge wraps to top");
            },
            () -> {
                var wrapped = toroidal.wrap(Cell.of(2, 5), 5, 5);
                assertTrue(wrapped.isPresent(), "Toroidal wrap should always return a cell");
                assertEquals(Cell.of(2, 0), wrapped.orElseThrow(), "Right edge wraps to left");
            }
        );
    }
    
    @Test
    @DisplayName("Infinite boundary accepts all cells")
    void infiniteBoundaryAcceptsAll() {
        var infinite = new BoundaryCondition.Infinite();

        assertAll("Infinite boundary accepts all coordinates",
            () -> assertTrue(infinite.wrap(Cell.of(1000, 1000), 5, 5).isPresent(),
                "Large positive coordinates should be accepted"),
            () -> assertTrue(infinite.wrap(Cell.of(-1000, -1000), 5, 5).isPresent(),
                "Large negative coordinates should be accepted"),
            () -> assertTrue(infinite.wrap(Cell.of(0, 0), 5, 5).isPresent(),
                "Origin should be accepted"),
            () -> {
                var wrapped = infinite.wrap(Cell.of(1000, 1000), 5, 5);
                assertTrue(wrapped.isPresent(), "Infinite boundary should always return a cell");
                assertEquals(Cell.of(1000, 1000), wrapped.orElseThrow(),
                    "Coordinates should remain unchanged");
            }
        );
    }
}
