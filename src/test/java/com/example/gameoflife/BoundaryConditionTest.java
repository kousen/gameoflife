package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BoundaryConditionTest {
    
    @Test
    @DisplayName("Fixed boundary rejects out-of-bounds cells")
    void fixedBoundaryRejects() {
        var fixed = new BoundaryCondition.Fixed();
        
        assertTrue(fixed.wrap(Cell.of(2, 3), 5, 5).isPresent());
        assertFalse(fixed.wrap(Cell.of(5, 3), 5, 5).isPresent());
        assertFalse(fixed.wrap(Cell.of(2, -1), 5, 5).isPresent());
    }
    
    @Test
    @DisplayName("Toroidal boundary wraps around edges")
    void toroidalBoundaryWraps() {
        var toroidal = new BoundaryCondition.Toroidal();
        
        var wrapped = toroidal.wrap(Cell.of(-1, -1), 5, 5);
        assertTrue(wrapped.isPresent());
        assertEquals(Cell.of(4, 4), wrapped.get());
        
        wrapped = toroidal.wrap(Cell.of(6, 7), 5, 5);
        assertTrue(wrapped.isPresent());
        assertEquals(Cell.of(1, 2), wrapped.get());
    }
    
    @Test
    @DisplayName("Infinite boundary accepts all cells")
    void infiniteBoundaryAcceptsAll() {
        var infinite = new BoundaryCondition.Infinite();
        
        assertTrue(infinite.wrap(Cell.of(1000, 1000), 5, 5).isPresent());
        assertTrue(infinite.wrap(Cell.of(-1000, -1000), 5, 5).isPresent());
    }
}
