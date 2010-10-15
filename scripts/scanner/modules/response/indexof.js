var module = {
  name : "Directory Index",
  type: "response-processor"
};

function run() {
  var param_prefix=httpRequest.requestLine.uri +".indexof";
  if(model.get(param_prefix))
	  return;
  
  if(response.bodyAsString.match(/<title>Index of/)){
	  	model.set(param_prefix, "true");
	    model.alert("indexof", {"output": response.bodyAsString, "resource": httpRequest.requestLine.uri} );

  }
  
}
