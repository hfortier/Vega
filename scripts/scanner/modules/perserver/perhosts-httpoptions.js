importPackage(java.net);

var module = {
  name : "test",
  type: "per-server"
};

function run() {
	var methods=new Array();
	var uri=host.getURI();
	var param_prefix = uri.getScheme() + "." + uri.getHost  + "." +  uri.getPort;
	var request= new HttpOptions(host.getURI().toString()+"/*");
	print("test host: " + host.getURI());
	var response=sendRequest(request);
    var	methods=response.header("Allow").split(",");
  	model.set(param_prefix+".methods",response.header("Allow") );

	for(x in methods){
		if(!methods[x].match(/(GET|HEAD|POST|OPTIONS)/)){
			print("Host method: " + methods[x]);
		  	model.set(param_prefix+".methods."+methods[x], "true");
		}
	}
	model.alert("host-methods", { "output": methods.join(", "), "resource": uri });
}
