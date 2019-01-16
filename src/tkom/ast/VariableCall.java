package tkom.ast;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.expression.MathExpr;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.IndexOutOfBoundsException;
import tkom.exception.ExecutionException.TypeMismatchException;

public class VariableCall {
    private String variableReference;

    private Variable anonymousVariable;

    private MathExpr column;

    private MathExpr row;

    private Position position;

    private VariableCall(Position position) {
        this.position = position;
    }

    public VariableCall(String variableReference, Position position) {
        this(position);
        this.variableReference = variableReference;
    }

    public VariableCall(Variable anonymousVariable, Position position) {
        this(position);
        this.anonymousVariable = anonymousVariable;
    }

    public TokenID getType() {
        if (column != null && row != null)
            return TokenID.Num;

        if (anonymousVariable != null)
            return anonymousVariable.getType();

        Variable variable = Program.getVariable(variableReference);
        return variable != null ? variable.getType() : TokenID.Invalid;
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

    public void setValue(Variable newVariable) throws ExecutionException {
        TokenID type = getType();
        if (type != newVariable.getType())
            throw new TypeMismatchException(position, type, newVariable.getType());
        Variable refVariable = Program.getVariable(variableReference);

        if (row != null && column != null) {
            Variable rowVar = row.evaluate();
            Variable colVar = column.evaluate();
            if (rowVar.getType() != TokenID.Num)
                throw new TypeMismatchException(position, TokenID.Num, rowVar.getType());
            if (colVar.getType() != TokenID.Num)
                throw new TypeMismatchException(position, TokenID.Num, colVar.getType());
            if (rowVar.getInt() < 1 || rowVar.getInt() > refVariable.getHeight())
                throw new IndexOutOfBoundsException(position, rowVar.getInt());
            if (colVar.getInt() < 1 || colVar.getInt() > refVariable.getWidth())
                throw new IndexOutOfBoundsException(position, colVar.getInt());
            refVariable.setValue(rowVar.getInt(), colVar.getInt(), newVariable.getInt());
        }
        else {
            refVariable.setValue(newVariable.getValue());
        }
    }
}
