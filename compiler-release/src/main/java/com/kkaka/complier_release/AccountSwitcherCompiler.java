package com.kkaka.complier_release;

import com.kkaka.base.annotation.Account;
import com.kkaka.base.bean.AccountBean;
import com.kkaka.complier.AccountSwitcherCompilerDebug;
import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;

import static com.kkaka.base.Constants.VAR_ACCOUNT_PREFIX;

public class AccountSwitcherCompiler extends AccountSwitcherCompilerDebug{

    @Override
    protected FieldSpec generateEnvironmentField(Account accountAnnotation, FieldSpec.Builder defaultAccountFiledBuilder, String accountUpperCaseName, String accountName, String password, String alias) {
        return FieldSpec
                .builder(AccountBean.class,
                        String.format("%s%s",VAR_ACCOUNT_PREFIX,accountUpperCaseName),
                        Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(String.format("new %s(\"%s\",\"%s\",\"%s\")",
                        AccountBean.class.getSimpleName(), "", "",""))
                .build();
    }
}
