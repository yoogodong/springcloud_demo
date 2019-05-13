package dong.yoogo.springcloud_demo.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private String[] functions;
    private Account[] accounts;
    private User user;

    @BeforeEach
    void setUp() {
        functions = new String[]{"转账", "现金", "理财"};
        accounts = new Account[]{new Account(1), new Account(2), new Account(3)};
        user = new User(1);
        user.getSimplePermissions().add(new SimplePermission(accounts[0], Operation.APPLY));
        user.getSimplePermissions().add(new SimplePermission(accounts[1], Operation.REVIEW));
    }

    @Test
    @DisplayName("缺省，制单员对同一账户的所有的功能都有制单权限")
    void have_all_function_permission_default() {

        for (String f : functions) {
            boolean yes = user.hasPermission(new DetailPermission(f, accounts[0], Operation.APPLY));
            assertTrue(yes);
            boolean yes2 = user.hasPermission(new DetailPermission(f, accounts[1], Operation.REVIEW));
            assertTrue(yes2);
        }

    }


    @Nested
    @DisplayName("权限覆盖")
    class one {
        @Test
        @DisplayName("指定一个账户的某个权限，就会覆盖指定功能的缺省权限")
        void override_permission() {
            boolean yes = user.hasPermission(new DetailPermission(functions[0], accounts[0], Operation.APPLY));
            assertTrue(yes);
            user.addOverridePermission(new DetailPermission(functions[0], accounts[0], Operation.REVIEW));
            boolean non = user.hasPermission(new DetailPermission(functions[0], accounts[0], Operation.APPLY));
            assertFalse(non);
        }

        @Test
        @DisplayName("覆盖权限，相当于对缺省权限的补充")
        void override_permission_added() {
            user.addOverridePermission(new DetailPermission(functions[0], accounts[0], Operation.REVIEW));
            boolean yes = user.hasPermission(new DetailPermission(functions[0], accounts[0], Operation.REVIEW));
            assertTrue(yes);
        }

        @Test
        @DisplayName("重复添加同一个权限并不会其作用")
        void should_not_override_the_same() {
            boolean first = user.addOverridePermission(new DetailPermission(functions[0], accounts[0], Operation.REVIEW));
            assertTrue(first);
            boolean second = user.addOverridePermission(new DetailPermission(functions[0], accounts[0], Operation.REVIEW));
            assertFalse(second);
        }

        @Test
        @DisplayName("添加的权限在缺省权限范围内，不会添加成功")
        void should_not_override_if_repeate_simple_permission() {
            boolean noMatter = user.addOverridePermission(new DetailPermission(functions[0], accounts[0], Operation.APPLY));

            assertFalse(noMatter);
        }

        @Test
        @DisplayName("覆盖权限，就会移除原来的权限")
        void override_again() {
            boolean defaultBySimple = user.hasPermission(new DetailPermission(functions[0], accounts[0], Operation.APPLY));
            assertTrue(defaultBySimple);

            boolean review = user.addOverridePermission(new DetailPermission(functions[0], accounts[0], Operation.REVIEW));
            assertTrue(review);
            assertTrue(user.hasPermission(new DetailPermission(functions[0], accounts[0], Operation.REVIEW)));

            boolean search = user.addOverridePermission(new DetailPermission(functions[0], accounts[0], Operation.SEARCH));
            assertTrue(search);
            assertTrue(user.hasPermission(new DetailPermission(functions[0], accounts[0], Operation.SEARCH)));
            assertFalse(user.hasPermission(new DetailPermission(functions[0], accounts[0], Operation.REVIEW)));

        }
    }


}