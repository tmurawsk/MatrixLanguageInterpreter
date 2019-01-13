package tkom.ast.statement;

import tkom.Position;
import tkom.ast.VariableCall;

public class ReadStatement extends Statement {
    private VariableCall variableCall;

    public ReadStatement(Position position, VariableCall variableCall) {
        super(position);
        this.variableCall = variableCall;
    }

    @Override
    public void execute() {
        //TODO
    }
}
