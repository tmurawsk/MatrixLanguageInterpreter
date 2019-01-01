package tkom.ast;

import tkom.ast.expression.FunctionCall;
import tkom.ast.statement.InitStatement;

import java.util.HashMap;
import java.util.LinkedList;

public class Program {
    private static LinkedList<InitStatement> initStatements;

    private static HashMap<String, FunctionDef> functions;

    private static HashMap<String, Variable> globalVariables;

    static {
        initStatements = new LinkedList<>();
        functions = new HashMap<>();
        globalVariables = new HashMap<>();
    }

    public static boolean functionExists(FunctionCall functionCall) {
        boolean exists = false;
        for(FunctionDef def : functions.values()) {
            if(!def.name.equals(functionCall.name) || def.getArguments().size() != functionCall.getArguments().size())
                continue;
//            for(int i = 0; i < def.getArguments().size(); i++) //TODO validate if argument types match
            exists = true;
            break;
        }
        return exists;
    }

    public static void addFunction(FunctionDef function) {
        functions.put(function.name, function);
    }

    public static void addInitStatement(InitStatement initStatement) {
        initStatements.add(initStatement);
    }
}
