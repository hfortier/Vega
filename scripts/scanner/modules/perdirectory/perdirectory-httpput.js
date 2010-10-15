var module = {
  name : "test",
  type: "per-directory"
};

function run() {
	print("Per Directory HttpPut: " + directory.getURI().toString() );
	var methods=new Array();
	var uri=directory.getURI();
	var param_prefix = uri.getScheme() + "." + uri.getHost  + "." +  uri.getPort;
	var request= new HttpPut(directory.getURI().toString()+"vegauploadtest");
	var response=sendRequest(request);
    if(response.code!=405){
    	model.alert("httpput", { "output": response.code, "resource": uri });
    }

}
