package com.juricicjuraj.ai.planner.helper;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.juricicjuraj.ai.planner.model.Task;
import com.juricicjuraj.ai.planner.model.evolution.EmptyMoment;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import com.juricicjuraj.ai.planner.model.evolution.TaskMoment;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * I know, right? :)
 */
public class GenotypeHelperTest {
    @Test
    public void createMomentTest() throws Exception {
        // [1|1|1|1|1|1|2|2|2|0]
        MomentSeqFactory msf = new MomentSeqFactory(
                Duration.ofMinutes(10),
                1,
                new ArrayList<>(),
                LocalDateTime.parse("2017-01-12T00:00")
        );

        Task task1 = new Task("1", Duration.ONE_HOUR, LocalDateTime.parse("2017-01-12T00:00"));
        Task task2 = new Task("2", Duration.HALF_HOUR, LocalDateTime.parse("2017-01-12T00:00"));

        List<Moment> expected = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            expected.add(new TaskMoment(task1, i, msf));
        }
        for (int i = 0; i < 3; i++) {
            expected.add(new TaskMoment(task2, i, msf));
        }
        expected.add(EmptyMoment.create(msf));

        List<Moment> actual = GenotypeHelper.createMoments("[1|1|1|1|1|1|2|2|2|0]", msf);

        // asserts
        assertEquals(expected, actual);
    }

    @Test
    public void createOneEmptyMomentTest() throws Exception {
        // [0]
        MomentSeqFactory msf = new MomentSeqFactory(
                Duration.ofMinutes(10),
                1,
                new ArrayList<>(),
                LocalDateTime.parse("2017-01-12T00:00")
        );

        List<Moment> expected = new ArrayList<>();
        expected.add(EmptyMoment.create(msf));

        List<Moment> actual = GenotypeHelper.createMoments("[0]", msf);

        // asserts
        assertEquals(expected, actual);
    }

    private void assertEquals(List<Moment> expected, List<Moment> actual) {
        Assert.assertEquals(expected.size(), actual.size());
        int size = actual.size();
        for(int i = 0; i < size; i++) {
            Moment expectedI = expected.get(i);
            Moment actualI = actual.get(i);
            if (expectedI instanceof EmptyMoment) {
                assertTrue(actualI instanceof EmptyMoment);
                continue;
            }
            if (expectedI instanceof TaskMoment) {
                assertTrue(actualI instanceof TaskMoment);

                String expectedName = ((TaskMoment) expectedI).task.name;
                String actualName = ((TaskMoment) actualI).task.name;
                Assert.assertEquals(expectedName, actualName);
                continue;
            }

            throw new IllegalStateException("Unexpected type??");
        }
    }
}
