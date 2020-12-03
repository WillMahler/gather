// Imports
const functions = require('firebase-functions');
const admin = require('firebase-admin');

"use strict"; // Strict Mode Enabled

// Cloud Firestore Instance
admin.initializeApp();
const db = admin.firestore();

/* Listens for events added to events/:eventID and adds event to
the userEvents collections of the event organizer + adds event's 
private subcollection */
exports.addUserEvents = functions.firestore
    .document('events/{eventID}')
    .onCreate((snap, context) => {
        let event = snap.data();
        let ownerID = event.ownerID;
        let eventID = context.params.eventID;

        functions.logger.log('Addding Private Subcollection', eventID);

        let eventPrivate = {
            roles: {},
            permissions: ['title', 'date', 'location', 'description']
        };
        eventPrivate['roles'][ownerID] = 'owner';

        db.collection('events')
          .doc(eventID)
          .collection('private')
          .doc(ownerID)
          .set(eventPrivate);

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

/* Listens for events updated from events/:eventID and updates event from the 
releavant User Events collections. */
exports.updateUserEvents = functions.firestore
    .document('events/{eventID}')
    .onUpdate((change, context) => {
        let updatedEvent = change.after.data();
        let eventID = context.params.eventID;
        
        let updatedUserEvent = {
            title: updatedEvent.title,
            date: updatedEvent.date,
            location: updatedEvent.location,
            ownerID: updatedEvent.ownerID,
            ownerFirstName: updatedEvent.ownerFirstName,
            ownerLastName: updatedEvent.ownerLastName, 
            description: updatedEvent.description,
            published: updatedEvent.published
        };

        functions.logger.log('Updating User Events for all attendees', eventID);

        // Update UserEvents document of this Event for all Attendees
        return db.collection('events')
                 .doc(eventID)
                 .collection('attendees')
                 .get()
                 .then((querySnapshot) => {
                     querySnapshot.forEach((doc) => {
                     let attendeeID = doc.id;
                     functions.logger.log('Updating User Events of Attendee', attendeeID);
                     
                     db.collection('users')
                       .doc(attendeeID)
                       .collection('userEvents')
                       .doc(eventID)
                       .set(updatedUserEvent)
                       .catch(e => {
                           functions.logger.log('Error', e);
                           return false;
                        });
                });

                return true;
            })
            .catch(e => {
                functions.logger.log('Error', e);
                return false;
            });
    });

/* Listens for events deleted from events/:eventID and deletes event from
the relevant User Events collections + deletes event's private subcollection*/
exports.deleteUserEvents = functions.firestore
    .document('events/{eventID}')
    .onDelete((snap, context) => {
        let deletedEvent = snap.data();
        let ownerID = deletedEvent.ownerID;
        let eventID = context.params.eventID;

        functions.logger.log('Deleting Event Private Subcollection', eventID);
        db.collection('events')
          .doc(eventID)
          .collection('private')
          .doc(ownerID)
          .delete();

        functions.logger.log('Deleting from User Events', eventID);
        // // TODO: Published; Delete from Attendees
        // if (deletedEvent.published) {}

        return db
            .collection('users')
            .doc(ownerID)
            .collection('userEvents')
            .doc(eventID)
            .delete();
    });