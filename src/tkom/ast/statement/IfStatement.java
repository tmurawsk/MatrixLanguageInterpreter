package tkom.ast.statement;

import tkom.ast.expression.LogicExpr;

import java.util.LinkedList;

public class IfStatement implements Statement {
    private LogicExpr condition;

    private LinkedList<Statement> ifStatements;

    private LinkedList<Statement> elseStatement;

    public IfStatement() {
        ifStatements = new LinkedList<>();
        elseStatement = new LinkedList<>();
    }

    public IfStatement(LogicExpr condition) {
        this();
        this.condition = condition;
    }

    public void addIfStatement(Statement statement) {
        ifStatements.add(statement);
    }

    public void addElseStatement(Statement statement) {
        elseStatement.add(statement);
    }

    @Override
    public void execute() {
        //TODO
    }
}
