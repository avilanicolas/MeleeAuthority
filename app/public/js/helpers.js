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
            var first = 1;
            for (var key in data[i]) {
               var text = data[i][key];
               if (typeof text === 'object') {
                  text = hbs.handlebars.escapeExpression(JSON.stringify(text));
               } else {
                  text = hbs.handlebars.escapeExpression(text);
               }
               tbody += '<td>';
               if (first == 0) {
                  tbody += exports.get(hbs).link(text, this.url + '/' + text);
               } else {
                  tbody += text;
               }
               first--;
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
         console.log(Object.keys(data[0])[0]);
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

            body += '<tr><th>' +
               hbs.handlebars.escapeExpression(key) + '</th>';
            for (var j = 0; j < data.length; j++) {

               text = data[j][key];
               if (typeof text === 'object') {
                  text = hbs.handlebars.escapeExpression(JSON.stringify(text));
               } else {
                  text = hbs.handlebars.escapeExpression(text);
               }
               body += '<td>' + text + '</td>';
            }
            body += '</tr>';
            if (i == 0 && !framestrip)
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


