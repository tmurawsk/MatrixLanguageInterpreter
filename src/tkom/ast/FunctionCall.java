package tkom.ast;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.expression.MathExpr;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class FunctionCall extends Statement {
    public String name;

    private FunctionDef functionDef;

    private LinkedList<MathExpr> parameters;

    public FunctionCall(Statement parent, Position position, String name) {
        super(parent, position);
        this.name = name;
        parameters = new LinkedList<>();
    }

    LinkedList<MathExpr> getParameters() {
        return parameters;
    }

    public void addArgument(MathExpr expr) {
        parameters.add(expr);
    }

    public void setFunctionDef(FunctionDef functionDef) {
        this.functionDef = functionDef;
    }

    public TokenID getType() {
        if (functionDef == null)
            return TokenID.Invalid;
        return functionDef.returnType;
    }

    public Variable evaluate() {
        return null; //TODO
    }

    @Override
    public void execute() {
        //TODO
    }
}
