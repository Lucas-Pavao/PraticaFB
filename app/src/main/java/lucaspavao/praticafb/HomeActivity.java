package lucaspavao.praticafb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lucaspavao.praticafb.model.Message;
import lucaspavao.praticafb.model.User;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;
    ViewGroup vgChat;

    DatabaseReference drUser;
    DatabaseReference drChat;

    User user;

    TextView txWelcome;
    TextView edMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);
        vgChat = findViewById(R.id.chat_area);
        txWelcome = findViewById(R.id.text_welcome);
        edMessage = findViewById(R.id.edit_message);

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        drUser = fbDB.getReference("users/" + fbUser.getUid());
        drChat = fbDB.getReference("chat");
        drUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User tempUser = dataSnapshot.getValue(User.class);
                if (tempUser != null) {
                    HomeActivity.this.user = tempUser;
                    txWelcome.setText("Welcome " + tempUser.getName() + "!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        drChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                showMessage(message);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
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


    public void buttonSignOutClick(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut();
//            this.finish();
        } else {
            Toast.makeText(HomeActivity.this, "Erro!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(Message message) {
        TextView tvMsg = new TextView(this);
        tvMsg.setText(message.getName() + ": " + message.getText());
        vgChat.addView(tvMsg);
    }

    public void buttonSendMessageClick (View view){
        String message = edMessage.getText().toString();
        edMessage.setText("");
        drChat.push().setValue(new Message(user.getName(), message));
    }

}