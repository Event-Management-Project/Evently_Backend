const express = require('express');
const router = express.Router();
const Review = require('../models/review');

// GET grievances by customerId
router.get('/customer', async (req, res) => {
    const customerId = req.headers['customerid'];
    if (!customerId || customerId.length < 1) {
        return res.status(400).json({ message: 'Invalid customer ID format' });
    }

    try {
        const reviews = await Review.find({ customerId: customerId });
        if (reviews.length === 0) {
            return res.status(404).json({ message: 'No reviews found for this customer' });
        }
        res.json(reviews);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

router.get('/event', async(req,res) =>{
    const eventId = req.headers['eventid'];
    // Postman allows you to send headers with a key like eventId (with a capital "I"), and it will automatically map 
    // it to lowercase in the req.headers object when received by the server.

    // In JavaScript (specifically with Node.js and Express), the req.headers object is case-insensitive for HTTP headers 
    // as per the HTTP standard, but it automatically converts the header names to lowercase.
    if (!eventId || eventId.length < 1) {
        return res.status(400).json({ message: 'Invalid event ID format' });
    }

    try {
        const reviews = await Review.find({ eventId: eventId });
        if (reviews.length === 0) {
            return res.status(404).json({ message: 'No reviews found for this customer' });
        }
        res.json(reviews);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
})

// POST a review
router.post('/', async (req, res) => {
    const customerId = req.headers['customerid'];
    const eventId = req.headers['eventid'];

    if (!customerId || !eventId) {
        return res.status(400).json({ message: 'customerId and eventId are required in headers' });
    }

    // Create a new review
    const review = new Review({
        customerId: customerId,
        eventId: eventId,
        subject: req.body.subject,
        description: req.body.description,
        star: req.body.star
    });

    try {
        const savedReview = await review.save();
        res.status(201).json(savedReview);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

router.patch('/', async (req, res) => {
    const customerId = req.headers['customerid'];
    const eventId = req.headers['eventid'];

    if (!customerId || !eventId) {
        return res.status(400).json({ message: 'Missing customerId or eventId in headers' });
    }

    try {
        // Find the grievance with both customerId and eventId
        const review = await Review.findOne({ customerId: customerId, eventId: eventId });

        if (!review) {
            return res.status(404).json({ message: 'Review not found for given customer and event' });
        }

        // Update fields from body
        if (req.body.subject) review.subject = req.body.subject;
        if (req.body.description) review.description = req.body.description;
        if (req.body.star) review.star = req.body.star;

        const updatedReview = await review.save();
        res.json(updatedReview);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});


module.exports = router;