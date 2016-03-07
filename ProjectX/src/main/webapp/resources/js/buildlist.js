function cancelTask(taskIdInQueue) {
	var btnId = "#btn_cancel_" + taskIdInQueue;
	$.ajax({
        url: "/autospring/cancel/" + taskIdInQueue,
        cache: false,
        success: function(response) {
        	swal("Cancel result", response);
            $(btnId).prop("disabled", true);
        }
    });
}