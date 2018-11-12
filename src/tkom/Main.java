package tkom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, world!");

        try {
            Lexer lexer = new Lexer(new FileInputStream("inputFiles/inputData.txt"));
            Token token;

            do {
                token = lexer.nextToken();
                System.out.println(Token.getKeywordByToken(token.getId()) + ":\t" + token.getValue());
            } while (token.getId() != TokenID.Eof);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
