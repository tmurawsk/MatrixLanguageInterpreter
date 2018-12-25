package tkom.ast.statement;

import tkom.ast.expression.MathExpr;

import java.util.LinkedList;

public class FunctionCall implements Statement {
    private String name; //TODO rethink if not FunctionDef

    private LinkedList<MathExpr> arguments;

    public FunctionCall() {
        arguments = new LinkedList<>();
    }

    public FunctionCall(String name) {
        this();
        this.name = name;
    }

    public void addArgument(MathExpr expr) {
        arguments.add(expr);
    }

    @Override
    public void execute() {
        //TODO
    }
}
