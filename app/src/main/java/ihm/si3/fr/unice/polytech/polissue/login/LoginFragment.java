package ihm.si3.fr.unice.polytech.polissue.login;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ihm.si3.fr.unice.polytech.polissue.R;

/**
 * {@link Fragment} subclass to handle the login method selection
 * Activities that contain this fragment must implement the
 * {@link LoginFragmentListener} interface to handle the login method being selected
 */
public class LoginFragment extends Fragment {

    private LoginFragmentListener mListener;

    private AutoCompleteTextView emailTextView;
    private TextView passwordTextView;
    private Button forgotPassword;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private static String TAG = "LoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_login_selector, container, false);


        ImageButton googleButton = mainView.findViewById(R.id.google_login);
        googleButton.setOnClickListener((View view) -> {
            if (mListener != null)
                mListener.methodSelected(LoginFragmentListener.LoginMethod.GOOGLE);
        });

        ImageButton facebookButton = mainView.findViewById(R.id.facebook_login);
        facebookButton.setOnClickListener((View view) -> {
            if (mListener != null)
                mListener.methodSelected(LoginFragmentListener.LoginMethod.FACEBOOK);
        });

        Button signUpButton = mainView.findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener((View view) -> {
            if (mListener != null) mListener.toSignUp("", "");
        });

        emailTextView = mainView.findViewById(R.id.email);
        passwordTextView = mainView.findViewById(R.id.password);
        passwordTextView.setOnEditorActionListener(
                (TextView v, int actionId, KeyEvent event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        login();
                        return true;
                    }
                    return false;
                }
        );

        Button login = mainView.findViewById(R.id.log_in);
        login.setOnClickListener((View v) -> login());

        Button signup = mainView.findViewById(R.id.sign_up_button);
        signup.setOnClickListener((View v) -> {
            if (mListener != null)
                mListener.toSignUp(emailTextView.getText().toString(), passwordTextView.getText().toString());
        });

        forgotPassword = mainView.findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(v -> passwordPopUp());
        return mainView;
    }

    private void passwordPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.forgot_password));


        final EditText passwordEmail = new EditText(getContext());
        passwordEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        passwordEmail.setHint(R.string.prompt_email);
        String emailText = emailTextView.getText().toString();
        if (!emailText.isEmpty()) passwordEmail.setText(emailText);
        builder.setView(passwordEmail);

        builder.setPositiveButton(getString(R.string.change), (dialog, which) -> {
                    auth.sendPasswordResetEmail(passwordEmail.getText().toString())
                            .addOnSuccessListener(t -> Toast.makeText(getContext(), getString(R.string.email_sent), Toast.LENGTH_LONG).show())
                            .addOnFailureListener(t -> Toast.makeText(getContext(), getString(R.string.error_invalid_email), Toast.LENGTH_LONG).show());
                }
        );
        builder.setNegativeButton(R.string.dialog_negative_button, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void login() {
        String emailText = this.emailTextView.getText().toString();
        String passwordText = this.passwordTextView.getText().toString();
        if (validateLogin()) {
            auth.signOut();
            auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this::loginAckowledge);
        }
    }

    /**
     * Check the form for errors, return false if data are not valid
     *
     * @return true if user can login, false otherwise.
     */
    private boolean validateLogin() {
        String email = this.emailTextView.getText().toString();
        String password = this.passwordTextView.getText().toString();

        boolean valid = true;
        String error = "";
        if (email.isEmpty()) error = getString(R.string.error_field_required);
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            error = getString(R.string.error_invalid_email);

        if (!error.isEmpty()) {
            emailTextView.setError(error);
            valid = false;
        }

        error = "";
        if (password.isEmpty()) error = getString(R.string.error_field_required);
        if (!error.isEmpty()) {
            passwordTextView.setError(error);
            valid = false;
        }
        return valid;
    }

    private void loginAckowledge(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "createUserWithEmail:success");
            mListener.done();
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            Toast.makeText(getContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentListener) {
            mListener = (LoginFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement LoginFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
