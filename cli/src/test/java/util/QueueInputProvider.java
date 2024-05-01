package util;

import ingsis.interpreter.interpretStatement.Input;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Queue;

public class QueueInputProvider implements Input {

    final private Queue<String> messages;

    public QueueInputProvider(Queue<String> messages) {
        this.messages = messages;
    }

    @NotNull
    @Override
    public String readInput(@NotNull String string) {
        return Objects.requireNonNull(messages.poll());
    }
}
