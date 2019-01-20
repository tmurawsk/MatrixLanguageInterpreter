package tkom.ast.statement;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Value;
import tkom.ast.Variable;
import tkom.ast.VariableCall;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.TypeMismatchException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ReadStatement extends Statement {
    private VariableCall variableCall;

    public ReadStatement(Position position, VariableCall variableCall) {
        super(position);
        this.variableCall = variableCall;
    }

    @Override
    public void execute() throws ExecutionException {
        Scanner scanner = new Scanner(System.in);
        int value;
        try {
            value = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new TypeMismatchException(position, TokenID.Num, TokenID.String);
        }
        variableCall.setValue(new Variable(new Value(value)));
        scanner.close();
    }
}
