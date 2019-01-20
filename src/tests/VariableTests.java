package tests;

import org.junit.jupiter.api.Test;
import tkom.TokenID;
import tkom.ast.Variable;

import static org.junit.jupiter.api.Assertions.*;

class VariableTests {
    @Test
    void initializeMatrixVariable() {
        Variable v = new Variable(TokenID.Mat, "v");
        v.initSize(3, 4);

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 4; j++)
                    assertEquals(0, v.getThrows(i, j));
        });
    }
}
