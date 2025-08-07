const express = require('express');
const router = express.Router();
const Notification = require('../models/notification');

router.post('/customer', async (req, res) => {
    const customerId = req.headers['customerid'];

    if (!customerId) {
        return res.status(400).json({ message: 'customerId is required in headers' });
    }

    const notification = new Notification({
        userId: customerId,
        subject: req.body.subject,
        description: req.body.description,
        role: 'Customer'
    });

    try {
        const savedNotification = await notification.save();
        res.status(201).json({ message: 'Notification added successfully' });
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

router.get('/getNotificationCustomer', async (req, res) => {
    const customerId = req.headers['customerid'];
    if (!customerId || customerId.length < 1) {
        return res.status(400).json({ message: 'Invalid customer ID format' });
    }

    try {
        const notification = await Notification.find({ userId: customerId.toString(), role: "Customer" });
        if (notification.length === 0) {
            return res.status(404).json({ message: 'No Notification found for this customer' });
        }
        res.json(notification);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
})

router.get('/getNotificationOrganiser', async (req, res) => {
    const organiserId = req.headers['organiserid'];
    console.log("Fetching notifications for organiserId:", organiserId);

    if (!organiserId || organiserId.length < 1) {
        return res.status(400).json({ message: 'Invalid organiser ID format' });
    }

    try {
        const notification = await Notification.find({
            userId: organiserId.toString(),
            role: "Organiser"
        });

        res.json(notification);
    } catch (err) {
        console.error("Error in getNotificationOrganiser:", err);
        res.status(500).json({ message: err.message });
    }
});

router.put("/markAsRead", async (req, res) => {
  const { id } = req.body;

  if (!id) {
    return res.status(400).json({ message: "Notification id is required" });
  }

  try {
    const notification = await Notification.findById(id);
    if (!notification) {
      return res.status(404).json({ message: "Notification not found" });
    }

    // Mark as read
    notification.isRead = true;
    await notification.save();

    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});




module.exports = router;