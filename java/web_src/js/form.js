$ = require('jquery');

minuteSliderFormatter = function(value) {
    return ("0" + Math.floor(value/60)).slice(-2) + ":" + ("0" + Math.floor(value%60)).slice(-2) + "h";
};

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

    let tasksTable = $('#tasksTable');
    $('#tasks-add-new').click(function() {
        addTasksTableRow(tasksTable);
    });

    let fixedEventsTable = $('#fixedEventsTable');
    $('#fixedEvents-add-new').click(function() {
        addFixedEventsTableRow(fixedEventsTable);
    });

    $('#submit').click(function() {
        submitParamsForm($('#params-form'))
    });

    $('.input-group.date').datetimepicker({
        locale: 'hr'
    });
});

var tasksTableRowPrototype = '<tr class="dataRow"> \
    <td class="tasksTable-name"><input type="text" name="title" class="form-control"></td> \
    <td class="tasksTable-priority"><input type="number" name="priority" size="2" class="form-control"></td> \
    <td class="tasksTable-duration">\
        <input class="minuteSlider granulated" type="text" name="duration" /> \
    </td> \
    <td class="tasksTable-deadline"> \
        <div class="input-group date"> \
            <input type="text" class="form-control" name="deadline" /> \
                <span class="input-group-addon"> \
                    <span class="glyphicon glyphicon-calendar"></span> \
                </span> \
        </div> \
    </td> \
    <td class="tasksTable-deleteField"> \
        <span aria-hidden="true" class="button-delete-row">&times;</span>\
    </td> \
</tr>';

addTasksTableRow = function(table) {
    let row = $(tasksTableRowPrototype).appendTo(table);
    row.find('.input-group.date').datetimepicker({
        locale: 'hr'
    });

    let slider = row.find('.minuteSlider').slider({
        min: 0,
        max: 1440,
        step: 10,
        value: 10,
        formatter: minuteSliderFormatter
    });

    row.find('.button-delete-row').click(function() {
        row.remove();
    })
};

var fixedEventsTableRowPrototype = '<tr class="dataRow"> \
    <td class="fixedEventsTable-name"><input type="text" name="title" class="form-control"></td> \
    <td class="fixedEventsTable-start"> \
        <div class="input-group date"> \
            <input type="text" class="form-control" name="start" /> \
                <span class="input-group-addon"> \
                    <span class="glyphicon glyphicon-calendar"></span> \
                </span> \
        </div> \
    </td> \
    <td class="fixedEventsTable-end"> \
        <div class="input-group date"> \
            <input type="text" class="form-control" name="end" /> \
                <span class="input-group-addon"> \
                    <span class="glyphicon glyphicon-calendar"></span> \
                </span> \
        </div> \
    </td> \
    <td class="fixedEventsTable-deleteField"> \
        <span aria-hidden="true" class="button-delete-row">&times;</span>\
    </td> \
</tr>';

addFixedEventsTableRow = function(table) {
    let row = $(fixedEventsTableRowPrototype).appendTo(table);
    row.find('.input-group.date').datetimepicker({
        locale: 'hr'
    });

    row.find('.button-delete-row').click(function() {
        row.remove();
    })
};

submitParamsForm = function(form) {
    let tasksDOM = form.find('#tasksTable .dataRow');
    let tasks = [];
    tasksDOM.each(function(i, row) {
        let jQrow = $(row);
        let task = {
            title: jQrow.find('input[name=title]').val(),
            priority: jQrow.find('input[name=priority]').val(),
            duration: jQrow.find('input[name=duration]').val(),
            deadline: jQrow.find('input[name=deadline]').val(),
        };

        tasks.push(task);
    });

    let fixedEventsDOM = form.find('#fixedEventsTable .dataRow');
    let fixedEvents = [];
    fixedEventsDOM.each(function(i, row) {
        let jQrow = $(row);
        let fixedEvent = {
            title: jQrow.find('input[name=title]').val(),
            start: jQrow.find('input[name=start]').val(),
            end: jQrow.find('input[name=end]').val(),
        };

        fixedEvents.push(fixedEvent);
    });

    let algoParamsDOM = form.find('input.algo_param');
    let algoParams = {};
    algoParamsDOM.each(function(i, input) {
        let jQinput = $(input);
        algoParams[jQinput.attr('name')] = jQinput.val();
    });

    $.ajax({
        method: 'POST',
        data: JSON.stringify({
            tasks: tasks,
            fixedEvents: fixedEvents,
            params: algoParams
        }),
        url: '/',
        success: function() {
            window.location.replace("/calendar");
        }
    })
};
