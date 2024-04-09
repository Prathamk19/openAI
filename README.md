# Exploring Spring AI APIs 

---

### In this application I am exploring Spring AI APIs on DocumentReader, PDFReader, VectorStores, Token Splitter etc.

### Currently Open AI Chatbox responses to the queries with the provided URLs and PDFs.

---

Usage: -
-
### Make sure you use your Open AI API key, update the environment variables appropriately.

-    Docker image for PostGres DB
-    To provide a webpage to application, submit a POST request ` $ curl -X POST http://localhost:8080/urls -H "Content-Type: application/json" -d '{"url":"<YOUR_URL>"}' `
-    To make a Query specific to loaded data ` $ curl -X GET http://localhost:8080/query -H "Content-Type: application/json" -d '{"query":" <YOUR QUERY> "}' `
