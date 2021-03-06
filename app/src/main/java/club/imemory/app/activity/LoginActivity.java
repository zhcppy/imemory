package club.imemory.app.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.tauth.Tencent;

import org.litepal.crud.DataSupport;

import club.imemory.app.R;
import club.imemory.app.db.User;
import club.imemory.app.listener.LoginListener;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.RegexUtils;
import club.imemory.app.util.SnackbarUtil;

/**
 * 实现手机号与密码登录
 *
 * @Author: 张杭
 * @Date: 2017/3/25 12:12
 */

public class LoginActivity extends BaseActivity {

    /**
     * 启动LoginActivity
     */
    public static void actionStart(Context context, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, sharedElements).toBundle());
    }

    private CoordinatorLayout coordinator;
    private TextInputLayout mPhoneText;
    private TextInputLayout mPasswordText;
    private View mProgressView;
    private View mLoginFormView;
    private View logo;
    private Button loginBtn;
    private Tencent mTencent; //qq主操作对象
    private LoginListener mLoginListener; //授权登录监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button mQQLoginBtn = (Button) findViewById(R.id.btn_QQ);
        Button mWeiBoLoginBtn = (Button) findViewById(R.id.btn_weibo);
        Button mRegisterBtn = (Button) findViewById(R.id.btn_register);
        Button mForgetBtn = (Button) findViewById(R.id.btn_forget);
        logo = findViewById(R.id.image_logo);
        loginBtn = (Button) findViewById(R.id.btn_login);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPhoneText = (TextInputLayout) findViewById(R.id.text_input_layout_phone);
        mPasswordText = (TextInputLayout) findViewById(R.id.text_input_layout_password);
        mPhoneText.setHint("手机号码");
        mPasswordText.setHint("密码");
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin();
            }
        });

        mPhoneText.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为失去焦点时的处理内容
                    if (RegexUtils.isMobileExact(((TextView) v).getText().toString().trim())) {
                        mPhoneText.setError(null);
                        mPhoneText.setErrorEnabled(false);
                    } else {
                        mPhoneText.setError("手机号码不正确");
                    }
                }
            }
        });

        //响应软件盘上的事件
        mPhoneText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (RegexUtils.isMobileExact(v.getText().toString().trim())) {
                    mPhoneText.setError(null);
                    mPhoneText.setErrorEnabled(false);
                    return false;
                } else {
                    mPhoneText.setError("手机号码不正确");
                    return true;
                }
            }
        });
        mPasswordText.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                validateLogin();
                return false;
            }
        });

        mRegisterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });

        mForgetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
                AppManager.showToast("忘记密码就重新注册一个呗");
            }
        });

        mQQLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLogin();
            }
        });

        mWeiBoLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.showToast("该功能没实现,试试QQ登录");
                SnackbarUtil.ShortSnackbar(coordinator, "该功能没实现,试试QQ登录", 0).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final User user = DataSupport.findLast(User.class);
                if (user != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPhoneText.getEditText().setText(user.getPhone());
                            mPasswordText.getEditText().setText(user.getPassword());
                        }
                    });
                }
            }
        }).start();
    }

    private  void gotoRegister(){
        mPhoneText.setError(null);
        mPasswordText.setError(null);
        mPhoneText.setErrorEnabled(false);
        mPasswordText.setErrorEnabled(false);
        RegisterActivity.actionStart(LoginActivity.this,
                Pair.create(logo, "logo"),
                Pair.create(((View)loginBtn), "btn_logo_register"));
    }

    private void qqLogin() {
        mTencent = Tencent.createInstance("1106090620", LoginActivity.this);
        mLoginListener = new LoginListener(mTencent, mHandler);
        //调用QQ登录，用IUiListener对象作参数
        if (!mTencent.isSessionValid()) {
            mTencent.login(LoginActivity.this, "all", mLoginListener);
        }
    }

    /**
     * QQ登录成功后跳转到主界面
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                AppManager.showToast("QQ授权成功");
                RegisterActivity.actionStart(LoginActivity.this);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, mLoginListener);
    }

    /**
     * 尝试登录，对数据进行验证
     */
    private void validateLogin() {
        // 重置错误
        mPhoneText.setError(null);
        mPasswordText.setError(null);
        mPhoneText.setErrorEnabled(false);
        mPasswordText.setErrorEnabled(false);

        String phone = mPhoneText.getEditText().getText().toString().trim();
        String password = mPasswordText.getEditText().getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        //验证密码
        if (TextUtils.isEmpty(password) && password.length() < 6) {
            mPasswordText.setError("密码错误");
            focusView = mPasswordText;
            cancel = true;
        }

        // 验证手机号
        if (TextUtils.isEmpty(phone)) {
            mPhoneText.setError("手机号不能为空");
            focusView = mPhoneText;
            cancel = true;
        } else if (!RegexUtils.isMobileExact(phone)) {
            mPhoneText.setError("手机号不正确");
            focusView = mPhoneText;
            cancel = true;
        }

        if (cancel) {
            //有错误，停止登录
            focusView.requestFocus();
        } else {
            showProgress(true); // 显示进度条
            new UserLoginTask().execute(phone, password);
        }
    }

    public class UserLoginTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return 0;
            }
            User user = DataSupport.findLast(User.class);
            if (user == null) {
                return 0;
            } else if (params[0].equals(user.getPhone()) && params[1].equals(user.getPassword())) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            showProgress(false);

            switch (result) {
                case 0:
                    SnackbarUtil.ShortSnackbar(coordinator, "账号不存在请先注册", SnackbarUtil.Alert).show();
                    break;
                case 1:
                    AppManager.showToast("登录成功");
                    finishAfterTransition();
                    break;
                case 2:
                    SnackbarUtil.ShortSnackbar(coordinator, "账号或密码错误", SnackbarUtil.Alert).show();
                    mPhoneText.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    /**
     * 显示进度UI和隐藏登录表单
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}