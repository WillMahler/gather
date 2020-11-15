// Imports
const functions = require('firebase-functions');
const admin = require('firebase-admin');

"use strict"; // Strict Mode Enabled

// Cloud Firestore Instance
admin.initializeApp();
const db = admin.firestore();

/* Listens for events added to /events/:eventID and adds event to
the userEvents collections of the event organizer */
exports.addUserEvents = functions.firestore
    .document('events/{eventID}')
    .onCreate((snap, context) => {
        let event = snap.data();
        let ownerID = event.ownerID;
        let eventID = context.params.eventID;
        let userEvent = {
            title: event.title,
            date: event.date,
            location: event.location,
            ownerID: event.ownerID,
            ownerFirstName: event.ownerFirstName,
            ownerLastName: event.ownerLastName, 
            description: event.description,
            status: 1,
            published: event.published
        };

        functions.logger.log('Add to User Events', eventID);

        return db
            .collection('users')
            .doc(ownerID)
            .collection('userEvents')
            .doc(eventID)
            .set(userEvent)
            .catch(e => {
                functions.logger.log('Error', e);
                return false;
            });
    });