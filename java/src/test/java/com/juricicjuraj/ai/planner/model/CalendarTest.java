//package com.juricicjuraj.ai.planner.model;
//
//import com.juricicjuraj.ai.fasttime.Duration;
//import com.juricicjuraj.ai.planner.model.calendar.Calendar;
//import com.juricicjuraj.ai.planner.model.calendar.CalendarEvent;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.Assert.*;
//
//public class CalendarTest {
//
//	private static final LocalDateTime INSTANT_201701112300 = LocalDateTime.parse("2017-01-11T23:00");
//	private static final LocalDateTime INSTANT_201701120000 = LocalDateTime.parse("2017-01-12T00:00");
//	private static final LocalDateTime INSTANT_201701120100 = LocalDateTime.parse("2017-01-12T01:00");
//	private static final LocalDateTime INSTANT_201701120200 = LocalDateTime.parse("2017-01-12T02:00");
//	private static final LocalDateTime INSTANT_201701120300 = LocalDateTime.parse("2017-01-12T03:00");
//	private static final LocalDateTime INSTANT_201701120400 = LocalDateTime.parse("2017-01-12T04:00");
//	private static final LocalDateTime INSTANT_201701120500 = LocalDateTime.parse("2017-01-12T05:00");
//	private static final LocalDateTime INSTANT_201701120600 = LocalDateTime.parse("2017-01-12T06:00");
//	private static final LocalDateTime INSTANT_201701120700 = LocalDateTime.parse("2017-01-12T07:00");
//
//	private Calendar calendar1;
//	private Calendar calendar2;
//	private Calendar calendar3;
//	private Calendar calendar4;
//	private Calendar calendar5;
//	private Calendar calendar6;
//
//	private CalendarEvent event1;
//	private CalendarEvent event2;
//
//	@Before
//	public void initialize(){
//		calendar1 = new Calendar();
//		calendar2 = new Calendar();
//		calendar3 = new Calendar();
//		calendar4 = new Calendar();
//		calendar5 = new Calendar();
//		calendar6 = new Calendar();
//
//		// calendar 1
//		// Jan 12, 2017 03:00 - 06:00
//		calendar1.add(new CalendarEvent(INSTANT_201701120300, INSTANT_201701120600));
//		// Jan 12, 2017 06:00 - 07:00
//		calendar1.add(new CalendarEvent(INSTANT_201701120600, INSTANT_201701120700));
//
//		// calendar 2
//		// Jan 11, 2017 23:00 - Jan 12, 2017 04:00
//		calendar2.add(new CalendarEvent(INSTANT_201701112300, INSTANT_201701120400));
//		// Jan 12, 2017 00:00 (duration = 0)
//		calendar2.add(new CalendarEvent(INSTANT_201701120000, Duration.ZERO));
//
//		// calendar 3
//		// Jan 12, 2017 00:00 - Jan 12, 2017 05:00
//		calendar3.add(new CalendarEvent(INSTANT_201701120000, INSTANT_201701120500));
//		// Jan 12, 2017 00:00 - Jan 12, 2017 03:00
//		calendar3.add(new CalendarEvent(INSTANT_201701120000, INSTANT_201701120300));
//
//		// calendar 4
//		// Jan 12, 2017 00:00 - Jan 12, 2017 05:00
//		calendar4.add(new CalendarEvent(INSTANT_201701120000, INSTANT_201701120500));
//		// Jan 12, 2017 00:00 - Jan 12, 2017 03:00
//		calendar4.add(new CalendarEvent(INSTANT_201701120000, INSTANT_201701120300));
//		// Jan 12, 2017 04:00 - Jan 12, 2017 07:00
//		calendar4.add(new CalendarEvent(INSTANT_201701120400, INSTANT_201701120700));
//
//		// calendar 5
//		// Jan 12, 2017 00:00 - Jan 12, 2017 04:00
//		calendar5.add(new CalendarEvent(INSTANT_201701120000, INSTANT_201701120400));
//		// Jan 12, 2017 01:00 - Jan 12, 2017 03:00
//		event1 = new CalendarEvent(INSTANT_201701120100, INSTANT_201701120300);
//		calendar5.add(event1);
//		// Jan 12, 2017 02:00 - Jan 12, 2017 04:00
//		event2 = new CalendarEvent(INSTANT_201701120200, INSTANT_201701120400);
//		calendar5.add(event2);
//
//		// calendar 6
//		// Jan 12, 2017 00:00 - Jan 12, 2017 05:00
//		calendar6.add(new CalendarEvent(INSTANT_201701120000, INSTANT_201701120500));
//		// Jan 12, 2017 01:00 - Jan 12, 2017 02:00
//		calendar6.add(new CalendarEvent(INSTANT_201701120100, INSTANT_201701120200));
//		// Jan 12, 2017 03:00 - Jan 12, 2017 04:00
//		calendar6.add(new CalendarEvent(INSTANT_201701120300, INSTANT_201701120400));
//	}
//
//	@Test
//	public void countCollisionsNoCollisions() throws Exception {
//		// calendar 1 - no collision
//		assertEquals(0, calendar1.countCollisions());
//	}
//
//	@Test
//	public void countCollisions1() throws Exception {
//		// calendar 2 - 1 collision
//		assertEquals(1, calendar2.countCollisions());
//	}
//
//	@Test
//	public void countCollisions2() throws Exception {
//		// calendar 3 - 1 collision
//		assertEquals(1, calendar3.countCollisions());
//	}
//
//	@Test
//	public void countCollisions3() throws Exception {
//		// calendar 4 - 2 collisions (e1 && e2, e1 && e3)
//		assertEquals(2, calendar4.countCollisions());
//	}
//
//	@Test
//	public void countCollisions4() throws Exception {
//		//calendar 5 - 3 collisions (e1 && e2, e1 && e3, e2 && e3)
//		assertEquals(3, calendar5.countCollisions());
//	}
//
//	@Test
//	public void countCollisions5() throws Exception {
//		//calendar 6 - 2 collisions (e1 && e2, e1 && e3)
//		assertEquals(2, calendar6.countCollisions());
//	}
//
//	@Test
//	public void calendarSum() throws Exception {
//		Calendar calendar12 = new Calendar();
//		calendar12.addAll(calendar1);
//		calendar12.addAll(calendar2);
//
//		assertEquals(calendar1.size() + calendar2.size(), calendar12.size());
//	}
//
//	@Test
//	public void calendarFrom() throws Exception {
//		Calendar calendarFrom = calendar5.from(LocalDateTime.parse("2017-01-12T01:30"), false);
//		assertEquals(1, calendarFrom.size());
//
//		assertFalse(calendarFrom.getEventSet().contains(event1));
//		assertTrue(calendarFrom.getEventSet().contains(event2));
//	}
//
//	@Test
//	public void calendarTo() throws Exception {
//		Calendar calendarFrom = calendar5.to(LocalDateTime.parse("2017-01-12T01:30"), false);
//		assertEquals(2, calendarFrom.size());
//
//		assertFalse(calendarFrom.getEventSet().contains(event2));
//		assertTrue(calendarFrom.getEventSet().contains(event1));
//	}
//
//}