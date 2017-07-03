function minuteSliderFormatter(value) {
    return ("0" + Math.floor(value/60)).slice(-2) + ":" + ("0" + Math.floor(value%60)).slice(-2) + "h";
}

$(document).ready(function() {
    // slider
    $('.numSlider').slider({
        formatter: function(value) {
            return 'Trenutna vrijednost: ' + value;
        }
    });

    $('.minuteSlider').slider({
        formatter: minuteSliderFormatter
    });

    $('#timeGranulation').on("slide", function(evt) {
        $('.minuteSlider.granulated').slider('setAttribute', 'step', evt.value);
    });

    var calendar = $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'listHalfYear,month,agendaWeek,agendaDay'
        },
        allDaySlot: true,
        defaultDate: '2017-01-12',
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
        events: {
            url: '/events',
            error: function(response) {
                var message = response.responseJSON.error;
                alert("Error: " + message);
            }
        }
    });
});

var tasksTableRowPrototype = '<tr id="tasksTable-row-prototype"> \
    <th class="tasksTable-id"></th> \
    <th class="tasksTable-name"><input type="text" name="tasks_name[]"></th> \
    <th class="tasksTable-duration">\
        <input class="minuteSlider granulated" type="text" /> \
    </th> \
    <th class="tasksTable-deadline"> \
        <div class="input-group date"> \
            <input type="text" class="form-control" name="deadline[]" /> \
                <span class="input-group-addon"> \
                    <span class="glyphicon glyphicon-calendar"></span> \
                </span> \
        </div> \
    </th> \
</tr>';
var granulatedSliders = [];
function addTasksTableRow(tasksTable) {
    var row = $(tasksTableRowPrototype).appendTo(tasksTable);
    row.find('.input-group.date').datetimepicker({
        locale: 'hr'
    });

    var slider = row.find('.minuteSlider').slider({
        min: 0,
        max: 1440,
        step: 10,
        value: 10,
        formatter: minuteSliderFormatter
    });
    granulatedSliders.push(slider);
}
