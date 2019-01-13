package tkom.ast;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.statement.Statement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class FunctionDef extends Statement {
    public String name;

    public TokenID returnType;

    private LinkedList<Variable> arguments;

    private LinkedList<Statement> statements;

    private Stack<HashMap<String, Variable>> localVariables;

    private Variable result;

    private FunctionDef(Position position) {
        super(position);
        localVariables = new Stack<>();
    }

    public FunctionDef(Position position, String name, TokenID returnType, LinkedList<Variable> arguments) {
        this(position);
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public LinkedList<Variable> getArguments() {
        return arguments;
    }

    public void setStatements(LinkedList<Statement> statements) {
        this.statements = statements;
    }

    public void addVariable(Variable variable) {
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

    public Variable evaluate(LinkedList<Variable> parameters) {
        return null; //TODO
    }

    @Override
    public void execute() {
        //TODO trzeba tu robić new FunctionDef(); wtedy będzie się ją dodawać do stosu wywołań FunctionDef w Programie
        // wtedy jak chcemy się odwołać do Variable to bierzemy z wierzchu stosu FunctionDef i getVariable(name)
        // wtedy jak kończymy evaluate na FunctionDef to robimy na stosie pop()

        //TODO saves result to "result" variable
    }
}
