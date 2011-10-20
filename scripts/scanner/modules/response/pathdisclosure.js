importPackage(java.net);

var module = {
  name : "path disclosure module",
  type: "response-processor"
};


function run() {
 print("Path disclosure module")
  var uri=new URI(this.httpRequest.getRequestLine().getUri());
//  var pathArray = response.bodyAsString.match(/([a-zA-Z]:\\\|file:\/\/[a-ZA-F0-9._\\\-]+|\/(usr|home|etc|var|root|opt)\/[a-ZA-F0-9._\\\-]+)/gi);
 var pathArray = response.bodyAsString.match(/([a-zA-Z]:\\\|file:\/\/[a-zA-Z0-9._\/-]+|\/(usr|home|etc|var|root|opt)\/[a-zA-Z0-9._\/-]+)/gi);
 	var listpath="";
 	if (pathArray) {
	  var pathattr = "server.path." + uri.toString() + pathArray[i];
	  model.set(pathattr, pathArray[i]);

	  for (var i = 0; i < pathArray.length; i++) {
		  listpath += pathArray[i] + "\r\n"; 
	  }
	  model.alert("pathdisclosure", { "output": listpath, "resource": uri.toString() });	

 	}
 }
