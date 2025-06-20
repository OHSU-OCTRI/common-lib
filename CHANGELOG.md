# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [unreleased]

## [1.4.0]

### Added

- Added interfaces and utilities for exporting data to CSV and Excel

### Changed

- Added POM metadata required for publishing to Maven Central. (CIS-3192)
- Flesh out Javadoc to resolve warnings when building documentation. (CIS-3193)

## [1.3.0] - 2025-06-13

### Added

- Added the file UPGRADING.md for tracking upgrade instructions.
- Added table-filtering.js to standardize Datatables using the 'filtered' class

## [1.2.0] - 2025-05-27

### Added

- Added script to the new entity form that will reset the form so that browser cache does not populate it when the Back button is used.
- Added table-sorting.js script to standardize Datatables using the 'sorted' class

### Changed

- Use the ViewUtils methods to add scripts and webjars

### Fixed

- Fixed ClassCastException when adding multiple scripts or webjars to the model

## [1.1.0] - 2025-04-29

### Added

- Add method to `OptionList` for building a list from arbitrary strings.

## [1.0.0] - 2025-04-22

Initial release of code extracted from other projects.

### Added

- Add utility types used in user interfaces (CIS-3122)
  - `Labelled` interface
  - Generic `EntitySelectOption`, `EnumSelectOption`, `OptionList`, `SelectOption` used for `<select>` lists
  - `ViewUtils` utility class for manipulating JavaScript and WebJars added to the page
- Add `AbstractEntity` class and related controller classes (CIS-3122)

[unreleased]: https://source.ohsu.edu/OCTRI-Apps/common-lib/compare/v1.4.0...HEAD
[1.0.0]: https://source.ohsu.edu/OCTRI-Apps/common-lib/releases/tag/v1.0.0
[1.1.0]: https://source.ohsu.edu/OCTRI-Apps/common-lib/releases/tag/v1.1.0
[1.2.0]: https://source.ohsu.edu/OCTRI-Apps/common-lib/releases/tag/v1.2.0
[1.3.0]: https://source.ohsu.edu/OCTRI-Apps/common-lib/releases/tag/v1.3.0
[1.4.0]: https://source.ohsu.edu/OCTRI-Apps/common-lib/releases/tag/v1.4.0
