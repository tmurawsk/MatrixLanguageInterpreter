package tkom.exception;

import tkom.Position;
import tkom.ast.FunctionDef;

public class MissingReturnStatementException extends BaseException {

    public MissingReturnStatementException(Position position, FunctionDef functionDef) {
        super(position, "Missing return statement in function " + functionDef.name);
    }
}
