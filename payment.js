const express = require('express');
const stripe = require('stripe')('your_stripe_secret_key');
const bodyParser = require('body-parser');

const app = express();

// Middleware to parse JSON bodies
app.use(bodyParser.json());

// Endpoint to handle Stripe token from client-side
app.post('/process-payment', async (req, res) => {
    const token = req.body.token;
    const amount = 1000; 
    
    try {
        // Create charge using the token
        const charge = await stripe.charges.create({
            amount: amount,
            currency: 'ksh',
            source: token,
            description: 'Example Charge'
        });

        // Handle successful charge
        res.status(200).json({ success: true, message: 'Payment successful' });
    } catch (error) {
        // Handle error
        res.status(500).json({ success: false, message: error.message });
    }
});

// Start the server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
