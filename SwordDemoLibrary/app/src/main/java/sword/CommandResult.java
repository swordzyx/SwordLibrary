package sword;

public class CommandResult {
    private String errorString;
    private String successString;
    String[] args = new String[5];

    CommandResult(String errorString, String successString) {
        this.errorString = errorString;
        this.successString = successString;
    }

    public String getErrorString() {
        return errorString;
    }

    public String getSuccessString() {
        return successString;
    }
}
