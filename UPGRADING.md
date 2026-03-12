# Upgrading

## 2.0.0

Applications should update the filename used to include the DataTables WebJar on pages, from `jquery.dataTables.min.js` to `dataTables.min.js`. Additionally, packages and applications should:

* Upgrade [authentication-lib](github.com/OHSU-OCTRI/authentication-lib) to 2.0.0,
* Upgrade [survey-lib](https://source.ohsu.edu/OCTRI-Apps/survey-lib/) to 1.0.0 (if present), and
* Remove the following WebJar dependencies from their `pom.xml` files:
  * Bootstrap
  * DataTables
  * Font Awesome
  * jQuery
  * jQuery-UI

## 1.3.0

Applications can remove the asset table-filtering.js from their own repositories. It is now managed by the Common Library.

## 1.2.0

Applications can remove the assets table-sorting.js and form-reset.js from their own repositories. They are now managed by the Common Library.
