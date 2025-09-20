package com.example.gameoflife;

public record Cell(int row, int col) implements Comparable<Cell> {

    public static Cell of(int row, int col) {
        return new Cell(row, col);
    }
    
    public Cell withOffset(int dRow, int dCol) {
        return new Cell(row + dRow, col + dCol);
    }
    
    @Override
    public int compareTo(Cell other) {
        return row != other.row ? 
            Integer.compare(row, other.row) : 
            Integer.compare(col, other.col);
    }
}
