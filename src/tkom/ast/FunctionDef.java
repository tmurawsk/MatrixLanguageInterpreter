package tkom.ast;

import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class FunctionDef {
    private String name;

    private LinkedList<Variable> arguments;

    private LinkedList<Statement> statements;

    public FunctionDef() {
        arguments = new LinkedList<>();
        statements = new LinkedList<>();
    }

    public FunctionDef(String name) {
        this();
        this.name = name;
    }

    public void addArgument(Variable v) {
        arguments.add(v);
    }

    public void addStatement(Statement stmnt) {
        statements.add(stmnt);
    }
}
