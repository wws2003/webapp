function registerGCM() {
  console.log("To register on GCM");

  chrome.storage.local.get("registered", function(result) {
    // If already registered, bail out.
    if (result["registered"])
      return;

    // Up to 100 senders are allowed.
    var senderIds = ["925909503799"];
    chrome.gcm.register(senderIds, registerCallback);
  });
}

function unregisterGCM() {
  //TODO Implement
}

function registerCallback(registrationId) {
  if (chrome.runtime.lastError) {
    // When the registration fails, handle the error and retry the
    // registration later.
    console.log("Register error:", chrome.runtime.lastError);
    return;
  }

  //Log received registrationId
  showRegistrationId(registrationId);
}

function showRegistrationId(registrationId) {
  console.log("Registration id= ", registrationId);
  document.querySelector('#spn_registration_id').innerHTML=registrationId;
  document.querySelector('#btn_done').style.display="inherit";
}

function dismissRegistrationId() {
  document.querySelector('#spn_registration_id').innerHTML="";
  document.querySelector('#btn_done').style.display="none";
}

function unregisterCallback() {
  if (chrome.runtime.lastError) {
    // When the unregistration fails, handle the error and retry
    // the unregistration later.
    return;
  }
}

chrome.runtime.onStartup.addListener(function() {
  console.log("Chrome runtime started up");
});

document.addEventListener('DOMContentLoaded', function () {
  console.log("Content loaded");
  document.querySelector('#btn_register').addEventListener('click', registerGCM);
  document.querySelector('#btn_done').addEventListener('click', dismissRegistrationId);
});

