package au.csiro.ontology.snomed.refset.rf2;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

public class ModuleDependencyRefsetTest {

    @Test
    public void test() {
        Logger log = Logger.getLogger(ModuleDependencyRefset.class);
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
                throw new AssertionError("Should not log: " + event.getMessage());
            }
        });
        Set<ModuleDependencyRow> members = new HashSet<ModuleDependencyRow>();
        members.add(new ModuleDependencyRow("a", "20010101", true, "A", "A", "B", "20010101", "10010101"));
        members.add(new ModuleDependencyRow("b", "10010101", true, "B", "A", "C", "10010101", "10010101"));
        members.add(new ModuleDependencyRow("c", "20010101", true, "A", "A", "C", "20010101", "10010101"));
        new ModuleDependencyRefset(members);
    }

    @Test(expected=AssertionError.class)
    public void testIncomplete() {
        Logger log = Logger.getLogger(ModuleDependencyRefset.class);
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
                throw new AssertionError("Should not log: " + event.getMessage());
            }
        });
        Set<ModuleDependencyRow> members = new HashSet<ModuleDependencyRow>();
        members.add(new ModuleDependencyRow("a", "20010101", true, "A", "A", "B", "20010101", "10010101"));
        members.add(new ModuleDependencyRow("b", "10010101", true, "B", "A", "C", "10010101", "10010101"));
        new ModuleDependencyRefset(members);
    }

}
