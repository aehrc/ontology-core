package au.csiro.ontology.input;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

public enum StructuredLog {
    GenericException(null, "message", "exception"),

    OntologyGeneration("Building ontology", "id", "version"),
    UsingInferredRelationships("No stated relationships or OWL Axioms provided; using inferred relationships instead."),
    MissingMetadata("Metadata value for %s was not found. Import process might produce unexpected results.",
            "element"),

    ModuleLoadFailure("Unable to load module dependencies. Possibly broken input configuration file.",
            "inputType", "moduleId", "exception"),
    FileLoadFailure("Unable to load %s file. Possibly broken input configuration file.",
            "fileType", "inputType", "file", "exception"),
    IgnoredModules("Refset: Ignored data from module '%s' found in %s", "moduleId", "refsetFile"),

    RefsetLoadFailure("Error loading %s reference set file. Possibly has wrong number of columns.",
            "fileType", "file", "exception"),

    MissingRefsetId("Could not find refset id %s in meta-data hierarchy. There might be a problem with the concrete domains definitions.",
            "refsetId"),
    UntypedConcreteDomainRefsetId("Could not determine the type (int/float) of %s from position in hierarchy.",
            "refsetId"),

    DefinedWithoutParents("Concept %s has defining (non-ISA) relationships but no parents (ISA relationships).", "concept"),

    UndeclaredFeature("Ignoring feature %s (it has no type). There might be a problem with the concrete domains definitions.",
            "concept"),
    UnknownConcreteDomainType("Unknown concerete domain type: %s", "typeName"),
    UnknownConcreteDomainOperator("Unknown concerete domain operator: %s", "operatorId"),

    OWLUnknownReferencedComponent("Unexpected referencedComponentId %s (should be 734146004 or 734147008)", "refsetId"),
    OWLUnknownRefset("Unexpected refsetId %s (should be 733073007 or 762103008)", "refsetId"),

    InactiveDependency("Inactive dependency: %s to\t%s", "version", "requiredModule"),
    MalformedMDRSEntry("Ignoring malformed MDRS entry: %s to\t%s", "version", "requiredModule"),
    RefsetIdMismatch("refsetId does not match that for the MDRS (900000000000534007)"),
    TimesMismatch("effectiveTime and sourceEffectiveTime must be equal"),
    EffectgiveTimeOrderMismatch("effectiveTime cannot be earlier than sourceEffectiveTime"),
    SourceTimeMismatch("sourceEffectiveTime cannot be earlier than targetEffectiveTime"),

    InvalidEffectiveTime("Could not parse effectiveTime", "effectiveTime", "reasonTimestamp", "reasonDatestamp"),

    ImpliedTransitiveDependency("Added implied transitive dependency from %s to %s via %s",
            "srcModule", "targetModule", "transitiveModule"),
    CyclicDependency("Cyclic dependency"),

    GroupingError("Found grouped relationship for attribute type marked as never-grouped: %s", "attribute"),
    UngroupedConcreteDomains("Concrete domain data supplied (%s) for never-grouped attribute: %s", "relationshipId", "attribute"),

    None(null); // sentinal; do not use

    final private static String PREFIX = "json: ";

    final private ThreadLocal<UUID> uuid = ThreadLocal.withInitial(() -> UUID.randomUUID());

    final private String format;
    final private String[] keys;

    private StructuredLog(String format, String... keys) {
        this.format = format;
        this.keys = keys;
    }

    public String info(Logger log, Object... args) {
        return info((Map<String, Object>) null, log, args);
    }

    public String warn(Logger log, Object... args) {
        return warn((Map<String, Object>) null, log, args);
    }

    public String error(Logger log, Object... args) {
        return error((Map<String, Object>) null, log, args);
    }

    public String info(MapView obj, Logger log, Object... args) {
        return info(obj.toMap(), log, args);
    }

    public String warn(MapView obj, Logger log, Object... args) {
        return warn(obj.toMap(), log, args);
    }

    public String error(MapView obj, Logger log, Object... args) {
        return error(obj.toMap(), log, args);
    }

    public String info(Map<String, Object> map, Logger log, Object... args) {
        map = buildMap(log, map, args);
        log.info(PREFIX + renderMap(map));
        return (String) map.get("message");
    }

    public String warn(Map<String, Object> map, Logger log, Object... args) {
        map = buildMap(log, map, args);
        log.warn(PREFIX + renderMap(map));
        return (String) map.get("message");
    }

    public String error(Map<String, Object> map, Logger log, Object... args) {
        map = buildMap(log, map, args);
        log.error(PREFIX + renderMap(map));
        return (String) map.get("message");
    }

    public Map<String, Object> buildMap(Logger log, MapView obj, Object... args) {
        return buildMap(log, obj.toMap(), args);
    }

    public void reset() {
        uuid.set(UUID.randomUUID());
    }

    private Map<String, Object> buildMap(Logger log, Map<String, Object> map, Object... args) {
        final String message = format == null ? null : String.format(format, args);
        if (null == map) {
            map = new HashMap<>();
        }
        map.put("uuid", uuid.get());
        if (null != keys) {
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i], args[i]);
            }
        }
        if (null != message) {
            map.put("message", message);
        }
        map.put("logContext", log.getName());
        return map;
    }

    public static String renderMap(Map<String, Object> args) {
        final StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<String,Object> entry : args.entrySet()) {
            Object value = entry.getValue();
            value = renderValue(value);
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append("\"").append(entry.getKey()).append("\": ").append(value);
        }
        sb.append("}");
        return sb.toString();
    }

    private static String renderValue(Object value) {
        if (null == value) {
            value = "";
        } else if (value instanceof Collection<?>) {
            StringBuffer newValue = new StringBuffer("[");
            boolean first = true;
            for (Object contained : ((Collection<?>) value)) {
                if (!first) {
                    newValue.append(',');
                } else {
                    first = false;
                }
                newValue.append(renderValue(contained));
            }
            newValue.append("]");
            return newValue.toString();
        } else if (value instanceof Map<?,?>) {
            return renderMap((Map<String, Object>) value);
        } else if (value instanceof MapView) {
            return renderMap(((MapView) value).toMap());
        }

        if (value == null || value.toString() == null) {
            return "\"\"";
        }

        if (!(value instanceof Integer || value instanceof Long || value instanceof Boolean)) {
            value = value.toString().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t");
            value = "\"" + value + "\"";
        }

        return value.toString();
    }

}
