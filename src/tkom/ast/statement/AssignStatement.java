package tkom.ast.statement;

import tkom.ast.Variable;
import tkom.ast.expression.MathExpr;

public class AssignStatement extends Statement {
    private Variable variable;

    private MathExpr expression;

    public AssignStatement(Statement parent, Variable variable, MathExpr expression) {
        super(parent);
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute() {
        //TODO
    }
}
