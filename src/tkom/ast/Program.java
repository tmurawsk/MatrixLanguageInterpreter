package tkom.ast;

import tkom.ast.expression.FunctionCall;
import tkom.ast.statement.InitStatement;
import tkom.exception.DuplicateException;

import java.util.HashMap;
import java.util.LinkedList;

public class Program {
    private static LinkedList<InitStatement> initStatements;

    private static LinkedList<FunctionDef> functions;

    private static HashMap<String, Variable> globalVariables;

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

    public static void addGlobalVariable(Variable variable) {
        globalVariables.put(variable.name, variable);
    }

    public static Variable getVariable(String name) {
        return globalVariables.get(name);
    }

    public static FunctionDef getFunctionDef(FunctionCall functionCall) {
        for(FunctionDef def : functions) {
            if(!def.name.equals(functionCall.name) || def.getArguments().size() != functionCall.getArguments().size())
                continue;
//            for(int i = 0; i < def.getArguments().size(); i++) //TODO validate if argument types match
            return def;
        }
        return null;
    }

    public static void checkFunctionDuplicate(FunctionDef functionDef) throws DuplicateException {
        for(FunctionDef def : functions)
            if (def.name.equals(functionDef.name) && def.getArguments().size() == functionDef.getArguments().size()) {
                boolean matched = true;
                for (int i = 0; i < def.getArguments().size(); i++) {
                    if (def.getArguments().get(i).type != functionDef.getArguments().get(i).type) {
                        matched = false;
                        break;
                    }
                }
                if (matched)
                    throw new DuplicateException(functionDef.getPosition(), functionDef);
            }
    }
}
