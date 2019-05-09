package dong.yoogo.springcloud_demo.model;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class User {
    private int id;
    private Set<SimplePermission> simplePermissions = new HashSet<>();
    private Set<DetailPermission> overridePermission = new HashSet<>();

    public User(int id) {
        this.id = id;
    }

    public Set<SimplePermission> getSimplePermissions() {
        return simplePermissions;
    }

    public int getId() {
        return id;
    }


    public boolean addOverridePermission(DetailPermission added) {
        if (hasPermission(added))
            return false;
        removeSameFunctionAccountPairInOverridePermission(added);
        return overridePermission.add(added);
    }


    public boolean hasPermission(DetailPermission detailPermission) {
        if (overridePermission.contains(detailPermission))
            return true;
        if (isOverride(detailPermission))
            return false;
        SimplePermission simplePermission = detailPermission.getSimplePermission();
        return simplePermissions.contains(simplePermission);
    }


    private void removeSameFunctionAccountPairInOverridePermission(DetailPermission added) {
        sameFunctionAndAccountStream(added).findAny().ifPresent(overridePermission::remove);
    }

    private boolean isOverride(DetailPermission detailPermission) {
        return sameFunctionAndAccountStream(detailPermission).count() > 0;
    }

    private Stream<DetailPermission> sameFunctionAndAccountStream(DetailPermission detailPermission) {
        return overridePermission.stream().filter(x -> {
            boolean sameFunction = x.getFunction().equals(detailPermission.getFunction());
            Account account = detailPermission.getSimplePermission().getAccount();
            boolean sameAccount = x.getSimplePermission().getAccount().equals(account);
            return sameFunction && sameAccount;
        });
    }


}
