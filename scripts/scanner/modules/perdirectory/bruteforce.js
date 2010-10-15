var module = {
  name : "Directory Bruteforce",
  type: "per-directory"
};

function run() {
	var dictionary=new Array();
	dictionary.push("test/");
	dictionary.push("admin/");
	dictionary.push("temp/");
	dictionary.push("src/");

	for(x in dictionary){
		var uri=directory.getURI().toString() + dictionary[x];
		var request= new HttpGet(uri);
		var response=sendRequest(request);
	    if(response.code==200){
	    	print("Brutefocerd " + uri);
	    	var path = webModel.addURI(new URI(uri));
	    	path.setVisited(true);
		/*	var getTarget = path.addGetTarget(page.getQuery(), mimeType);
			getTarget.setVisited(true);
			if(mimeType != null && mimeType.contains("html")) {
				List<URI> uris = extractor.findUrls(response);
				filterAndQueueURIs(uris);
			}*/
	    }
	}

}
