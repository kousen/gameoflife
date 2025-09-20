# Claude Code Project Context

## Project Overview
Conway's Game of Life implementation in Java 25 with modern features and performance optimizations.

## Technology Stack
- **Java Version**: 25
- **Build Tool**: Gradle 9.0+ with Kotlin DSL
- **Test Framework**: JUnit 6.0.0-RC3
- **Java Features Used**: Virtual threads, sealed interfaces, records, pattern matching, text blocks

## Key Design Decisions

### Architecture
- **Sealed Interfaces**: `CellState` and `BoundaryCondition` for type-safe state management
- **Records**: Immutable data carriers (Cell, state implementations)
- **Functional Interfaces**: `GameRules` for pluggable rule systems
- **Virtual Threads**: Parallel cell evolution via `Executors.newVirtualThreadPerTaskExecutor()`

### Design Patterns Used

#### Creational Patterns
- **Singleton**: `CellState.ALIVE` and `CellState.DEAD` - single instances prevent memory allocation
- **Static Factory Methods**:
  - `Cell.of(row, col)` - more expressive than constructor
  - `GameRules.conway()` and `GameRules.highLife()` - named constructors
  - `CellState.fromChar()` and `CellState.fromBoolean()` - conversion factories
- **Builder Pattern** (implicit): Method chaining in Pattern (e.g., `Pattern.GLIDER.toGrid()`)

#### Behavioral Patterns
- **Strategy**: `BoundaryCondition` interface with three strategies (Fixed, Toroidal, Infinite)
- **Strategy**: `GameRules` interface for different rule sets (Conway, HighLife, custom)
- **Template Method** (subtle): Grid evolution follows template: evaluate cells → apply rules → create new grid
- **Observer** (simple): `simulate()` method accepts Consumer<GameOfLife> for observation
- **Command** (functional): Direction enum encapsulates coordinate transformations

#### Structural Patterns
- **Flyweight**: Reused CellState instances (ALIVE/DEAD) shared across all cells
- **Facade**: `GameOfLife` class provides simple interface to complex grid evolution
- **Adapter** (implicit): Pattern adapts string representations to Grid objects

#### Other Patterns
- **Immutable Object**: Cell, CellState implementations using records
- **Value Object**: Cell record represents coordinates as values
- **Null Object** (via Optional): Empty Optional instead of null for invalid cells
- **Factory Method**: Each BoundaryCondition creates appropriate Optional<Cell>

### Performance Optimizations
- **Singleton Cell States**: `CellState.ALIVE` and `CellState.DEAD` to prevent memory allocation
- **ConcurrentHashMap**: Thread-safe sparse matrix for live cells only
- **Efficient Neighbor Evaluation**: HashSet to eliminate duplicate cell evaluations
- **Timeout Protection**: 5-second timeout on CompletableFuture operations

### Testing Strategy
- **Comprehensive Unit Tests**: All core components have dedicated test classes
- **assertAll Groups**: Related assertions grouped for better diagnostics
- **Parameterized Tests**: Used for testing multiple input scenarios
- **Test Patterns**: Blinker (oscillator), Glider (spaceship), Block (still life)

## Code Style Guidelines

### General
- **NO COMMENTS** in code unless explicitly requested
- Use singleton instances for CellState (ALIVE/DEAD)
- Prefer `Optional.orElseThrow()` over `Optional.get()`
- Use `assertAll` for grouping related test assertions
- Use underscore `_` for unused lambda parameters

### Naming Conventions
- `withOffset` instead of `translate` for coordinate calculations
- `wrap` for boundary condition transformations
- Clear method names that describe intent

### Testing
- Test method names should use `@DisplayName` annotations
- Group related assertions with `assertAll`
- Include descriptive messages in assertions

## Common Commands

```bash
# Build and run tests
./gradlew clean build

# Run tests only
./gradlew test

# Run the application
./gradlew run

# Run specific patterns
./gradlew runBlinker
./gradlew runGlider

# Check for code issues (if linting is configured)
# Add linting commands here when configured
```

## Project Structure

- `src/main/java/com/example/gameoflife/` - Main source code
  - `Cell.java` - Immutable coordinate record
  - `CellState.java` - Sealed interface for cell states
  - `Direction.java` - Enum for 8 neighbor directions
  - `BoundaryCondition.java` - Sealed interface for grid boundaries
  - `Grid.java` - Game grid with sparse matrix implementation
  - `GameRules.java` - Functional interface for rule variants
  - `Pattern.java` - Predefined patterns (Blinker, Glider, etc.)
  - `GameOfLife.java` - Main game engine with virtual threads
  - `Main.java` - Demo application

- `src/test/java/com/example/gameoflife/` - Test classes
  - Comprehensive unit tests for all components

## Known Issues and TODOs

- None currently identified

## Performance Considerations

- Maximum grid size limited by available memory
- Virtual threads provide efficient parallelism for large grids
- Sparse matrix implementation optimizes memory for sparse patterns
- Consider implementing infinite grid with quadtree for very large simulations

## Future Enhancements (Potential)

- GUI visualization (JavaFX/Swing)
- Pattern file I/O (RLE format support)
- Performance metrics and benchmarking
- Additional rule sets (Day & Night, Seeds, etc.)
- Pattern analysis and recognition

## AI Assistant Notes

When working on this project:
1. Always run tests after making changes
2. Use singleton CellState instances (ALIVE/DEAD)
3. Maintain the functional programming style where appropriate
4. Keep the code clean and concise
5. Follow existing patterns and conventions
6. Ensure all tests pass before committing

## Recent Changes

- Upgraded from Java 21 to Java 25
- Upgraded to JUnit 6.0.0-RC3
- Replaced all CellState instantiations with singleton constants
- Renamed `translate` method to `withOffset` for clarity
- Added `assertAll` groupings in tests
- Fixed Optional.get() warnings with proper checks
- Changed project name from `gameoflife-java21` to `gameoflife`