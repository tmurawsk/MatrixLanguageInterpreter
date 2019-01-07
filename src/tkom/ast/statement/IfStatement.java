package tkom.ast.statement;

import tkom.Position;
import tkom.ast.expression.LogicExpr;

import java.util.LinkedList;

public class IfStatement extends Statement {
    private LogicExpr condition;

    private LinkedList<Statement> statements;

    private IfStatement elseStatement;

    public IfStatement(Statement parent, Position position, LogicExpr condition) {
        super(parent, position);
        this.condition = condition;
        statements = new LinkedList<>();
    }

    public void setStatements(LinkedList<Statement> ifStatements) {
        this.statements = ifStatements;
    }

    public void setElseStatement(IfStatement elseStatement) {
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {
        //TODO
    }
}
