package tkom.ast.statement;

import tkom.Position;
import tkom.ast.VariableCall;

public class ReadStatement extends Statement {
    private VariableCall variableCall;

    public ReadStatement(Statement parent, Position position, VariableCall variableCall) {
        super(parent, position);
        this.variableCall = variableCall;
    }

    @Override
    public void execute() {
        //TODO
    }
}
