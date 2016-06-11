$(document).ready(function() {
   var table = $('.datatable').DataTable({paging: false});
   $('.dataTables_filter').prepend('<div class="dropdown">' +
      '<button class="btn btn-default dropdown-toggle" type="button" ' +
      'data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">' +
      'All Columns ' +
      '<span class="caret"></span>' + 
      '</button>' +
      '<ul class="dropdown-menu" id="dropdown-search">' +
      '<li id="all"><a>All Columns</a></li><li role="separator" class="divider"></li>' +
      '</ul></div>');
   var ul = $('#dropdown');
   var dropdownlist = new Array();
   $('.datatable tr th').each(function(id) {
      var item = '<li id="'+id+'"><a>'+$(this).text()+'</a></li>';
      dropdownlist.push(item);
   });
   ul.append(dropdownlist.join(''));
   
   var idx = 'all';
   $('.form-control').on('keyup', function() {
      if (idx != 'all') {
         table.columns(idx).search($(this).val()).draw();
      } else {
         table.search($(this).val()).draw();
      }
   });
   $("input:first").focus();
   var selectedChar = "";
   $('#dropdown-character li a').on('click', function() {
      var selText = $(this).text();
      $(this).parents('.dropdown').find('.dropdown-toggle')
         .html(selText+' <span class="caret"></span>');
      movesList(selText, function(moves) {
         if (selText != selectedChar) {
            $('#dropdown-move').empty();
            for (var i = 0; i < moves.length; i++) {
               $('#dropdown-move').append($("<li></li>").append($("<a></a>").text(moves[i])))
            }
         }
         selectedChar = selText;
         $('#dropdown-move li a').on('click', function() {
            var selText = $(this).text();
            console.log(selText);
            $(this).parents('.dropdown').find('.dropdown-toggle')
               .html(selText+' <span class="caret"></span>');
         });
      });
   });
   $('#dropdown-search.dropdown-menu li a').on('click', function() {
      var selText = $(this).text();
      $(this).parents('.dropdown').find('.dropdown-toggle')
             .html(selText+' <span class="caret"></span>');
      table.columns(idx).search('').draw();
      idx = $(this).parent().attr('id');
      if (idx != 'all') {
         table.columns(idx).search($('.form-control').val()).draw();
      } else {
         table.search($('.form-control').val()).draw();
      }
   });
   $('.datatable th').each(function(){
      $(this).css('min-width', ($(this).width()+20) + 'px');
      $(this).css('padding-right', 8 + 'px');
      $(this).css('text-align', 'center');
   });
   if ($('.dataTables_wrapper').length) {
      $('.dataTables_wrapper .row .col-sm-6').eq(0).prepend($('.page-header h1'));
      $('.page-header').hide();
   }
   $('#dropdown-move li a').on('click', function() {
      var selText = $(this).text();
      console.log(selText);
      $(this).parents('.dropdown').find('.dropdown-toggle')
             .html(selText+' <span class="caret"></span>');
   });
   var start = 1
   $('#add').on('click', function() {
      charName = $('#dropdown-character').parent().find('button').text().trim();
      animName = $('#dropdown-move').parent().find('button').text().trim();
      getMove(charName, animName, function(data) {
         $('#framestrip').append($('<table class="table-bordered builder" id="framestrip" />')
            .html($.parseHTML(verticaltable(data['frames'], start))));
         start = Number($('#framestrip thead:last tr td:last-child').text()) + 1;
         console.log(start);
      });
   });
});

var movesList = function (charName, callback) {
   $.getJSON('public/json/characterIds.json',
      function (charIds){
      var charId = charIds[1][charName];
      $.getJSON('/move?charId=' + charId,
         function (data) {
            moves = []
            for (var i = 0; i < data.length; i++) {
               moves[i] = data[i]['animation'];
            }
            callback(moves);
         });
      });
}

var getMove = function (charName, anim, callback) {
   $.getJSON('public/json/characterIds.json',
      function (charIds){
      var charId = charIds[1][charName];
      $.getJSON('/move?charId=' + charId,
         function (data) {
            moves = []
            for (var i = 0; i < data.length; i++) {
               if (data[i]['animation'] == anim) {
                  callback(data[i])
               }
            }
         });
      });
}

var verticaltable = function(data, start) {
   if (!data)
      return "";

   for (var i = 0; i < data.length; i++) {
      for (key in data[i]) {
         if (data[i][key]) {
            data[i][key] = key + "True";
         } else {
            data[i][key] = "";
         }
      }
   }

   var body = '<thead>';
   var framestrip = false;
   if (Object.keys(data[0])[0] == 'hitbox') {
      framestrip = true;
      if (start == 1) {
         body += '<tr><th>frame</th>';
      } else {
         body += '<tr>';
      }
      for (var c = 0; c < data.length; c++) {
         body += '<td>' + (c+start) + '</td>';
      }
      body += '</tr></thead>';
   }

   var i = 0;
   for (var key in data[0]) {
      if (key != 'name') {
         if (start == 1) {
         body += '<tr><th>' +
            key + '</th>';
         } else {
            body += '<tr>'
         }
         for (var j = 0; j < data.length; j++) {

            text = data[j][key];
            if (typeof text === 'object') {
               text = JSON.stringify(text);
            } else {
               text = text;
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

   return body;
}

/*$('dropdown-menu option:selected').val();
$('#msds-select').change(function () {
   table.draw();
});*/