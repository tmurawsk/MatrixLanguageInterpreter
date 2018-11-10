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
        Position positionBefore = new Position(reader.getPosition());

        try {
            do {
                c = reader.read();
            } while (Character.isWhitespace(c));

            positionBefore = new Position(reader.getPosition());

            if (Character.isLetter(c))
                return nameTokenHandler(c, positionBefore);

            if (Character.isDigit(c))
                return numberTokenHandler(c, positionBefore);

            return otherTokenHandler(c, positionBefore);
        } catch (EOFException e) {
            return new Token(TokenID.Eof, positionBefore);
        } catch (IOException e) {
            return new Token(TokenID.Invalid, positionBefore);
        }
    }

    private Token nameTokenHandler(char c, Position positionBefore) throws IOException {
        token.append(c);

        try {
            while (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_') {
                token.append(reader.read());

                if (token.length() >= Token.MAX_NAME_LENGTH) {
                    lexError("ERROR: NAME TOO LONG!");

                    while (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_')
                        reader.read();

                    return new Token(TokenID.Invalid, positionBefore, token.toString());
                }
            }
        } catch (EOFException ignored) {
        }

        TokenID tokenID = Token.getTokenByKeyword(token.toString());

        if (tokenID != TokenID.Invalid)
            return new Token(tokenID, positionBefore);
        else
            return new Token(TokenID.Name, positionBefore, token.toString());
    }

    private Token numberTokenHandler(char c, Position positionBefore) throws IOException {
        long number = c - '0';

        try {
            while (Character.isDigit(reader.peek())) {
                number = number * 10 + (reader.read() - '0');

                if (number < 0) {
                    lexError("ERROR: NUMBER OUT OF LIMITS");

                    while (Character.isDigit(reader.peek())) {
                        reader.read();
                    }

                    return new Token(TokenID.Invalid, positionBefore);
                }
            }
        } catch (EOFException e) {
        }

        if (Character.isLetter(c)) {
            lexError("ERROR: INVALID TOKEN");

            while (Character.isLetterOrDigit(reader.peek()))
                reader.read();

            return new Token(TokenID.Invalid, positionBefore);
        }

        return new Token(TokenID.Number, positionBefore, String.valueOf(number));
    }

    private Token otherTokenHandler(char c, Position position) throws IOException {

        switch (c) {
            case ';':
                return new Token(TokenID.Semicolon, position);
            case ',':
                return new Token(TokenID.Comma, position);
            case '(':
                return new Token(TokenID.RoundBracketOpen, position);
            case ')':
                return new Token(TokenID.RoundBracketClose, position);
            case '{':
                return new Token(TokenID.CurlyBracketOpen, position);
            case '}':
                return new Token(TokenID.CurlyBracketClose, position);
            case '[':
                return new Token(TokenID.SquareBracketOpen, position);
            case ']':
                return new Token(TokenID.SquareBracketClose, position);
            case '+':
                return new Token(TokenID.Plus, position);
            case '-':
                return new Token(TokenID.Minus, position);
            case '*':
                return new Token(TokenID.Multiply, position);
            case '/':
                return new Token(TokenID.Divide, position);
            case '=':
                return ifNextIsEqual('=', TokenID.Equal, TokenID.Assign, position);
            case '!':
                return ifNextIsEqual('=', TokenID.Unequal, TokenID.Negation, position);
            case '<':
                return ifNextIsEqual('=', TokenID.LessOrEqual, TokenID.Less, position);
            case '>':
                return ifNextIsEqual('=', TokenID.GreaterOrEqual, TokenID.Greater, position);
            case '&':
                return ifNextIsEqual('&', TokenID.And, TokenID.Invalid, position);
            case '|':
                return ifNextIsEqual('|', TokenID.Or, TokenID.Invalid, position);
            case '"':
                try {
                    do {
                        c = reader.read();
                    } while (c != '"');
                } catch (EOFException e) {
                    lexError("ERROR: NO STRING CLOSING MARK FOUND");
                    return new Token(TokenID.Invalid, position);
                }
                return new Token(TokenID.String, position);
            default:
                lexError("ERROR: INVALID TOKEN");
                return new Token(TokenID.Invalid, position);
        }
    }

    private Token ifNextIsEqual(char expectedChar, TokenID tokenIfTrue, TokenID tokenIfFalse, Position position) throws IOException {
        char c = reader.peek();

        if (c == expectedChar) {
            reader.read();
            return new Token(tokenIfTrue, position);
        } else {
            if (tokenIfFalse == TokenID.Invalid)
                lexError("ERROR: INVALID TOKEN");
            return new Token(tokenIfFalse, position);
        }
    }

    private void lexError(String msg) {
        System.err.println("In line: " + reader.getPosition().lineNum + ":\n\t" + msg);
    }
}
