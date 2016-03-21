
$(document).ready(function() {
	setupRadioBtns();
});

function setupRadioBtns() {
	$("#rbtn_buildscript_write").click(function() {
		if ($(this).is(':checked')) {
			onSelectedModeForBuildScript(true);
		}
	});
	
	$("#rbtn_buildscript_select").click(function() {
		if ($(this).is(':checked')) {
			onSelectedModeForBuildScript(false);
		}
	});
	
	$("#rbtn_buildscript_write").prop("checked", true);
	onSelectedModeForBuildScript(true);
}

function loadPossibleBuildScripts(workspaceId) {
	$.ajax({
        url: "/autospring/api/getPossibleBuildScripts/" + workspaceId,
        cache: false,
        success: function(browsingObjects) {
        	$.each(browsingObjects, function(index, browsingObject) {
        		renderBrowsingObjectForBuildScript(browsingObject);
        	});
        	setupFileSelectBtns();
        }, 
        error: renderError
    });
}

function onSelectedModeForBuildScript(toWrite) {
	resetBuildScriptInput(toWrite);
	resetBuildScriptSelection(!toWrite);
}

function resetBuildScriptInput(enabled) {
	$("#txt_buildscript_name").prop("readonly", !enabled);
	$("#txta_buildscript_content").prop("readonly", !enabled);
	resetElementSelection($("#txt_buildscript_name"), enabled);
	resetElementSelection($("#txta_buildscript_content"), enabled);
	//Should reset name of build script to be default ?
	if(enabled) {
		$("#txt_buildscript_name").val("build_script.sh");	
	}
}

function resetBuildScriptSelection(enabled) {
	$("#div_select_options button").each(function() {
		$(this).prop("disabled", !enabled);
	});
	resetElementSelection($("#div_select"), enabled);
}

function resetHighlight(btn) {
	//Reset color for all button div
	$("#div_select_options div").each(function() {
		$(this).removeClass("highlight");
	});
	
	//Highlight the clicked button div only
	btn.parent().addClass("highlight");
}

function resetElementSelection(element, selected) {
	if(!selected) {
		element.addClass("deselected");
	}
	else {
		element.removeClass("deselected");
	}
}

function renderBrowsingObjectForBuildScript(browsingObject) {
	var btnHtml = "<div><button value='" + browsingObject.id + "'>" + browsingObject.name  + "</button></div>";
	$("#div_select_options").append(btnHtml);
}

function setupFileSelectBtns() {
	$("#div_select_options button").click(function() {
		resetHighlight($(this));
		loadSelectedFile($(this));
	});
}

function renderError() {
	var messageHTML = "<div>Some things went wrong<div>";
	var messageObj = $(messageHTML);
	messageObj.css("color", "red");
	$("div_select_options").append(messageObj);
}

function loadSelectedFile(btn) {
	//Set file name as build script name
	$("#txt_buildscript_name").val(btn.text());
	
	//Load by AJAX content of the file
	$.ajax({
        url: "/autospring/api/getFileContent/" + btn.val(),
        cache: false,
        success: function(fileContent) {
        	$("#txta_buildscript_content").val(fileContent);
        }
    });
}