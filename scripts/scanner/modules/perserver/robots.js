importPackage(java.net);

var module = {
  name : "test",
  type: "per-server"
};

function run() {
	var methods=new Array();
	var uri=host.getURI();
	var param_prefix = uri.getScheme() + "." + uri.getHost  + "." +  uri.getPort;
	var url=host.getURI().toString()+"/robots.txt"
	print(url);
	var request= new HttpGet(url);
	print("test host: " + host.getURI());
	var response=sendRequest(request);
	if(response.code==200){
		model.alert("robots", { "output": response.bodyAsString, "resource": uri });
    	var path = webModel.addURI(new URI(url));
    	path.setVisited(true);
	}
}
