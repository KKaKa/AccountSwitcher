package com.kkaka.base;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

public interface Constants {
    String ACCOUNT_SWITCHER_FILE_NAME = "AccountSwitcher";
    String PACKAGE_NAME = "com.kkaka.base";

    String GROUP = "GROUP";
    String ACCOUNT = "ACCOUNT";
    String VAR_CURRENT_ACCOUNT = "CURRENT_ACCOUNT";
    String VAR_NEW_ACCOUNT = "newAccount";

    String MODE_PRIVATE = "android.content.Context.MODE_PRIVATE";

    /*********************************
     *          LIST                 *
     *********************************/
    String ON_ACCOUNT_CHANGE_LISTENER_LIST = "ON_ACCOUNT_CHANGE_LISTENER_LIST";
    String GROUPS = "GROUPS";
    String ACCOUNT_LIST = "ACCOUNT_LIST";

    /*********************************
     *          METHOD               *
     *********************************/
    String METHOD_NAME_GET_ACCOUNT = "getAccountList";
    String METHOD_NAME_ADD_ACCOUNT_CHANGE_LISTENER = "addAccountChangeListener";
    String METHOD_NAME_REMOVE_ACCOUNT_CHANGE_LISTENER = "removeAccountChangeListener";
    String METHOD_NAME_REMOVE_ALL_ACCOUNT_CHANGE_LISTENERS = "removeAllAccountChangeListeners";
    String METHOD_NAME_ON_ACCOUNT_CHANGE = "onAccountChange";
    String METHOD_NAME_GET_GROUPS = "getGroups";
    String METHOD_NAME_GET_CURRENT_ACCOUNT_BEAN = "getCurrentAccountBean";
    String METHOD_NAME_SET_CURRENT_ACCOUNT_BEAN = "setCurrentAccountBean";

    /*********************************
     *          SUFFIX               *
     *********************************/
    String VAR_ACCOUNT_NAME_SUFFIX = "AccountName";
    String VAR_ACCOUNT_PASS_WORD_SUFFIX = "Password";
    String VAR_ACCOUNT_ALIAS_NAME_SUFFIX = "ALIAS";
    String VAR_DEFAULT_ACCOUNT_SUFFIX = "_ACCOUNT";
    /*********************************
     *          PREFIX               *
     *********************************/
    String VAR_GROUP_PREFIX = "GROUP_";
    String VAR_ACCOUNT_PREFIX = "ACCOUNT_";
    String VAR_DEFAULT_ACCOUNT_PREFIX = "DEFAULT_";

    /*********************************
     *          Params               *
     *********************************/
    String PARAM_ON_ACCOUNT_CHANGE_LISTNER = "onAccountChangeListener";
    String PARAM_ACCOUNT_BEAN = "accountBean";
    String PARAMETER_IS_DEBUG = "isDebug";

    TypeName CONTEXT_TYPE_NAME = ClassName.get("android.content", "Context");
    String VAR_CONTEXT = "context";



}
