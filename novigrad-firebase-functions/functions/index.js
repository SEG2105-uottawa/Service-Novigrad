const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { debug, log } = require('firebase-functions/lib/logger');
admin.initializeApp(functions.config().firebase);

let auth = admin.auth();
let db = admin.firestore();

exports.novigradAdminDeleteUser = functions.https.onRequest(async (request, res) => {
    try {
        let uid = request.query.id;
        let ref = await db.collection("users").doc(uid);
        let doc = (await ref.get()).data();
        await ref.delete();
        await auth.deleteUser(uid);
        await removeRequests(doc.role.toLowerCase(), ref);
        res.send("Success");
    } catch (error) {
        res.send(error);
    }
});

exports.novigradAdminDeleteService = functions.https.onRequest(async (request, res) => {
    try {
        let id = request.query.id;
        let ref = await db.collection("available_services").doc(id);
        await ref.delete();
        await removeRequests('service', ref);
        res.send("Success");
    } catch (error) {
        res.send(error);
    }
});



const removeRequests = async (key, ref) => {
    // remove request related to customer or employee
    let snapshot = await db.collection('service_requests').where(key, '==', ref).get();
    let batch = db.batch();
    snapshot.docs.forEach(doc => {
        log("DOC: " + JSON.stringify(doc))
        batch.delete(doc.ref)
    });
    await batch.commit();
    log(`Successfully deleted request by '${key}' == '${ref.id}'`);
}

