package tkom.ast;

import javafx.util.Pair;
import tkom.Position;
import tkom.TokenID;
import tkom.ast.statement.Statement;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.ReturnValue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class FunctionDef extends Statement {
    public String name;

    public TokenID returnType;

    private LinkedList<Pair<TokenID, String>> arguments;

    private LinkedList<Statement> statements;

    private LinkedList<Variable> callParameters;

    private Stack<HashMap<String, Variable>> localVariables;

    private Variable result;

    private FunctionDef(Position position) {
        super(position);
        localVariables = new Stack<>();
        localVariables.push(new HashMap<>());
    }

    public FunctionDef(Position position, String name, TokenID returnType, LinkedList<Pair<TokenID, String>> arguments) {
        this(position);
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    @SuppressWarnings("CopyConstructorMissesField")
    private FunctionDef(FunctionDef f) {
        this(f.getPosition(), f.name, f.returnType, f.getArguments());
        this.setStatements(f.getStatements());
    }

    public LinkedList<Pair<TokenID, String>> getArguments() {
        return arguments;
    }

    private LinkedList<Statement> getStatements() {
        return statements;
    }

    public void setStatements(LinkedList<Statement> statements) {
        this.statements = statements;
    }

    void addVariable(Variable variable) {
        localVariables.peek().put(variable.name, variable);
    }

    Variable getVariable(String name) {
        for (HashMap<String, Variable> variables : localVariables) {
            Variable v = variables.get(name);
            if (v != null)
                return v;
        }
        return null;
    }

    private Variable getResult() {
        return result;
    }

    public void setResult(Variable result) {
        this.result = result;
    }

    void pushStackLevel() {
        localVariables.push(new HashMap<>());
    }

    void popStackLevel() {
        localVariables.pop();
    }

    public Variable evaluate(LinkedList<Variable> parameters) throws ExecutionException {
        result = null;
        execute(parameters);
        return result;
    }

    public void execute(LinkedList<Variable> callParameters) throws ExecutionException {
        this.callParameters = callParameters;
        execute();
    }

    @Override
    public void execute() throws ExecutionException {
        FunctionDef functionDef = new FunctionDef(this);
        Program.pushFunctionCall(functionDef);

        if (callParameters != null) {
            for (int i = 0; i < callParameters.size(); i++) {
                Variable param = new Variable(arguments.get(i).getKey(), arguments.get(i).getValue());
                param.set(callParameters.get(i).evaluate());
                functionDef.addVariable(param);
            }
        }

        try {
            for (Statement stmnt : statements)
                stmnt.execute();
        } catch (ReturnValue e) {
            result = functionDef.getResult();
        }

        Program.popFunctionCall();
    }
}
