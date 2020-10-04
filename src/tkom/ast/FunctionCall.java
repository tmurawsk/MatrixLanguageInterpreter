package tkom.ast;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.expression.MathExpr;
import tkom.ast.statement.Statement;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.NotDefinedException;

import java.util.LinkedList;

public class FunctionCall extends Statement {
    public String name;

    private FunctionDef functionDef;

    private LinkedList<MathExpr> parameters;

    public FunctionCall(Position position, String name) {
        super(position);
        this.name = name;
        parameters = new LinkedList<>();
    }

    public LinkedList<MathExpr> getParameters() {
        return parameters;
    }

    public void addArgument(MathExpr expr) {
        parameters.add(expr);
    }

    public TokenID getType() {
        if (functionDef == null)
            return TokenID.Any;
        return functionDef.returnType;
    }

    public Variable evaluate() throws ExecutionException {
        findFunctionDef();
        return functionDef.evaluate(evaluateParameters());
    }

    @Override
    public void execute() throws ExecutionException {
        findFunctionDef();
        functionDef.execute(evaluateParameters());
    }

    private LinkedList<Variable> evaluateParameters() throws ExecutionException {
        LinkedList<Variable> parametersValues = new LinkedList<>();
        for (MathExpr expr : parameters)
            parametersValues.add(expr.evaluate());
        return parametersValues;
    }

    private void findFunctionDef() throws NotDefinedException {
        FunctionDef functionDef = Program.getFunctionDef(this, true);
        if (functionDef == null)
            throw new NotDefinedException(position, this);
        this.functionDef = functionDef;
    }
}
