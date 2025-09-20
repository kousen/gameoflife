package com.example.gameoflife;

/**
 * Pre-Java 8 version using abstract class instead of functional interface
 */
public abstract class GameRulesBase {
    public abstract CellState nextState(CellState currentState, int liveNeighbors);

    public static GameRulesBase conway() {
        return new GameRulesBase() {
            @Override
            public CellState nextState(CellState currentState, int liveNeighbors) {
                if (currentState == CellState.ALIVE) {
                    if (liveNeighbors == 2 || liveNeighbors == 3) {
                        return CellState.ALIVE;
                    } else {
                        return CellState.DEAD;
                    }
                } else {
                    if (liveNeighbors == 3) {
                        return CellState.ALIVE;
                    } else {
                        return CellState.DEAD;
                    }
                }
            }
        };
    }

    public static GameRulesBase highLife() {
        return new GameRulesBase() {
            @Override
            public CellState nextState(CellState currentState, int liveNeighbors) {
                if (currentState == CellState.ALIVE) {
                    if (liveNeighbors == 2 || liveNeighbors == 3) {
                        return CellState.ALIVE;
                    } else {
                        return CellState.DEAD;
                    }
                } else {
                    if (liveNeighbors == 3 || liveNeighbors == 6) {
                        return CellState.ALIVE;
                    } else {
                        return CellState.DEAD;
                    }
                }
            }
        };
    }
}