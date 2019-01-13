package tkom.ast.statement;

import tkom.Position;

public abstract class Statement {
    protected Position position;

    public Statement(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public abstract void execute();
}
