package tkom.ast;

import tkom.TokenID;
import tkom.ast.statement.Statement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FunctionDef {
    public String name;

    public TokenID returnType;

    private LinkedList<Variable> arguments;

    private LinkedList<Statement> statements;

    private HashMap<String, Variable> localVariables;

    public FunctionDef() {
        arguments = new LinkedList<>();
        statements = new LinkedList<>();
        localVariables = new HashMap<>();
    }

    public FunctionDef(String name) {
        this();
        this.name = name;
    }

    public LinkedList<Variable> getArguments() {
        return arguments;
    }

    public void addArgument(Variable v) {
        arguments.add(v);
    }

    public void addStatement(Statement stmnt) {
        statements.add(stmnt);
    }

    public Variable execute(List<Variable> givenArguments) {
        return new Variable(); //TODO
    }
}
