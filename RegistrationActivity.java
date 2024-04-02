import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Initialize Retrofit service
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create registration request
                    RegistrationRequest request = new RegistrationRequest(username, password);

                    // Call API to register user
                    apiService.registerUser(request).enqueue(new Callback<RegistrationResponse>() {
                        @Override
                        public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                            if (response.isSuccessful()) {
                                // Registration successful
                                RegistrationResponse registrationResponse = response.body();
                                if (registrationResponse != null && registrationResponse.isSuccess()) {
                                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    
                                } else {
                                    Toast.makeText(RegistrationActivity.this, "Registration failed: " + registrationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Registration failed due to server error
                                Toast.makeText(RegistrationActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                            // Registration failed due to network error
                            Toast.makeText(RegistrationActivity.this, "Failed to register user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
