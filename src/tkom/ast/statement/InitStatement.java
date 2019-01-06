package tkom.ast.statement;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.expression.MathExpr;

public class InitStatement extends Statement {
    private TokenID type;

    private String name;

    private MathExpr leftExpr;

    private MathExpr rightExpr;

    public InitStatement(Statement parent, Position position, TokenID type, String name) {
        super(parent, position);
        this.type = type;
        this.name = name;
    }

    public void setExpressions(MathExpr leftExpr, MathExpr rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    @Override
    public void execute() {
        //TODO
    }
}
