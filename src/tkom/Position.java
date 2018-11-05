package tkom;

public class Position {
    public int lineNum;
    public int charNum;

    public Position() {
        this(1, 1);
    }

    public Position(int lineNum, int charNum) {
        this.lineNum = lineNum;
        this.charNum = charNum;
    }
}
