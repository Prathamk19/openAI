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

--

```
 $ curl -X POST http://localhost:8080/urls -H "Content-Type: application/json" -d '{"url":"https://ai.google.dev/docs/gemini_api_overview"}' 
org.springframework.http.ResponseEntity$DefaultBuilder@5bf9453a


$ curl -X GET http://localhost:8080/query -H "Content-Type: application/json" -d '{"query":"What is Google Gemini? where can I read more?"}'
Google Gemini is a series of multimodal generative AI models developed by Google. These models can accept text and image inputs in prompts
and generate text responses. Google Gemini API provides access to these generative models.

You can read more about Google Gemini and access additional information on the official Google AI for Developers Products page. Here is the
link for more details: [Google AI for Developers Products](https://ai.google.dev/docs/gemini_api_overview)


$ curl -X POST http://localhost:8080/pdfs
org.springframework.http.ResponseEntity$DefaultBuilder@674288e1


$ curl -X GET http://localhost:8080/query -H "Content-Type: application/json" -d '{"query":"tell me something about G20 summit of 2023"}'
The G20 summit of 2023 was held in New Delhi on 9-10 September, marking India's G20 Presidency. The summit was significant as it included all
P5 countries and accounted for 85% of global GDP, 75% of world trade, and 2/3 of the world's population. The theme of the G20 Presidency was
"One Earth, One Family, One Future", emphasizing unity and inclusivity. The summit culminated with the adoption of the New Delhi G20 Leaders'
Declaration (NDLD), which addressed various global challenges. India highlighted its unique perspective by focusing on universal, equitable,
and inclusive solutions, promoting peace, dialogue, and diplomacy. The summit saw large in-person participation, with over 100,000 participants
from 135 nationalities attending various meetings. India's G20 Presidency aimed to amplify the voice of the Global South and enhance engagement
in future G20 activities.


$ curl -X GET http://localhost:8080/query -H "Content-Type: application/json" -d '{"query":"Give more details "}'
The document provided seems to be an itinerary or schedule of various meetings, events, and gatherings related to different working groups and summits. It includes
details such as the date, location, and nature of the event.

For example:
- The 2nd Culture Working Group Meeting took place from 15-17 May in Bhubaneswar, Odisha.
- The 3rd Energy Transitions Working Group Meeting was held from 15-17 May in Mumbai, Maharashtra.
- The RIIG event (MoES) occurred on 18-19 May in Diu, Dadra & Nagar Haveli and Daman & Diu.
- The 3rd Environment and Climate Sustainability Working Group Meeting was conducted from 21-23 May in Mumbai, Maharashtra.
- The Beach cleaning exercise took place on 21 May in Mumbai, Maharashtra.
- The 3rd Tourism Working Group Meeting was held from 22-24 May in Srinagar, J&K.
- The 4th Finance and Central Bank Deputies Meeting was on 11 October in Marrakesh, Morocco.
- The Parliament 20 Summit took place from 12-14 October in Delhi, Delhi.
- The G20 Virtual Summit was held on 22 November virtually.
