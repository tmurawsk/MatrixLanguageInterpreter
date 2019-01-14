package tkom.exception.ParseException;

import javafx.util.Pair;
import tkom.Position;
import tkom.Token;
import tkom.TokenID;
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

    private static String getArgumentTypesString(LinkedList<Pair<TokenID, String>> arguments) {
        LinkedList<String> types = new LinkedList<>();
        for (Pair<TokenID, String> v : arguments)
            types.add(Token.getNameByToken(v.getKey()));
        return String.join(", ", types);
    }
}
