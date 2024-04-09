This is a Sample Project exploring Spring AI API's. Currently Chatbox responses to the queries with the provided URL's and PDF's.
In this application I am exploring Spring AI API's on DocumentReader, PDFReader, VectorStores, Tocken Splitter etc.

Make sure you use your Open AI API key, update the environment variables appropriately.

Usage:
    1. PostGres database used from docker
    2. At the start of the App it deletes everything from DB instance
    3. POST request with URL, $ curl -X POST http://localhost:8080/urls -H "Content-Type: application/json" -d '{"url":"https://ai.google.dev/docs/gemini_api_overview"}'
    4. Query sample $ curl -X GET http://localhost:8080/query -H "Content-Type: application/json" -d '{"query":" <YOUR QUERY> "}'
