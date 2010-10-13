importPackage(java.net);

var module = {
  name : "error",
  type: "response-processor"
};


	
function run() {
  var errormsg = new Array();
  errormsg.push(/Microsoft VBScript runtime/);
  errormsg.push(/java\.lang\./);
  errormsg.push(/<b>Fatal Error<\/b>/);
  errormsg.push(/Error/);

  var uri=new URI(this.httpRequest.getRequestLine().getUri());
  var pathattr = "server.path.error" + uri.toString();

  print("Testing error message on ressource: " + this.httpRequest.getRequestLine().getUri());
  for (var i = 0; i < errormsg.length; i++) {
	  if(response.bodyAsString.match(errormsg[i])){
		  model.set(pathattr, pathArray[i]);

		  model.alert("error", { "output": response.bodyAsString , "resource": uri.toString() });	
	  }
  }

}
