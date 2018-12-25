package tkom.ast.statement;

import tkom.TokenID;
import tkom.ast.expression.MathExpr;

public class InitStatement implements Statement {
    private TokenID type;

    private String name;

    private MathExpr leftExpr;

    private MathExpr rightExpr;

    public InitStatement(TokenID type) {
        this.type = type;
    }

    @Override
    public void execute() {
        //TODO
    }
}
