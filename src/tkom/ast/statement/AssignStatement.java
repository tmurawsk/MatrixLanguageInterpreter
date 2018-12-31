package tkom.ast.statement;

import tkom.ast.expression.MathExpr;

public class AssignStatement extends Statement {
    private String variableName;

    private MathExpr expression;

    private MathExpr columnToSet;

    private MathExpr rowToSet;

    public AssignStatement(Statement parent, String variableName, MathExpr columnToSet, MathExpr rowToSet, MathExpr expression) {
        super(parent);
        this.variableName = variableName;
        this.columnToSet = columnToSet;
        this.rowToSet = rowToSet;
        this.expression = expression;
    }

    @Override
    public void execute() {
        //TODO
    }

    public void setColumnToSet(MathExpr columnToSet) {
        this.columnToSet = columnToSet;
    }

    public void setRowToSet(MathExpr rowToSet) {
        this.rowToSet = rowToSet;
    }
}
