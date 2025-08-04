const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
  userId: { type: String, ref: 'User', required: true },
  role: { type: String, required: true },
  subject: { type: String, required: true },
  description: { type: String, required: true },
  isRead: { type: Boolean, default: false },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Notification', notificationSchema);
