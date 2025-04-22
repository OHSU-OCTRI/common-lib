# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

## [1.0.0] - 2025-04-22

Initial release of code extracted from other projects.

### Added

- Add utility types used in user interfaces (CIS-3122)
  - `Labelled` interface
  - Generic `EntitySelectOption`, `EnumSelectOption`, `OptionList`, `SelectOption` used for `<select>` lists
  - `ViewUtils` utility class for manipulating JavaScript and WebJars added to the page
- Add `AbstractEntity` class and related controller classes (CIS-3122)

[unreleased]: https://source.ohsu.edu/OCTRI-Apps/common-lib/compare/v1.0.0...HEAD
[1.0.0]: https://source.ohsu.edu/OCTRI-Apps/common-lib/releases/tag/v1.0.0