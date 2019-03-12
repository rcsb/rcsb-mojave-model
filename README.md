# RCSB-JSON-SCHEMA
The repository is meant as a schema store that hosts [JSON Schemas](http://json-schema.org/latest/json-schema-core.html) 
for RCSB data models.


### Vesrsioning
Versioning of schema files is handled by adding a tag to a specific commit in the repository’s history referring 
to a release point. Version numbers should follow [Semantic Versioning Specification](https://semver.org/#semantic-versioning-specification-semver) 
(SemVer) specification. Release version takes the **x.y.z** form, where **x** is the major version, **y** is the minor 
version, and **z** is the patch version (e.g. 0.1.0). Pre-release versions are denoted by appending a hyphen and a series of dot separated 
identifiers immediately following the patch version (e.g. 1.0.0-3.7, 1.0.0-alpha.3.7, 1.0.0-dev.3.7).
