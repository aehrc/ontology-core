package au.csiro.ontology.snomed.refset.rf2;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class ModuleDependencyRefsetTest {
    final String MDRS_ID = "900000000000534007";

    @Test
    public void test() throws ValidationException {
        Set<ModuleDependencyRow> members = new HashSet<ModuleDependencyRow>();
        members.add(new ModuleDependencyRow("a", "20010101", true, "A", MDRS_ID, "B", "20010101", "10010101"));
        members.add(new ModuleDependencyRow("b", "10010101", true, "B", MDRS_ID, "C", "10010101", "10010101"));
        members.add(new ModuleDependencyRow("c", "20010101", true, "A", MDRS_ID, "C", "20010101", "10010101"));
        new ModuleDependencyRefset(members, true);
    }

    @Test
    public void testIncomplete() throws ValidationException {
        Set<ModuleDependencyRow> members = new HashSet<ModuleDependencyRow>();
        members.add(new ModuleDependencyRow("a", "20010101", true, "A", MDRS_ID, "B", "20010101", "10010101"));
        members.add(new ModuleDependencyRow("b", "10010101", true, "B", MDRS_ID, "C", "10010101", "10010101"));
        ModuleDependencyRefset mdrs = new ModuleDependencyRefset(members, true);
        Assert.assertEquals(2, mdrs.getModuleDependencies().size());
    }

}
