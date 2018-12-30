package tkom.ast.statement;

import tkom.TokenID;
import tkom.ast.expression.MathExpr;

public class InitStatement extends Statement {
    private TokenID type;

    private String name;

    private MathExpr leftExpr;

    private MathExpr rightExpr;

    public InitStatement(Statement parent, TokenID type, String name, MathExpr leftExpr, MathExpr rightExpr) {
        super(parent);
        this.type = type;
        this.name = name;
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public InitStatement(Statement parent, TokenID type, String name, MathExpr leftExpr) {
        this(parent, type, name, leftExpr, null);
    }

    @Override
    public void execute() {
        //TODO
    }
}
