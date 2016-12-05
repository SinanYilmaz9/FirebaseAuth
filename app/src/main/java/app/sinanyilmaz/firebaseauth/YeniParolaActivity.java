package app.sinanyilmaz.firebaseauth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;



public class YeniParolaActivity extends AppCompatActivity {

    private EditText email;
    private Button yeniParolaGonder;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeni_parola_layout);

        email = (EditText) findViewById(R.id.uyeEmail);
        yeniParolaGonder = (Button) findViewById(R.id.yeniParolaGonder);


        //FirebaseAuth sınıfının referans olduğu nesneleri kullanabilmek için getInstance methodunu kullanıyoruz.
        auth = FirebaseAuth.getInstance();


        yeniParolaGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = email.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(getApplication(), "Lütfen email adresinizi giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }


                //FirebaseAuth sınıfı üzerinden sendPasswordResetEmail nesnesi ile girilen maile parola sıfırlama bağlantısı gönderebiliyoruz.
                auth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(YeniParolaActivity.this, "Yeni parola için gerekli bağlantı adresinize gönderildi!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(YeniParolaActivity.this, "Mail gönderme hatası!", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
            }
        });
    }

}

