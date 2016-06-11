var charAttributes = require('../json/characterAttributes.json');
var moveNames = require('../json/moveInternalNames.json');
var charIds = require('../json/characterIds.json');
var request = require('request');

exports.getCharId = function(name) {
   return charIds[1][name];
}

exports.getName = function(name) {
   return charIds[0][name];
}

exports.getMove = function (data) {
   if (!data)
      return "";
   return moveNames[1][data];
}

exports.getFrameStrip = function (data) {
   if (!data)
      return [];
   data = data[0]['frames'];
   for (var i = 0; i < data.length; i++) {
      for (key in data[i]) {
         if (data[i][key]) {
            data[i][key] = key + "True";
         } else {
            data[i][key] = "";
         }
      }
   }
   return data;
}

exports.parseChar = function (data) {
   if (!data)
      return [];
   for (var i in data) {
      for (var j in data[i]) {
         console.log(j);
         console.log(charAttributes[0][j]);
         if (j == 'id' || j == 'charId') {
            delete data[i][j];
         } else if (/.*Unknown.*/i.test(j)) {
            delete data[i][j];
         } else if (charAttributes[0][j] !== undefined && charAttributes[0][j] != j) {
            data[i][charAttributes[0][j]] = data[i][j];
            delete data[i][j];
         }
      }
   }
   return data;
}

exports.parseMove = function (data) {
   if (!data)
      return [];
   for (var i in data) {
      for (var j in data[i]) {
         if (j == 'id' || j == 'charId' || j == 'internalName') {
            delete data[i][j];
         } else if (moveNames[0][j] !== undefined) {
            data[i][moveNames[0][j]] = data[i][j];
            delete data[i][j];
         }
      }
   }
   return data;
}

exports.parseUrl = function (data) {
   if (!data)
      return "";
   if (data.endsWith("/")) {
      return data.slice(0, -1);
   } else {
      return data;
   }
}

exports.movesList = function (charName) {
   var charId = exports.getCharId(charName);
   var moves = [];
   request(api + '/move?charId=' + charId,
      function (error, response, body) {
         var data = JSON.parse(response1.body);
         for (var i = 0; i < data.length; i++) {
            moves[i] = data[i]['animation'];
         }
      });
   return moves;
}

