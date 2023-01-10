import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;

public class MainWindow implements Runnable {
    private int m_height;
    private int m_width;
    private File beginningConfigurationFile;
    private byte[][] beginningConfiguration;
    private GameEngine m_engine;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow mainWindow = new MainWindow();
                mainWindow.create();
            }
        });
    }

    private JFrame window;
    private JPanel panel;
    private Timer timer;

    public void create() {
        inputField();
        inputFile();
        insertConfiguration();

        Thread mainThread = new Thread(this);
        mainThread.start();
    }

    public void run()
    {
        window = new JFrame("Game of life");
        window.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(m_height, m_width));
        panel.setBorder(new EmptyBorder(0, 0, 10, 15));
        for(int i = 0; i < m_height; i++) {
            for (int j = 0; j < m_width; j++) {
                panel.add(new GCell(m_engine.cellAt(j, i) != 0));
            }
        }
        window.getContentPane().add(BorderLayout.CENTER, panel);

        timer = new Timer(300, listener);
        timer.start();

        window.setBounds(120, 120, 640, 480);
        window.setVisible(true);
    }

    ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            update();
        }
    };

    public boolean update() {
        if(m_engine.step() == false) {
            timer.stop();
            System.out.println("Position is repeated. Program stopped");
            Scanner scanner = new Scanner(System.in);
            scanner.next();
            System.exit(0);
        }
        Component[] cells = panel.getComponents();
        byte[][] currentField = m_engine.getField();
        for(int i = 0; i < m_height; i++) {
            for(int j = 0; j < m_width; j++) {
                GCell cell = (GCell) cells[i * m_width + j];
                cell.setState(currentField[i][j] != 0);
            }
        }
        return true;
    }

    private void printWarning(String text) {
        System.out.println("Game_of_life WARNING: " + text + "\n");
    }

    private void inputField() {
        Scanner input = new Scanner(System.in);
        boolean inputCorrect = false;
        while (!inputCorrect) {
            try {
                System.out.println("Please, enter height and width of the field");
                m_width = input.nextInt();
                m_height = input.nextInt();
                inputCorrect |= true;
            } catch (Exception e) {
                printWarning("Invalid input");
                input.nextLine();
            }
        }
    }

    private void inputFile() {
        Scanner input = new Scanner(System.in);
        boolean inputCorrect = false;
        while (!inputCorrect) {
            try {
                System.out.println("Please, enter path to file");
                String pathToFile = input.nextLine();
                beginningConfigurationFile = new File(pathToFile);

                if(beginningConfigurationFile.exists() == false) {
                    printWarning("File " + pathToFile + " not exist");
                    continue;
                }
                Scanner fileInput = new Scanner(beginningConfigurationFile);
                String configText = new String();
                while (fileInput.hasNextLine())
                    configText += fileInput.nextLine() + "\n";

                beginningConfiguration = readConfiguration(configText);

                if(beginningConfiguration == null) {
                    printWarning("File " + pathToFile + " is not correct");
                    continue;
                }

                inputCorrect |= true;
            } catch (Exception e) {
                printWarning("Invalid input");
                input.nextLine();
            }
        }
    }

    private void insertConfiguration() {
        Scanner input = new Scanner(System.in);
        boolean inputCorrect = false;
        while (!inputCorrect) {
            try {
                int insX, insY;
                System.out.println("Please enter the coordinates to insert your field\nDefault: 0 0");
                insX = input.nextInt(); // InsertedX
                insY = input.nextInt(); // InsertedY
                createEngine(insX, insY);
                inputCorrect |= true;
            } catch (Exception e) {
                e.printStackTrace();
                printWarning("Invalid input");
                input.nextLine();
            }
        }
    }

    private byte[][] readConfiguration(String text) {
        String[] lines = text.split("\n");
        int width = lines[0].length();
        int height = lines.length;

        if(width > m_width || height > m_height) {
            printWarning("The input field is larger than the specified one");
            return null;
        }

        byte[][] arr = new byte[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                char character = lines[i].charAt(j);
                if (character == '1')
                    arr[i][j] = 1;
                else if(character == '0')
                    arr[i][j] = 0;
                else {
                    printWarning("Unknown character " + character);
                    return null;
                }
            }
        }
        return arr;
    }
    private void createEngine(int insX, int insY) {
        Field field = new Field(m_width, m_height);
        field.setEmptyField();
        for(int i = 0; i < beginningConfiguration.length; i++) {
            for(int j = 0; j < beginningConfiguration[0].length; j++) {
                field.setCell(j + insX, i + insY, beginningConfiguration[i][j]);
            }
        }
        m_engine = new GameEngine(field);
    }
}
