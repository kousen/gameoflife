package com.example.gameoflife;

public sealed interface CellState permits CellState.Alive, CellState.Dead {
    // Singleton instances to prevent memory allocation on every state change
    CellState ALIVE = new Alive();
    CellState DEAD = new Dead();

    record Alive() implements CellState {
        @Override
        public boolean isAlive() { return true; }

        @Override
        public char symbol() { return '█'; }

        @Override
        public CellState next(int liveNeighbors) {
            return (liveNeighbors == 2 || liveNeighbors == 3) ? this : DEAD;
        }
    }

    record Dead() implements CellState {
        @Override
        public boolean isAlive() { return false; }

        @Override
        public char symbol() { return '░'; }

        @Override
        public CellState next(int liveNeighbors) {
            return liveNeighbors == 3 ? ALIVE : this;
        }
    }

    boolean isAlive();
    char symbol();
    CellState next(int liveNeighbors);

    static CellState fromBoolean(boolean alive) {
        return alive ? ALIVE : DEAD;
    }

    static CellState fromChar(char c) {
        return switch (c) {
            case 'O', '*', '█', '1' -> ALIVE;
            case '.', ' ', '░', '0' -> DEAD;
            default -> throw new IllegalArgumentException("Unknown cell character: " + c);
        };
    }
}
