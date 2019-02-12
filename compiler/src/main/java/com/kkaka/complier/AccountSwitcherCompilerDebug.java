package com.kkaka.complier;

import com.google.auto.service.AutoService;
import com.kkaka.base.annotation.Account;
import com.kkaka.base.bean.AccountBean;
import com.kkaka.base.listener.OnAccountChangeListener;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.kkaka.base.Constants.ACCOUNT;
import static com.kkaka.base.Constants.ACCOUNT_LIST;
import static com.kkaka.base.Constants.ACCOUNT_SWITCHER_FILE_NAME;
import static com.kkaka.base.Constants.CONTEXT_TYPE_NAME;
import static com.kkaka.base.Constants.METHOD_NAME_ADD_ACCOUNT_CHANGE_LISTENER;
import static com.kkaka.base.Constants.METHOD_NAME_GET_ACCOUNT;
import static com.kkaka.base.Constants.METHOD_NAME_GET_CURRENT_ACCOUNT_BEAN;
import static com.kkaka.base.Constants.METHOD_NAME_ON_ACCOUNT_CHANGE;
import static com.kkaka.base.Constants.METHOD_NAME_REMOVE_ACCOUNT_CHANGE_LISTENER;
import static com.kkaka.base.Constants.METHOD_NAME_REMOVE_ALL_ACCOUNT_CHANGE_LISTENERS;
import static com.kkaka.base.Constants.METHOD_NAME_SET_CURRENT_ACCOUNT_BEAN;
import static com.kkaka.base.Constants.MODE_PRIVATE;
import static com.kkaka.base.Constants.ON_ACCOUNT_CHANGE_LISTENER_LIST;
import static com.kkaka.base.Constants.PACKAGE_NAME;
import static com.kkaka.base.Constants.PARAM_ACCOUNT_BEAN;
import static com.kkaka.base.Constants.PARAM_ON_ACCOUNT_CHANGE_LISTNER;
import static com.kkaka.base.Constants.VAR_ACCOUNT_ALIAS_NAME_SUFFIX;
import static com.kkaka.base.Constants.VAR_ACCOUNT_NAME_SUFFIX;
import static com.kkaka.base.Constants.VAR_ACCOUNT_PASS_WORD_SUFFIX;
import static com.kkaka.base.Constants.VAR_ACCOUNT_PREFIX;
import static com.kkaka.base.Constants.VAR_CONTEXT;
import static com.kkaka.base.Constants.VAR_CURRENT_ACCOUNT;
import static com.kkaka.base.Constants.VAR_DEFAULT_ACCOUNT_PREFIX;
import static com.kkaka.base.Constants.VAR_NEW_ACCOUNT;

/**
 * @Description:
 * @Author: laizexin
 * @Time: 2019/1/24
 */
@AutoService(Processor.class)
public class AccountSwitcherCompilerDebug extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //Builder
        TypeSpec.Builder builder = TypeSpec.classBuilder(ACCOUNT_SWITCHER_FILE_NAME).addModifiers(Modifier.PUBLIC,Modifier.FINAL);

        //OnAccountChangeListeners = new Arraylist<>();
        FieldSpec onAccountChangeListeners = FieldSpec.builder(ArrayList.class,ON_ACCOUNT_CHANGE_LISTENER_LIST)
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC,Modifier.FINAL)
                .initializer("new ArrayList<OnAccountChangeListener>()")
                .build();
        builder.addField(onAccountChangeListeners);

        //addAccountChangeListener
        MethodSpec addAccountChangeListener = MethodSpec.methodBuilder(METHOD_NAME_ADD_ACCOUNT_CHANGE_LISTENER)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(void.class)
                .addParameter(OnAccountChangeListener.class,PARAM_ON_ACCOUNT_CHANGE_LISTNER)
                .addStatement(String.format("%s.add(%s)",ON_ACCOUNT_CHANGE_LISTENER_LIST,PARAM_ON_ACCOUNT_CHANGE_LISTNER))
                .build();
        builder.addMethod(addAccountChangeListener);

        //removeAccountChangeListener
        MethodSpec removeAccountChangeListener = MethodSpec.methodBuilder(METHOD_NAME_REMOVE_ACCOUNT_CHANGE_LISTENER)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(void.class)
                .addParameter(OnAccountChangeListener.class,PARAM_ON_ACCOUNT_CHANGE_LISTNER)
                .addStatement(String.format("%s.remove(%s)",ON_ACCOUNT_CHANGE_LISTENER_LIST,PARAM_ON_ACCOUNT_CHANGE_LISTNER))
                .build();
        builder.addMethod(removeAccountChangeListener);

        //removeAllListener
        MethodSpec removeAllListener = MethodSpec.methodBuilder(METHOD_NAME_REMOVE_ALL_ACCOUNT_CHANGE_LISTENERS)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(void.class)
                .addStatement(String.format("%s.clear()",ON_ACCOUNT_CHANGE_LISTENER_LIST))
                .build();
        builder.addMethod(removeAllListener);

        //onAccountChange
        MethodSpec onAccountChange = MethodSpec.methodBuilder(METHOD_NAME_ON_ACCOUNT_CHANGE)
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .returns(void.class)
                .addParameter(AccountBean.class,PARAM_ACCOUNT_BEAN)
                .addCode(String.format(
                        "for(Object onAccountChangeListener : %s){\n" +
                                "   if(onAccountChangeListener instanceof %s){\n" +
                                "       ((%s) onAccountChangeListener).onAccountChange(%s);\n"+
                                "   }\n"+
                                "}",
                        ON_ACCOUNT_CHANGE_LISTENER_LIST,
                        OnAccountChangeListener.class.getSimpleName(),
                        OnAccountChangeListener.class.getSimpleName(),PARAM_ACCOUNT_BEAN
                )).build();
        builder.addMethod(onAccountChange);

        //Arraylist ACCOUNT_LIST
        FieldSpec accountList = FieldSpec.builder(ArrayList.class,ACCOUNT_LIST)
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC,Modifier.FINAL)
                .initializer(String.format("new %s<%s>()",ArrayList.class.getSimpleName(), AccountBean.class.getSimpleName()))
                .build();
        builder.addField(accountList);

        //getAccountListBuilder
        MethodSpec.Builder getAccountListBuilder = MethodSpec.methodBuilder(METHOD_NAME_GET_ACCOUNT)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(ArrayList.class);

        //DEFAULT_ACCOUNT
        FieldSpec.Builder defaultAccountFiledBuilder = FieldSpec
                .builder(AccountBean.class, String.format("%s%s", VAR_DEFAULT_ACCOUNT_PREFIX,ACCOUNT),
                        Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

        CodeBlock.Builder staticCodeBlockBuilder = CodeBlock.builder();

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Account.class);

        //ACCOUNT_LIST.add(xxx)
        for(Element element : elements){
            Account accountAnnotation = element.getAnnotation(Account.class);
            if(accountAnnotation == null){
                continue;
            }
            String accountAnnotationName = element.getSimpleName().toString();
            String accountUpperCaseName = accountAnnotationName.toUpperCase();
            String accountLowerCaseName = accountAnnotationName.toLowerCase();
            String accountName = accountAnnotation.accountName();
            String password = accountAnnotation.password();
            String alias = accountAnnotation.alias();

            //public static final AccountBean ACCOUNT_accountAnnotationName = new AccountBean(name,password,alias)
            FieldSpec accountField = generateEnvironmentField(
                    accountAnnotation,
                    defaultAccountFiledBuilder,
                    accountUpperCaseName,
                    accountName,
                    password,
                    alias);

            builder.addField(accountField);

            staticCodeBlockBuilder
                    .add("\n")
                    .addStatement(String.format("%s.add(%s%s)",ACCOUNT_LIST,VAR_ACCOUNT_PREFIX,accountUpperCaseName));
        }
        builder.addField(defaultAccountFiledBuilder.build()).build();

        //currentAccount
        FieldSpec currentAccountField = FieldSpec
                .builder(AccountBean.class, VAR_CURRENT_ACCOUNT)
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .build();
        builder.addField(currentAccountField);

        //getAccountBeanMethod
        MethodSpec getAccountBeanMethod = MethodSpec.methodBuilder(METHOD_NAME_GET_CURRENT_ACCOUNT_BEAN)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC,Modifier.FINAL)
                .returns(AccountBean.class)
                .addParameter(CONTEXT_TYPE_NAME,VAR_CONTEXT)
                .addCode(String.format(
                        "if (%s == null) {\n" +
                                "android.content.SharedPreferences sharedPreferences = %s.getSharedPreferences(%s.getPackageName() + \".%s\", %s);\n" +
                                "String accountName = sharedPreferences.getString(\"%s_%s\", %s%s.getAccountName());\n" +
                                "String password = sharedPreferences.getString(\"%s_%s\", %s%s.getPassword());\n" +
                                "String alias = sharedPreferences.getString(\"%s_%s\", %s%s.getAlias());\n" +
                                "for (AccountBean accountBean : (ArrayList<%s>)ACCOUNT_LIST) {\n" +
                                "    if (android.text.TextUtils.equals(accountBean.getAccountName(), accountName)\n" +
                                "           && android.text.TextUtils.equals(accountBean.getPassword(), password)\n" +
                                "                && android.text.TextUtils.equals(accountBean.getAlias(), alias)) {\n" +
                                "            %s = accountBean;\n" +
                                "            break;\n" +
                                "         }\n" +
                                "     }\n" +
                                "     if (%s == null) {\n" +
                                "         %s = %s%s;\n" +
                                "    }\n" +
                                "}\n"
                        ,VAR_CURRENT_ACCOUNT
                        ,VAR_CONTEXT,VAR_CONTEXT,ACCOUNT_SWITCHER_FILE_NAME.toLowerCase(),MODE_PRIVATE
                        ,ACCOUNT_SWITCHER_FILE_NAME,VAR_ACCOUNT_NAME_SUFFIX,VAR_DEFAULT_ACCOUNT_PREFIX,ACCOUNT
                        ,ACCOUNT_SWITCHER_FILE_NAME,VAR_ACCOUNT_PASS_WORD_SUFFIX,VAR_DEFAULT_ACCOUNT_PREFIX,ACCOUNT
                        ,ACCOUNT_SWITCHER_FILE_NAME,VAR_ACCOUNT_ALIAS_NAME_SUFFIX,VAR_DEFAULT_ACCOUNT_PREFIX,ACCOUNT
                        ,AccountBean.class.getSimpleName()
                        ,VAR_CURRENT_ACCOUNT
                        ,VAR_CURRENT_ACCOUNT
                        ,VAR_CURRENT_ACCOUNT,VAR_DEFAULT_ACCOUNT_PREFIX,ACCOUNT)
                )
                .addStatement("return "+VAR_CURRENT_ACCOUNT)
                .build();
        builder.addMethod(getAccountBeanMethod);

        //setAccountBeanMethod
        MethodSpec setAccountBeanMethod = MethodSpec.methodBuilder(METHOD_NAME_SET_CURRENT_ACCOUNT_BEAN)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(void.class)
                .addParameter(CONTEXT_TYPE_NAME, VAR_CONTEXT)
                .addParameter(AccountBean.class,VAR_NEW_ACCOUNT)
                .addStatement(String.format(
                        "%s.getSharedPreferences(%s.getPackageName() + \".%s\", %s).edit()\n" +
                                ".putString(\"%s_%s\", %s.getAccountName())\n" +
                                ".putString(\"%s_%s\", %s.getPassword())\n" +
                                ".putString(\"%s_%s\", %s.getAlias())\n" +
                                ".commit()"
                        ,VAR_CONTEXT,VAR_CONTEXT,ACCOUNT_SWITCHER_FILE_NAME.toLowerCase(),MODE_PRIVATE
                        ,ACCOUNT_SWITCHER_FILE_NAME,VAR_ACCOUNT_NAME_SUFFIX,VAR_NEW_ACCOUNT
                        ,ACCOUNT_SWITCHER_FILE_NAME,VAR_ACCOUNT_PASS_WORD_SUFFIX,VAR_NEW_ACCOUNT
                        ,ACCOUNT_SWITCHER_FILE_NAME,VAR_ACCOUNT_ALIAS_NAME_SUFFIX,VAR_NEW_ACCOUNT
                ))
                .addCode(String.format(
                        "if (!%s.equals(%s)) {\n" +
                                "    %s = %s;\n" +
                                "    onAccountChange(%s);\n" +
                                "}\n"
                        ,VAR_NEW_ACCOUNT,VAR_CURRENT_ACCOUNT
                        ,VAR_CURRENT_ACCOUNT,VAR_NEW_ACCOUNT
                        ,VAR_NEW_ACCOUNT
                )).build();
        builder.addMethod(setAccountBeanMethod);

        getAccountListBuilder.addStatement(String.format("return %s",ACCOUNT_LIST));
        builder.addMethod(getAccountListBuilder.build()).build();
        builder.addStaticBlock(staticCodeBlockBuilder.build());
        JavaFile switchEnvironmentJavaFile = JavaFile.builder(PACKAGE_NAME, builder.build()).build();
        try {
            switchEnvironmentJavaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected FieldSpec generateEnvironmentField(Account accountAnnotation,
                                                 FieldSpec.Builder defaultAccountFiledBuilder,
                                                 String accountUpperCaseName,
                                                 String accountName,
                                                 String password,
                                                 String alias){
        if (accountAnnotation.isDefault()) {
            defaultAccountFiledBuilder.initializer(String.format("%s%s", VAR_ACCOUNT_PREFIX,accountUpperCaseName));
        }

        return FieldSpec
                .builder(AccountBean.class,
                        String.format("%s%s",VAR_ACCOUNT_PREFIX,accountUpperCaseName),
                        Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(String.format("new %s(\"%s\",\"%s\",\"%s\")",
                        AccountBean.class.getSimpleName(), accountName, password,alias))
                .build();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Account.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
