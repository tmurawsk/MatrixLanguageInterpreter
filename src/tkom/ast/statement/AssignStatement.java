package tkom.ast.statement;

import tkom.ast.VariableCall;
import tkom.ast.expression.MathExpr;

public class AssignStatement extends Statement {
    private VariableCall variableCall;

    private MathExpr expression;

    public AssignStatement(Statement parent, VariableCall variableCall, MathExpr expression) {
        super(parent);
        this.variableCall = variableCall;
        this.expression = expression;
    }

    @Override
    public void execute() {
        //TODO
    }
}
