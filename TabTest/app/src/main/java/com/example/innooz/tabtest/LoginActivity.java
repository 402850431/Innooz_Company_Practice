package com.example.innooz.tabtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private SignInButton googleLoginButton;
    private static int RC_SIGN_IN=10001;
//    private TextView tv_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 配置登錄以請求用戶的ID，電子郵件地址和基本個人資料
        // ID和基本個人資料 包含在 DEFAULT_SIGN_IN 中
        GoogleSignInOptions gso = new GoogleSignInOptions .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();

        // 建立一個可以訪問Google登錄API的GoogleApiClient gso指定的選項
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleLoginButton = (SignInButton)findViewById(R.id.sign_in_button);
        // 設置登錄按鈕的尺寸
        googleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        googleLoginButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Log.i(" ", " 點擊google login button ");
                signIn();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("啟動Intent返回的結果", "requestCode==" + requestCode + ",resultCode==" + resultCode + ",data==" + data);

        //從GoogleSignInApi.getSignInIntent(...)啟動Intent返回的結果
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.i("登入是否成功", "handleSignInResult:" + result.isSuccess() + "\nStatus :" + result.getStatus());
        if (result.isSuccess()) {
            Log.i(" ", "成功");
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                Toast.makeText(this, "用戶名是:" + acct.getDisplayName()
                        +"\n用戶email是:" + acct.getEmail()
                        +"\n用戶頭像是:" + acct.getPhotoUrl()
                        +"\n用戶Id是: " + acct.getId()
                        +"\n用戶IdToken是: " + acct.getIdToken()
                        , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            } else {
                // Signed out, show unauthenticated UI.
                Log.i(" ", "擷取資料失敗" + result.getStatus());
                Toast.makeText(this, "擷取資料失敗", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Log.i(" ", "失敗");
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    public void LoginButton(View view)
//    {
//        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(" ","google登錄-->onConnected,bundle=="+bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(" ","google登錄-->onConnectionSuspended,i=="+i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(" ","google登錄-->onConnectionFailed,connectionResult=="+connectionResult);
    }

}
