package com.kkaka.accountswitcher;

import com.kkaka.base.annotation.Account;

/**
 * @Description:请勿引用此类中任何变量
 * @Author: laizexin
 * @Time: 2019/1/24
 */
public class AccountConfig {

    @Account(accountName = "13737373737",password = "12341234",alias = "奥巴马",isDefailt = true)
    String accountAo;

    @Account(accountName = "14711111111",password = "45674567",alias = "马冬梅")
    String accountMei;

    @Account(accountName = "15521155958",password = "78907890",alias = "自己的")
    String accountMY;

}
