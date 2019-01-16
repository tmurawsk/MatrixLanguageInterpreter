package tkom.ast.statement;

import tkom.Position;
import tkom.ast.Program;
import tkom.ast.expression.LogicExpr;
import tkom.exception.ExecutionException.ExecutionException;

import java.util.LinkedList;

public class WhileStatement extends Statement {
    private LogicExpr condition;

    private LinkedList<Statement> statements;

    public WhileStatement(Position position, LogicExpr condition) {
        super(position);
        this.condition = condition;
        statements = new LinkedList<>();
    }

    public void setStatements(LinkedList<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void execute() throws ExecutionException {
        while (!condition.evaluate()) {
            Program.pushStackLevel();

            for (Statement stmnt : statements)
                stmnt.execute();

            Program.popStackLevel();
        }
    }
}
