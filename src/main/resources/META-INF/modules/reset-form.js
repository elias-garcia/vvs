// This module observes the "firstName" element, switching its color on every keystroke.

define(["jquery"], function($) {

    return function() {
        $("form").on("submit", function() {
            if (!$("form-group").hasClass("has-errors") && !$("div").hasClass("alert")){
                $("form").reset();
            }
        });
    }

})
