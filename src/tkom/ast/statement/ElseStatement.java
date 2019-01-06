package tkom.ast.statement;

import tkom.Position;

import java.util.LinkedList;

public class ElseStatement extends Statement {

    private LinkedList<Statement> statements;

    public ElseStatement(Statement parent, Position position) {
        super(parent, position);
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
