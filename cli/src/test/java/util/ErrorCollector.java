package util;

import java.util.ArrayList;
import java.util.List;

public class ErrorCollector{

    final private List<String> errors = new ArrayList<>();

    public void reportError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }
}
