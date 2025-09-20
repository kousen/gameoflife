package com.example.gameoflife;

@FunctionalInterface
public interface GameRules {
    CellState nextState(CellState currentState, int liveNeighbors);

    static GameRules conway() {
        return (currentState, liveNeighbors) -> switch (currentState) {
            case CellState.Alive _ -> switch (liveNeighbors) {
                case 2, 3 -> currentState;
                default -> CellState.DEAD;
            };
            case CellState.Dead _ -> liveNeighbors == 3 ? CellState.ALIVE : currentState;
        };
    }

    static GameRules highLife() {
        return (currentState, liveNeighbors) -> switch (currentState) {
            case CellState.Alive _ -> switch (liveNeighbors) {
                case 2, 3 -> currentState;
                default -> CellState.DEAD;
            };
            case CellState.Dead _ -> switch (liveNeighbors) {
                case 3, 6 -> CellState.ALIVE;
                default -> currentState;
            };
        };
    }
}
