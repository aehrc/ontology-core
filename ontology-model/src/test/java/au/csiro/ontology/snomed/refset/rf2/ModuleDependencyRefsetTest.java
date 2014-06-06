package au.csiro.ontology.snomed.refset.rf2;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

public class ModuleDependencyRefsetTest {
    final String MDRS_ID = "900000000000534007";

    @Test
    public void test() throws ValidationException {
        Logger log = Logger.getLogger(ModuleDependencyRefset.class);
        log.removeAllAppenders();
        log.addAppender(new AppenderSkeleton(true) {

            @Override
            public boolean requiresLayout() {
                return false;
            }

            @Override
            public void close() {
            }

            @Override
            protected void append(LoggingEvent event) {
                final String message = String.valueOf(event.getMessage());
                if (!message.startsWith("MDRS entry for version:")) {
                    throw new AssertionError("Should not log: " + message);
                }
            }
        });
        Set<ModuleDependencyRow> members = new HashSet<ModuleDependencyRow>();
        members.add(new ModuleDependencyRow("a", "20010101", true, "A", MDRS_ID, "B", "20010101", "10010101"));
        members.add(new ModuleDependencyRow("b", "10010101", true, "B", MDRS_ID, "C", "10010101", "10010101"));
        members.add(new ModuleDependencyRow("c", "20010101", true, "A", MDRS_ID, "C", "20010101", "10010101"));
        new ModuleDependencyRefset(members, true);
    }

    @Test(expected=RuntimeException.class)
    public void testIncomplete() throws ValidationException {
        Logger log = Logger.getLogger(ModuleDependencyRefset.class);
        log.removeAllAppenders();
        log.addAppender(new AppenderSkeleton(true) {

            @Override
            public boolean requiresLayout() {
                return false;
            }

            @Override
            public void close() {
            }

            @Override
            protected void append(LoggingEvent event) {
                final String message = String.valueOf(event.getMessage());
                if (message.startsWith("Added implied transitive dependency")) {
                    throw new RuntimeException("Should not log: " + message);
                } else if (!message.startsWith("MDRS entry for version:")) {
                    throw new AssertionError("Should not log: " + message);
                }
            }
        });
        Set<ModuleDependencyRow> members = new HashSet<ModuleDependencyRow>();
        members.add(new ModuleDependencyRow("a", "20010101", true, "A", MDRS_ID, "B", "20010101", "10010101"));
        members.add(new ModuleDependencyRow("b", "10010101", true, "B", MDRS_ID, "C", "10010101", "10010101"));
        new ModuleDependencyRefset(members, true);
    }

}
