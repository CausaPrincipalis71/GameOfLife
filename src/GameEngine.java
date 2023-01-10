import java.util.ArrayList;
import java.util.List;

public class GameEngine
{
    private Field m_pastField;
    private Field m_currentField;
    private int m_height;
    private int m_width;
    private List<Integer> hashCodes = new ArrayList<Integer>();
    public GameEngine(int height, int width) {
        super();
        m_currentField = new Field(height, width);
        m_height = height;
        m_width = width;
        m_currentField.setEmptyField();
    }
    public GameEngine(int height, int width, byte[] field) {
        super();
        m_currentField = new Field(height, width);
        m_height = height;
        m_width = width;
        m_currentField.setField(field);
    }

    public GameEngine(Field field)
    {
        super();
        m_currentField = field.clone();
        m_height = m_currentField.height();
        m_width = m_currentField.width();
    }

    public byte[][] getField() {
        byte[][] field = new byte[m_height][m_width];
        for (int i = 0; i < m_width; i ++) {
            for (int j = 0; j < m_height; j++) {
                field[j][i] = m_currentField.cellAt(i, j);
            }
        }
        return field;
    }

    public byte cellAt(int x, int y) {
        return m_currentField.cellAt(x, y);
    }

    public boolean step() {
        Field nextField = new Field(m_width, m_height);
        for (int i = 0; i < m_width; i++) {
            for (int j = 0; j < m_height; j++) {
                byte currentCell = m_currentField.cellAt(i, j);
                byte neighbors = m_currentField.calculateNeighbors(i, j);

                if (currentCell == 1 && (neighbors > 3 || neighbors < 2))
                    currentCell = 0;
                else if (currentCell == 0 && neighbors == 3)
                    currentCell = 1;

                nextField.setCell(i, j, currentCell);
                System.out.print("");
            }
        }
        m_pastField = m_currentField;
        m_currentField = nextField;

        Integer hash = m_currentField.calculateHash();

        if (hashCodes.contains(hash))
            return false;
        else
            hashCodes.add(hash);

        return true;
    }
}
