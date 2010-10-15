var module = {
  name : "test",
  type: "per-directory"
};

function run() {
	var methods=new Array();
	var uri=directory.getURI();
	var param_prefix = uri.getScheme() + "." + uri.getHost  + "." +  uri.getPort;
	var request= new HttpOptions(directory.getURI().toString());
	print("test directory: " + directory.getURI());
	var response=sendRequest(request);
	if(response.header("Allow")!=null){
		var	methods=response.header("Allow").split(",");
		for(x in methods){
			if(!methods[x].match(/(GET|HEAD|POST|OPTIONS)/)){
				if(!model.get(param_prefix+".methods."+methods[x])){
					print(methods[x]);
				}
			}
		}
	}

}
