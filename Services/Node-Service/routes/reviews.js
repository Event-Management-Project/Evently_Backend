const express = require('express');
const router = express.Router();
const Review = require('../models/review');

router.post('/customer', async (req, res) => {
    const { customerId } = req.body;

    if (!customerId || customerId.length < 1) {
        return res.status(400).json({ message: 'Invalid customer ID format' });
    }

    try {
        const reviews = await Review.find({ customerId });
        if (reviews.length === 0) {
            return res.status(404).json({ message: 'No reviews found for this customer' });
        }
        res.json(reviews);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

router.post('/event', async (req, res) => {
    const { eventId } = req.body;

    if (!eventId || eventId.length < 1) {
        return res.status(400).json({ message: 'Invalid event ID format' });
    }

    try {
        const reviews = await Review.find({ eventId });
        if (reviews.length === 0) {
            return res.status(404).json({ message: 'No reviews found for this event' });
        }
        res.json(reviews);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

router.post('/', async (req, res) => {
    const { customerId, eventId, subject, description, star } = req.body;

    if (!customerId || !eventId) {
        return res.status(400).json({ message: 'customerId and eventId are required in request body' });
    }

    try {
        const existingReview = await Review.findOne({ customerId, eventId });
        if (existingReview) {
            return res.status(400).json({ message: 'You have already added a review for this event.' });
        }

        const review = new Review({
            customerId,
            eventId,
            subject,
            description,
            star
        });

        const savedReview = await review.save();
        res.status(201).json(savedReview);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

router.patch('/', async (req, res) => {
    const { customerId, eventId, subject, description, star } = req.body;

    if (!customerId || !eventId) {
        return res.status(400).json({ message: 'Missing customerId or eventId in request body' });
    }

    try {
        const review = await Review.findOne({ customerId, eventId });

        if (!review) {
            return res.status(404).json({ message: 'Review not found for given customer and event' });
        }

        if (subject) review.subject = subject;
        if (description) review.description = description;
        if (star !== undefined) review.star = star;

        const updatedReview = await review.save();
        res.json(updatedReview);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

router.post('/check', async (req, res) => {
    const { customerId, eventId } = req.body;

    if (!customerId || !eventId) {
        return res.status(400).json({ message: 'customerId and eventId are required' });
    }

    try {
        const review = await Review.findOne({ customerId, eventId });

        if (review) {
            return res.status(200).json({ exists: true, review });
        } else {
            return res.status(200).json({ exists: false });
        }
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

module.exports = router;