package tkom.ast.statement;

import tkom.ast.Program;
import tkom.ast.Variable;

import java.util.HashMap;

public abstract class Statement {
    protected HashMap<String, Variable> localVariables;

    protected Statement parentStatement;

    public Statement() {
        localVariables = new HashMap<>();
        parentStatement = null;
    }

    public Statement(Statement parentStatement) {
        this();
        this.parentStatement = parentStatement;
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

    public boolean variableExists(String name) {
        return getVariable(name) != null;
    }

    public abstract void execute();
}
