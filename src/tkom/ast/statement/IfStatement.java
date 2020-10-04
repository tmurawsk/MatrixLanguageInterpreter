package tkom.ast.statement;

import tkom.Position;
import tkom.ast.Program;
import tkom.ast.expression.LogicExpr;
import tkom.exception.ExecutionException.ExecutionException;

import java.util.LinkedList;

public class IfStatement extends Statement {
    private LogicExpr condition;

    private LinkedList<Statement> statements;

    private IfStatement elseStatement;

    public IfStatement(Position position, LogicExpr condition) {
        super(position);
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
    public void execute() throws ExecutionException {
        Program.pushStackLevel();

        if (condition == null || condition.evaluate()) {
            for (Statement stmnt : statements)
                stmnt.execute();
        } else if (elseStatement != null) {
            elseStatement.execute();
        }

        Program.popStackLevel();
    }
}
