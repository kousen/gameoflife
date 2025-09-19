package com.example.gameoflife;

import java.util.Optional;

public sealed interface BoundaryCondition 
    permits BoundaryCondition.Fixed, 
            BoundaryCondition.Toroidal, 
            BoundaryCondition.Infinite {
    
    record Fixed() implements BoundaryCondition {
        @Override
        public Optional<Cell> wrap(Cell cell, int rows, int cols) {
            return (cell.row() >= 0 && cell.row() < rows && 
                    cell.col() >= 0 && cell.col() < cols) ?
                Optional.of(cell) : Optional.empty();
        }
    }
    
    record Toroidal() implements BoundaryCondition {
        @Override
        public Optional<Cell> wrap(Cell cell, int rows, int cols) {
            var wrappedRow = Math.floorMod(cell.row(), rows);
            var wrappedCol = Math.floorMod(cell.col(), cols);
            return Optional.of(Cell.of(wrappedRow, wrappedCol));
        }
    }
    
    record Infinite() implements BoundaryCondition {
        @Override
        public Optional<Cell> wrap(Cell cell, int rows, int cols) {
            return Optional.of(cell); // Always valid in infinite grid
        }
    }
    
    Optional<Cell> wrap(Cell cell, int rows, int cols);
}
