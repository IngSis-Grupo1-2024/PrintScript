package util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ReadFromConsole {
    public static String execute(String inputFilePath, String version){
        Exec exec = new Exec();
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        // Call the method that prints to console
        exec.execute("execution", inputFilePath, version);

        // Restore original System.out
        System.out.flush();
        System.setOut(originalOut);

        // Get the captured output
        return outputStream.toString();

    }
}