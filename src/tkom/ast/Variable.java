package tkom.ast;

import tkom.TokenID;

import java.util.ArrayList;

public class Variable {
    public String name;

    public TokenID type;

    private ArrayList<ArrayList<Value>> valueExpressions;

    private ArrayList<ArrayList<Integer>> values;

    public Variable(TokenID type, String name) {
        this.type = type;
        this.name = name;
    }

    public Variable(Value value) {
        ArrayList<ArrayList<Value>> matrix = new ArrayList<>(1);
        ArrayList<Value> column = new ArrayList<>(1);
        column.set(0, value);
        matrix.set(0, column);

        this.valueExpressions = matrix;
        initializeValuesMatrix();
    }

    public Variable(ArrayList<ArrayList<Value>> valueExpressions) {
        this.valueExpressions = valueExpressions;
        initializeValuesMatrix();
    }

    public Variable(int height, int width) {
        valueExpressions = new ArrayList<>(height);
        values = new ArrayList<>(height);

        for (int i = 0; i < height; i++) {
            valueExpressions.set(i, new ArrayList<>(width));
            values.set(i, new ArrayList<>(width));
        }

        if(height == width && height == 1)
            type = TokenID.Num;
        else
            type = TokenID.Mat;
    }

    private void initializeValuesMatrix() {
        int width = valueExpressions.get(0).size();
        int height = valueExpressions.size();
        values = new ArrayList<>(height);

        for (int i = 0; i < height; i++)
            values.set(i, new ArrayList<>(width));

        if(width == height && width == 1)
            type = TokenID.Num;
        else
            type = TokenID.Mat;
    }

    public int get(int i, int j) {
        if (values == null || i > values.size() || j > values.get(0).size())
            return 0; //TODO throw exception?

        return values.get(i - 1).get(j - 1);
    }

    public Variable add(Variable v) {
//        if (value == null || v.value == null
//                || value.size() != v.value.size()
//                || value.get(0).size() != v.value.get(0).size())
//            return null; //TODO throw exception
//
//        Variable result = new Variable(value.size(), value.get(0).size());
//        for (int i = 0; i < value.size(); i++)
//            for (int j = 0; j < value.get(0).size(); j++)
//                result.value.get(i).set(j, get(i, j) + v.get(i, j));
//        return result;
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
