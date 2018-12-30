package tkom.ast.statement;

import tkom.ast.expression.LogicExpr;
import tkom.ast.expression.MathExpr;

import java.util.LinkedList;

public class PrintStatement extends Statement {
    private LinkedList<Printable> toPrint;

    public PrintStatement(Statement parent) {
        super(parent);
        toPrint = new LinkedList<>();
    }

    public void addString(String s) {
        toPrint.add(new Printable(s));
    }

    public void addLogicExpr(LogicExpr expr) {
        toPrint.add(new Printable(expr));
    }

    public void addMathExpr(MathExpr expr) {
        toPrint.add(new Printable(expr));
    }

    @Override
    public void execute() {
        //TODO
    }

    private class Printable {
        String string;

        LogicExpr logicExpr;

        MathExpr mathExpr;

        public Printable(String string) {
            this.string = string;
        }

        public Printable(LogicExpr logicExpr) {
            this.logicExpr = logicExpr;
        }

        public Printable(MathExpr mathExpr) {
            this.mathExpr = mathExpr;
        }
    }
}
