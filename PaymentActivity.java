import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private CardInputWidget cardInputWidget;
    private Button buttonPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize Stripe
        PaymentConfiguration.init(getApplicationContext(), "your_stripe_publishable_key");

        cardInputWidget = findViewById(R.id.cardInputWidget);
        buttonPay = findViewById(R.id.buttonPay);

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Card object from the card input widget
                Card card = cardInputWidget.getCard();
                if (card != null) {
                    // Validate the card details
                    if (!card.validateCard()) {
                        Toast.makeText(PaymentActivity.this, "Invalid card details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create a token from the card
                    Stripe stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance().getPublishableKey());
                    stripe.createToken(
                            card,
                            new TokenCallback()
                    );
                } else {
                    Toast.makeText(PaymentActivity.this, "Card details are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class TokenCallback implements Callback<Token> {
        @Override
        public void onResponse(Call<Token> call, Response<Token> response) {
            if (response.isSuccessful()) {
                Token token = response.body();
                if (token != null) {
                    // Send the token to server for processing
                    sendTokenToServer(token.getId());
                } else {
                    Toast.makeText(PaymentActivity.this, "Token is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PaymentActivity.this, "Failed to create token: " + response.message(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Token> call, Throwable t) {
            Toast.makeText(PaymentActivity.this, "Failed to create token: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendTokenToServer(String tokenId) {
        // Implementing server-side integration 
        String serverUrl = "https://your-server-url.com/process-payment";
        
        Toast.makeText(this, "Token sent to server: " + tokenId, Toast.LENGTH_SHORT).show();
    }
}
