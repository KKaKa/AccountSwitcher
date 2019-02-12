package com.kkaka.complier_release;

import com.google.auto.service.AutoService;
import com.kkaka.base.annotation.Account;
import com.kkaka.base.bean.AccountBean;
import com.kkaka.complier.AccountSwitcherCompilerDebug;
import com.squareup.javapoet.FieldSpec;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Modifier;

import static com.kkaka.base.Constants.VAR_ACCOUNT_PREFIX;

@AutoService(Processor.class)
public class AccountSwitcherCompiler extends AccountSwitcherCompilerDebug{

    @Override
    protected FieldSpec generateEnvironmentField(Account accountAnnotation, FieldSpec.Builder defaultAccountFiledBuilder, String accountUpperCaseName, String accountName, String password, String alias) {

        if (accountAnnotation.isDefault()) {
            defaultAccountFiledBuilder.initializer(String.format("%s%s", VAR_ACCOUNT_PREFIX,accountUpperCaseName));
        }

        return FieldSpec
                .builder(AccountBean.class,
                        String.format("%s%s",VAR_ACCOUNT_PREFIX,accountUpperCaseName),
                        Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(String.format("new %s(\"%s\",\"%s\",\"%s\")",
                        AccountBean.class.getSimpleName(), "", "",""))
                .build();
    }
}
