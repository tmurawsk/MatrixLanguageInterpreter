package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tkom.Position;
import tkom.Token;
import tkom.TokenID;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {
    Token token;
    TokenID defaultId = TokenID.Name;
    String defaultValue = "newName";
    Position defaultPos = new Position(7, 3);

    @BeforeEach
    void setUp() {
        token = new Token(defaultId, defaultPos, defaultValue);
    }

    @Test
    void getId() {
        assertEquals(defaultId, token.getId());
    }

    @Test
    void getValue() {
        assertEquals(defaultValue, token.getValue());
    }

    @Test
    void getPosition() {
        assertEquals(defaultPos, token.getPosition());
    }
}