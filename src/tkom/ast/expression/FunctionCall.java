package tkom.ast.expression;

import tkom.Position;
import tkom.ast.FunctionDef;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class FunctionCall extends MathExpression {
    public String name;

    private FunctionDef functionDef;

    private LinkedList<MathExpr> arguments;

    public FunctionCall(Statement parent, Position position, String name) {
        super(parent, position);
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
        this.type = functionDef.returnType;
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
