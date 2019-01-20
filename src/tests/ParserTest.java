package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tkom.Lexer;
import tkom.Parser;
import tkom.ast.FunctionDef;
import tkom.ast.Program;

import java.io.ByteArrayInputStream;
import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private Parser parser;
    private StringBuilder code;

    @BeforeEach
    void resetProgram() {
        code = new StringBuilder();
        FunctionDef f;
        try {
            do {
                f = Program.popFunctionCall();
            } while (f != null);
        }
        catch (EmptyStackException ignored) {}
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
    void programWithMainOnly() {
        addMain("");
        initialize();

        assertDoesNotThrow(() -> parser.parseProgram());
    }
}
