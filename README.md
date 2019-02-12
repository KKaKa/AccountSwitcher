[![Build Status](https://travis-ci.org/KKaKa/AccountSwitcher.svg?branch=master)](https://travis-ci.org/KKaKa/AccountSwitcher)[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

# AccountSwitcher
可设置多个测试账号并且能快速切换的工具。无需自行构建界面，轻松配置测试账号，同时避免测试账号的泄漏。

>  :smile: 如果这对你有帮助，随手给个star，这将是我前进的动力。

## 由来
由于在项目中，经常需要切换测试账号来测试不同的场景，每次都要手动输入那些烂熟如心的账号和密码，就想着有个一键切换账号来避免手输，而且如果能连界面都不写的话就更好了，能在多个项目中直接配置使用，不用每次都去重新写界面，于是乎就产生了AccountSwitcher。

## 接入
```
implementation 'com.sdj.kkaka:account-switcher:1.0.3'
debugAnnotationProcessor 'com.sdj.kkaka:account-switcher-compiler:1.0.3'
releaseAnnotationProcessor 'com.sdj.kkaka:account-switcher-compiler-release:1.0.3'
```

## 使用
推荐private修饰且勿引用任何一个变量。避免后续混淆无法正常混淆该文件。
使用@Account修饰的属性表示一个账号。accountName,password,alias三个值必须指定。isDefault默认为false，所有账号中，需要指定一个为isDefault，**有且只能有一个账号isDefault = true！！！**

```java
/**
 * @Description:请勿引用此类中任何变量
 */
public class AccountConfig {

    @Account(accountName = "13737373737",password = "12341234",alias = "奥巴马",isDefault = true)
    private String accountAo;

    @Account(accountName = "14711111111",password = "45674567",alias = "马冬梅")
    private String accountMei;

    @Account(accountName = "15521155958",password = "78907890",alias = "自己的")
    private String accountMY;

}
```

在任何需要监听账号变换的地方添加监听，
```java
AccountSwitcher.addAccountChangeListener(new OnAccountChangeListener() {
    @Override
    public void onAccountChange(AccountBean account) {
       //......
    }
});
```

## 安全
由于账号密码这种极度机密的信息不能随意泄漏，AccountSwitcher这在方面做了处理，在debug版本中显示如下：
```java
public final class AccountSwitcher {
  ...
  public static final AccountBean ACCOUNT_ACCOUNTAO = new AccountBean("13737373737","12341234","奥巴马");

  public static final AccountBean ACCOUNT_ACCOUNTMEI = new AccountBean("14711111111","45674567","马冬梅");

  public static final AccountBean ACCOUNT_ACCOUNTMY = new AccountBean("15521155958","78907890","自己的");

  private static final AccountBean DEFAULT_ACCOUNT = ACCOUNT_ACCOUNTAO;
  ....
}
```
在release版中，显示如下：
```java
public final class AccountSwitcher {
  ...
  public static final AccountBean ACCOUNT_ACCOUNTAO = new AccountBean("","","");

  public static final AccountBean ACCOUNT_ACCOUNTMEI = new AccountBean("","","");

  public static final AccountBean ACCOUNT_ACCOUNTMY = new AccountBean("","","");

  private static final AccountBean DEFAULT_ACCOUNT = ACCOUNT_ACCOUNTAO;
  ....
}
```
AccountSwitcher在release中会自行将敏感信息替换为""，避免泄漏，同时开启混淆会将AccountConfig混淆，同样不会造成信息泄漏。这也是为什么AccountConfig的变量要为private且不要引用的原因。

## 自带账号切换界面
如果需要账号切换界面AccountSwitcherBoxActivity，需导入'com.sdj.kkaka:account-switcher:1.0.3'，建议添加debug检查，
```java
if (!BuildConfig.DEBUG) {
    //....
    AccountSwitcherBoxActivity.toAccountSwitcherBoxActivity(this);
}
```




