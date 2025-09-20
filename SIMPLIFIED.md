# Simplified Game of Life - Educational Version

## Purpose

This branch contains a minimal, single-file implementation of Conway's Game of Life designed for teaching beginners. It demonstrates the core algorithm without any advanced features, design patterns, or OOP complexity.

## What This Version Shows

### Core Algorithm Only
- 2D boolean array for the grid
- Simple nested loops for evolution
- Basic neighbor counting
- Conway's rules in their simplest form

### No Advanced Features
- No design patterns
- No OOP beyond basic class structure
- No threading or parallelism
- No multiple boundary types
- No configurable rules
- No error handling
- Single file, ~100 lines

## Educational Progression

Use this as the starting point for teaching, then progress through:

1. **This branch (`simplified`)**: Core algorithm, procedural style
2. **`main` branch**: Full OOP with patterns, modern Java features
3. **`signals-based` branch**: Alternative metaphor, same complexity
4. **`pre-java8` branch**: Shows what Java was like before lambdas

## Key Learning Points

### 1. The Essential Algorithm
```java
// Count neighbors
int count = 0;
for (int r = row - 1; r <= row + 1; r++) {
    for (int c = col - 1; c <= col + 1; c++) {
        if (r == row && c == col) continue;
        if (inBounds(r, c) && grid[r][c]) count++;
    }
}

// Apply rules
if (isAlive) {
    nextGrid[row][col] = (count == 2 || count == 3);
} else {
    nextGrid[row][col] = (count == 3);
}
```

### 2. Why We Need Better Design

This simple version has limitations:
- Fixed grid size
- Fixed boundary behavior
- Fixed rules
- No way to extend without modifying code
- No parallelism
- Poor separation of concerns

### 3. Refactoring Opportunities

Students can identify and implement:
- Extract neighbor counting to a strategy
- Make grid size configurable
- Add different boundary conditions
- Separate display from logic
- Add pattern loading
- Implement different rule sets

## Running the Simple Version

```bash
# Compile
javac SimpleGameOfLife.java

# Run
java SimpleGameOfLife
```

## Exercises for Students

1. **Add a block pattern** - Modify to show a 2x2 block (stable pattern)
2. **Add user input** - Let user specify initial live cells
3. **Add file loading** - Read patterns from a text file
4. **Extract methods** - Refactor duplicate code
5. **Add boundaries** - What happens at edges? Implement wrapping
6. **Different rules** - Implement HighLife (B36/S23)
7. **Performance** - How would you optimize for large grids?
8. **Testing** - How would you test this code?

## Comparison with Full Version

| Feature | Simple Version | Full Version |
|---------|---------------|--------------|
| Lines of Code | ~100 | ~500 |
| Files | 1 | 15+ |
| Design Patterns | 0 | 6+ |
| Boundary Types | 1 (fixed) | 3 |
| Rule Sets | 1 (Conway) | Pluggable |
| Parallelism | None | Virtual threads |
| Testing | None | Comprehensive |
| Extensibility | Poor | Excellent |

## Teaching Tips

1. **Start here** - Show the working algorithm first
2. **Identify pain points** - What's hard to change?
3. **Introduce patterns gradually** - Strategy for rules, Factory for patterns
4. **Show evolution** - How did we get to the full version?
5. **Discuss trade-offs** - When is simple better? When do we need complexity?

## Next Steps

After understanding this version:
1. Try adding one improvement (e.g., configurable size)
2. Compare with the full version's approach
3. Discuss why the added complexity might be worth it
4. Implement one design pattern (e.g., Strategy for rules)