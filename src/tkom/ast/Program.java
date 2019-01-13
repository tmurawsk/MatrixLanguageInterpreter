package tkom.ast;

import tkom.Position;
import tkom.ast.statement.InitStatement;
import tkom.ast.statement.Statement;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.MissingMainException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class Program {
    private static LinkedList<InitStatement> initStatements;

    private static LinkedList<FunctionDef> functionDefinitions;

    private static HashMap<String, Variable> globalVariables;

    private static Stack<FunctionDef> functionCallStack;

    static {
        initStatements = new LinkedList<>();
        functionDefinitions = new LinkedList<>();
        globalVariables = new HashMap<>();
        functionCallStack = new Stack<>();
    }

    public static void addFunction(FunctionDef function) {
        functionDefinitions.add(function);
    }

    public static void addInitStatement(InitStatement initStatement) {
        initStatements.add(initStatement);
    }

    public static void addVariable(Variable variable) {
        if (functionCallStack.empty())
            globalVariables.put(variable.name, variable);
        else
            functionCallStack.peek().addVariable(variable);
    }

    public static Variable getVariable(String name) {
        if (!functionCallStack.empty()) {
            Variable variable = functionCallStack.peek().getVariable(name);
            if (variable != null)
                return variable;
        }
        return globalVariables.get(name);
    }

    public static FunctionDef getFunctionDef(FunctionCall functionCall) {
        for (FunctionDef def : functionDefinitions) {
            if (!def.name.equals(functionCall.name) || def.getArguments().size() != functionCall.getParameters().size())
                continue;
            boolean matched = true;
//            for (int i = 0; i < def.getArguments().size(); i++) {
//                if (def.getArguments().get(i).getType() != functionCall.getParameters().get(i).getType()) {
//                    matched = false;
//                    break;
//                }
//            }
            if (matched)
                return def;
        }
        return null;
    }

    public static boolean isFunctionDuplicate(FunctionDef functionDef) {
        for (FunctionDef def : functionDefinitions)
            if (def.name.equals(functionDef.name) && def.getArguments().size() == functionDef.getArguments().size()) {
                boolean matched = true;
                for (int i = 0; i < def.getArguments().size(); i++) {
                    if (def.getArguments().get(i).getType() != functionDef.getArguments().get(i).getType()) {
                        matched = false;
                        break;
                    }
                }
                if (matched)
                    return true;
            }
        return false;
    }

    private static FunctionDef getMainFunction() {
        for (FunctionDef functionDef : functionDefinitions)
            if (functionDef.name.equals("main") && functionDef.getArguments().isEmpty())
                return functionDef;

        return null;
    }

    public static void execute() throws ExecutionException {
        for (InitStatement initStatement : initStatements)
            initStatement.execute();

        FunctionDef main = getMainFunction();
        if (main == null)
            throw new MissingMainException(new Position());

        main.execute();
    }
}
