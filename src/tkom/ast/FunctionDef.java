package tkom.ast;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.statement.Statement;

import java.util.HashMap;
import java.util.LinkedList;

public class FunctionDef extends Statement {
    public String name;

    public TokenID returnType;

    private LinkedList<Variable> arguments;

    private LinkedList<Statement> statements;

    private Variable result;

    private FunctionDef(Position position) {
        super(null, position);
        localVariables = new HashMap<>();
    }

    public FunctionDef(Position position, String name, TokenID returnType, LinkedList<Variable> arguments) {
        this(position);
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        for (Variable v : arguments)
            localVariables.put(v.name, v);
    }

    public LinkedList<Variable> getArguments() {
        return arguments;
    }

    public void setStatements(LinkedList<Statement> statements) {
        this.statements = statements;
    }

    public Variable evaluate(LinkedList<Variable> parameters) {
        return null; //TODO
    }

    @Override
    public void execute() {
        //TODO saves result to "result" variable
    }
}
