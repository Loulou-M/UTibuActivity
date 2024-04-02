import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatementActivity extends AppCompatActivity {
    private TextView textViewStatement;
    private Button buttonRefresh;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        textViewStatement = findViewById(R.id.textViewStatement);
        buttonRefresh = findViewById(R.id.buttonRefresh);

        // Initialize Retrofit service
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Fetch initial statement data
        fetchStatementData();

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh statement data
                fetchStatementData();
            }
        });
    }

    private void fetchStatementData() {
        // Call API to fetch statement data
        apiService.getStatementData().enqueue(new Callback<StatementResponse>() {
            @Override
            public void onResponse(Call<StatementResponse> call, Response<StatementResponse> response) {
                if (response.isSuccessful()) {
                    // Update textViewStatement with fetched statement data
                    StatementResponse statementResponse = response.body();
                    if (statementResponse != null && statementResponse.getData() != null) {
                        textViewStatement.setText(statementResponse.getData());
                    }
                } else {
                    // Handle failure to fetch statement data
                    textViewStatement.setText("Failed to fetch statement data");
                }
            }
            
            @Override
            public void onFailure(Call<StatementResponse> call, Throwable t) {
                // Handle network or server error
                textViewStatement.setText("Failed to fetch statement data: " + t.getMessage());
            }
        });
    }
}