# RCSB-JSON-SCHEMA
The repository is a schema store that hosts [JSON Schemas](http://json-schema.org/latest/json-schema-core.html) 
for RCSB data models!


### Vesrsioning
Versioning of schema files is handled by adding a tag to a specific commit in the repository’s history referring 
to a release point. Version numbers should follow [Semantic Versioning Specification](https://semver.org/#semantic-versioning-specification-semver) 
(SemVer). Release version takes the **x.y.z** form, where **x** is the major version, **y** is the minor version, 
and **z** is the patch version (e.g. 0.1.0). Pre-release versions are denoted by appending a hyphen and a series 
of dot separated identifiers immediately following the patch version (e.g. 1.0.0-3.7, 1.0.0-alpha.3.7, 1.0.0-dev.3.7).

Given a version number **major.minor.patch**, increment the:

- **major** version when changes are incompatible (removing fields, changing fields name or type) with previous version,
- **minor** version when changes are backwards-compatible (adding new fields), and
- **patch** version when changes are backwards-compatible and related to metadata (changing field description, adding examples, etc.).

### Distribution
This package provides tar files for the distribution of releases. The tar file for version **x.y.z** will be named 
*dw_source_schemas_[branch name]_**x.y.z**.tar.gz*. Every build will provide the latest snapshot named as
*dw_source_schemas_[branch name]_**latest**.tar.gz*. As a part of CI/CD process source schemas 
(current version and latest snapshot) will be distributed to the build locker.