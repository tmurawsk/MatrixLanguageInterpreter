package tkom.ast;

import tkom.TokenID;

import java.util.ArrayList;

public class Variable {
    public String name;

    private TokenID type;

    private ArrayList<ArrayList<Value>> valueExpressions;

    private ArrayList<ArrayList<Integer>> values;

    public Variable(TokenID type, String name) {
        this.type = type;
        this.name = name;
    }

    public Variable(Value value) {
        ArrayList<ArrayList<Value>> matrix = new ArrayList<>();
        ArrayList<Value> column = new ArrayList<>();
        column.add(value);
        matrix.add(column);

        this.type = TokenID.Num;
        this.valueExpressions = matrix;
        initializeValuesMatrix();
    }

    public Variable(ArrayList<ArrayList<Value>> valueExpressions) {
        this.type = (valueExpressions.size() > 1 || valueExpressions.get(0).size() > 1) ? TokenID.Mat : TokenID.Num;
        this.valueExpressions = valueExpressions;
        initializeValuesMatrix();
    }

    public Variable(int height, int width) {
        valueExpressions = new ArrayList<>();
        values = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            ArrayList<Value> expressionRow = new ArrayList<>();
            ArrayList<Integer> valueRow = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                expressionRow.add(new Value(0));
                valueRow.add(0);
            }
            valueExpressions.add(expressionRow);
            values.add(valueRow);
        }

        type = (height == width && height == 1) ? TokenID.Num : TokenID.Mat;
    }

    public TokenID getType() {
        return type;
    }

    private void initializeValuesMatrix() {
        if (valueExpressions == null)
            return;

        int width = valueExpressions.get(0).size();
        int height = valueExpressions.size();
        values = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            ArrayList<Integer> valueRow = new ArrayList<>();
            for (int j = 0; j < width; j++)
                valueRow.add(0);

            values.add(valueRow);
        }
    }

    int get(int i, int j) {
        if (values == null || i > values.size() || j > values.get(0).size())
            return 0; //TODO throw exception?

        return values.get(i - 1).get(j - 1);
    }

    public Variable add(Variable v) {
        return null; //TODO
    }

    public Variable multiply(Variable v) {
        return null; //TODO
    }

    public Variable divide(Variable v) {
        return null; //TODO
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int maxInt = -1000000;
        for (ArrayList<Integer> array : values)
            for (int i : array)
                maxInt = i > maxInt ? i : maxInt;

        int tabNumber = String.valueOf(maxInt).length() / 4 + 1;
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.get(0).size(); j++) {
                int currInt = get(j, i);
                sb.append(currInt);
                for (int k = 0; k < tabNumber - String.valueOf(currInt).length() / 4; k++)
                    sb.append('\t');
            }
        }

        return sb.toString();
    }
}
