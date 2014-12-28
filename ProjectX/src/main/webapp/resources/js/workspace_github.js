
var g_sparseCheckout = false;
var g_denseCheckoutScriptUrl = "/autospring/workspace/github/script/0";
var g_sparseCheckoutScriptUrl = "/autospring/workspace/github/script/1";
var g_githubRepositoryPattern = /REPOSRC.*\n/i;
var g_githubSubDirectoryPattern = /SUBDIR.*\n/i;

function loadScripContent(sparseCheckout) {
	var scriptUrl = sparseCheckout ? g_sparseCheckoutScriptUrl : g_denseCheckoutScriptUrl;
	$("#txtarea_scriptcontent").load(scriptUrl, function(responseTxt, statusTxt, xhr) {
		if(statusTxt=="error") {
			alert("Error: "+ xhr.status + ": " + xhr.statusText);
		}
		else {
			$("#txtarea_scriptcontent").val(responseTxt);
			g_sparseCheckout = sparseCheckout;
		}
	});
}

function updateGithubRepository(repository) {
	var scriptContent = $("#txtarea_scriptcontent").val();
	scriptContent = scriptContent.replace(g_githubRepositoryPattern, repository);
	$("#txtarea_scriptcontent").val(scriptContent);
}

function updateGithubSubDirectory(subDirectory) {
	var scriptContent = $("#txtarea_scriptcontent").val();
	scriptContent = scriptContent.replace(g_githubSubDirectoryPattern, subDirectory);
	$("#txtarea_scriptcontent").val(scriptContent);
}

$(document).ready(function() {
	
	loadScripContent(false);
	
	$("#rbtn_dense_checkout").click(function() {
		if ($(this).is(':checked')) {
			$("span#spn_subdir_checkout").hide();
			loadScripContent(false);
		}
	});
	
	$("#rbtn_sparse_checkout").click(function() {
		if ($(this).is(':checked')) {
			$("span#spn_subdir_checkout").show();
			loadScripContent(true);
		}
	});
	
	$("#rbtn_reset").click(function() {
		if ($(this).is(':checked')) {
			if(g_sparseCheckout) {
				$("span#spn_subdir_checkout").show();
			}
			else {
				$("span#spn_subdir_checkout").hide();
			}
			
			loadScripContent(g_sparseCheckout);
		}
	});
	
	$("#btn_github_properties_confirm").click(function(event) {
		event.preventDefault();
		var repository = $("#txt_github_repository").val();
		repository = "REPOSRC=" + repository + "\n";
		updateGithubRepository(repository);
		if(g_sparseCheckout) {
			var subDir = $("#txt_subdir_checkout").val();
			subDir = "SUBDIR=" + subDir + "\n";
			updateGithubSubDirectory(subDir);
		}
	});
});