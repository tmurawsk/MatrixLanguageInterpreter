package tkom.ast.statement;

import tkom.Position;
import tkom.ast.Program;
import tkom.ast.Variable;

import java.util.HashMap;

public abstract class Statement {
    private Statement parentStatement;

    protected Position position;

    protected HashMap<String, Variable> localVariables;

    private Statement() {
        localVariables = new HashMap<>();
        parentStatement = null;
    }

    public Statement(Statement parentStatement, Position position) {
        this();
        this.parentStatement = parentStatement;
        this.position = position;
    }

    public void addVariable(Variable variable) {
        localVariables.put(variable.name, variable);
    }

    public Variable getVariable(String name) {
        Variable toReturn = localVariables.get(name);

        if (toReturn == null)
            toReturn = parentStatement != null ? parentStatement.getVariable(name) : Program.getVariable(name);

        return toReturn;
    }

    public Position getPosition() {
        return position;
    }

    public Statement getParent() {
        return parentStatement;
    }

    public boolean variableExists(String name) {
        return getVariable(name) != null;
    }

    public abstract void execute();
}
