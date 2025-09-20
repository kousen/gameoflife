# Pre-Java 8 Implementation Notes

This branch demonstrates Conway's Game of Life without Java 8+ features, showing traditional Java patterns.

## Major Changes from Main Branch

### 1. No Functional Interfaces
- `GameRules` interface → `GameRulesBase` abstract class
- Lambda expressions → Anonymous inner classes

### 2. No Lambdas or Method References
**Modern (Java 8+):**
```java
cells.stream()
    .filter(CellState::isAlive)
    .count();
```

**Pre-Java 8:**
```java
int count = 0;
for (CellState state : cells) {
    if (state.isAlive()) {
        count++;
    }
}
```

### 3. No Streams API
**Modern:**
```java
return Direction.getAllNeighbors(cell).stream()
    .map(neighbor -> boundary.wrap(neighbor, rows, cols))
    .filter(Optional::isPresent)
    .map(Optional::get)
    .toList();
```

**Pre-Java 8:**
```java
List<Cell> result = new ArrayList<>();
for (Cell neighbor : Direction.getAllNeighbors(cell)) {
    Optional<Cell> wrapped = boundary.wrap(neighbor, rows, cols);
    if (wrapped.isPresent()) {
        result.add(wrapped.get());
    }
}
return result;
```

### 4. No Optional (properly)
While Optional existed in Guava, standard Java pre-8 would use null checks:
```java
Cell wrapped = boundary.wrap(cell, rows, cols);
if (wrapped != null) {
    // use wrapped
}
```

### 5. No Default Methods in Interfaces
Interfaces cannot have default implementations, requiring abstract classes or separate utility classes.

### 6. No CompletableFuture
Would use `Future` and `Callable` with `ExecutorService`:
```java
List<Future<Map.Entry<Cell, CellState>>> futures = new ArrayList<>();
for (final Cell cell : cellsToEvaluate) {
    futures.add(executor.submit(new Callable<Map.Entry<Cell, CellState>>() {
        public Map.Entry<Cell, CellState> call() {
            CellState currentState = getCellState(cell);
            int liveNeighbors = countLiveNeighbors(cell);
            CellState nextState = rules.nextState(currentState, liveNeighbors);
            return new AbstractMap.SimpleEntry<>(cell, nextState);
        }
    }));
}
```

### 7. No `var` keyword
All types must be explicitly declared:
```java
Grid nextGrid = new Grid(rows, cols, boundary);  // not var
List<Cell> neighbors = getNeighbors(cell);        // not var
```

### 8. No Text Blocks
Multi-line strings require concatenation:
```java
String pattern = ".O.\n" +
                 "..O\n" +
                 "OOO";
```

### 9. Collections Without Convenience Methods
No `List.of()`, `Set.of()`, etc.:
```java
// Modern
List<Cell> cells = List.of(cell1, cell2, cell3);

// Pre-Java 8
List<Cell> cells = Arrays.asList(cell1, cell2, cell3);
// or
List<Cell> cells = new ArrayList<>();
cells.add(cell1);
cells.add(cell2);
cells.add(cell3);
```

## Design Patterns More Visible

Without modern language features, classic design patterns become more prominent:

### Strategy Pattern (Explicit)
```java
public abstract class GameRulesBase {
    public abstract CellState nextState(CellState currentState, int liveNeighbors);
}

public class ConwayRules extends GameRulesBase {
    @Override
    public CellState nextState(CellState currentState, int liveNeighbors) {
        // implementation
    }
}
```

### Factory Method (Explicit)
```java
public class RulesFactory {
    public static GameRulesBase createConwayRules() {
        return new ConwayRules();
    }

    public static GameRulesBase createHighLifeRules() {
        return new HighLifeRules();
    }
}
```

### Observer Pattern (Explicit)
Instead of `Consumer<GameOfLife>`:
```java
public interface GameObserver {
    void onGenerationComplete(GameOfLife game);
}

public void simulate(int generations, GameObserver observer) {
    for (int i = 0; i <= generations; i++) {
        observer.onGenerationComplete(this);
        if (i < generations) {
            evolve();
        }
    }
}
```

## Educational Value

This branch shows:
1. **Why Java 8 was needed** - The verbosity of pre-Java 8 code
2. **Design patterns explicitly** - Patterns that lambdas hide
3. **Manual resource management** - More complex concurrency
4. **Type declarations everywhere** - No type inference
5. **Anonymous inner classes** - The predecessor to lambdas

## Running the Pre-Java 8 Version

The game works identically but requires Java 7 compatibility:
```bash
./gradlew run
```

Note: While this branch removes Java 8+ features from the source code, the Gradle build still requires a modern JDK to run.