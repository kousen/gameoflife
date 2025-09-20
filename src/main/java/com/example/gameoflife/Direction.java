package com.example.gameoflife;

import java.util.Arrays;
import java.util.List;

public enum Direction {
    NORTH(-1, 0), NORTHEAST(-1, 1), EAST(0, 1), SOUTHEAST(1, 1),
    SOUTH(1, 0), SOUTHWEST(1, -1), WEST(0, -1), NORTHWEST(-1, -1);
    
    private final int dRow;
    private final int dCol;
    
    Direction(int dRow, int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }
    
    public Cell apply(Cell cell) {
        return cell.withOffset(dRow, dCol);
    }
    
    public static List<Cell> getAllNeighbors(Cell cell) {
        return Arrays.stream(values())
            .map(dir -> dir.apply(cell))
            .toList();
    }
}
