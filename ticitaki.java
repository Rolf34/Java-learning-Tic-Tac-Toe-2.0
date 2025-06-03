import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


class GameSettings {
    private int boardSize;
    private String player1Name;
    private String player2Name;
    private static final String CONFIG_FILE = "config.txt";
    
    public GameSettings() {
        this.boardSize = 3;
        this.player1Name = "Player 1";
        this.player2Name = "Player 2";
    }
    
    public GameSettings(int boardSize, String player1Name, String player2Name) {
        this.boardSize = boardSize;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }
    
    public int getBoardSize() { return boardSize; }
    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public String getConfigFile() { return CONFIG_FILE; }

    public void setBoardSize(int boardSize) { this.boardSize = boardSize; }
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }
    
    public void displayCurrentSettings() {
        System.out.println(": Board size: " + boardSize + "x" + boardSize);
        System.out.println("Players: " + player1Name + " (X) vs " + player2Name + " (O)");
    }
}

class GameStatistics {
    private String date;
    private int boardSize;
    private String player1Name;
    private String player2Name;
    private String winner;
    private static final String STATS_FILE = "statistics.txt";
    
    public GameStatistics(int boardSize, String player1Name, String player2Name, String winner) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = dateFormat.format(new Date());
        this.boardSize = boardSize;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.winner = winner;
    }
    
    public String getDate() { return date; }
    public int getBoardSize() { return boardSize; }
    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public String getWinner() { return winner; }
    public String getStatsFile() { return STATS_FILE; }
    
    public String formatStatistics() {
        return "Date: " + date + "\n" +
               "Board Size: " + boardSize + "x" + boardSize + "\n" +
               "Player 1: " + player1Name + " (X)\n" +
               "Player 2: " + player2Name + " (O)\n" +
               "Winner: " + winner + "\n" +
               "------------------------\n";
    }
}

class GameBoard {
    private char[][] board;
    private int boardSize;
    private int rows;
    private int cols;
    
    public GameBoard(int boardSize) {
        this.boardSize = boardSize;
        this.rows = boardSize * 2 + 1;
        this.cols = boardSize * 4 - 1;
        this.board = createGameBoard();
    }
    
    private char[][] createGameBoard() {
        char[][] displayBoard = new char[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                displayBoard[i][j] = ' ';
            }
        }

        for (int i = 0; i < boardSize; i++) {
            displayBoard[0][i * 4 + 2] = (char) ('1' + i);
            displayBoard[i * 2 + 2][0] = (char) ('1' + i);
        }
        
        for (int i = 1; i < rows; i += 2) {
            for (int j = 1; j < cols; j++) {
                displayBoard[i][j] = '-';
            }
        }
        
        for (int i = 0; i < rows; i++) {
            for (int j = 4; j < cols; j += 4) {
                displayBoard[i][j - 1] = '|';
            }
        }
        
        return displayBoard;
    }
    
    public void displayBoard() {
        for (char[] rowArray : board) {
            System.out.println(rowArray);
        }
    }
    
    public boolean isValidMove(int row, int col) {
        return row >= 1 && row <= boardSize && 
               col >= 1 && col <= boardSize && 
               board[(row - 1) * 2 + 2][(col - 1) * 4 + 2] == ' ';
    }
    
    public void makeMove(int row, int col, char player) {
        int displayRow = (row - 1) * 2 + 2;
        int displayCol = (col - 1) * 4 + 2;
        board[displayRow][displayCol] = player;
    }
    
    public boolean checkWin(char player) {
        for (int i = 2; i < rows; i += 2) {
            int count = 0;
            for (int j = 2; j < cols; j += 4) {
                if (board[i][j] == player) {
                    count++;
                    if (count == boardSize) return true;
                } else {
                    count = 0;
                }
            }
        }
        
        for (int j = 2; j < cols; j += 4) {
            int count = 0;
            for (int i = 2; i < rows; i += 2) {
                if (board[i][j] == player) {
                    count++;
                    if (count == boardSize) return true;
                } else {
                    count = 0;
                }
            }
        }
        
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            if (board[i * 2 + 2][i * 4 + 2] == player) {
                count++;
                if (count == boardSize) return true;
            } else {
                count = 0;
            }
        }
        
        count = 0;
        for (int i = 0; i < boardSize; i++) {
            if (board[i * 2 + 2][(boardSize - 1 - i) * 4 + 2] == player) {
                count++;
                if (count == boardSize) return true;
            } else {
                count = 0;
            }
        }
        
        return false;
    }
    
    public boolean checkDraw() {
        for (int i = 2; i < rows; i += 2) {
            for (int j = 2; j < cols; j += 4) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }
}

public class ticitaki {
    private static Scanner arbuz = new Scanner(System.in);
    private static GameSettings gameSettings = new GameSettings();
    
    public static void main(String[] args) {
        loadConfiguration();
        mainGameLoop();
        arbuz.close();
        System.out.println("Vihid z programi...");
    }

    private static void mainGameLoop() {
        boolean codeisrunning = true;
        while (codeisrunning) {
            displayMainMenu();
            if (!arbuz.hasNextLine()) {
                System.out.println("Nepravilniy vvid.");
                continue;
            }
            
            char choice = arbuz.nextLine().charAt(0);
            switch (choice) {
                case '1' -> handleGameMenu();
                case '2' -> handleSettingsMenu();
                case '3' -> showStatistics();
                case '4' -> codeisrunning = handleExitMenu();
                default -> System.out.println("Pomilka v zapiti, spobuyte she raz.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("""
                ===Welcome to Main Menu===
                 ==Vas vitae Pes Patron==
                1. Start game
                2. Settings
                3. Statistics
                4. Exit""");
    }

    private static void handleGameMenu() {
        boolean inGameMenu = true;
        while (inGameMenu) {
            gameSettings.displayCurrentSettings();
            System.out.println("Are you ready?(1) Yes! (2)Go back to main menu");
            
            char choice = arbuz.nextLine().charAt(0);
            if (choice == '2') {
                inGameMenu = false;
            } else if (choice == '1') {
                playGame();
                inGameMenu = false;
            } else {
                System.out.println("pomilka v zapiti, spobuyte she raz.");
            }
        }
    }

    private static void playGame() {
        GameBoard gameBoard = new GameBoard(gameSettings.getBoardSize());
        char currentPlayer = 'X';
        boolean isGameOver = false;
        String winner = null;
        String currentPlayerName = gameSettings.getPlayer1Name();

        while (!isGameOver) {
            System.out.println("\nZaraz shturmuye: " + currentPlayerName + " (" + currentPlayer + ")");
            gameBoard.displayBoard();

            int[] move = getPlayerMove(gameBoard);
            if (move[0] == 0) {
                break;
            }

            gameBoard.makeMove(move[0], move[1], currentPlayer);

            if (gameBoard.checkWin(currentPlayer)) {
                System.out.println("POTUZHNA PEREMOGA " + currentPlayerName + " (" + currentPlayer + ") !!!");
                winner = currentPlayerName;
                isGameOver = true;
            } else if (gameBoard.checkDraw()) {
                System.out.println("Nichya nachalnika!");
                winner = "Draw";
                isGameOver = true;
            }

            if (currentPlayer == 'X') {
                currentPlayer = 'O';
                currentPlayerName = gameSettings.getPlayer2Name();
            } else {
                currentPlayer = 'X';
                currentPlayerName = gameSettings.getPlayer1Name();
            }
        }

        System.out.println("\nItogove pole:");
        gameBoard.displayBoard();
        
        if (winner != null) {
            GameStatistics stats = new GameStatistics(
                gameSettings.getBoardSize(),
                gameSettings.getPlayer1Name(),
                gameSettings.getPlayer2Name(),
                winner
            );
            saveGameStatistics(stats);
        }
    }

    private static int[] getPlayerMove(GameBoard gameBoard) { 
        while (true) {
            System.out.println("Vvedit ryad (1-" + gameSettings.getBoardSize() + ", or 0 to exit):");
            String input = arbuz.nextLine();
            if (input.isEmpty()) continue;
            int row = input.charAt(0) - '0';
            
            if (row == 0) return new int[]{0, 0};

            System.out.println("Vvedit colonku (1-" + gameSettings.getBoardSize() + "):");
            input = arbuz.nextLine();
            if (input.isEmpty()) continue;
            int col = input.charAt(0) - '0';

            if (gameBoard.isValidMove(row, col)) { 
                return new int[]{row, col};
            }
            System.out.println("Kabinka zaynyata! Sprobuyte she raz."); 
        }
    }

    private static void handleSettingsMenu() {
        boolean inSettingsMenu = true;
        while (inSettingsMenu) {
            System.out.println("""
                ===Settings Menu===
                1. Change board size
                2. Change player names
                0. Go back to main menu""");
            
            char choice = arbuz.nextLine().charAt(0);
            switch (choice) {
                case '1' -> changeBoardSize();
                case '2' -> changePlayerNames();
                case '0' -> inSettingsMenu = false;
                default -> System.out.println("pomilka v zapiti, spobuyte she raz.");
            }
            
            if (choice == '1' || choice == '2') {
                saveConfiguration();
            }
        }
    }
    
    private static void changeBoardSize() {
        System.out.println("""
            Vibir rozmiru doshki:
            1. 3x3
            2. 5x5
            3. 7x7
            4. 9x9
            0. Cancel""");
        
        char choice = arbuz.nextLine().charAt(0);
        switch (choice) {
            case '1' -> gameSettings.setBoardSize(3);
            case '2' -> gameSettings.setBoardSize(5);
            case '3' -> gameSettings.setBoardSize(7);
            case '4' -> gameSettings.setBoardSize(9);
            case '0' -> { return; }
            default -> {
                System.out.println("pomilka v zapiti, spobuyte she raz.");
                return;
            }
        }
        System.out.println("Vstanovleno rozmir " + gameSettings.getBoardSize() + "x" + gameSettings.getBoardSize());
    }
    
    private static void changePlayerNames() {
        System.out.println("Enter name for Player 1 (X): ");
        String input = arbuz.nextLine();
        if (!input.isEmpty()) {
            gameSettings.setPlayer1Name(input);
        }
        
        System.out.println("Enter name for Player 2 (O): ");
        input = arbuz.nextLine();
        if (!input.isEmpty()) {
            gameSettings.setPlayer2Name(input);
        }
        
        System.out.println("Player names updated: " + gameSettings.getPlayer1Name() + " (X) vs " + gameSettings.getPlayer2Name() + " (O)");
    }

    private static boolean handleExitMenu() {
        System.out.println("Are you sure bra? ( enter 1(Yep) or 2(NUH UH)");
        char choice = arbuz.nextLine().charAt(0);
        if (choice == '1') {
            return false;
        } else if (choice == '2') {
            System.out.println("Deltuyemo v golovne menu.");
            return true;
        } else {
            System.out.println("Pomidka v zapiti, spobuyte she raz.");
            return true;
        }
    }
    
    private static void saveConfiguration() {
        try {
            FileWriter writer = new FileWriter(gameSettings.getConfigFile());
            writer.write("boardSize=" + gameSettings.getBoardSize() + "\n");
            writer.write("player1=" + gameSettings.getPlayer1Name() + "\n");
            writer.write("player2=" + gameSettings.getPlayer2Name() + "\n");
            writer.close();
            System.out.println("Configuration saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving configuration: " + e.getMessage());
        }
    }
    
    private static void loadConfiguration() {
        File configFile = new File(gameSettings.getConfigFile());
        if (!configFile.exists()) {
            System.out.println("No configuration file found. Using default settings.");
            return;
        }
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    
                    if (key.equals("boardSize")) {
                        gameSettings.setBoardSize(Integer.parseInt(value));
                    } else if (key.equals("player1")) {
                        gameSettings.setPlayer1Name(value);
                    } else if (key.equals("player2")) {
                        gameSettings.setPlayer2Name(value);
                    }
                }
            }
            reader.close();
            System.out.println("Configuration loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
        }
    }
    
    private static void saveGameStatistics(GameStatistics stats) {
        try {
            FileWriter writer = new FileWriter(stats.getStatsFile(), true);
            writer.write(stats.formatStatistics());
            writer.close();
            System.out.println("Game statistics saved.");
        } catch (IOException e) {
            System.out.println("Error saving game statistics: " + e.getMessage());
        }
    }
    
    private static void showStatistics() {
        GameStatistics dummyStats = new GameStatistics(3, "", "", "");
        File statsFile = new File(dummyStats.getStatsFile());
        if (!statsFile.exists()) {
            System.out.println("No statistics file found. Play some games first!");
            return;
        }
        
        System.out.println("\n===Game Statistics===");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(statsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            
            System.out.println("\nPress Enter to continue...");
            arbuz.nextLine();
        } catch (IOException e) {
            System.out.println("Error reading statistics: " + e.getMessage());
        }
    }
}