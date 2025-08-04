const express = require('express');
const router = express.Router();
const Notification = require('../models/notification');

// POST a notification
router.post('/customer', async (req, res) => {
    const customerId = req.headers['customerid'];

    if (!customerId) {
        return res.status(400).json({ message: 'customerId is required in headers' });
    }

    // Create a new review
    const notification = new Notification({
        userId: customerId,
        subject: req.body.subject,
        description: req.body.description,
        role: 'Customer'
    });

    try {
        const savedNotification = await notification.save();
        res.status(201).json({message: 'Notification added successfully'});
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

router.post('/organiser', async (req, res) => {
    const organiserId = req.headers['organiserid'];

    if (!organiserId) {
        return res.status(400).json({ message: 'organiserId is required in headers' });
    }

    // Create a new review
    const notification = new Notification({
        userId: organiserId,
        subject: req.body.subject,
        description: req.body.description,
        role: 'Organiser'
    });

    try {
        await notification.save();
        res.status(201).json({ message: 'Notification added successfully' });
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

router.get('/getNotificationCustomer', async(req, res) =>{
    const customerId = req.headers['customerid'];
        if (!customerId || customerId.length < 1) {
            return res.status(400).json({ message: 'Invalid customer ID format' });
        }
    
        try {
            const notification = await Notification.find({ userId: customerId, role: "Customer" });
            if (notification.length === 0) {
                return res.status(404).json({ message: 'No Notification found for this customer' });
            }
            res.json(notification);
        } catch (err) {
            res.status(500).json({ message: err.message });
        }
})

router.get('/getNotificationOrganiser', async(req, res) =>{
    const organiserId = req.headers['organiserid'];
        if (!organiserId || organiserId.length < 1) {
            return res.status(400).json({ message: 'Invalid customer ID format' });
        }
    
        try {
            const notification = await Notification.find({ userId: organiserId, role: "Organiser" });
            if (notification.length === 0) {
                return res.status(404).json({ message: 'No Notification found for this customer' });
            }
            res.json(notification);
        } catch (err) {
            res.status(500).json({ message: err.message });
        }
})


module.exports = router;