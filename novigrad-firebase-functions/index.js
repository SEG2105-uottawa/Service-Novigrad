const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

let auth = admin.auth();
let db = admin.firestore();


exports.novigradAdminDeleteUser = functions.https.onRequest(async (request, res) => {
    try {
        let uid = request.query.id;
        await db.collection("users").doc(uid).delete();
        await auth.deleteUser(uid);
        res.send("Success");
    } catch (error) {
        res.send(error);
    }
});


const cleanServiceDelete = (service) => {
    db.collection('av').
}

exports.novigradAdminDeleteService = functions.https.onRequest(async (request, res) => {
    try {
        let uid = request.query.id;
        await db.collection("service").doc(uid).delete();
        res.send("Success");
    } catch (error) {
        res.send(error);
    }
});
