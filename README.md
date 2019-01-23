
# Setup and execution:

Refer to the UserGuide.doc for the steps to setup and run the project. It contains screenshots and references for various steps.

# Test Coverage 

•	All the Valid and Invalid flow scenarios covered

•	Scenarios for invalid status codes covered

•	Scenarios for invalid status error messages covered

•	Validation of JSON responses covered

# Knows Issues / Scenarios not covered
•	Scenarios to validate createdAT,completedAt,CancelledAt fields as the exact time could not be picked accurately.

•	Due to dynamic nature of "stops" field currently the JSON payload for the request is fetched from the excel file. This data could be dynamically generated but was more complex to implement in the given timeframe.


