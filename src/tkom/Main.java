package tkom;

import tkom.ast.FunctionDef;
import tkom.ast.Program;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

//        lexerTest();

//        parserTest();

        execute();
    }

    private static void lexerTest() {
        try {
            Lexer lexer = new Lexer(new FileInputStream("inputFiles/invalidInputData.txt"));
            Token token;

            do {
                token = lexer.readToken();
                System.out.println(Token.getNameByToken(token.getId()) + ":\t" + token.getValue());
            } while (token.getId() != TokenID.Eof);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void parserTest() {
        try {
            Lexer lexer = new Lexer(new FileInputStream("inputFiles/validInputData.txt"));
            Parser parser = new Parser(lexer);

            parser.parseProgram();

//            LinkedList<FunctionDef> functions = Program.functionDefinitions;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void execute() {
        try {
            Lexer lexer = new Lexer(new FileInputStream("inputFiles/validInputData.txt"));
            Parser parser = new Parser(lexer);

            parser.parseProgram();

            Program.execute();

//            LinkedList<FunctionDef> functions = Program.functionDefinitions;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
