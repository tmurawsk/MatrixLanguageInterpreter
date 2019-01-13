package tkom.ast.statement;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Program;
import tkom.ast.Variable;
import tkom.ast.expression.MathExpr;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.TypeMismatchException;

public class InitStatement extends Statement {
    private TokenID type;

    private String name;

    private MathExpr leftExpr;

    private MathExpr rightExpr;

    public InitStatement(Position position, TokenID type, String name) {
        super(position);
        this.type = type;
        this.name = name;
    }

    public void setExpressions(MathExpr leftExpr, MathExpr rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public String getName() {
        return name;
    }

    @Override
    public void execute() throws ExecutionException {
        Variable newVariable;
        if (leftExpr != null) {
            Variable left = leftExpr.evaluate();
            if (rightExpr != null) {
                newVariable = new Variable(type, name);
                Variable right = rightExpr.evaluate();
                if (left.getType() != TokenID.Num)
                    throw new TypeMismatchException(leftExpr.getPosition(), TokenID.Num, left.getType());
                if (right.getType() != TokenID.Num)
                    throw new TypeMismatchException(rightExpr.getPosition(), TokenID.Num, right.getType());
                newVariable.initSize(left.getInt(), right.getInt());
            }
            else {
                if (type != left.getType())
                    throw new TypeMismatchException(leftExpr.getPosition(), type, left.getType());
                newVariable = left;
                newVariable.setName(name);
            }
        }
        else {
            newVariable = new Variable(type, name);
        }
        Program.addVariable(newVariable);
    }
}
