package tkom;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class Lexer {
    private InputReader reader;
    private StringBuilder tokenValue;
    private LinkedList<Token> tokens;

    public Lexer(InputStream stream) {
        reader = new InputReader(stream);
        tokenValue = new StringBuilder();
        tokens = new LinkedList<>();
    }

    public Token peekToken() {
        Token token = getNextToken();
        tokens.add(token);
        return token;
    }

    public Token readToken() {
        if (!tokens.isEmpty())
            return tokens.removeFirst();
        return getNextToken();
    }

    private Token getNextToken() {
        char c;
        tokenValue = new StringBuilder();
        Position positionBefore = new Position(reader.getPosition());

        try {
            while (Character.isWhitespace(reader.peek())) {
                reader.read();
            }
            positionBefore = new Position(reader.getPosition());
            c = reader.read();
            tokenValue.append(c);

            if (Character.isLetter(c))
                return nameTokenHandler(c, positionBefore);

            if (Character.isDigit(c) && c != '0')
                return numberTokenHandler(c, positionBefore);

            if (c == '"')
                return stringTokenHandler(positionBefore);

            return otherTokenHandler(c, positionBefore);
        } catch (EOFException e) {
            return new Token(TokenID.Eof, positionBefore);
        } catch (IOException e) {
            return new Token(TokenID.Invalid, positionBefore);
        }
    }

    private Token nameTokenHandler(char c, Position positionBefore) throws IOException {

        try {
            while (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_') {
                tokenValue.append(reader.read());

                if (tokenValue.length() >= Token.MAX_NAME_LENGTH) {
                    while (Character.isLetterOrDigit(reader.peek()) || reader.peek() == '_')
                        reader.read();

                    return new Token(TokenID.Invalid, positionBefore, "ERROR: NAME TOO LONG!");
                }
            }
        } catch (EOFException ignored) {
        }

        TokenID tokenID = Token.getTokenByKeyword(tokenValue.toString());

        if (tokenID != TokenID.Invalid)
            return new Token(tokenID, positionBefore, tokenValue.toString());
        else
            return new Token(TokenID.Name, positionBefore, tokenValue.toString());
    }

    private Token numberTokenHandler(char c, Position positionBefore) throws IOException {
        long number = c - '0';

        try {
            while (Character.isDigit(reader.peek())) {
                c = reader.read();
                tokenValue.append(c);
                number = number * 10 + (c - '0');

                if (number < 0) {
                    while (Character.isLetterOrDigit(reader.peek())) {
                        reader.read();
                    }
                    return new Token(TokenID.Invalid, positionBefore, "ERROR: NUMBER OUT OF LIMITS!");
                }
            }

            if (Character.isLetter(reader.peek())) {
                do {
                    tokenValue.append(reader.read());
                } while (Character.isLetterOrDigit(reader.peek()));

                return new Token(TokenID.Invalid, positionBefore, tokenValue.toString());
            }
        } catch (EOFException ignored) {
        }

        return new Token(TokenID.Number, positionBefore, String.valueOf(number));
    }

    private Token otherTokenHandler(char c, Position position) throws IOException {

        switch (c) {
            case ';':
                return new Token(TokenID.Semicolon, position, c);
            case ',':
                return new Token(TokenID.Comma, position, c);
            case '(':
                return new Token(TokenID.RoundBracketOpen, position, c);
            case ')':
                return new Token(TokenID.RoundBracketClose, position, c);
            case '{':
                return new Token(TokenID.CurlyBracketOpen, position, c);
            case '}':
                return new Token(TokenID.CurlyBracketClose, position, c);
            case '[':
                return new Token(TokenID.SquareBracketOpen, position, c);
            case ']':
                return new Token(TokenID.SquareBracketClose, position, c);
            case '+':
                return new Token(TokenID.Plus, position, c);
            case '-':
                return new Token(TokenID.Minus, position, c);
            case '*':
                return new Token(TokenID.Multiply, position, c);
            case '/':
                return new Token(TokenID.Divide, position, c);
            case '=':
                return ifNextIsEqual('=', TokenID.Equal, TokenID.Assign, position, c);
            case '!':
                return ifNextIsEqual('=', TokenID.Unequal, TokenID.Negation, position, c);
            case '<':
                return ifNextIsEqual('=', TokenID.LessOrEqual, TokenID.Less, position, c);
            case '>':
                return ifNextIsEqual('=', TokenID.GreaterOrEqual, TokenID.Greater, position, c);
            case '&':
                return ifNextIsEqual('&', TokenID.And, TokenID.Invalid, position, c);
            case '|':
                return ifNextIsEqual('|', TokenID.Or, TokenID.Invalid, position, c);
            default:
                return new Token(TokenID.Invalid, position, c);
        }
    }

    private Token stringTokenHandler(Position positionBefore) throws IOException {
        tokenValue = new StringBuilder();
        char c;

        try {
            c = reader.read();
            while (c != '"') {
                tokenValue.append(c);
                c = reader.read();
            }
        } catch (EOFException e) {
            return new Token(TokenID.Invalid, positionBefore, "ERROR: NO STRING CLOSING MARK FOUND!");
        }
        return new Token(TokenID.String, positionBefore, tokenValue.toString());
    }

    private Token ifNextIsEqual(char expectedChar, TokenID tokenIfTrue, TokenID tokenIfFalse, Position position, char givenChar) throws IOException {
        char c = reader.peek();

        if (c == expectedChar) {
            reader.read();
            return new Token(tokenIfTrue, position, tokenValue.append(c).toString());
        } else {
            return new Token(tokenIfFalse, position, tokenValue.toString());
        }
    }

}
