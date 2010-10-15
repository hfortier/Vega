importPackage(java.net);

var module = {
  name : "test",
  type: "per-directory"
};

function run() {
	var url=host.getURI().toString()+".htaccess"
	var request= new HttpGet(url);
	var response=sendRequest(request);
	if(response.code==200){
		model.alert("htaccess", { "output": response.bodyAsString, "resource": uri });
    	var path = webModel.addURI(new URI(url));
    	path.setVisited(true);
	}
}
