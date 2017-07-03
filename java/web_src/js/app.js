import './form.js';
import './calendar.js';

$(function() {
    $('.minuteSlider').slider({
        formatter: minuteSliderFormatter
    });
});
