package tkom.ast;

import tkom.TokenID;
import tkom.ast.expression.MathExpr;

public class VariableCall {
    private Variable variable;

    private MathExpr column;

    private MathExpr row;

    public VariableCall(Variable variable) {
        this.variable = variable;
        column = null;
        row = null;
    }

    public VariableCall(Variable variable, MathExpr column, MathExpr row) {
        this.variable = variable;
        this.column = column;
        this.row = row;
    }

    public TokenID getType() {
        return (column == null || row == null) ? TokenID.Mat : TokenID.Num;
    }

    public void setColumn(MathExpr column) {
        this.column = column;
    }

    public void setRow(MathExpr row) {
        this.row = row;
    }
}
