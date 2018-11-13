package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tkom.InputReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class InputReaderTest {

    private InputReader reader;
    private InputStream stream;
    private final String inputString = "ab \t\n;c1\n2 \n\ne 3f";
    private final byte[] inputBytes = inputString.getBytes();

    @BeforeEach
    void setUp() {
        stream = new ByteArrayInputStream(inputBytes);
        reader = new InputReader(stream);
    }

    @AfterEach
    void tearDown() {
        try {
            stream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testSetup() {
        assertEquals(reader.getPosition().charNum, 1);
        assertEquals(reader.getPosition().lineNum, 1);
        assertDoesNotThrow(() -> assertFalse(reader.isEof()));
    }

    @Test
    void testPeekingInput() {
        assertDoesNotThrow(() -> {
            char c;
            int charBefore, lineBefore;

            charBefore = reader.getPosition().charNum;
            lineBefore = reader.getPosition().lineNum;
            c = reader.peek();
            assertEquals('a', c);
            assertEquals(charBefore, reader.getPosition().charNum);
            assertEquals(lineBefore, reader.getPosition().lineNum);

            c = reader.peek();
            assertEquals('a', c);
            assertEquals(charBefore, reader.getPosition().charNum);
            assertEquals(lineBefore, reader.getPosition().lineNum);
        });
    }

    @Test
    void testReadingInput() {
        assertDoesNotThrow(() -> {
            char c;

            c = reader.read();
            assertEquals('a', c);
            assertEquals(1, reader.getPosition().lineNum);
            assertEquals(2, reader.getPosition().charNum);

            c = reader.read();
            assertEquals('b', c);
            assertEquals(1, reader.getPosition().lineNum);
            assertEquals(3, reader.getPosition().charNum);

            c = reader.read();
            assertEquals(' ', c);

            c = reader.read();
            assertEquals('\t', c);
            assertEquals(1, reader.getPosition().lineNum);
            assertEquals(5, reader.getPosition().charNum);

            c = reader.read();
            assertEquals('\n', c);
            assertEquals(2, reader.getPosition().lineNum);
            assertEquals(1, reader.getPosition().charNum);

            c = reader.read();
            assertEquals(';', c);
            assertEquals(2, reader.getPosition().lineNum);
            assertEquals(2, reader.getPosition().charNum);
        });
    }

    @Test
    void testReadingAndPeeking() {
        assertDoesNotThrow(() -> {
            char c;
            int lineBefore, charBefore;

            charBefore = reader.getPosition().charNum;
            lineBefore = reader.getPosition().lineNum;
            c = reader.peek();
            assertEquals('a', c);
            assertEquals(lineBefore, reader.getPosition().lineNum);
            assertEquals(charBefore, reader.getPosition().charNum);

            charBefore = reader.getPosition().charNum;
            lineBefore = reader.getPosition().lineNum;
            c = reader.read();
            assertEquals('a', c);
            assertEquals(lineBefore, reader.getPosition().lineNum);
            assertNotEquals(charBefore, reader.getPosition().charNum);

            charBefore = reader.getPosition().charNum;
            lineBefore = reader.getPosition().lineNum;
            c = reader.read();
            assertEquals('b', c);
            assertEquals(lineBefore, reader.getPosition().lineNum);
            assertNotEquals(charBefore, reader.getPosition().charNum);

            charBefore = reader.getPosition().charNum;
            lineBefore = reader.getPosition().lineNum;
            c = reader.peek();
            assertEquals(' ', c);
            assertEquals(lineBefore, reader.getPosition().lineNum);
            assertEquals(charBefore, reader.getPosition().charNum);
        });
    }

    @Test
    void testEndOfFile() {
        reader = new InputReader(new ByteArrayInputStream("a".getBytes()));

        assertDoesNotThrow(() -> {
            assertFalse(reader.isEof());
            reader.read();
            assertTrue(reader.isEof());
        });

        assertThrows(IOException.class, () -> reader.peek());
        assertThrows(IOException.class, () -> reader.read());
    }
}