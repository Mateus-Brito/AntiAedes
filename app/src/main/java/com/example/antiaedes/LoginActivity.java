package com.example.antiaedes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antiaedes.dao.FuncionarioDao;
import com.example.antiaedes.dao.UsuarioDao;
import com.example.antiaedes.entities.Funcionario;
import com.example.antiaedes.entities.Usuario;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private RadioGroup mGroup;
    private RadioButton mNormalRadio,radio1,radio2;
    private RadioButton mFunctionaryRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // Set up the login form.
        radio1 = (RadioButton) findViewById(R.id.radioNormal);
                radio2 = (RadioButton) findViewById(R.id.radioFunctionary);
        mEmailView = (TextView) findViewById(R.id.email);
        mGroup = (RadioGroup) findViewById(R.id.radioGP);
        //populateAutoComplete();
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = mGroup.findViewById(checkedId);
                int index = mGroup.indexOfChild(radioButton);

                switch (index) {
                    case 0:
                        radioButton.setBackgroundColor(Color.parseColor("#3CB371"));
                        radio2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case 1:
                        radioButton.setBackgroundColor(Color.parseColor("#3CB371"));
                        radio1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                }

        }
    });

        mPasswordView = (EditText) findViewById(R.id.password);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mNormalRadio = (RadioButton) findViewById(R.id.radioNormal);
        mFunctionaryRadio = (RadioButton) findViewById(R.id.radioFunctionary);
    }

    /** private void populateAutoComplete() {
     if (!mayRequestContacts()) {
     return;
     }

     getLoaderManager().initLoader(0, null, this);
     }

     private boolean mayRequestContacts() {
     if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
     return true;
     }
     if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
     return true;
     }
     if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
     Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
     .setAction(android.R.string.ok, new View.OnClickListener() {
    @Override
    @TargetApi(Build.VERSION_CODES.M) public void onClick(View v) {
    requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
    }
    });
     } else {
     requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
     }
     return false;
     }*/

    /**
     * Callback received when a permissions request has been completed.

     @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
     @NonNull int[] grantResults) {
     if (requestCode == REQUEST_READ_CONTACTS) {
     if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
     populateAutoComplete();
     }
     }
     }*/


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            int userType = mNormalRadio.isChecked() ? 0 : 1;
            mAuthTask = new UserLoginTask(email, password, userType);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final int mUserType;
        private Usuario mUsuario;
        private Funcionario mFuncionario;

        UserLoginTask(String email, String password, int userType) {
            mEmail = email;
            mPassword = password;
            mUserType = userType;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            if (mUserType == 0) {
                UsuarioDao usuarioDao = new UsuarioDao();
                mUsuario = usuarioDao.login(mEmail, encrypt(mPassword));
                if (mUsuario != null) return true;
            } else {
                FuncionarioDao funcionarioDao = new FuncionarioDao();
                mFuncionario = funcionarioDao.searchFunctionary(mEmail, encrypt(mPassword));
                if (mFuncionario != null) return true;
            }
            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Session session = makeSession();
                Intent intent;
                if (mUserType == 0)
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                else
                    intent = new Intent(LoginActivity.this, MainFunctionaryActivity.class);
                intent.putExtra("session", session);
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
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

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        public Session makeSession() {
            Session session = new Session();
            if (mUsuario != null) {
                session.setId(mUsuario.getId());
                session.setNome(mUsuario.getNome());
                session.setEmail(mUsuario.getEmail());
                session.setSaldo(mUsuario.getSaldo());
                return session;
            } else {
                session.setId(mFuncionario.getId());
                session.setCpf(mFuncionario.getCpf());
                session.setNome(mFuncionario.getNome());
                session.setEmail(mFuncionario.getEmail());
                return session;
            }
        }
    }
}

