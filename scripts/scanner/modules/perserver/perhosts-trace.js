importPackage(java.net);

var module = {
  name : "Per host trace",
  type: "per-server"
};

function run() {
	print("Perhost Trace");
	var methods=new Array();
	var uri=host.getURI();
	var param_prefix = uri.getScheme() + "." + uri.getHost  + "." +  uri.getPort;
	var request= new HttpTrace(host.getURI().toString()+"/");
	request.addHeader("test","vega");
	var response=sendRequest(request);
	print(response.bodyAsString);
	request.addHeader("xss","<script>alert('vega')</script>");
	var response2=sendRequest(request);
	print(response2.bodyAsString);

	if(response2.bodyAsString.indexOf("<script>alert('vega')</script>")>0){
		print("XSS");
		model.alert("trace-xss", { "output": response2.bodyAsString, "resource": uri });
	}else if(response.bodyAsString.indexOf("test: vega")>0){
		model.alert("trace", { "output": response.bodyAsString, "resource": uri });
	}
}
