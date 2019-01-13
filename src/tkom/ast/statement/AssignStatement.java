package tkom.ast.statement;

import tkom.Position;
import tkom.ast.VariableCall;
import tkom.ast.expression.MathExpr;

public class AssignStatement extends Statement {
    private VariableCall variableCall;

    private MathExpr expression;

    public AssignStatement(Position position, VariableCall variableCall, MathExpr expression) {
        super(position);
        this.variableCall = variableCall;
        this.expression = expression;
    }

    @Override
    public void execute() {
        //TODO
    }
}
