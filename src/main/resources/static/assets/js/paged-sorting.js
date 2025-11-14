$(function () {
  $('.paged').each(function () {
    const $table = $(this);
    const baseRoute = $table.attr('data-base-route');
    const ajaxUrl = baseRoute + '/data';
    const showId = $table.attr('data-show-id') !== 'false';

    let columns = JSON.parse($table.attr('data-columns'));
    columns.push({ data: null });

    const ACTIONS_COLUMN_INDEX = columns.length - 1;

    // find default sort column
    let defaultOrder = columns
      .map((col, idx) => ({ idx, dir: col.defaultSort }))
      .find(c => c.dir); // find first with defaultSort

    let order = defaultOrder ? [[defaultOrder.idx, defaultOrder.dir]] : [[1, 'asc']]; // fallback if none specified

    $table.DataTable({
      serverSide: true,
      processing: true,
      ajax: {
        url: ajaxUrl,
        type: 'GET'
      },
      columns: columns,
      order: order,
      columnDefs: [
        {
          targets: 0, // FIRST column = ID th
          visible: showId,
          searchable: showId
        },
        {
          targets: ACTIONS_COLUMN_INDEX, // LAST column = Actions th
          data: 'id', // use the row's id for the link
          orderable: false,
          searchable: false,
          render: function (data, type, row, meta) {
            return `<a href="${baseRoute}/${data}">View/Edit</a>`;
          }
        }
      ]
    });
  });
});
