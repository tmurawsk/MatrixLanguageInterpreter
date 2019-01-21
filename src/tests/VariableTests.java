package tests;

import org.junit.jupiter.api.Test;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.MathException;
import tkom.exception.ExecutionException.TypeMismatchException;

import java.util.ArrayList;
import java.util.Arrays;

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

    @Test
    void numVariableOperations() {
        Variable n1 = new Variable(10);
        Variable n2 = new Variable(-38);
        Variable zero = new Variable(0);

        assertDoesNotThrow(() -> assertEquals(-28, n1.add(n2).getInt()));
        assertDoesNotThrow(() -> assertEquals(-28, n2.add(n1).getInt()));

        assertDoesNotThrow(() -> assertEquals(48, n1.subtract(n2).getInt()));
        assertDoesNotThrow(() -> assertEquals(-48, n2.subtract(n1).getInt()));

        assertDoesNotThrow(() -> assertEquals(-380, n1.multiply(n2).getInt()));
        assertDoesNotThrow(() -> assertEquals(-380, n2.multiply(n1).getInt()));

        assertDoesNotThrow(() -> assertEquals(0, n1.divide(n2).getInt()));
        assertDoesNotThrow(() -> assertEquals(-3, n2.divide(n1).getInt()));

        assertDoesNotThrow(() -> assertEquals(0, zero.divide(n2).getInt()));
        assertThrows(MathException.class, () -> n2.divide(zero).getInt());
    }

    @Test
    void matrixVariableOperations() {
        Variable m1 = new Variable(prepareMatrix2x4(1), true);
        Variable m2 = new Variable(prepareMatrix4x3(), true);

        assertDoesNotThrow(() -> assertTrue(m1.add(m1).equals(new Variable(prepareMatrix2x4(2), true))));
        assertDoesNotThrow(() -> assertTrue(m1.subtract(m1).equals(new Variable(prepareMatrix2x4(0), true))));

        assertThrows(MathException.class, () -> m1.multiply(m1));
        assertThrows(TypeMismatchException.class, () -> m1.divide(m1));

        assertDoesNotThrow(() -> assertTrue(m1.multiply(m2).equals(new Variable(prepareMatrix2x3(), true))));
    }

    @Test
    void matrixAndNumVariableOperations() {
        Variable m = new Variable(prepareMatrix2x4(1), true);
        Variable n = new Variable(3);

        assertDoesNotThrow(() -> assertTrue(m.multiply(n).equals(new Variable(prepareMatrix2x4(3), true))));
        assertDoesNotThrow(() -> assertTrue(n.multiply(m).equals(new Variable(prepareMatrix2x4(3), true))));

        assertDoesNotThrow(() -> assertTrue(m.divide(n).equals(new Variable(prepareMatrix2x4Div(3), true))));
        assertThrows(TypeMismatchException.class, () -> n.divide(m));
    }

    private ArrayList<ArrayList<Integer>> prepareMatrix2x4(int n) {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(Arrays.asList(3*n, (-5)*n, 2*n, 4*n)));
        matrix.add(new ArrayList<>(Arrays.asList(1*n, 1*n, (-1)*n, 17*n)));
        return matrix;
    }

    private ArrayList<ArrayList<Integer>> prepareMatrix2x4Div(int n) {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(Arrays.asList(3/n, (-5)/n, 2/n, 4/n)));
        matrix.add(new ArrayList<>(Arrays.asList(1/n, 1/n, (-1)/n, 17/n)));
        return matrix;
    }

    private ArrayList<ArrayList<Integer>> prepareMatrix4x3() {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(Arrays.asList(-8, 0, 4)));
        matrix.add(new ArrayList<>(Arrays.asList(0, -1, -13)));
        matrix.add(new ArrayList<>(Arrays.asList(-9, 9, 3)));
        matrix.add(new ArrayList<>(Arrays.asList(13, 2, 6)));
        return matrix;
    }

    private ArrayList<ArrayList<Integer>> prepareMatrix2x3() {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(Arrays.asList(10, 31, 107)));
        matrix.add(new ArrayList<>(Arrays.asList(222, 24, 90)));
        return matrix;
    }
}
