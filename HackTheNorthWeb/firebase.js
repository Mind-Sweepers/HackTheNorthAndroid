// Initialize Firebase
var config = {
    apiKey: "AIzaSyCE1zeW72z6wxtUmu8NshZ8VrIxZU6T7P0",
    authDomain: "htn2018-dcc64.firebaseapp.com",
    databaseURL: "https://htn2018-dcc64.firebaseio.com",
    projectId: "htn2018-dcc64",
    storageBucket: "htn2018-dcc64.appspot.com",
    messagingSenderId: "560530870668"
};

firebase.initializeApp(config);
var database = firebase.database();

var ref = firebase.database().ref();

var updater;

window.onload = function () {

}

function getInput() {
    var code = document.getElementById("input").value;
    ref.on("value", function(snapshot) {
        //console.log(snapshot.val());
        var data = snapshot.val()[code];
        console.log(data)
        
        document.getElementById("messages").innerHTML = data[Object.keys(data)[Object.keys(data).length-1]];
    }, function (error) {
        console.log("Error: " + error.code);
    });

    updater = setInterval(getInput, 1000);
}