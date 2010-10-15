var module = {
  name : "HTTP Banner Module",
  type: "response-processor"
};

function run() {
  requestLog.addRequestResponse(httpRequest, httpResponse.getRawResponse());
}
