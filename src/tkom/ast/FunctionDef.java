package tkom.ast;

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

    private FunctionDef() {
        localVariables = new HashMap<>();
    }

    public FunctionDef(String name, TokenID returnType, LinkedList<Variable> arguments) {
        this();
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public LinkedList<Variable> getArguments() {
        return arguments;
    }

    public void addArgument(Variable v) {
        arguments.add(v);
    }

    public void setStatements(LinkedList<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void execute() {
        //TODO saves result to "result" variable
    }
}
