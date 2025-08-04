const mongoose = require('mongoose');

const reviewSchema = new mongoose.Schema({
    customerId: {  
        type: String, 
        required: true,        
    },
    eventId: {  
        type: String,  
        required: true,
    },
    subject: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    star: {
        type: Number,
        required: true,
        min: 1,
        max: 5
}
});

module.exports = mongoose.model('Review', reviewSchema);