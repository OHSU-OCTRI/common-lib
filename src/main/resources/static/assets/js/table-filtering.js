/**
 * Functionality for adding interactions to a table with toggle items that filter on a hidden column.
 * This table is initialized using the Datatables javascript library and requires
 * datatables to be included in the pageScripts.
 *
 * See the Datatables library documentation for more options: https://datatables.net
 */
(function () {
  'use strict';
  const tables = document.querySelectorAll('.filtered');
  tables.forEach(t => {
    const tableSelector = `#${t.id}`;

    const searchBoxSelector = `${tableSelector}_filter input`;
    const toggleSelector = `${tableSelector}_toggle-menu .toggle-item`;
    const filterColumnSelector = `${tableSelector} th.filter-column`;
    const pageToTodayColumnSelector = `${tableSelector} th.page-to-today-column`;

    const ALL = 'all';

    let table = null;

    function initDataTable() {
      const column = $(tableSelector).attr('data-column') || 0;
      const orientation = $(tableSelector).attr('data-orientation') || 'asc';
      const paging = $(tableSelector).attr('data-paging') || true;
      const searching = $(tableSelector).attr('data-searching') || true;
      const info = $(tableSelector).attr('data-info') || true;
      const searchTitle = $(tableSelector).attr('data-search-title') || null;

      table = $(tableSelector).DataTable({
        order: [[column, orientation]],
        paging: paging,
        searching: searching,
        info: info,
        initComplete: function (settings, json) {
          if (searchTitle) {
            const searchBox = document.querySelector(searchBoxSelector);
            if (searchBox) {
              searchBox.setAttribute('title', searchTitle);
            }
          }
        }
      });
    }

    function setActiveFilter(elements, activeControl) {
      elements.forEach(el => el.classList.remove('active'));
      activeControl.classList.add('active');
    }

    /**
     * Filter the table using the hidden filterColumn.
     *
     * @param {String} value
     */
    function filterData(value) {
      if (table) {
        filterTableData(value);
      }
    }

    function filterTableData(value) {
      let filterColumn = $(filterColumnSelector).index();
      if (value === ALL) {
        // clear the search
        table.columns(filterColumn).search('').draw();
      } else {
        table.columns(filterColumn).search(value).draw();
      }
    }

    /**
     * Set the order of the table based on the active toggle button.
     */
    function setOrientation() {
      const activeToggle = document.querySelector(`${toggleSelector}.active`);
      const orientation = $(activeToggle).attr('data-orientation') || 'asc';
      const column = $(tableSelector).attr('data-column') || 0;
      table.order([[column, orientation]]).draw();
    }

    /**
     * Checks the active toggle and pages forward to today or after if the data attributes are configured.
     */
    function setPage() {
      const activeToggle = document.querySelector(`${toggleSelector}.active`);
      if ($(activeToggle).attr('data-page-to-today') !== undefined) {
        let dateColumn = $(pageToTodayColumnSelector).index();
        const parseDate = str => new Date(str);
        const todayString = new Date().toISOString().split('T')[0];
        const today = parseDate(todayString);

        const rows = table.rows({ order: 'applied', search: 'applied' }).data().toArray();

        const targetIndex = rows.findIndex(row => {
          const dateStr = row[dateColumn]['@data-order'];
          const rowDate = parseDate(dateStr);
          return !isNaN(rowDate) && rowDate >= today;
        });

        if (targetIndex >= 0) {
          const pageLength = table.page.info().length;
          const page = Math.floor(targetIndex / pageLength);
          const visibleIndex = targetIndex % pageLength;
          const handleDraw = function () {
            const currentPageRows = table.rows({ page: 'current' });
            const rowNode = currentPageRows.nodes()[visibleIndex];

            if (rowNode) {
              rowNode.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }

            table.off('draw', handleDraw);
          };

          table.on('draw', handleDraw);
          table.page(page).draw(false);
        }
      }
    }

    /**
     * Set the given value in the search box and search the datatable using this value.
     *
     * @param {String} value
     */
    function search(value) {
      if (table) {
        searchTable(value);
      }
    }

    function searchTable(value) {
      if (value) {
        document.querySelector(searchBoxSelector).value = value;
        table.search(value).draw();
      }
    }

    /**
     * Toggle buttons are used to filter rows based on a preset number of values
     * which correspond to the value in a hidden column. It is up to the implementer
     * if they want toggle buttons for all possible values of the column or not and if
     * they want to use the 'ALL' option to remove the filter.
     */
    function initToggleButtons() {
      const controls = document.querySelectorAll(toggleSelector);
      controls.forEach(control => {
        const filterValue = control.dataset.toggle;

        control.addEventListener('click', event => {
          event.preventDefault();
          setActiveFilter(controls, control);
          filterData(filterValue);
          setOrientation();
          setPage();
        });
      });
      const activeToggle = document.querySelector(`${toggleSelector}.active`);
      filterData(activeToggle.dataset.toggle);
      setOrientation();
      setPage();
    }

    /**
     * Enable table cells with a data-value property to be used as searches.
     */
    function initSearchValues() {
      const selector = `${tableSelector} [data-value]`;
      document.querySelectorAll(selector).forEach(el => {
        const searchTerm = el.dataset.value;
        el.addEventListener('click', event => {
          event.preventDefault();
          search(searchTerm);
        });
      });
    }

    window.addEventListener(
      'load',
      function () {
        initDataTable();
        initToggleButtons();
        initSearchValues();
      },
      false
    );
  });
})();
