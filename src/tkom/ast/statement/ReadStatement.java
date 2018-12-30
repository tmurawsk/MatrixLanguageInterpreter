package tkom.ast.statement;

import tkom.ast.Variable;

public class ReadStatement extends Statement {
    private Variable variable;

    public ReadStatement(Statement parent, Variable variable) {
        super(parent);
        this.variable = variable;
    }

    @Override
    public void execute() {
        //TODO
    }
}
