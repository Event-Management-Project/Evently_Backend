# 🎉 Evently – Event Management System  

**Evently** is a full-stack event management platform designed for both **Customers** and **Organisers**.  
It simplifies event discovery, booking, payments, and management through role-based dashboards and dynamic workflows.  

---

## 👤 Customer Flow  
1. 🔑 **Login / Register** (secured with **JWT Authentication**).  
2. 🔄 **Update / Change Password** anytime.  
3. 🔍 **Browse Events** with filters (date, venue, category).  
4. 📝 **Create Booking** for selected events.  
5. 💳 **Make Payment** securely.  
   - ✅ If payment is successful → a **dynamic ticket** is generated (downloadable).  
   - ⏳ If payment fails or is pending → shown as *Pending* in **Booking History** with a **Complete Payment** option (redirects to payment page).  
6. 📜 **Booking History** → shows all booked events with payment status.  
7. ⭐ **Review System**  
   - Customers can add reviews **only after successful payment**.  
   - Once reviewed, the same event **cannot be reviewed again**.  
8. 🔔 **Read Notifications** about events, payments, or organiser updates.  

---

## 🏢 Organiser Flow  
1. ➕ **Add Events** with details (title, venue, dates, description).  
2. 🛠️ **Edit Events** (only for upcoming/present events, not past events).  
3. 🏷️ **Add Categories & Facilities** to events.  
4. 📝 **Manage Facilities** assigned to specific events.  
5. 👥 **View Event Attendees** (customer information for each event).  
6. ⭐ **Check Reviews** provided by customers.  
7. 🔔 **Read Notifications** related to events, bookings, and feedback.  

---

## 🚀 Tech Stack  
- **Frontend:** React.js, Redux, Tailwind CSS  
- **Backend:** Spring Boot (Microservices), Node.js + Express.js  
- **Database:** MySQL, MongoDB  
- **Others:** Cloudinary, API Gateway, Service Discovery  

---

✨ Evently ensures a smooth experience for **customers** to discover and book events, and for **organisers** to manage, analyse, and engage with their audience effectively.  
