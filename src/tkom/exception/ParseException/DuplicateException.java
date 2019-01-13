package tkom.exception.ParseException;

import tkom.Position;
import tkom.Token;
import tkom.ast.FunctionDef;
import tkom.ast.Variable;

import java.util.LinkedList;

public class DuplicateException extends ParseException {
    private DuplicateException(Position position, String subjectType, String subjectName) {
        super(position, subjectType + " already defined: " + subjectName);
    }

    public DuplicateException(Position position, Variable variable) {
        this(position, "Variable", variable.name);
    }

    public DuplicateException(Position position, FunctionDef functionDef) {
        this(position, "Function", functionDef.name + "(" + getArgumentTypesString(functionDef.getArguments()) + ")");
    }

    private static String getArgumentTypesString(LinkedList<Variable> arguments) {
        LinkedList<String> types = new LinkedList<>();
        for (Variable v : arguments)
            types.add(Token.getNameByToken(v.getType()));
        return String.join(", ", types);
    }
}
