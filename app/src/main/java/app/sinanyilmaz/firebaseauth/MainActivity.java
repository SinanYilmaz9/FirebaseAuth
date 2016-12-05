package app.sinanyilmaz.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button kullaniciSil, cikisYap;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kullaniciSil = (Button) findViewById(R.id.kullaniciSil);
        textView = (TextView) findViewById(R.id.text);
        cikisYap = (Button) findViewById(R.id.cikis_yap);

        //FirebaseAuth sınıfının referans olduğu nesneleri kullanabilmek için getInstance metodunu kullanıyoruz.
        auth = FirebaseAuth.getInstance();

        //Bir AuthStateListener dinleyicisi oluşturuyoruz ve bu dinleyici onAuthStateChanged bölümünü çalıştırır.
        // Buradaki getCurrentUser metodu ile kullanıcı verilerine ulaşabilmekteyiz.
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // Eğer geçerli bir kullanıcı oturumu yoksa LoginActivity e geçilir.
                // Oturum kapatma işlemi yapıldığında bu sayede LoginActivity e geçilir.
                if (user == null) {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        //getCurrentUser metodu üzerinden ulaştığımız kullanıcı verilerinde getEmail ile de kullanıcının mailini kullanarak,
        // kullanıcıya bir text gösteriyoruz.
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        textView.setText("Merhaba, " + user.getEmail());


        kullaniciSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //Silme işlemi başarılı oldugunda kullanıcıya bir mesaj gösterilip UyeOlActivity e geçiliyor.
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Hesabın silindi.Yeni bir hesap oluştur!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, UyeOlActivity.class));
                                        finish();

                                    } else {
                                        //İşlem başarısız olursa kullanıcı bilgilendiriliyor.
                                        Toast.makeText(MainActivity.this, "Hesap silinemedi!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });

        cikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FirebaseAuth.getInstance().signOut ile oturumu kapatabilmekteyiz.
                auth.signOut();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
