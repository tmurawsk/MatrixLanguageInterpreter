package tkom;

public class Position {
    public int lineNum;
    public int charNum;

    public Position() {
        this(1, 0);
    }

    public Position(int lineNum, int charNum) {
        this.lineNum = lineNum;
        this.charNum = charNum;
    }

    public Position(Position position) {
        this(position.lineNum, position.charNum);
    }

    @Override
    public String toString() {
        return "(line: " + lineNum + ", char: " + charNum + ")";
    }
}
