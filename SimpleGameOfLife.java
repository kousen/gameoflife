/**
 * Simplified Conway's Game of Life - Educational Version
 *
 * This is a minimal implementation in a single file to demonstrate
 * the core algorithm without design patterns or advanced features.
 */
public class SimpleGameOfLife {

    private boolean[][] grid;
    private int rows;
    private int cols;

    public SimpleGameOfLife(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new boolean[rows][cols];
    }

    // Set a cell to alive
    public void setAlive(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            grid[row][col] = true;
        }
    }

    // Count live neighbors for a cell
    private int countNeighbors(int row, int col) {
        int count = 0;

        // Check all 8 neighbors
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Skip the cell itself
                if (r == row && c == col) continue;

                // Check if neighbor is in bounds and alive
                if (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c]) {
                    count++;
                }
            }
        }

        return count;
    }

    // Evolve to next generation
    public void evolve() {
        boolean[][] nextGrid = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int neighbors = countNeighbors(row, col);
                boolean isAlive = grid[row][col];

                // Apply Conway's rules
                if (isAlive) {
                    // Live cell with 2 or 3 neighbors survives
                    nextGrid[row][col] = (neighbors == 2 || neighbors == 3);
                } else {
                    // Dead cell with exactly 3 neighbors becomes alive
                    nextGrid[row][col] = (neighbors == 3);
                }
            }
        }

        grid = nextGrid;
    }

    // Print the grid
    public void print() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                System.out.print(grid[row][col] ? "█" : "░");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Main method with demo
    public static void main(String[] args) {
        SimpleGameOfLife game = new SimpleGameOfLife(10, 10);

        // Create a blinker pattern
        game.setAlive(4, 3);
        game.setAlive(4, 4);
        game.setAlive(4, 5);

        System.out.println("Conway's Game of Life - Simple Version\n");
        System.out.println("Initial state (Blinker pattern):");
        game.print();

        // Run 5 generations
        for (int i = 1; i <= 5; i++) {
            game.evolve();
            System.out.println("Generation " + i + ":");
            game.print();

            // Pause for visibility
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        // Create a glider
        SimpleGameOfLife glider = new SimpleGameOfLife(20, 20);
        glider.setAlive(1, 2);
        glider.setAlive(2, 3);
        glider.setAlive(3, 1);
        glider.setAlive(3, 2);
        glider.setAlive(3, 3);

        System.out.println("\nGlider pattern:");
        glider.print();

        // Run glider for 10 generations
        for (int i = 1; i <= 10; i++) {
            glider.evolve();
            System.out.println("Generation " + i + ":");
            glider.print();

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }
}