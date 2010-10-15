importPackage(java.net);

var module = {
  name : "File Bruteforce",
  type: "per-directory"
};

function run() {
	var dictionary=new Array();
	var extdict=new Array();

	dictionary.push("test");
	dictionary.push("admin");
	dictionary.push("temp");
	dictionary.push("src");
	dictionary.push("upload");
	dictionary.push("download");
	dictionary.push("index");
	extdict.push(".html");
	extdict.push(".jsp");
	extdict.push(".asp");
	extdict.push(".php");
	
	for(x in dictionary){
		for(y in extdict){
			var uri=directory.getURI().toString() + dictionary[x] + extdict[y];
			var request= new HttpGet(uri);
			var response=sendRequest(request);
		    if(response.code==200){
		    	print("Brutefocerd " + uri);
		    	var path = webModel.addURI(new URI(uri));	
		    	path.setVisited(true);	
		    }
		}
	}

}
