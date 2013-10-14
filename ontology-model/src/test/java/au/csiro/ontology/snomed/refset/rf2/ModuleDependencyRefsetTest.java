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
        members.add(new ModuleDependencyRow("a", "2", true, "A", "A", "B", "2", "1"));
        members.add(new ModuleDependencyRow("b", "1", true, "B", "A", "C", "1", "1"));
        members.add(new ModuleDependencyRow("c", "2", true, "A", "A", "C", "2", "1"));
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
        members.add(new ModuleDependencyRow("a", "2", true, "A", "A", "B", "2", "1"));
        members.add(new ModuleDependencyRow("b", "1", true, "B", "A", "C", "1", "1"));
        new ModuleDependencyRefset(members);
    }

}
