package com.example.gameoflife;

@FunctionalInterface
public interface GameRules {
    CellState nextState(CellState currentState, long liveNeighbors);
    
    static GameRules conway() {
        return (currentState, liveNeighbors) -> switch (currentState) {
            case CellState.Alive ignored1 -> switch ((int) liveNeighbors) {
                case 2, 3 -> currentState;
                default -> new CellState.Dead();
            };
            case CellState.Dead ignored -> liveNeighbors == 3 ?
                new CellState.Alive() : currentState;
        };
    }
    
    static GameRules highLife() {
        return (currentState, liveNeighbors) -> switch (currentState) {
            case CellState.Alive ignored1 -> switch ((int) liveNeighbors) {
                case 2, 3 -> currentState;
                default -> new CellState.Dead();
            };
            case CellState.Dead ignored -> switch ((int) liveNeighbors) {
                case 3, 6 -> new CellState.Alive();
                default -> currentState;
            };
        };
    }
}
