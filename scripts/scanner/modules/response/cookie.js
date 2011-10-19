var module = {
  name : "Cookie Security Module",
  type: "response-processor",
};

function run(request, response, ctx) {
   		var cookies = new Array();  
 	    cookies=response.getHeaders("Set-Cookie");
		// Test for SSL Missing!!!
		// so assume it is ssl until we can fix this
		var ssl=0;
		if(response.host.schemeName=="https"){
			ssl=1;
		}
		for(var i=0; i<cookies.length; i++) {
			var httponly=0;
			var secure=0; 	
            
            var params = new Array();
            params = cookies[i].getValue().split(";");
            for(var j=1; j<params.length; j++) {    
            	if(params[j]==" secure"){
            		secure=1;
            	}            	
            	if(params[j]==" httponly"){
            		httponly=1;
            	}            	   	
            }   
            
            if(secure!=1&&ssl==1){
            		ctx.alert("cookie-secure", request, response, {
          		    output: cookies[i].getValue(),
                    key: "cookie-secure:" + cookies[i].getValue(),
                    resource: request.requestLine.uri
           			 });    
            }
         	
           	if(httponly!=1){
           			java.lang.System.out.println("http-only");
            		ctx.alert("cookie-httponly", request, response, {
          		    output: cookies[i].getValue(),
                    key: "cookie-httponly:" + cookies[i].getValue(),
                    resource: request.requestLine.uri
           			 });
            } 

        }
}