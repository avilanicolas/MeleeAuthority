$(document).ready(function() {
   var table = $('.datatable').DataTable({paging: false});
   $('.dataTables_filter').prepend('<div class="dropdown">' +
      '<button class="btn btn-default dropdown-toggle" type="button" ' +
      'data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">' +
      'All Columns ' +
      '<span class="caret"></span>' + 
      '</button>' +
      '<ul class="dropdown-menu" id="dropdown">' +
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
   $('.dropdown-menu li a').on('click', function() {
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
});


/*$('dropdown-menu option:selected').val();
$('#msds-select').change(function () {
   table.draw();
});*/