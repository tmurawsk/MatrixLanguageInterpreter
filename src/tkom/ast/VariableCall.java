package tkom.ast;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.expression.MathExpr;

public class VariableCall {
    private Variable variable;

    private MathExpr column;

    private MathExpr row;

    private Position position;

    public VariableCall(Variable variable, Position position) {
        this.variable = variable;
        this.position = position;
        column = null;
        row = null;
    }

    public TokenID getType() {
        return (column == null || row == null) ? variable.getType() : TokenID.Num;
    }

    public void setColumn(MathExpr column) {
        this.column = column;
    }

    public void setRow(MathExpr row) {
        this.row = row;
    }

    public Position getPosition() {
        return position;
    }
}
