function titleCase(str) {
   if (!str) {
      console.log("Error: No title given");
      return "";
   }

   str = str.toLowerCase().split(' ');

   for(var i = 0; i < str.length; i++){
      str[i] = str[i].charAt(0).toUpperCase() + str[i].slice(1);
   }
   return str.join(' ');
}

exports.get = function(hbs) {
   return {
      table: function(data) {
         if (!data)
            return "";

         var thead = '<thead>';
         thead += '<tr>';
         for (var key in data[0]) {
            thead += '<th>' +
               hbs.handlebars.escapeExpression(key) +
              '</th>';
         }
         thead += '</tr>';
         thead += '</thead>';

         var tbody = '<tbody>';
         for (var i = 0; i < data.length; i++ ) {
            tbody += '<tr>'
            var first = true;
            for (var key in data[i]) {
               var text = data[i][key];
               if (typeof text === 'object') {
                  text = hbs.handlebars.escapeExpression(JSON.stringify(text));
               }
               tbody += '<td>';
               if (first) {
                  tbody += exports.get(hbs).link(text, this.url + '/' + text);
                  first = false;
               } else {
                  tbody += text;
               }
               tbody += '</td>';
            }
            tbody += '</tr>';
         }
         tbody += '</tbody>';

         return new hbs.handlebars.SafeString(thead + tbody);
      },

      verticaltable: function(data) {
         if (!data)
            return "";

         var body = '<thead>';
         var framestrip = false;
         if (Object.keys(data[0])[0] == 'hitbox') {
            framestrip = true;
            body += '<tr><th>frame</th>';
            for (var c = 0; c < data.length; c++) {
               body += '<td>' + c + '</td>';
            }
            body += '</tr></thead>';
         }

         var i = 0;
         for (var key in data[0]) {
            if (key != 'name') {
               body += '<tr><th>' +
                  hbs.handlebars.escapeExpression(key) + '</th>';
               for (var j = 0; j < data.length; j++) {

                  text = data[j][key];
                  if (typeof text === 'object') {
                     text = hbs.handlebars.escapeExpression(JSON.stringify(text));
                  } else {
                     text = hbs.handlebars.escapeExpression(text);
                  }
                  if (text == 'hitboxTrue') {
                     body += '<td class="btn-warning"></td>';
                  } else if (text == 'iasaTrue') {
                     body += '<td class="btn-success"></td>';
                  } else if (text == 'autocancelTrue') {
                     body += '<td class="btn-info"></td>';
                  } else {
                     body += '<td>' + text + '</td>';
                  }
                  
               }
               body += '</tr>';
               if (i == 0 && !framestrip)
                  body += '</thead>';
            }
            i++;
         }

         return new hbs.handlebars.SafeString(body);
      },

      hitboxvertical: function(data) {
         
         console.log(data);
         if (!data)
            return "";
         var body = '<thead>';

         var i = 0;
         for (var key in data[0]) {
            var arr = data[0][key];
            var text = hbs.handlebars.escapeExpression(key);
            body += '<tr><th rowspan="' + arr.length + '">' +
            exports.get(hbs).link(text, this.url + '/' + text) + '</th>';
            var curgroup = 0;
            var curgrouplen = 0;
            var groupchange = true;
            for (var k = 0; k < arr.length; k++) {
               curgroup = arr[k]['groupId'];
               var found = false;
               for (var len = k; len < arr.length; len++) {
                  if (!found) {
                     if (arr[len]['groupId'] != curgroup) {
                        console.log("groupId:" + arr[k]['groupId']);
                        found = true;
                     } else {
                        curgrouplen++;
                     }
                  }
               }
               console.log(curgrouplen);
               for (var keyy in arr[k]) {
                  if (groupchange) {
                     body += '<td rowspan="' + curgrouplen + '">' + arr[k][keyy] + '</td>';
                     groupchange=false;
                     console.log("here");
                  } else {
                     if (keyy != 'groupId')
                        body += '<td>' + arr[k][keyy] + '</td>';
                  }
               }
               if (curgrouplen == 1) {
                  groupchange = true;
               } else {
                  groupchange = false;
               }
               curgrouplen = 0;
               if (k != arr.length -1) {
                  body += '</tr><tr>';
               }
            }
            
            body += '</tr>';
            if (i == 0)
               body += '</thead>';
            i++;
         }

         return new hbs.handlebars.SafeString(body);
      },

      link: function(text, url) {
         url = hbs.handlebars.escapeExpression(url);
         text = hbs.handlebars.escapeExpression(text);
         return new hbs.handlebars.SafeString(
            "<a href=\"" + url + "\">" + text + "</a>"
         );
      },

      maketitle: function(text) {
         return titleCase(text);
      }
   }
}


