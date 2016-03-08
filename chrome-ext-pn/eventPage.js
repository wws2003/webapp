chrome.gcm.onMessage.addListener(function(message) {
      // A message is an object with a data property that
      // consists of key-value pairs.
      showMessageInNotification(message);
});

function showMessageInNotification(message) {
  // Note: There's no need to call webkitNotifications.checkPermission().
  // Extensions that declare the notifications permission are always
  // allowed create notifications.

  console.log("Received message ", message);

  // Create a simple text notification:
  var options = {type: "basic", 
      title: message.data.title,
      message: "Build status " + message.data.status, 
      iconUrl: "icon64.png"
  };

  chrome.notifications.create("notification", options);

  var openTabFunc = function() {
    var newURL = message.data.link;
    chrome.tabs.create({ url: newURL });
  };

  chrome.notifications.onClicked.addListener(openTabFunc);
}