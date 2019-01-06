package tkom.ast.statement;

import tkom.Position;
import tkom.ast.expression.LogicExpr;

import java.util.LinkedList;

public class WhileStatement extends Statement {
    private LogicExpr condition;

    private LinkedList<Statement> statements;

    public WhileStatement(Statement parent, Position position, LogicExpr condition) {
        super(parent, position);
        this.condition = condition;
        statements = new LinkedList<>();
    }

    public void setStatements(LinkedList<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void execute() {
        //TODO
    }
}
