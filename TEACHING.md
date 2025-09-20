# Teaching Guide - Conway's Game of Life

## Repository Structure for Education

This repository contains multiple branches designed for teaching software design, patterns, and Java evolution. Each branch represents a different level of complexity and design sophistication.

## Branch Progression for Teaching

### 1. `simplified` - Starting Point (Week 1-2)
- **Purpose**: Introduce the algorithm
- **Complexity**: Minimal (~100 lines, single file)
- **Concepts**: Loops, arrays, basic logic
- **Key Learning**: Understanding the problem domain

```bash
git checkout simplified
javac SimpleGameOfLife.java
java SimpleGameOfLife
```

### 2. `pre-java8` - Traditional OOP (Week 3-4)
- **Purpose**: Classic Java patterns without modern features
- **Complexity**: Medium
- **Concepts**: Abstract classes, anonymous inner classes, explicit patterns
- **Key Learning**: Why Java 8 was revolutionary

```bash
git checkout pre-java8
```

### 3. `main` - Modern Professional Java (Week 5-8)
- **Purpose**: Production-quality code with all modern features
- **Complexity**: High (~500 lines, full architecture)
- **Concepts**:
  - Design Patterns (Strategy, Singleton, Factory, Observer, Flyweight, Facade)
  - Functional programming (lambdas, streams, method references)
  - Modern Java (records, sealed types, pattern matching, virtual threads)
- **Key Learning**: Professional software architecture

```bash
git checkout main
./gradlew run
```

### 4. `signals-based` - Alternative Design (Week 9)
- **Purpose**: Same complexity, different metaphor
- **Complexity**: High (same as main)
- **Concepts**: How different mental models affect implementation
- **Key Learning**: Multiple valid approaches exist

```bash
git checkout signals-based
./gradlew run
```

## Design Patterns Teaching Map

### Patterns in Main Branch

| Pattern | Location | Teaching Points |
|---------|----------|-----------------|
| **Singleton** | `CellState.ALIVE/DEAD` | Memory optimization, instance control |
| **Strategy** | `GameRules`, `BoundaryCondition` | Algorithm families, runtime selection |
| **Factory Method** | `Cell.of()`, `GameRules.conway()` | Object creation abstraction |
| **Flyweight** | Shared `CellState` instances | Memory optimization for many objects |
| **Observer** | `simulate(Consumer)` | Decoupled notification |
| **Facade** | `GameOfLife` class | Simplified interface to complex subsystem |
| **Template Method** | Grid evolution process | Algorithm skeleton with hooks |
| **Command** | `Direction` enum | Encapsulated requests |

## Suggested Course Structure

### Module 1: Algorithm First (Week 1-2)
- Start with `simplified` branch
- Focus on understanding Conway's rules
- Implement variations (different patterns)
- Identify pain points in the simple design

### Module 2: Object-Oriented Design (Week 3-4)
- Refactor simplified version step by step
- Introduce one pattern at a time
- Show `pre-java8` branch for comparison
- Discuss verbosity vs. clarity

### Module 3: Modern Java Features (Week 5-6)
- Switch to `main` branch
- Compare functional vs. imperative
- Explore Java 8+ features
- Virtual threads and parallelism

### Module 4: Design Patterns Deep Dive (Week 7-8)
- Identify all patterns in `main`
- Implement patterns from scratch
- Discuss pattern trade-offs
- When patterns become anti-patterns

### Module 5: Alternative Approaches (Week 9-10)
- Explore `signals-based` branch
- Compare with functional approach (reference implementation)
- Discuss mental models in programming
- Performance analysis

## Assignments and Exercises

### Assignment 1: Extend the Simple Version
Starting from `simplified`:
1. Add toroidal boundary conditions
2. Implement pattern loading from file
3. Add different rule sets (HighLife)

### Assignment 2: Pattern Implementation
Starting from scratch:
1. Implement Strategy pattern for rules
2. Add Factory pattern for patterns
3. Use Observer for generation tracking

### Assignment 3: Modern Refactoring
Take `pre-java8` code and:
1. Convert to use lambdas
2. Replace loops with streams
3. Use Optional properly

### Assignment 4: Performance Analysis
1. Benchmark all versions
2. Analyze memory usage
3. Compare parallelization approaches
4. Write performance report

### Assignment 5: Design Your Own
1. Choose a different cellular automaton
2. Apply learned patterns
3. Document design decisions
4. Present to class

## Discussion Topics

### Week 1-2: Simplicity vs. Complexity
- When is simple code better?
- Technical debt vs. over-engineering
- YAGNI principle

### Week 3-4: Design Patterns
- Pattern recognition in existing code
- When patterns help vs. hurt
- Pattern evolution with language features

### Week 5-6: Language Evolution
- How Java has changed
- Functional vs. OOP
- Performance implications

### Week 7-8: Architecture
- Separation of concerns
- Dependency management
- Testing strategies

### Week 9-10: Software Philosophy
- Different mental models
- Code as communication
- Maintenance vs. performance

## Assessment Rubric

### Code Quality (40%)
- Correctness
- Readability
- Appropriate abstraction
- Proper pattern usage

### Design (30%)
- Extensibility
- Separation of concerns
- Pattern selection
- Architecture decisions

### Testing (20%)
- Test coverage
- Test quality
- Edge cases
- Performance tests

### Documentation (10%)
- Code comments (when needed)
- README quality
- Design documentation
- Rationale for decisions

## Resources

### Required Reading
- Design Patterns (Gang of Four) - Chapters on used patterns
- Effective Java (Bloch) - Items on design and Java features
- Clean Code (Martin) - Principles and patterns

### Optional Reading
- Java Concurrency in Practice
- Functional Programming in Java
- Growing Object-Oriented Software

## Tips for Instructors

1. **Start Simple**: Don't show `main` branch first - build up to it
2. **Live Coding**: Refactor `simplified` to add patterns live
3. **Compare Approaches**: Show same problem, different solutions
4. **Encourage Questioning**: Why this pattern? Why this structure?
5. **Real-World Context**: When would you use each approach?

## Contact and Contributions

For questions or to contribute additional teaching materials, please open an issue or submit a pull request.