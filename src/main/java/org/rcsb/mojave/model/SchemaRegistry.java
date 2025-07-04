package org.rcsb.mojave.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Schema Registry assigns unique names for each Schema type that can be resolved. Each type has its own priority,
 * hardcoded inside, that maps a given type to a property name in 'model.module.properties' file.
 *
 * Created on 10/2/18.
 *
 * @author Yana Valasatava
 * @since 1.0.0
 */
public enum SchemaRegistry {

    GROUP_PROVENANCE_JSON("group.provenance.json.schema.name"),
    GROUP_PROVENANCE_BSON("validation.group.provenance.bson.schema.name"),

    GROUP_ENTRY_JSON("group.entry.json.schema.name"),
    GROUP_ENTRY_BSON("validation.group.entry.bson.schema.name"),

    GROUP_POLYMER_ENTITY_JSON("group.polymer.entity.json.schema.name"),
    GROUP_POLYMER_ENTITY_BSON("validation.group.polymer.entity.bson.schema.name"),

    GROUP_NON_POLYMER_ENTITY_JSON("group.non_polymer.entity.json.schema.name"),
    GROUP_NON_POLYMER_ENTITY_BSON("validation.group.non_polymer.entity.bson.schema.name"),

    CORE_ASSEMBLY_JSON("core.assembly.json.schema.name"),
    CORE_ASSEMBLY_BSON("validation.assembly.bson.schema.name"),

    CORE_INTERFACE_JSON("core.interface.json.schema.name"),
    CORE_INTERFACE_BSON("validation.interface.bson.schema.name"),

    CORE_ENTRY_JSON("core.entry.json.schema.name"),
    CORE_ENTRY_BSON("validation.entry.bson.schema.name"),

    CORE_POLYMER_ENTITY_JSON("core.polymer.entity.json.schema.name"),
    CORE_POLYMER_ENTITY_BSON("validation.polymer.entity.bson.schema.name"),

    CORE_NONPOLYMER_ENTITY_JSON("core.nonpolymer.entity.json.schema.name"),
    CORE_NONPOLYMER_ENTITY_BSON("validation.nonpolymer.entity.bson.schema.name"),

    CORE_BRANCHED_ENTITY_JSON("core.branched.entity.json.schema.name"),
    CORE_BRANCHED_ENTITY_BSON("validation.branched.entity.bson.schema.name"),

    CORE_POLYMER_ENTITY_INSTANCE_JSON("core.polymer.entity.instance.json.schema.name"),
    CORE_POLYMER_ENTITY_INSTANCE_BSON("validation.polymer.entity.instance.bson.schema.name"),

    CORE_NONPOLYMER_ENTITY_INSTANCE_JSON("core.nonpolymer.entity.instance.json.schema.name"),
    CORE_NONPOLYMER_ENTITY_INSTANCE_BSON("validation.nonpolymer.entity.instance.bson.schema.name"),

    CORE_BRANCHED_ENTITY_INSTANCE_JSON("core.branched.entity.instance.json.schema.name"),
    CORE_BRANCHED_ENTITY_INSTANCE_BSON("validation.branched.entity.instance.bson.schema.name"),

    CORE_UNIPROT_JSON("core.uniprot.json.schema.name"),
    CORE_UNIPROT_BSON("validation.uniprot.bson.schema.name"),

    CORE_NCBI_JSON("core.ncbi.json.schema.name"),
    CORE_NCBI_BSON("validation.ncbi.bson.schema.name"),

    CORE_BIRD_CHEMCOMP_JSON("core.bird.chemcomp.json.schema.name"),
    CORE_BIRD_CHEMCOMP_BSON("validation.bird.chemcomp.bson.schema.name"),

    CORE_DRUGBANK_JSON("core.drugbank.json.schema.name"),
    CORE_DRUGBANK_BSON("validation.drugbank.bson.schema.name"),

    CORE_PFAM_JSON("core.pfam.json.schema.name"),
    CORE_PFAM_BSON("validation.pfam.bson.schema.name"),

    CORE_PUBMED_JSON("core.pubmed.json.schema.name"),
    CORE_PUBMED_BSON("validation.pubmed.bson.schema.name"),

    TREE_JSON("core.graph.json.schema.name"),
    TREE_BSON("validation.graph.bson.schema.name"),

    CURRENT_ENTRIES_JSON("holdings.current.entry.json.schema.name"),
    CURRENT_ENTRIES_BSON("validation.current.entry.bson.schema.name"),

    REMOVED_ENTRIES_JSON("holdings.removed.entry.json.schema.name"),
    REMOVED_ENTRIES_BSON("validation.removed.entry.bson.schema.name"),

    UNRELEASED_ENTRIES_JSON("holdings.unreleased.entry.json.schema.name"),
    UNRELEASED_ENTRIES_BSON("validation.unreleased.entry.bson.schema.name"),

    COMBINED_ENTRIES_JSON("holdings.combined.entry.json.schema.name"),
    COMBINED_ENTRIES_BSON("validation.combined.entry.bson.schema.name"),

    SHAPE_DESCRIPTOR_JSON("shape.json.schema.name"),
    SHAPE_DESCRIPTOR_BSON("validation.shape.bson.schema.name"),

    ANNOTATIONS_UNIPROT_DESCRIPTOR_JSON("annotations.uniprot.json.schema.name"),
    ANNOTATIONS_UNIPROT_DESCRIPTOR_BSON("validation.annotations.uniprot.bson.schema.name"),

    ANNOTATIONS_POLYMER_ENTITY_DESCRIPTOR_JSON("annotations.polymer.entity.json.schema.name"),
    ANNOTATIONS_POLYMER_ENTITY_DESCRIPTOR_BSON("validation.annotations.polymer.entity.bson.schema.name"),

    ANNOTATIONS_POLYMER_ENTITY_INSTANCE_DESCRIPTOR_JSON("annotations.polymer.entity.instance.json.schema.name"),
    ANNOTATIONS_POLYMER_ENTITY_INSTANCE_DESCRIPTOR_BSON("validation.annotations.polymer.entity.instance.bson.schema.name"),

    ANNOTATIONS_INTERFACE_POLYMER_ENTITY_INSTANCE_DESCRIPTOR_JSON("annotations.interface.polymer.entity.instance.json.schema.name"),
    ANNOTATIONS_INTERFACE_POLYMER_ENTITY_INSTANCE_DESCRIPTOR_BSON("validation.annotations.interface.polymer.entity.instance.bson.schema.name"),
    SEQUENCE_COORDINATES_ALIGNMENTS_JSON("sequence.coordinates.alignments.json.schema.name"),
    SEQUENCE_COORDINATES_ALIGNMENTS_BSON("validation.sequence.coordinates.alignments.bson.schema.name"),
    SEQUENCE_COORDINATES_SEQUENCES_JSON("sequence.coordinates.sequences.json.schema.name"),
    SEQUENCE_COORDINATES_SEQUENCES_BSON("validation.sequence.coordinates.sequences.bson.schema.name"),
    SEQUENCE_COORDINATES_ANNOTATIONS_JSON("sequence.coordinates.annotations.json.schema.name"),
    SEQUENCE_COORDINATES_ANNOTATIONS_BSON("validation.sequence.coordinates.annotations.bson.schema.name"),
    SEQUENCE_COORDINATES_PDB_INSTANCE_MAP_JSON("sequence.coordinates.map.json.schema.name"),
    SEQUENCE_COORDINATES_PDB_INSTANCE_MAP_BSON("validation.sequence.coordinates.map.bson.schema.name"),

    EMBEDDINGS_JSON("embeddings.json.schema.name"),
    EMBEDDINGS_BSON("validation.embeddings.bson.schema.name");


    private final String value;
    private static final Map<String, SchemaRegistry> CONSTANTS = new HashMap<>();

    static {
        for (SchemaRegistry c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    SchemaRegistry(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static SchemaRegistry fromValue(String value) {
        SchemaRegistry constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
