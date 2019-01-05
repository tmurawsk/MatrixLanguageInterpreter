package tkom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {

//        lexerTest();

        parserTest();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
