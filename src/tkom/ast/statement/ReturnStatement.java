package tkom.ast.statement;

import tkom.Position;
import tkom.ast.expression.MathExpr;

public class ReturnStatement extends Statement {
    private MathExpr expression;

    public ReturnStatement(Statement parent, Position position, MathExpr expression) {
        super(parent, position);
        this.expression = expression;
    }

    @Override
    public void execute() {
        //TODO
    }
}
