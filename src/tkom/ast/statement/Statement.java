package tkom.ast.statement;

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

    public Variable getVariable(String name) {
        Variable toReturn = localVariables.get(name);

        if(toReturn == null && parentStatement != null)
            toReturn = parentStatement.getVariable(name);

        return toReturn;
    }

    public abstract void execute();
}
