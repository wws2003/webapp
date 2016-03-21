function resetBuildScriptInput(enabled) {
	$("#txt_buildscript_name").prop("disabled", !enabled);
	$("#txt_buildscript_name").css("background", enabled ? "#ffffff" : "rgb(235, 235, 228)");
	$("#txta_buildscript_content").prop("disabled", !enabled);
	$("#txta_buildscript_content").css("background", enabled ? "#ffffff" : "rgb(235, 235, 228)");
}

$(document).ready(function() {
	
	$("#rbtn_buildscript_later").click(function() {
		if ($(this).is(':checked')) {
			resetBuildScriptInput(false);
		}
	});
	
	$("#rbtn_buildscript_submit").click(function() {
		if ($(this).is(':checked')) {
			resetBuildScriptInput(true);
		}
	});
	
	$("#rbtn_buildscript_submit").prop("checked", true);
});