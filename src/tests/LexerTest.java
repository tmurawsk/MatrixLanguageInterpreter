package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tkom.Lexer;
import tkom.Token;
import tkom.TokenID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    private Lexer lexer;
    private Token token;

    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream myErr = new ByteArrayOutputStream();


    void initializeLexer(String s) {
        lexer = new Lexer(new ByteArrayInputStream(s.getBytes()));
    }

    @BeforeEach
    void setUpErrOutput() {
        System.setErr(new PrintStream(myErr));
    }

    @AfterEach
    void restoreErrOutput() {
        System.setErr(originalErr);
    }

    @Test
    void readEmptyString() {
        initializeLexer("");
        token = lexer.nextToken();

        assertEquals(TokenID.Eof, token.getId());
    }

    @Test
    void readOneTokenLetters() {
        initializeLexer("func");
        token = lexer.nextToken();

        assertEquals(TokenID.Func, token.getId());
        assertEquals(1, token.getPosition().lineNum);
        assertEquals(1, token.getPosition().charNum);
        assertEquals("func", token.getValue());
    }

    @Test
    void readOneTokenDigits() {
        initializeLexer("87654");
        token = lexer.nextToken();

        assertEquals(TokenID.Number, token.getId());
        assertEquals(1, token.getPosition().lineNum);
        assertEquals(1, token.getPosition().charNum);
        assertEquals("87654", token.getValue());
    }

    @Test
    void readTokensWithWhitespaces() {
        initializeLexer("    \t\nabd3  \t main\n \t 123;,   ");

        token = lexer.nextToken();
        assertEquals(TokenID.Name, token.getId());
        assertEquals(2, token.getPosition().lineNum);
        assertEquals(1, token.getPosition().charNum);
        assertEquals("abd3", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenID.Main, token.getId());
        assertEquals(2, token.getPosition().lineNum);
        assertEquals(9, token.getPosition().charNum);
        assertEquals("main", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenID.Number, token.getId());
        assertEquals(3, token.getPosition().lineNum);
        assertEquals(4, token.getPosition().charNum);
        assertEquals("123", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenID.Semicolon, token.getId());
        assertEquals(3, token.getPosition().lineNum);
        assertEquals(7, token.getPosition().charNum);
        assertEquals(";", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenID.Comma, token.getId());
        assertEquals(3, token.getPosition().lineNum);
        assertEquals(8, token.getPosition().charNum);
        assertEquals(",", token.getValue());
    }

    @Test
    void readInvalidTokens() {
        initializeLexer("123c45 " +
                "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij " +
                "commonName " +
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890 " +
                "965");

        assertEquals(TokenID.Invalid, lexer.nextToken().getId());
        assertEquals(TokenID.Invalid, lexer.nextToken().getId());
        assertEquals(TokenID.Name, lexer.nextToken().getId());
        assertEquals(TokenID.Invalid, lexer.nextToken().getId());
        assertEquals(TokenID.Number, lexer.nextToken().getId());
    }
}