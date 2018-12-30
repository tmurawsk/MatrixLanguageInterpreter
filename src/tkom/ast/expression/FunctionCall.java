package tkom.ast.expression;

import tkom.ast.Variable;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class FunctionCall extends MathExpression {
    public String name; //TODO rethink if not FunctionDef

    private LinkedList<MathExpr> arguments;

    public FunctionCall(Statement parent, String name) {
        super(parent);
        this.name = name;
        arguments = new LinkedList<>();
    }

    public void addArgument(MathExpr expr) {
        arguments.add(expr);
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
