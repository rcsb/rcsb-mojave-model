# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.9] - 2019-12-20

### Changed
- shape descriptor schema

### Added

### Removed

## [0.1.8] - 2019-12-16 (alpha-6)

### Changed
- various adjustments to search context

### Added
- merged removed entries schema
- UniProt data comes from Exchange DB
- GO data comes from Exchange DB

### Removed
- individual obsolete and in-silico schemas

## [0.1.7] - 2019-12-10

### Changed
- entity and entity instance cores are split into polymer, non-polymer, branched
- fixed type mismatch for search metadata from "core_uniprot_container_identifiers.pubmed_id"

### Added

### Removed
- search metadata from "core_entry_container_identifiers.pubmed_id"

## [0.1.6] - 2019-11-05

### Changed
- In bird_chem_comp_core, "citation" renamed to "rcsb_bird_citation"

### Added
- Latest revision object to core collections

### Removed
- "pdbx_molecule" from core_entry

## [0.1.5] - 2019-10-25

### Changed
- Various refinements of search metadata contexts


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
