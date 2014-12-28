
function show2NewButtons() {
	$("#btn_new").hide();
	$("#btn_new_github").show();
	$("#btn_new_normal").show();
	$(".round-button").css({"width": "30%"});
	$(".round-button").css({"margin": "0 5px 0 5px"});
	$("#div_btns_line").css({"width": "65%"});
}

function hide2NewButtons() {
	$("#btn_new").show();
	$("#btn_new_github").hide();
	$("#btn_new_normal").hide();
	$(".round-button").css({"width": "40%"});
	$(".round-button").css({"margin": "0 15px 0 15px"});
	$("#div_btns_line").css({"width": "50%"});
}

$(document).ready(function() {
	
  $("#btn_new").click(function(event) {
	  event.preventDefault();
	  show2NewButtons();
  });
  
  $("#div_btns_line").mouseleave(function() {
	  hide2NewButtons();
  });
  
  $(".round-button").hover(function() {
	  var $divExplanation = $(this).find(".vertical-explanation");
	  $divExplanation.css({"visibility" : "visible"});
  }, 
  function() {
	  var $divExplanation = $(this).find(".vertical-explanation");
	  $divExplanation.css({"visibility" : "hidden"});
  });
  
  
});