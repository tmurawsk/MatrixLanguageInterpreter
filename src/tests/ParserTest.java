package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tkom.Lexer;
import tkom.Parser;
import tkom.Position;
import tkom.TokenID;
import tkom.ast.FunctionCall;
import tkom.ast.FunctionDef;
import tkom.ast.Program;
import tkom.ast.expression.MathExpr;
import tkom.exception.ParseException.DuplicateException;
import tkom.exception.ParseException.NotDefinedException;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private Parser parser;
    private StringBuilder code;

    @BeforeEach
    void resetProgram() {
        code = new StringBuilder();
    }

    void initialize() {
        byte[] programCode = code.toString().getBytes();
        Lexer lexer = new Lexer(new ByteArrayInputStream(programCode));
        parser = new Parser(lexer);
    }

    void addMain(String s) {
        code.append("func main() num { ")
                .append(s)
                .append("return 0; }");
    }

    void addCode(String s) {
        code.append(s);
    }

    @Test
    void parseMainOnly() {
        addMain("");
        initialize();

        assertDoesNotThrow(() -> parser.parseProgram());
    }

    @Test
    void parseFunctionDef() {
        addMain("");
        addCode("func foo (num a, mat m) mat { return 2*a*m/3; }");
        initialize();

        assertDoesNotThrow(() -> parser.parseProgram());

        FunctionCall fooCall = new FunctionCall(new Position(), "foo");
        fooCall.addArgument(new MathExpr(new Position(), null));
        fooCall.addArgument(new MathExpr(new Position(), null));

        FunctionDef foo = Program.getFunctionDef(fooCall, false);
        assertNotEquals(null, foo);
        assertEquals("foo", foo.name);
        assertEquals(2, foo.getArguments().size());
        assertEquals(TokenID.Mat, foo.returnType);

        FunctionDef main = Program.getFunctionDef(new FunctionCall(new Position(), "main"), false);
        assertNotEquals(null, main);
        assertEquals("main", main.name);
        assertEquals(0, main.getArguments().size());
        assertEquals(TokenID.Num, main.returnType);
    }

    @Test
    void parseDuplicateFunctionDef() {
        addMain("");
        addCode("func foo (num a, mat m) num { return 2*a; }\n");
        addCode("func foo (num b, mat n) mat { return b*n; }");
        initialize();

        assertThrows(DuplicateException.class, () -> parser.parseProgram());
    }

    @Test
    void parseDuplicateVariable() {
        addMain("num a = 5; mat a = {1}{3};");
        initialize();

        assertThrows(DuplicateException.class, () -> parser.parseProgram());
    }

    @Test
    void parseDuplicateArgument() {
        addMain("");
        addCode("func foo (num a, mat a) num { return 2*a; }");
        initialize();

        assertThrows(DuplicateException.class, () -> parser.parseProgram());
    }

    @Test
    void parseVariableInitializations() {
        addMain("num a; \n" +
                "num b = 1; \n" +
                "num c = a * (b - a + 1) / 3;\n" +
                "mat m = {33}{12}; \n" +
                "mat n = [-9, c/a * b + 5; m[2][3] - c, 8];");
        initialize();

        assertDoesNotThrow(() -> parser.parseProgram());
    }

    @Test
    void parseVariableAssigns() {
        addMain("num a; a = -13; a = 33;\n" +
                "mat m = {3}{5}; m = [1, 2, 3; 4, 5, 6];\n" +
                "m[1][1] = foo(1); m[1][2] = 32; m[1][3] = 666;\n" +
                "m[2][1] = 99; m[2][2] = 9090; m[2][3] = foo(-9);");

        addCode("func foo(num a) num { return -a*3; }");
        initialize();

        assertDoesNotThrow(() -> parser.parseProgram());
    }

    @Test
    void parseUnknownFunctionCall() {
        addMain("foo (10);");
        addCode("func foo2 (num a) num { print(a); return a; }");
        initialize();

        assertThrows(NotDefinedException.class, () -> parser.parseProgram());
    }

    @Test
    void parseIfStatementWithLogicExpression() {
        addMain("num a = 3; num b = -10; num c; read(c);\n" +
                "if (a > c - b || (b > a * c + 10 && a < c)) { }");
        initialize();

        assertDoesNotThrow(() -> parser.parseProgram());
    }
}
