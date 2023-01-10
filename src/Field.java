public class Field implements Cloneable {
    protected int m_width;
    protected int m_height;
    protected int m_square;

    protected int m_byteWidth;

    protected byte[] m_field;
    public Field(int width, int height) {
        super();
        m_height = height;
        m_width = width;

        m_byteWidth = (int) Math.ceil((double) width/8);
        m_square = m_byteWidth * m_height;

        this.setEmptyField();
    }

    public void setEmptyField() {
        m_field = new byte[m_square];
    }

    public boolean setField(byte[] field) {
        if(field.length != m_square)
            return false;
        m_field = field.clone();
        return true;
    }

    public void setCell(int x, int y, byte cell) {
        for(; x >= m_width; x -= m_width) {}
        for(; y >= m_height; y -= m_height) {}
        for(; x < 0; x += m_width) {}
        for(; y < 0; y += m_height) {}
        int xByte = x / 8;
        m_field[y * m_byteWidth + xByte] |= (cell << (x % 8));
    }

    public byte cellAt(int x, int y) {
        for(; x >= m_width; x -= m_width) {}
        for(; y >= m_height; y -= m_height) {}
        for(; x < 0; x += m_width) {}
        for(; y < 0; y += m_height) {}
        int xByte = x / 8;
        return (byte) ((m_field[y * m_byteWidth + xByte] & (1 << (x % 8))) >> (x % 8));
    }

    public byte calculateNeighbors(int x, int y) {
        return (byte) (this.cellAt(x- 1, y - 1) + this.cellAt(x, y - 1) + this.cellAt(x+ 1, y - 1) +
                + this.cellAt(x- 1, y) + this.cellAt(x+ 1, y) +
                + this.cellAt(x- 1, y + 1) + this.cellAt(x, y + 1) + this.cellAt(x+ 1, y + 1));
    }

    public int calculateHash() {
        String hashString = new String();
        for(int i = 0; i < m_height; i++) {
            for(int j = 0; j < m_byteWidth; j++) {
                hashString += ((char) (m_field[i * m_byteWidth + j]));
            }
        }
        return hashString.hashCode();
    }

    public byte[][] getField() {
        byte[][] field = new byte[m_height][m_width];
        for (int i = 0; i < m_width; i ++) {
            for (int j = 0; j < m_height; j++) {
                field[j][i] = cellAt(i, j);
            }
        }
        return field;
    }

    public int width() {
        return m_width;
    }

    public int height() {
        return m_height;
    }

    public byte[] field() {
        return m_field.clone();
    }

    public Field clone() {
        Field field = new Field(m_width, m_height);
        field.setField(m_field);
        return field;
    }
}
