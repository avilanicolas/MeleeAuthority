function titleCase(str) {
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
               var text = hbs.handlebars.escapeExpression(data[i][key]);
               tbody += '<td>';
               if (first == 0) {
                  tbody += exports.get(hbs).link(text, this.url + '/' + text);
                  first = false;
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


