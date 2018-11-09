package tkom;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class InputReader {
    private InputStream stream;
    private Position position;
    private char peekedChar;
    private boolean isCharPeeked;

    public InputReader(InputStream stream) {
        this.stream = stream;
        position = new Position();
        isCharPeeked = false;
    }

    public Position getPosition() {
        return position;
    }

    public char read() throws IOException {
        if(isEof())
            throw new EOFException();

        char nextChar;
        if(isCharPeeked) {
            nextChar = peekedChar;
            isCharPeeked = false;
        } else {
            nextChar = (char) stream.read();
        }

        position.charNum++;
        if(nextChar == '\n') {
            position.charNum = 1;
            position.lineNum++;
        }

        return nextChar;
    }

    public char peek() throws IOException {
        if(isEof())
            throw new EOFException();

        if(!isCharPeeked) {
            peekedChar = (char) stream.read();
            isCharPeeked = true;
        }

        return peekedChar;
    }

    public boolean isEof() throws IOException {
        return !isCharPeeked && stream.available() <= 0;
    }
}
