package tkom.ast.statement;

import tkom.ast.Variable;
import tkom.ast.expression.MathExpr;

public class AssignStatement implements Statement {
    private Variable variable;

    private MathExpr expression;

    public AssignStatement(Variable variable) {
        this.variable = variable;
    }

    @Override
    public void execute() {
        //TODO
    }
}
