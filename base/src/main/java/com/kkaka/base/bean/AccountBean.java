package com.kkaka.base.bean;

import java.util.Objects;

/**
 * @Description:
 * @Author: laizexin
 * @Time: 2019/1/22
 */
public class AccountBean {

    private String accountName;
    private String password;
    private String alias;
    private boolean Checked;

    public String getAccountName() {
        return accountName == null? "":accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password == null? "":password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias == null? "":alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }

    public AccountBean(String accountName, String password, String alias) {
        this(accountName,password,alias,false);
    }

    public AccountBean(String accountName, String password, String alias, boolean checked) {
        this.accountName = accountName;
        this.password = password;
        this.alias = alias;
        Checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountBean)) return false;
        AccountBean that = (AccountBean) o;
        return isChecked() == that.isChecked() &&
                Objects.equals(getAccountName(), that.getAccountName()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getAlias(), that.getAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountName(), getPassword(), getAlias(),isChecked());
    }

    @Override
    public String toString() {
        return "AccountBean{" +
                "accountName='" + accountName + '\'' +
                ", password='" + password + '\'' +
                ", alias='" + alias + '\'' +
                ", Checked=" + Checked +
                '}';
    }
}
