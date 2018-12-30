package tkom.ast;

import tkom.ast.statement.InitStatement;

import java.util.HashMap;
import java.util.LinkedList;

public class Program {
    private static LinkedList<InitStatement> initStatements;

    private static LinkedList<FunctionDef> functions;

    public static HashMap<String, Variable> globalVariables;

    static {
        initStatements = new LinkedList<>();
        functions = new LinkedList<>();
        globalVariables = new HashMap<>();
    }

    public static void addFunction(FunctionDef function) {
        functions.add(function);
    }

    public static void addInitStatement(InitStatement initStatement) {
        initStatements.add(initStatement);
    }
}
