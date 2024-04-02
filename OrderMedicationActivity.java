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

public class OrderMedicationActivity extends AppCompatActivity {
    private EditText editTextMedicationName;
    private EditText editTextQuantity;
    private Button buttonOrder;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_medication);

        editTextMedicationName = findViewById(R.id.editTextMedicationName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        buttonOrder = findViewById(R.id.buttonOrder);

        // Initialize Retrofit service
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicationName = editTextMedicationName.getText().toString().trim();
                String quantity = editTextQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(medicationName) || TextUtils.isEmpty(quantity)) {
                    Toast.makeText(OrderMedicationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create order request
                    OrderRequest request = new OrderRequest(medicationName, Integer.parseInt(quantity));

                    // Call API to place order
                    apiService.placeOrder(request).enqueue(new Callback<OrderResponse>() {
                        @Override
                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                            if (response.isSuccessful()) {
                                // Order successful
                                OrderResponse orderResponse = response.body();
                                if (orderResponse != null && orderResponse.isSuccess()) {
                                    Toast.makeText(OrderMedicationActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OrderMedicationActivity.this, "Order failed: " + orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Order failed due to server error
                                Toast.makeText(OrderMedicationActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderResponse> call, Throwable t) {
                            // Order failed due to network error
                            Toast.makeText(OrderMedicationActivity.this, "Failed to place order: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}