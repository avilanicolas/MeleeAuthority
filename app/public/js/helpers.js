function titleCase(str) {  
   console.log("\n1\n" + str);
   str = str.toLowerCase().split(' ');

   for(var i = 0; i < str.length; i++){
      str[i] = str[i].charAt(0).toUpperCase() + str[i].slice(1);
   }
   console.log("\n2\n" + str.join(' '));
   return str.join(' ');
}

exports.get = function(hbs) {
   return {
      table: function(data) {
         var str = '';
         if (!data) {
            return "";
         }
         for (var i = 0; i < data.length; i++ ) {
            str += '<tr>';
            for (var key in data[i]) {
               str += '<td>' +
               hbs.handlebars.escapeExpression(data[i][key]) +
                      '</td>';
            }
            str += '</tr>';
         }
         return new hbs.handlebars.SafeString(str);
      },

      link: function(text, url) {
         url = hbs.handlebars.escapeExpression(url);
         text = hbs.handlebars.escapeExpression(text);
         return new hbs.handlebars.SafeString(
            "<a href=\"" + url + "\">" + text + "</a>"
         );
      },

      maketitle: function(text) {
         console.log(text);
         console.log(titleCase(text));
         return titleCase(text);
      }
   }
}


