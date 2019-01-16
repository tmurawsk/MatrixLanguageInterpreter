package tkom.ast;

import tkom.TokenID;
import tkom.exception.ExecutionException.IndexOutOfBoundsException;

import java.util.ArrayList;

public class Variable {
    public String name;

    private TokenID type;

    private ArrayList<ArrayList<Value>> valueExpressions;

    private ArrayList<ArrayList<Integer>> value;

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

    public TokenID getType() {
        return type;
    }

    public ArrayList<ArrayList<Integer>> getValue() {
        evaluate();
        return value;
    }

    public void setValue(ArrayList<ArrayList<Integer>> value) {
        this.value = value;
    }

    public void setValue(int i, int j, int newValue) {
        value.get(i).set(j, newValue);
    }

    public void setValue(int newValue) {
        value = new ArrayList<>();
        value.set(0, new ArrayList<>());
        value.get(0).set(0, newValue);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void initSize(int height, int width) {
        valueExpressions = new ArrayList<>();
        value = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            ArrayList<Value> expressionRow = new ArrayList<>();
            ArrayList<Integer> valueRow = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                expressionRow.add(new Value(0));
                valueRow.add(0);
            }
            valueExpressions.add(expressionRow);
            value.add(valueRow);
        }

        type = (height == width && height == 1) ? TokenID.Num : TokenID.Mat;
    }

    private void initializeValuesMatrix() {
        if (valueExpressions == null)
            return;

        int width = valueExpressions.get(0).size();
        int height = valueExpressions.size();
        value = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            ArrayList<Integer> valueRow = new ArrayList<>();
            for (int j = 0; j < width; j++)
                valueRow.add(0);

            value.add(valueRow);
        }
    }

    public int get(int i, int j) {
        if (value == null || i > value.size() || j > value.get(0).size())
            return 0; //TODO throw exception?

        return value.get(i - 1).get(j - 1);
    }

    public int getInt() {
        return get(1,1);
    }

    public int getHeight() {
        return value.size();
    }

    public int getWidth() {
        return value.get(0).size();
    }

    public Variable evaluate() {
        for (int i = 0; i < valueExpressions.size(); i++)
            for (int j = 0;j < valueExpressions.get(i).size(); j++)
                value.get(i).set(j, valueExpressions.get(i).get(j).evaluate());

        return this;
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
        for (ArrayList<Integer> array : value)
            for (int i : array)
                maxInt = i > maxInt ? i : maxInt;

        int tabNumber = String.valueOf(maxInt).length() / 4 + 1;
        for (int i = 0; i < value.size(); i++) {
            for (int j = 0; j < value.get(0).size(); j++) {
                int currInt = get(j, i);
                sb.append(currInt);
                for (int k = 0; k < tabNumber - String.valueOf(currInt).length() / 4; k++)
                    sb.append('\t');
            }
        }

        return sb.toString();
    }
}
