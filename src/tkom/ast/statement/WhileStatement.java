package tkom.ast.statement;

import tkom.ast.expression.LogicExpr;

import java.util.LinkedList;

public class WhileStatement implements Statement {
    private LogicExpr condition;

    private LinkedList<Statement> statements;

    public WhileStatement() {
        statements = new LinkedList<>();
    }

    public WhileStatement(LogicExpr condition) {
        this();
        this.condition = condition;
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
        //TODO
    }
}
