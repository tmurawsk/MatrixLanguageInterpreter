package tkom.ast;

import tkom.ast.statement.InitStatement;

import java.util.HashMap;
import java.util.LinkedList;

public class Program {
    private LinkedList<InitStatement> initStatements;

    private LinkedList<FunctionDef> functions;

    public HashMap<String, Variable> globalVariables;

    public Program() {
        initStatements = new LinkedList<>();
        functions = new LinkedList<>();
        globalVariables = new HashMap<>();
    }

    public void addFunction(FunctionDef function) {
        functions.add(function);
    }

    public void addInitStatement(InitStatement initStatement) {
        initStatements.add(initStatement);
    }
}
