$ = require('jquery');
import fullCalendar from 'fullcalendar';

$(function() {
    var calendar = $('#calendar').fullCalendar({
        locale: "hr",
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'listHalfYear,month,agendaWeek,agendaDay'
        },
        allDaySlot: true,
        navLinks: true, // can click day/week names to navigate views
        editable: false,
        eventLimit: false,
        defaultView: 'agendaWeek',
        views: {
            listHalfYear: {
                type: 'list',
                duration: { months: 6 }
            }
        },
        height: 'auto',
        events: {
            url: '/events',
            error: function(response) {
                let message = response.responseJSON.error;
                alert("Error: " + message);
            }
        }
    });
});
