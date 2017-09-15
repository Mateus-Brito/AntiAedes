package com.example.antiaedes;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.antiaedes.dao.UsuarioDao;
import com.example.antiaedes.entities.Usuario;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mNameView = (EditText) findViewById(R.id.register_name);
        mEmailView = (EditText) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
    }

    public void registerUser(View view) {
        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (!(mPasswordView.getText().toString().equals(mConfirmPasswordView.getText().toString()))) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            UsuarioDao usuarioDao = new UsuarioDao();
            Usuario usuario = new Usuario();
            usuario.setNome(name);
            usuario.setEmail(email);
            usuario.setSenha(encrypt(password));
            boolean register = usuarioDao.registerUser(usuario);

            if (register) {
                Usuario user = usuarioDao.login(usuario.getEmail(), usuario.getSenha());
                Session session = new Session(user);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("session", session);
                startActivity(intent);
            }
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public String encrypt(String password) {
        String passwordEncrypt = "";
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1, messageDigest.digest(password.getBytes()));
            passwordEncrypt = String.format("%32x", hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordEncrypt;
    }

}
