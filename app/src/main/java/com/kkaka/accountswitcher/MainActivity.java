package com.kkaka.accountswitcher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kkaka.accountswitch.AccountSwitcherBoxActivity;
import com.kkaka.base.AccountSwitcher;
import com.kkaka.base.bean.AccountBean;
import com.kkaka.base.listener.OnAccountChangeListener;

public class MainActivity extends AppCompatActivity {
    private TextView tvName;
    private TextView tvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tv_account_name);
        tvPassword = findViewById(R.id.tv_password);

        AccountSwitcher.addAccountChangeListener(new OnAccountChangeListener() {
            @Override
            public void onAccountChange(AccountBean account) {
                tvName.setText(account.getAccountName());
                tvPassword.setText(account.getPassword());
            }
        });

        AccountBean currentAccountBean = AccountSwitcher.getCurrentAccountBean(this);
        tvName.setText(currentAccountBean.getAccountName());
        tvPassword.setText(currentAccountBean.getPassword());


        FloatingActionButton button = findViewById(R.id.floatingActionButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountSwitcherBoxActivity.toAccountSwitcherBoxActivity(MainActivity.this);
            }
        });
    }
}
