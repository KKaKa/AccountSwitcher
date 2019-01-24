package com.kkaka.accountswitchbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kkaka.base.bean.AccountBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.kkaka.base.Constants.ACCOUNT_SWITCHER_FILE_NAME;
import static com.kkaka.base.Constants.METHOD_NAME_GET_ACCOUNT;
import static com.kkaka.base.Constants.METHOD_NAME_SET_CURRENT_ACCOUNT_BEAN;
import static com.kkaka.base.Constants.PACKAGE_NAME;

public class AccountSwitcherBoxActivity extends AppCompatActivity {
    private List<AccountBean> accounts = new ArrayList<>();
    private Adapter adapter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_switcher_box);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(AccountSwitcherBoxActivity.this,"Github",Toast.LENGTH_LONG).show();
                return true;
            }
        });

        try {
            Class<?> accountSwitcherClass = Class.forName(PACKAGE_NAME+"."+ACCOUNT_SWITCHER_FILE_NAME);
            Method getAccountConfigMethod = accountSwitcherClass.getMethod(METHOD_NAME_GET_ACCOUNT);
            this.accounts = (ArrayList<AccountBean>) getAccountConfigMethod.invoke(accountSwitcherClass.newInstance());

            String currentName = "";
            AccountBean currentAccount = null;

            for (AccountBean bean : this.accounts) {
                if (!TextUtils.equals(bean.getAccountName(), currentName) || currentAccount == null) {
                    currentName = bean.getAccountName();
                    Method getCurrentAccountBeanMethod = accountSwitcherClass.getMethod("getCurrentAccountBean", Context.class);
                    currentAccount = (AccountBean) getCurrentAccountBeanMethod.invoke(accountSwitcherClass.newInstance(), this);
                }
                bean.setChecked(currentAccount.equals(bean));
            }
            ListView listView = findViewById(R.id.list_view);
            adapter = new Adapter();
            listView.setAdapter(adapter);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void toAccountSwitcherBoxActivity(Context context){
        Intent intent = new Intent(context,AccountSwitcherBoxActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return accounts.size();
        }

        @Override
        public AccountBean getItem(int position) {
            return accounts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final AccountBean accountBean = getItem(position);
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
            TextView tvName = convertView.findViewById(R.id.tv_account_name);
            tvName.setText(accountBean.getAlias());
            TextView tvAlias = convertView.findViewById(R.id.tv_account_alias);
            tvAlias.setText(accountBean.getAccountName());
            ImageView iv = convertView.findViewById(R.id.iv);
            iv.setVisibility(accountBean.isChecked() ? View.VISIBLE : View.INVISIBLE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Class<?> accountSwitcher = Class.forName(PACKAGE_NAME + "." + ACCOUNT_SWITCHER_FILE_NAME);
                        Method method = accountSwitcher.getMethod(METHOD_NAME_SET_CURRENT_ACCOUNT_BEAN,Context.class,AccountBean.class);
                        method.invoke(accountSwitcher.newInstance(),AccountSwitcherBoxActivity.this,accountBean);
                        for(AccountBean bean : accounts){
                            bean.setChecked(bean.equals(accountBean));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });

            return convertView;
        }
    }
}
