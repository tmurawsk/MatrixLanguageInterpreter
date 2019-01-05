package tkom.ast.statement;

import tkom.ast.expression.LogicExpr;

import java.util.LinkedList;

public class IfStatement extends Statement {
    private LogicExpr condition;

    private LinkedList<Statement> ifStatements;

    private ElseStatement elseStatement;

    public IfStatement(Statement parent, LogicExpr condition) {
        super(parent);
        this.condition = condition;
        ifStatements = new LinkedList<>();
    }

    public void setIfStatements(LinkedList<Statement> ifStatements) {
        this.ifStatements = ifStatements;
    }

    public void addElseStatement(ElseStatement elseStatement) {
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {
        //TODO
    }
}
