// Imports
const functions = require('firebase-functions');
const admin = require('firebase-admin');

"use strict"; // Strict Mode Enabled

// Cloud Firestore Instance
admin.initializeApp();
const db = admin.firestore();


/*
  addUserEvents

  Listens for and gets called when an event is created in events 
  collection. Events are created through a call in the client.

  PURPOSE:
  Adds the event to the userEvent collection of the owner
*/
exports.addUserEvents = functions.firestore
    .document('events/{eventID}')
    .onCreate((snap, context) => {

        let event = snap.data();
        let ownerID = event.ownerID;
        let eventID = context.params.eventID;

        functions.logger.log('addUserEvents: triggered for event created by:', event.ownerFirstName);

        let userEvent = {
            title: event.title,
            date: event.date,
            location: event.location,
            ownerID: event.ownerID,
            ownerFirstName: event.ownerFirstName,
            ownerLastName: event.ownerLastName,
            description: event.description,
            status: 1,
            published: event.published,
            profileImg: event.profileImg
        };

        functions.logger.log('addUserEvents: adding to owner\'s userEvents collection');

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

/*
  updateUserEvents

  Listens for and gets called when an event is updated in events 
  collection. Events are updated through a call in the client.

  PURPOSE:
  Updates the UserEvents of all Attendees. It does this by grabbing every 
  User corresponding to an AttendeeID of the event and updating their UserEvent 
  corresponding to this updated event
*/
exports.updateUserEvents = functions.firestore
    .document('events/{eventID}')
    .onUpdate((change, context) => {
        let updatedEvent = change.after.data();
        let eventID = context.params.eventID;

        functions.logger.log('updateUserEvents: triggered for update to eventID:', eventID);

        let updatedUserEvent = {
            title: updatedEvent.title,
            date: updatedEvent.date,
            location: updatedEvent.location,
            ownerID: updatedEvent.ownerID,
            ownerFirstName: updatedEvent.ownerFirstName,
            ownerLastName: updatedEvent.ownerLastName,
            description: updatedEvent.description,
            published: updatedEvent.published,
            profileImg: updatedEvent.profileImg
        };

        functions.logger.log('updateUserEvents: updating UserEvents for all Attendees');

        // Update UserEvents document of this Event for all Attendees
        return db.collection('events')
                 .doc(eventID)
                 .collection('attendees')
                 .get()
                 .then((querySnapshot) => {
                     querySnapshot.forEach((doc) => {
                     let attendeeID = doc.id;

                     functions.logger.log('updateUserEvents: updating UserEvents for AttendeeID:', attendeeID);

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

/*
  deleteUserEvents

  Listens for and gets called when an event is deleted in events 
  collection. Events are deleted through a call in the client.

  PURPOSE:
  Right now, it just deletes the event from the owner's userEvent collection
*/
exports.deleteUserEvents = functions.firestore
    .document('events/{eventID}')
    .onDelete((snap, context) => {
        let deletedEvent = snap.data();
        let ownerID = deletedEvent.ownerID;
        let eventID = context.params.eventID;

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
        const promise = usersRef.where('email', '==', email).get();
        promise.then(async doc => {
            doc.forEach(userDoc => {
                let newAttendee = {
                  'firstName': userDoc.data().firstName,
                  'lastName': userDoc.data().lastName,
                  'email': userDoc.data().email,
                  'profileImg': userDoc.data().profileImg,
                  'status': 0
                }
                attendeesRef.doc(userDoc.id).set(newAttendee);
            });
            return true;
        }).catch(e => {
            functions.logger.log('Error', e);
            return false;
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
                published: eventData.published,
                profileImg: eventData.profileImg
            };
            userEventsRef.doc(eventDoc.id).set(userEvent);
            
            return true;
        }).catch(e => {
            functions.logger.log('Error', e);
            
            return false;
        });
    });

exports.deleteEventAttendee = functions.firestore
    .document('events/{eventID}/attendees/{userID}')
    .onDelete((snap, context) => {
          const userID = snap.id;
          const eventRef = snap.ref.parent.parent;
          eventRef.get().then(eventDoc => {
              const eventID = eventDoc.id;
              db.collection('users')
                .doc(userID)
                .collection('userEvents')
                .doc(eventID)
                .delete();

                return true;
          }).catch(e => {
            functions.logger.log('Error', e);
            
            return false;
        });
    });

exports.updateAttendeeStatus = functions.firestore
    .document('users/{userID}/userEvents/{eventID}')
    .onUpdate((change, context) => {
        let eventID = context.params.eventID;
        let attendeeID = context.params.userID;
        let updatedEvent = change.after.data();

        let newStatus = updatedEvent.status

        functions.logger.log('updateAttendeeStatus: updating status to:', newStatus);

        return db.collection('events')
                    .doc(eventID)
                    .collection('attendees')
                    .doc(attendeeID)
                    .update("status", newStatus)
                    .catch(e => {
                        functions.logger.log('Error', e);
                        return false;
                    });
    });
