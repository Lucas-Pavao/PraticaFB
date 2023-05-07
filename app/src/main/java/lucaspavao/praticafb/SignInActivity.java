package lucaspavao.praticafb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    TextView edPassword;
    TextView edEmail ;
    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        edPassword = findViewById(R.id.edit_password_signIn);
        edEmail = findViewById(R.id.edit_email_signIn);
        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }




    public void buttonSignInClick(View view) {
        String login = edEmail.getText().toString();
        String passwd = edPassword.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(login, passwd)
                .addOnCompleteListener(this, task -> {
                    String msg = task.isSuccessful() ? "SIGN IN OK!": "SIGN IN ERROR!";
                    Toast.makeText(SignInActivity.this, msg,
                            Toast.LENGTH_SHORT).show();
                });
//        Intent itent = new Intent(this, HomeActivity.class);
//        startActivity(itent);
    }

    public void telaCadastro(View view){
        Intent itent = new Intent(this, SignUpActivity.class);
        startActivity(itent);
    }

}