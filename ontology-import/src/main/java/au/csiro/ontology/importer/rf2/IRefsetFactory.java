/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import au.csiro.ontology.snomed.refset.rf2.RefsetRow;

/**
 * @author Alejandro Metke
 *
 */
interface IRefsetFactory<T extends RefsetRow> {

    static IRefsetFactory<RefsetRow> MODULE_DEPENDENCY = new IRefsetFactory<RefsetRow>() {
        final private String[] MD_COLS = { "sourceEffectiveTime", "targetEffectiveTime" };

        @Override
        public RefsetRow create(String id, String effectiveTime, String active, String moduleId, String refsetId,
                String referencedComponentId, String... fields) {
            assert fields.length == MD_COLS.length;
            return new RefsetRow(id, effectiveTime, active, moduleId, refsetId, referencedComponentId, fields) {
                @Override
                public String[] getColumns() {
                    return MD_COLS;
                }
            };
        }
    };

    static IRefsetFactory<RefsetRow> CD = new IRefsetFactory<RefsetRow>() {
        final private String[] CD_COLS = { "unitId", "operatorId", "value" };

        @Override
        public RefsetRow create(String id, String effectiveTime, String active, String moduleId, String refsetId,
                String referencedComponentId, String... fields) {
            assert fields.length == CD_COLS.length;
            return new RefsetRow(referencedComponentId, effectiveTime, active, moduleId, refsetId, 
                    referencedComponentId, fields) {
                @Override
                public String[] getColumns() {
                    return CD_COLS;
                }
            };
        }
    };

    T create(String id, String effectiveTime, String active, String moduleId, String refsetId, 
            String referencedComponentId, String... fields);
}
