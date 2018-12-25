package tkom.ast.statement;

import tkom.ast.Variable;

public class ReadStatement implements Statement {
    private Variable variable;

    public ReadStatement(Variable variable) {
        this.variable = variable;
    }

    @Override
    public void execute() {
        //TODO
    }
}
