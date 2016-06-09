$(document).ready(function() {
   var table = $('table').DataTable({paging: false});
   $('.dataTables_filter').prepend('<div class="dropdown">' +
      '<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">' +
      'All Columns ' +
      '<span class="caret"></span>' + 
      '</button>' +
      '<ul class="dropdown-menu" id="dropdown">' +
      '<li><a>All Columns</a></li><li role="separator" class="divider"></li>' +
      '</ul></div>');
   var ul = $('#dropdown');
   var dropdownlist = new Array();
   $('table thead tr th').each(function(id) {
      var item = '<li id="'+id+'"><a>'+$(this).text()+'</a></li>';
      dropdownlist.push(item);
   });
   ul.append(dropdownlist.join(''));
   
   var idx = 0;
   $('.form-control').on('keyup', function() {
      if (idx == 0) {
         table.columns(idx).search($(this).val()).draw();
      } else {
         table.search($(this).val()).draw();
      }
   })
   $('.dropdown-menu li a').on('click', function() {
      table.columns(idx).search('').draw();
      idx = $(this).parent().attr('id');
      var selText = $(this).text();
      $(this).parents('.dropdown').find('.dropdown-toggle').html(selText+' <span class="caret"></span>');
      table.columns(idx).search($('.form-control').val()).draw();
   });

});


/*$('dropdown-menu option:selected').val();
$('#msds-select').change(function () {
   table.draw();
});*/