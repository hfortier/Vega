importPackage(java.net);

var module = {
  name : "cookie module",
  type: "response-processor"
};


function findEmailAddresses(StrObj,requestline) {
	var emailsArray = StrObj.match(/([a-zA-Z0-9._-]+(@|\(at\)| \(at\) | at )[a-zA-Z0-9._-]+\.[a-zA-Z0-9._-]+)/gi);
	//var emailsArray = StrObj.match(/([a-zA-Z0-9._-]+(@|\(at\)| \(at\))[a-zA-Z0-9._-]+\.[a-zA-Z0-9._-]+)/gi);

	if (emailsArray) {
		for (var i = 0; i < emailsArray.length; i++) {
			if(!model.get("email."+emailsArray[i])) {
					if(!emailsArray[i].match(/^Server.*/)){
							model.set("email."+emailsArray[i], emailsArray[i]);
							model.alert("emails", { "output": emailsArray[i], "resource": requestline });	
					}
			}
		}
	}
}
	
function run() {
  var uri=new URI(this.httpRequest.getRequestLine().getUri());
  print("Testing error message on ressource: " + uri);
  findEmailAddresses(response.bodyAsString,uri);

}
