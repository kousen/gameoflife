# The Signals Metaphor for Conway's Game of Life

## Overview

This branch implements Conway's Game of Life using a "signals" metaphor instead of the traditional "neighbor counting" approach. This conceptual shift makes the algorithm more intuitive while maintaining identical behavior.

## Traditional Approach vs Signals Approach

### Traditional (Neighbor Counting)
```
For each cell:
  1. Count how many live neighbors it has
  2. Apply rules based on the count
```
*"Each cell looks around and counts its living neighbors"*

### Signals Approach
```
For each live cell:
  1. Send signals to all 8 neighbor positions
For each position receiving signals:
  2. Apply rules based on signal count
```
*"Live cells broadcast their existence to neighbors"*

## How Signals Work

### 1. Signal Generation
Each live cell "broadcasts" a signal to its 8 neighboring positions:
```
    [↖][↑][↗]
    [←][●][→]  ● = Live cell sending signals
    [↙][↓][↘]
```

### 2. Signal Counting
Each position counts how many signals it receives:
- A position surrounded by 3 live cells receives 3 signals
- An empty position receiving 3 signals becomes alive (birth)
- A live cell receiving 2-3 signals stays alive (survival)

### 3. Boundary Handling
The signals metaphor elegantly handles all boundary conditions:

- **Fixed Boundary**: Signals that would go outside the grid are discarded
- **Toroidal Boundary**: Signals wrap around edges (like radio waves on a torus)
- **Infinite Boundary**: All signals are valid regardless of position

## Code Comparison

### Counting Neighbors (Traditional)
```java
for (Cell cell : getAllCells()) {
    int neighbors = countLiveNeighbors(cell);
    CellState nextState = rules.apply(currentState, neighbors);
}
```

### Receiving Signals (This Branch)
```java
Map<Cell, Integer> signals = countSignals(liveCells);
for (Cell cell : cellsReceivingSignals()) {
    int signalCount = signals.getOrDefault(cell, 0);
    CellState nextState = rules.apply(currentState, signalCount);
}
```

## Advantages of the Signals Metaphor

1. **More Intuitive**: "Broadcasting" is easier to understand than "counting neighbors"
2. **Naturally Sparse**: Only live cells generate signals (efficient for sparse grids)
3. **Parallelizable**: Signal generation is embarrassingly parallel
4. **Extensible**: Could easily extend to:
   - Different signal ranges (beyond immediate neighbors)
   - Weighted signals (different cell types with different strengths)
   - Signal decay over distance
   - Multiple signal types

## Mathematical Equivalence

The signals approach is mathematically identical to neighbor counting:
- **Signal count** at position (x,y) = **Live neighbor count** for cell (x,y)
- The same Conway's rules apply: B3/S23 (Birth on 3, Survival on 2-3)

## Performance Characteristics

The signals approach has interesting performance properties:
- **Better for sparse grids**: Only processes positions near live cells
- **Natural parallelism**: Each cell's signal generation is independent
- **Cache-friendly**: Grouped operations (all signals, then all evaluations)

## Educational Value

This metaphor helps students understand:
1. **Different perspectives** on the same algorithm
2. **Functional thinking**: Cells don't "look" for neighbors, they "receive" signals
3. **Event-driven systems**: Similar to publish-subscribe patterns
4. **Physical analogies**: Like chemical signals in biology or radio broadcasts

## Implementation Details

The key changes in this branch:

1. `Grid.generateSignals(Cell)` - Returns the 8 positions a cell signals to
2. `Grid.countSignals(Set<Cell>)` - Aggregates all signals from live cells
3. `Grid.evolveWith()` - Uses signal counts instead of neighbor counts

All other features remain unchanged:
- Virtual threads for parallelism
- Design patterns (Strategy, Factory Method, etc.)
- Boundary conditions
- Predefined patterns
- Test coverage

## Try It Yourself

Run the same commands as the main branch:
```bash
./gradlew run
./gradlew runBlinker
./gradlew runGlider
```

The behavior is identical - only the internal metaphor has changed!