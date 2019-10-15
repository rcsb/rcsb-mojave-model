# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.4] - 2019-10-15
### Added
- Definitions for RCSB metadata extensions
- JSON schema instances are populated with metadata extensions 
- PubMed data is extracted in a separate core
- __core_entity__ schema defines summary info for polymer monomers present in the sequence
- Feature tables to entity, entity instance, uniprot and validation cores


### Changed
- __chem_comp__ and  __bird__ schemas are combined in a single file
- __core_uniprot__ has undergone major refactoring of attribute names 


### Removed
- _rcsb_publication_ field from __core_entry__. Data is available in PubMed core
- Individual __chem_comp__ and  __bird__ schemas
- __core_entity_monomer__ schema is no longer needed (content migrated to __core_entity__)

### Other
This is not a full list of changes.
