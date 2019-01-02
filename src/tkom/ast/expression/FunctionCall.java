package tkom.ast.expression;

import tkom.ast.FunctionDef;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class FunctionCall extends MathExpression {
    public String name;

    private FunctionDef functionDef;

    private LinkedList<MathExpr> arguments;

    public FunctionCall(Statement parent, String name) {
        super(parent);
        this.name = name;
        arguments = new LinkedList<>();
    }

    public LinkedList<MathExpr> getArguments() {
        return arguments;
    }

    public void addArgument(MathExpr expr) {
        arguments.add(expr);
    }

    public void setFunctionDef(FunctionDef functionDef) {
        this.functionDef = functionDef;
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
