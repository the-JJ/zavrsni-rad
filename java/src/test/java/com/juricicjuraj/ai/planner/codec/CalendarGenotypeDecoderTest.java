package com.juricicjuraj.ai.planner.codec;

import com.juricicjuraj.ai.fasttime.Duration;
import com.juricicjuraj.ai.planner.evolution.MomentSeqFactory;
import com.juricicjuraj.ai.planner.helper.GenotypeHelper;
import com.juricicjuraj.ai.planner.model.calendar.Calendar;
import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
import com.juricicjuraj.ai.planner.model.calendar.EventType;
import com.juricicjuraj.ai.planner.model.evolution.Moment;
import org.jenetics.EnumGene;
import org.jenetics.Genotype;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.juricicjuraj.ai.planner.helper.GenotypeHelper.createGenotype;
import static org.junit.Assert.assertEquals;

public class CalendarGenotypeDecoderTest {
    private MomentSeqFactory msf;

    @Before
    public void setUp() throws Exception {
        msf = new MomentSeqFactory(
                Duration.ofMinutes(10),
                1,
                new ArrayList<>(),
                LocalDateTime.parse("2017-01-12T00:00")
        );
    }

    @Test
    public void simpleDecodingTest() throws Exception {
        // [1|1|1|1|1|1|2|2|2|0]
        Calendar expected = new Calendar();
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:00"),
                        LocalDateTime.parse("2017-01-12T01:00"), // 60 minutes duration
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:00"),
                        LocalDateTime.parse("2017-01-12T01:30"), // 30 minutes duration
                        EventType.GENERATED,
                        "2"
                )
        );

        CalendarGenotypeDecoder decoder = new CalendarGenotypeDecoder(msf);

        List<Moment> moments = GenotypeHelper.createMoments("[1|1|1|1|1|1|2|2|2|0]", msf);
        Genotype<EnumGene<Moment>> genotype = createGenotype(moments);

        Calendar actual = decoder.apply(genotype);
        assertEquals(expected, actual);
    }

    @Test
    public void noEmptyDecodingTest() throws Exception {
        // [1|1|1|1|1|1|2|2|2]
        Calendar expected = new Calendar();
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:00"),
                        LocalDateTime.parse("2017-01-12T01:00"), // 60 minutes duration
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:00"),
                        LocalDateTime.parse("2017-01-12T01:30"), // 30 minutes duration
                        EventType.GENERATED,
                        "2"
                )
        );

        CalendarGenotypeDecoder decoder = new CalendarGenotypeDecoder(msf);


        List<Moment> moments = GenotypeHelper.createMoments("[1|1|1|1|1|1|2|2|2]", msf);
        Genotype<EnumGene<Moment>> genotype = createGenotype(moments);

        Calendar actual = decoder.apply(genotype);
        assertEquals(expected, actual);
    }

    @Test
    public void shuffledDecodingTest() throws Exception {
        // [1|1|2|1|1|1|1|2|2|0]
        Calendar expected = new Calendar();
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:00"),
                        LocalDateTime.parse("2017-01-12T00:20"),
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:20"),
                        LocalDateTime.parse("2017-01-12T00:30"),
                        EventType.GENERATED,
                        "2"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:30"),
                        LocalDateTime.parse("2017-01-12T01:10"),
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:10"),
                        LocalDateTime.parse("2017-01-12T01:30"),
                        EventType.GENERATED,
                        "2"
                )
        );

        CalendarGenotypeDecoder decoder = new CalendarGenotypeDecoder(msf);

        List<Moment> moments = GenotypeHelper.createMoments("[1|1|2|1|1|1|1|2|2|0]", msf);
        Genotype<EnumGene<Moment>> genotype = createGenotype(moments);

        Calendar actual = decoder.apply(genotype);
        assertEquals(expected, actual);
    }

    @Test
    public void slicedDecodingTest() throws Exception {
        // [1|2|0|0|0|0|2|1|2|1]
        Calendar expected = new Calendar();
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:00"),
                        LocalDateTime.parse("2017-01-12T00:10"),
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:10"),
                        LocalDateTime.parse("2017-01-12T00:20"),
                        EventType.GENERATED,
                        "2"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:00"),
                        LocalDateTime.parse("2017-01-12T01:10"),
                        EventType.GENERATED,
                        "2"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:10"),
                        LocalDateTime.parse("2017-01-12T01:20"),
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:20"),
                        LocalDateTime.parse("2017-01-12T01:30"),
                        EventType.GENERATED,
                        "2"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:30"),
                        LocalDateTime.parse("2017-01-12T01:40"),
                        EventType.GENERATED,
                        "1"
                )
        );

        CalendarGenotypeDecoder decoder = new CalendarGenotypeDecoder(msf);

        List<Moment> moments = GenotypeHelper.createMoments("[1|2|0|0|0|0|2|1|2|1]", msf);
        Genotype<EnumGene<Moment>> genotype = createGenotype(moments);

        Calendar actual = decoder.apply(genotype);
        assertEquals(expected, actual);
    }

    @Test
    public void emptyBeginningTest() throws Exception {
        // [0|1|1|1|1|1|2|2|2]
        Calendar expected = new Calendar();
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:10"),
                        LocalDateTime.parse("2017-01-12T01:00"),
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:00"),
                        LocalDateTime.parse("2017-01-12T01:30"),
                        EventType.GENERATED,
                        "2"
                )
        );

        CalendarGenotypeDecoder decoder = new CalendarGenotypeDecoder(msf);

        List<Moment> moments = GenotypeHelper.createMoments("[0|1|1|1|1|1|2|2|2]", msf);
        Genotype<EnumGene<Moment>> genotype = createGenotype(moments);

        Calendar actual = decoder.apply(genotype);
        assertEquals(expected, actual);
    }

    @Test
    public void longerEmptyBeginningTest() throws Exception {
        // [0|0|0|0|0|0|0|1|1|1|1|1|0|2|2|2]
        Calendar expected = new Calendar();
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T01:10"),
                        LocalDateTime.parse("2017-01-12T02:00"),
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T02:10"),
                        LocalDateTime.parse("2017-01-12T02:40"),
                        EventType.GENERATED,
                        "2"
                )
        );

        CalendarGenotypeDecoder decoder = new CalendarGenotypeDecoder(msf);

        List<Moment> moments = GenotypeHelper.createMoments("[0|0|0|0|0|0|0|1|1|1|1|1|0|2|2|2]", msf);
        Genotype<EnumGene<Moment>> genotype = createGenotype(moments);

        Calendar actual = decoder.apply(genotype);
        assertEquals(expected, actual);
    }


    @Test
    public void fiveMinuteAtomDecodingTest() throws Exception {
        // [1|2|0|0|0|0|2|1|2|2|1]
        msf = new MomentSeqFactory(Duration.ofMinutes(5),
                1,
                new ArrayList<>(),
                LocalDateTime.parse("2017-01-12T00:00"));

        Calendar expected = new Calendar();
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:00"),
                        LocalDateTime.parse("2017-01-12T00:05"),
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:05"),
                        LocalDateTime.parse("2017-01-12T00:10"),
                        EventType.GENERATED,
                        "2"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:30"),
                        LocalDateTime.parse("2017-01-12T00:35"),
                        EventType.GENERATED,
                        "2"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:35"),
                        LocalDateTime.parse("2017-01-12T00:40"),
                        EventType.GENERATED,
                        "1"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:40"),
                        LocalDateTime.parse("2017-01-12T00:50"),
                        EventType.GENERATED,
                        "2"
                )
        );
        expected.add(
                new CalendarEvent(
                        LocalDateTime.parse("2017-01-12T00:50"),
                        LocalDateTime.parse("2017-01-12T00:55"),
                        EventType.GENERATED,
                        "1"
                )
        );

        CalendarGenotypeDecoder decoder = new CalendarGenotypeDecoder(msf);

        List<Moment> moments = GenotypeHelper.createMoments("[1|2|0|0|0|0|2|1|2|2|1]", msf);
        Genotype<EnumGene<Moment>> genotype = createGenotype(moments);

        Calendar actual = decoder.apply(genotype);
        assertEquals(expected, actual);
    }
}
