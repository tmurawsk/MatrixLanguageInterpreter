package tkom.ast.statement;

import tkom.ast.VariableCall;

public class ReadStatement extends Statement {
    private VariableCall variableCall;

    public ReadStatement(Statement parent, VariableCall variableCall) {
        super(parent);
        this.variableCall = variableCall;
    }

    @Override
    public void execute() {
        //TODO
    }
}
