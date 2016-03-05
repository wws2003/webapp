function refreshBuildStreamContent(workspaceId) {
	var interval = 1000;
    var refresh = function() {
        $.ajax({
            url: "/autospring/buildingstream/" + workspaceId,
            cache: false,
            success: function(buildStreamContent) {
                $('#txta_buildstream').val(buildStreamContent);
                $('#txta_buildstream').scrollTop($('#txta_buildstream')[0].scrollHeight);
                setTimeout(function() {
                    refresh();
                	}, 
                	interval);
            }
        });
    };
    refresh();
}