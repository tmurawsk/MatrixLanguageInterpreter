package tkom.ast.statement;

import tkom.Position;
import tkom.ast.Program;
import tkom.ast.Variable;
import tkom.ast.expression.MathExpr;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.ReturnValue;
import tkom.exception.ExecutionException.TypeMismatchException;

public class ReturnStatement extends Statement {
    private MathExpr expression;

    public ReturnStatement(Position position, MathExpr expression) {
        super(position);
        this.expression = expression;
    }

    @Override
    public void execute() throws ExecutionException {
        Variable toReturn = expression.evaluate();
        if (toReturn.getType() != Program.getLastFunctionCall().returnType)
            throw new TypeMismatchException(position, this, toReturn.getType());
        Program.getLastFunctionCall().setResult(toReturn);
        throw new ReturnValue();
    }
}
