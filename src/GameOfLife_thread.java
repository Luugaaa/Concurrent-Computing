import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;


public class GameOfLife_thread {
    //Déclaration des variables 
    private static final int ALIVE = 1;
    private static final int DEAD = 0;

    private int[][] grid;
    private int[][] nextGenGrid;
    private Random random;
    private int seed;
    private int gridWidth;
    private int gridHeight;
    private JPanel panel;
    private JButton button;
    private CountDownLatch latch;



    public GameOfLife_thread(int gridWidth, int gridHeight) {
        this(gridWidth, gridHeight, 1234567890);
    }

    public GameOfLife_thread(int gridWidth, int gridHeight, int seed) {
        //Mise en place de l'interface 
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.seed = seed;
        random = new Random(seed);

        grid = new int[gridWidth][gridHeight];
        nextGenGrid = new int[gridWidth][gridHeight];
        initializeGrid();

        panel = new CellPanel(grid);
        JFrame frame = new JFrame("Game Of Life");
        frame.setTitle("Game Of Life");
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        button = new JButton("Suivant");
        frame.add(button, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 600);
        frame.setVisible(true);


        latch = new CountDownLatch(4); // Créez un décompte pour synchroniser les threads


        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lance les threads pour calculer la prochaine génération de la grille
                for (int i = 0; i < 4; i++) {
                    int startX = i * gridWidth / 4;
                    int startY = 0;
                    int width = gridWidth / 4;
                    int height = gridHeight;
        
                    new Thread(new CalculateNextGenTask(startX, startY, width, height)).start();
                }
        
                // Attend que tous les threads aient terminé leur calcul avant de mettre à jour la grille principale et de repeindre le panneau
                try {
                    latch.await();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        
                synchronized (grid) {
                    for (int i = 0; i < grid.length; i++) {
                        for (int j = 0; j < grid[0].length; j++) {
                            grid[i][j] = nextGenGrid[i][j];
                        }
                    }
                }
        
                panel.repaint();
            }
        });
    }

    private class CalculateNextGenTask implements Runnable {
        private int startX, startY, width, height;

        public CalculateNextGenTask(int startX, int startY, int width, int height) {
            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
        }

        @Override
        public void run() {
            // Calcule la prochaine génération de la partie de la grille assignée à ce thread
            for (int i = startX; i < startX + width; i++) {
                for (int j = startY; j < startY + height; j++) {
                    synchronized (nextGenGrid) {
                        nextGenGrid[i][j] = calculateNextGenState(i, j);
                    }
                }
            }

            // Signal au décompte que ce thread a terminé
            latch.countDown();
        }
    }
    
    private void initializeGrid() {
        // Initialise la grille principale avec des cellules vivantes et mortes aléatoires
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = random.nextInt(2);
            }
        }
    }
    
    private int calculateNextGenState(int row, int col) {
        // Calcule l'état de la cellule à la prochaine génération en fonction de son état actuel et du nombre de ses voisines vivantes
        int numLiveNeighbors = countLiveNeighbors(row, col);
    
        switch (grid[row][col]) {
            case ALIVE:
                if (numLiveNeighbors < 3) {
                    return DEAD; // Sous-population
                } else if (numLiveNeighbors > 4) {
                    return DEAD; // Surpopulation
                } else {
                    return ALIVE;
                }
            case DEAD:
                if (numLiveNeighbors == 4) {
                    return ALIVE; // Reproduction
                } else {
                    return DEAD;
                }
            default:
                throw new IllegalArgumentException("Invalid cell state");
        }
    }
    
    private int countLiveNeighbors(int row, int col) {
        // Calcule le nombre de voisines vivantes de la cellule
        int count = 0;
    
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length) {
                    if (grid[i][j] == ALIVE) {
                        count++;
                    }
                }
            }
        }
    
        // Soustrait 1 pour ne pas compter la cellule elle-même
        return count - 1;
    }
    
    public class CellPanel extends JPanel {
    
        private int[][] grid;
    
        public CellPanel(int[][] grid) {
            this.grid = grid;
        }
    
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
    
            // Dessine la grille
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    Color color = Color.BLACK;
    
                    if (grid[i][j] == 1) {
                        color = Color.WHITE;
                    }
    
                    g.setColor(color);
                    g.fillRect(i * 10, j * 10, 10, 10);
                }
            }
        }
    }


    public static void main(String[] args) {
        int gridWidth = 50;
        int gridHeight = 50;
        int seed = 1234567890;

        GameOfLife_thread myGame = new GameOfLife_thread(gridWidth, gridHeight, seed);
    }
}
