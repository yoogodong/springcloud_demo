package dong.yoogo.springcloud_demo.model;

public class SimplePermission {
    private Account account;
    private Operation operation;

    public SimplePermission(Account account, Operation operation) {
        this.account = account;
        this.operation = operation;
    }

    public Account getAccount() {
        return account;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePermission that = (SimplePermission) o;

        if (!account.equals(that.account)) return false;
        return operation == that.operation;

    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + operation.hashCode();
        return result;
    }
}
