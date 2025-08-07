const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

require('dotenv').config();
require('./utils/eureka-client');

const reviewRouter = require('./routes/reviews');
const notificationRouter = require('./routes/notification')

const app = express();
const PORT = process.env.PORT || 5000;


// Connect to MongoDB
mongoose.connect(process.env.MONGO_URI,)
    .then(() => console.log('Connected to MongoDB...'))
    .catch(err => console.log('Failed to connect to MongoDB:', err));

app.use(cors({
  origin: 'http://localhost:5173',
  credentials: true,
}));


app.use(express.json()); // Middleware to parse JSON

app.use('/reviews', reviewRouter);
app.use('/notification', notificationRouter);

app.listen(4000, '0.0.0.0', () => {
    console.log('Server started on port 4000');
});