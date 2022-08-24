# RCSB-MOJAVE-MODEL
The RCSB core data models that describe organisation of data available for RCSB services. 
We use [JSON Schema](http://json-schema.org/latest/json-schema-core.html) as a declarative language 
to describe the structure of the data, constraints that apply to it and other metadata that can guide 
on data transformations and facilitate data use.

## Schema-First Development Practice
The philosophy behind the design and implementation of RCSB services that rely on data definitions is represented 
by schema-first development practice.
In order to ensure a mutual understanding of DW content centralized schema establishes a contract between:
- ETL process (aka [_Redwood_](https://github.com/rcsb/rcsb-redwood)) 
- Text indexing (aka [ _Everglades_ ](https://github.com/rcsb/rcsb-everglades))
- Search middleware (aka [_Arches_](https://github.com/rcsb/rcsb-arches))
- Data API services (aka [_Yosemite_](https://github.com/rcsb/rcsb-yosemite)) 

In this context JSON schema offers a **single source of truth** that is used to perform data validation and 
programmatically generate additional resources, e.g. the Plain Old Java Objects (POJOs), which can be reused 
throughout the pipeline.

For example, in data warehouse (DW) we use JSON schemas to automatically derive validation constrains that 
should be applied on DB inserts. DW uses [MongoDB](https://docs.mongodb.com/manual/introduction/) as a storage 
solution where data is persisted as documents (JSON-style objects). 

The other example of use is text indexing. We use [Elasticsearch](https://www.elastic.co) and index configuration 
(mapping) is automatically derived from JSON schemas.

## Schema Sources
Schema source files are stored in [rcsb-json-schema](https://github.com/rcsb/rcsb-json-schema) repository.

## Product
This module contains: 
  - Manually curated JSON schemas for the sources we integrate that don't provide a schema: `src/main/resources/schema`.
  - Automatically generated JSON schemas for core collections: `target/generated-sources/schema/core`.
  - Automatically generated schemas for MongoDB validation: `target/generated-sources/schema/validation`.
  - Automatically generated POJOs from combined core JSON schemas: `target/generated-sources/classes/org.rcsb.mojave.auto`.
  - Automatically generated POJOs from UniProt KB schema:
  `target/generated-sources/classes/org.rcsb.uniprot.auto`.  
  - Automatically generated Enum Types from combined core JSON schemas: `target/generated-sources/classes/org.rcsb.mojave.enumeration`.
  - Automatically generated Java class with schema fields defined as constants: `target/generated-sources/classes/org.rcsb.mojave.CoreConstants.java`

##### Cardinal Identifier Containers
Cardinal identifier containers provide evidence about how the data described by core schemas are related. 
In each core you can find a container specific to core:
 - `rcsb_assembly_container_identifiers` for assembly core
 - `rcsb_entry_container_identifiers` for entry core
 - `rcsb_polymer_entity_container_identifiers` for entity core
 - `rcsb_uniprot_container_identifiers` for uniprot core

The content of these containers are pointers to the entries in related cores.

##### Schema Integration Rules
As we integrate multiple sources into a single schema, we want to ensure that the original schema is preserved 
as closely as possible. However, it may be necessary to change the original schema. When this happens a new or modified 
data item is appended to the original schema under `rcsb_` namespace to indicate provenance. Here is a set of rules that 
govern schema integration:
  - Data mutation (e.g. changing or adding new value) should be added as a new `rcsb_` item.
  - Data aggregation (e.g. merging multiple original data items in a new object) should be added as a new `rcsb_` item.
  - Data reduction (e.g. filtering of array) should be added as a new `rcsb_` item.
  - Schema reduction (e.g. removing fields from original data item) should be integrated with original schema.
  
##### Access Schemas
   
Schema Resolver (`org.rcsb.mojave.model.SchemaResolver`) allows to fetch requested schema from schema registry.
   
Schema Registry (`org.rcsb.mojave.model.SchemaRegistry`) assigns a unique name for each schema 'flavour'.
Each type maps to a given schema by a property name defined in 'model.module.properties' resource file. 
These properties are taken from _pom.xml_ file where schemas names are configured. 

## Validation with JSON schema
Data in MongoDB has a flexible schema. Collections do not enforce document structure by default. We do want, however, 
have a clear idea of what’s going into the database. Since v3.6 MongoDB provides the capability for schema validation 
during updates and insertions. _Redwood_ uses JSON schemas as an input for MongoDB 
[JSON Schema Validator](https://docs.mongodb.com/manual/core/schema-validation/) to introduce 
validation checks at the database level so that the data integrity is ensured.

 Due to [MongoDB’s implementation](https://docs.mongodb.com/manual/reference/operator/query/jsonSchema/#omissions) 
 of JSON Schema some of the specification-compliant definitions are not supported. BSON schemas compatible with validator 
 are generated and stored in `target/generated-sources/schema/validation`.

## Versioning
 Version numbers should follow [Semantic Versioning Specification](https://semver.org/#semantic-versioning-specification-semver) 
(SemVer). Release version takes the **x.y.z** form, where **x** is the major version, **y** is the minor version, 
and **z** is the patch version (e.g. 0.1.0).
