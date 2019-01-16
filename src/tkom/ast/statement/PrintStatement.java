package tkom.ast.statement;

import tkom.Position;
import tkom.ast.expression.MathExpr;

import java.util.LinkedList;

public class PrintStatement extends Statement {
    private LinkedList<Printable> toPrint;

    public PrintStatement(Position position) {
        super(position);
        toPrint = new LinkedList<>();
    }

    public void addPrintable(String s) {
        toPrint.add(new Printable(s));
    }

    public void addPrintable(MathExpr expr) {
        toPrint.add(new Printable(expr));
    }

    @Override
    public void execute() {
        StringBuilder sb = new StringBuilder();
        for (Printable p : toPrint) {
            sb.append(p.toString());
        }
        System.out.println(sb.toString());
    }

    private class Printable {
        String string;

        MathExpr mathExpr;

        Printable(String string) {
            this.string = string;
        }

        Printable(MathExpr mathExpr) {
            this.mathExpr = mathExpr;
        }

        public String toString(){
            if (mathExpr != null)
                return mathExpr.evaluate().toString();
            else
                return string;
        }
    }
}
