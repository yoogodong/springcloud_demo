package dong.yoogo.springcloud_demo.model;

public class DetailPermission {
    private String function;
    private SimplePermission simplePermission;

    public DetailPermission(String function, SimplePermission simplePermission) {
        this.function = function;
        this.simplePermission = simplePermission;
    }

    public DetailPermission(String function, Account account, Action action) {
        this.function = function;
        this.simplePermission = new SimplePermission(account, action);
    }

    public String getFunction() {
        return function;
    }

    public SimplePermission getSimplePermission() {
        return simplePermission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailPermission that = (DetailPermission) o;

        if (!function.equals(that.function)) return false;
        return simplePermission.equals(that.simplePermission);

    }

    @Override
    public int hashCode() {
        int result = function.hashCode();
        result = 31 * result + simplePermission.hashCode();
        return result;
    }
}
