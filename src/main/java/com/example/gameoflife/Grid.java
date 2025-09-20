package com.example.gameoflife;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.util.stream.Collectors;

public class Grid {
    private final int rows;
    private final int cols;
    private final Map<Cell, CellState> cells;
    private final BoundaryCondition boundary;
    
    public Grid(int rows, int cols) {
        this(rows, cols, new BoundaryCondition.Fixed());
    }
    
    public Grid(int rows, int cols, BoundaryCondition boundary) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException(
                "Grid dimensions must be positive: %dx%d".formatted(rows, cols)
            );
        }
        this.rows = rows;
        this.cols = cols;
        this.cells = new ConcurrentHashMap<>();
        this.boundary = Objects.requireNonNull(boundary, "Boundary condition cannot be null");
    }
    
    public Grid(String pattern) {
        this(pattern, new BoundaryCondition.Fixed());
    }
    
    public Grid(String pattern, BoundaryCondition boundary) {
        var lines = pattern.trim().lines().toList();
        this.rows = lines.size();
        this.cols = lines.isEmpty() ? 0 : lines.getFirst().length();
        this.cells = new ConcurrentHashMap<>();
        this.boundary = Objects.requireNonNull(boundary, "Boundary condition cannot be null");
        
        IntStream.range(0, rows).forEach(row -> {
            var line = lines.get(row);
            IntStream.range(0, Math.min(cols, line.length())).forEach(col -> {
                var state = CellState.fromChar(line.charAt(col));
                if (state.isAlive()) {
                    cells.put(Cell.of(row, col), state);
                }
            });
        });
    }
    
    public void setCellState(Cell cell, CellState state) {
        boundary.wrap(cell, rows, cols).ifPresent(wrapped -> {
            if (state.isAlive()) {
                cells.put(wrapped, state);
            } else {
                cells.remove(wrapped);
            }
        });
    }
    
    public CellState getCellState(Cell cell) {
        return boundary.wrap(cell, rows, cols)
            .map(wrapped -> cells.getOrDefault(wrapped, CellState.DEAD))
            .orElse(CellState.DEAD);
    }
    
    public List<Cell> getNeighbors(Cell cell) {
        return Direction.getAllNeighbors(cell).stream()
            .map(neighbor -> boundary.wrap(neighbor, rows, cols))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }
    
    public int countLiveNeighbors(Cell cell) {
        return (int) getNeighbors(cell).stream()
            .map(this::getCellState)
            .filter(CellState::isAlive)
            .count();
    }
    
    public Set<Cell> getLiveCells() {
        return new HashSet<>(cells.keySet());
    }
    
    // Generate signals for a single cell (8 neighbor positions)
    private List<Cell> generateSignals(Cell cell) {
        return Direction.getAllNeighbors(cell).stream()
            .map(neighbor -> boundary.wrap(neighbor, rows, cols))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    // Count signals at each position from all live cells
    private Map<Cell, Integer> countSignals(Set<Cell> liveCells) {
        return liveCells.stream()
            .flatMap(cell -> generateSignals(cell).stream())
            .collect(Collectors.groupingBy(
                cell -> cell,
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));
    }

    public Grid evolveWith(GameRules rules, Executor executor) throws InterruptedException, ExecutionException, TimeoutException {
        var nextGrid = new Grid(rows, cols, boundary);

        // Count signals from all live cells
        var signalCounts = countSignals(cells.keySet());

        // All cells that need evaluation: live cells + cells receiving signals
        Set<Cell> cellsToEvaluate = new HashSet<>(cells.keySet());
        cellsToEvaluate.addAll(signalCounts.keySet());

        var futures = cellsToEvaluate.stream()
            .map(cell -> CompletableFuture.supplyAsync(() -> {
                var currentState = getCellState(cell);
                var signalCount = signalCounts.getOrDefault(cell, 0);
                var nextState = rules.nextState(currentState, signalCount);
                return Map.entry(cell, nextState);
            }, executor))
            .toList();

        // Add timeout to prevent indefinite blocking
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
            .get(5, TimeUnit.SECONDS);

        futures.stream()
            .map(CompletableFuture::join)
            .forEach(entry -> nextGrid.setCellState(entry.getKey(), entry.getValue()));

        return nextGrid;
    }
    
    public Grid copy() {
        var newGrid = new Grid(rows, cols, boundary);
        cells.forEach(newGrid::setCellState);
        return newGrid;
    }
    
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public BoundaryCondition getBoundary() { return boundary; }
    
    @Override
    public String toString() {
        // Pre-size StringBuilder for better performance
        var sb = new StringBuilder(rows * (cols + 1));
        IntStream.range(0, rows).forEach(row -> {
            IntStream.range(0, cols)
                .mapToObj(col -> getCellState(Cell.of(row, col)).symbol())
                .forEach(sb::append);
            sb.append('\n');
        });
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Grid other &&
            rows == other.rows &&
            cols == other.cols &&
            cells.equals(other.cells) &&
            boundary.equals(other.boundary);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(rows, cols, cells, boundary);
    }
}
