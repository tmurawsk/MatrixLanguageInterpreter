package tkom.exception.ParseException;

import tkom.Position;
import tkom.ast.FunctionDef;

public class MissingReturnStatementException extends ParseException {

    public MissingReturnStatementException(Position position, FunctionDef functionDef) {
        super(position, "Missing return statement in function " + functionDef.name);
    }
}
