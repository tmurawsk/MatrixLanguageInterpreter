package tkom;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class Lexer {
    private InputReader reader;
    private StringBuilder token;

    public Lexer(InputStream stream) {
        reader = new InputReader(stream);
        token = new StringBuilder();
    }

    public Token nextToken() {
        char c;
        token = new StringBuilder();

        try {
            do {
                c = reader.read();
            } while (Character.isWhitespace(c));

            if(Character.isLetter(c)) {
                return nameTokenHandler(c);
            }

            if(Character.isDigit(c)) {
                return numberTokenHandler(c);
            }

            return otherTokenHandler(c);
        } catch (EOFException e) {
            return new Token(TokenID.Eof, reader.getPosition());
        } catch (IOException e) {
            return new Token(TokenID.Invalid, reader.getPosition()); //TODO: maybe add e.getMessage() as value?
        }
    }

    private Token nameTokenHandler(char c) throws IOException {
        Position positionBefore = reader.getPosition();

        do {
            token.append(c);
            if(token.length() >= Token.MAX_NAME_LENGTH) {
                lexError("ERROR: NAME TOO LONG!");

                while(Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_')
                    reader.read();

                return new Token(TokenID.Invalid, positionBefore, token.toString());
            }
            c = reader.read();
        } while (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_');



        return new Token(); //TODO
    }

    private Token numberTokenHandler(char c) {
        return new Token(); //TODO
    }

    private Token otherTokenHandler(char c) {
        return new Token(); //TODO
    }

    private void lexError(String msg) {
        System.err.println("In line: " + reader.getPosition().lineNum + ":\n\t" + msg);
    }
}
