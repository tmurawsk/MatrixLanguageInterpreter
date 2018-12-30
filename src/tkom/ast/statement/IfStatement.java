package tkom.ast.statement;

import tkom.ast.expression.LogicExpr;

import java.util.LinkedList;

public class IfStatement extends Statement {
    private LogicExpr condition;

    private LinkedList<Statement> ifStatements;

    private LinkedList<Statement> elseStatements;

    public IfStatement(Statement parent, LogicExpr condition) {
        super(parent);
        ifStatements = new LinkedList<>();
        elseStatements = new LinkedList<>();
        this.condition = condition;
    }

    public void addIfStatement(Statement statement) {
        ifStatements.add(statement);
    }

    public void addElseStatement(Statement statement) {
        elseStatements.add(statement);
    }

    @Override
    public void execute() {
        //TODO
    }
}
