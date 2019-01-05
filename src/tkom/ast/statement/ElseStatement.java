package tkom.ast.statement;

import java.util.LinkedList;

public class ElseStatement extends Statement {

    private LinkedList<Statement> statements;

    public ElseStatement(Statement parent) {
        super(parent);
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
