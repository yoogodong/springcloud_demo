package dong.yoogo.springcloud_demo.model;

public class SimplePermission {
    private Account account;
    private Action action;

    public SimplePermission(Account account, Action action) {
        this.account = account;
        this.action = action;
    }

    public Account getAccount() {
        return account;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePermission that = (SimplePermission) o;

        if (!account.equals(that.account)) return false;
        return action == that.action;

    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + action.hashCode();
        return result;
    }
}
