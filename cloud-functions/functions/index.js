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
            ownerID: ownerID,
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

<<<<<<< HEAD
/* Listens for events updated from events/:eventID and updates event from the 
=======
/* Listens for events updated from events/:eventID and updates event from the
>>>>>>> master
releavant User Events collections. */
exports.updateUserEvents = functions.firestore
    .document('events/{eventID}')
    .onUpdate((change, context) => {
        let updatedEvent = change.after.data();
        let eventID = context.params.eventID;
<<<<<<< HEAD
        
=======

>>>>>>> master
        let updatedUserEvent = {
            title: updatedEvent.title,
            date: updatedEvent.date,
            location: updatedEvent.location,
            ownerID: updatedEvent.ownerID,
            ownerFirstName: updatedEvent.ownerFirstName,
<<<<<<< HEAD
            ownerLastName: updatedEvent.ownerLastName, 
=======
            ownerLastName: updatedEvent.ownerLastName,
>>>>>>> master
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
<<<<<<< HEAD
                     
=======

>>>>>>> master
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

//Using an email list, it sends an invite to every user on the List
//If the list is being updated, those people get removed from the event
exports.sendInvites = functions.https
  .onCall(async (data, context) => {
    const eventID = data.eventID;
    const emailList = JSON.parse(data.invites);
    let newEmailList;
    const newEvent = data.newEvent;

    const attendeesRef = db.collection('events').doc(eventID).collection('attendees');
    const usersRef = db.collection('users');

    //If the Event is not new, we might have to remove attendees
    if(!newEvent){
        const snapshot = await attendeesRef.get();
        snapshot.forEach(doc => {
            //If the user is no longer attending, remove them from the list.
            if(!emailList.includes(doc.data().get('email'))){
                attendeesRef.doc(doc.id).delete();
            }else{
                newAttendees.add(email);
            }
        });
    }
    //Otherwise this is a new event
    else{
        newEmailList = emailList;
    }

    for(const email of newEmailList){
        const snapshot = await usersRef.where('email', '==', email).get();
        snapshot.forEach(userDoc => {
            let newAttendee = {
              'firstName': userDoc.data().firstName,
              'lastName': userDoc.data().lastName,
              'email': userDoc.data().email,
              'profileImg': userDoc.data().profileImg,
              'status': 0
            }
            attendeesRef.doc(userDoc.id).set(newAttendee);
        });
    }
  });

exports.createEventAttendee = functions.firestore
    .document('events/{eventID}/attendees/{userID}')
    .onCreate((snap, context) => {
        //Db refference to event
        const eventRef = snap.ref.parent.parent;
        //Db refference to userEvent
        const userEventsRef = db.collection('users')
                                .doc(snap.id)
                                .collection('userEvents');
        //Retrieves the event from events and puts it in userEvents of user
        eventRef.get().then(eventDoc => {
            const eventData = eventDoc.data();
            let userEvent = {
                title: eventData.title,
                date: eventData.date,
                location: eventData.location,
                ownerID: eventData.ownerID,
                ownerFirstName: eventData.ownerFirstName,
                ownerLastName: eventData.ownerLastName,
                description: eventData.description,
                status: 0,
                published: eventData.published
            };
            userEventsRef.doc(eventDoc.id).set(userEvent);
        })
    });

exports.deleteEventAttendee = functions.firestore
    .document('events/{eventID}/attendees/{userID}')
    .onDelete((snap, context) => {
          const userID = snap.id;
          const eventRef = snap.ref.parent.parent;
          eventRef.get().then(eventDoc => {
              const eventID = eventDoc.id;
              console.log("User: " + userID + " Event: " + eventID);
              db.collection('users')
                .doc(userID)
                .collection('userEvents')
                .doc(eventID)
                .delete();
          });
    });

    exports.updateAttendeeStatus = functions.firestore
        .document('users/{userID}/userEvents/{eventID}')
        .onUpdate((change, context) => {
            let eventID = context.params.eventID;
            let attendeeID = context.params.userID;
            let updatedAttendee = change.after.data();

            return db.collection('events')
                     .doc(eventID)
                     .collection('attendees')
                     .doc(attendeeID)
                     .set(updatedAttendee)
                     .catch(e => {
                         functions.logger.log('Error', e);
                         return false;
                      });
        });
