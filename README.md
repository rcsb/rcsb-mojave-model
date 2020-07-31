# RCSB-JSON-SCHEMA
The repository is a schema store that hosts [JSON Schemas](http://json-schema.org/latest/json-schema-core.html)
for RCSB data models!

### Versioning
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
*dw_source_schemas_**x.y.z**.tar.gz*. Every build will provide the latest snapshot named as
*dw_source_schemas_**branch_name**.tar.gz*. As a part of CI/CD process source schemas
(current version and latest snapshot) will be distributed to the build locker.

### Cardinal Identifier Containers
Cardinal identifier containers provide evidence about how the data described by core schemas are related.
In each core you can find a container specific to core:
 - `rcsb_assembly_container_identifiers` for assembly core
 - `rcsb_entry_container_identifiers` for entry core
 - `rcsb_polymer_entity_container_identifiers` for entity core
 - `rcsb_uniprot_container_identifiers` for uniprot core

The content of these containers are pointers to the entries in related cores.

### Schema Integration Rules
As we integrate multiple sources into a single schema, we want to ensure that the original schema is preserved
as closely as possible. However, it may be necessary to change the original schema. When this happens a new or modified
data item is appended to the original schema under `rcsb_` namespace to indicate provenance. Here is a set of rules that
govern schema integration:
  - Data mutation (e.g. changing or adding new value) should be added as a new `rcsb_` item.
  - Data aggregation (e.g. merging multiple original data items in a new object) should be added as a new `rcsb_` item.
  - Data reduction (e.g. filtering of array) should be added as a new `rcsb_` item.
  - Schema reduction (e.g. removing fields from original data item) should be integrated with original schema.
