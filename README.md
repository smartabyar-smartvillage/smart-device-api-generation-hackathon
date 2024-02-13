

# Smart Device API Code Generation with AI/ML hackathon

## Written by Christopher Tate

- Red Hat Principal Software Engineer in Red Hat Research - Creator of the Smart
Village Operator and Smart Village Platform - Architect of the Red Hat Social
Innovation Program - Founder of the Smarta Byar Smart Village Community FIWARE
Innovation Hub - Principal Software Engineer for the New England Research Cloud

## Problem Statement: Describe the specific problem or challenge you aimed to
#address during the hackathon using AI tools/technologies.

Because reactive/asynchronous APIs are more efficient and scalable, but are much
more complex to build by hand. We can use code generation to build extendable
APIs consistently, faster, and more secure with AI/ML Code Generation based on
the well established [computate open source
project](https://github.com/computate-org/computate). Increase Quarkus API and
web developer productivity with OpenAPI Code Generation based on code comments.
Deploy new edge device models and edge device models following open source
FIWARE smart data model schemas with AI/ML predictive code generation.

## Solution Overview: Provide a brief overview of your solution, emphasizing the
#innovative use of AI tools or technologies.


## Technical Details: Outline the technical aspects of your solution, including
#the AI tools, algorithms, and technologies employed. Be specific about the key
#technical components.

- Starting from a new OpenShift Cluster and Project, we start a new OpenShift AI
Workbench using a VSCode image with JupyterHub, IJava, OpenJDK, and Maven
dependencies installed.

- We deploy some edit role bindings so that we can deploy the Smart Village
Platform components directly from a Jupyter Notebook in our VSCode Workbench.

- We install some Python dependencies like Ansible, since we have already
automated the deployment of the Smart Village Platform with an Ansible Operator.

- From a Jupyter Notebook, we deploy all of the FIWARE and Smart Village
applications:

### Technologies deployed

- MongoDB: A MongoDB No-SQL Database is required for FIWARE IoT Agents to
store smart device registration data about each smart device in the project.

- RabbitMQ: A message broker like RabbitMQ can receive messages from smart
devices and forward them on to an IoT Agent to process the message.

- PostgreSQL: A relational SQL database like PostgreSQL is required by the
ScorpioBroker to store smart device context data for each smart device.

- ScorpioBroker: A FIWARE Context Broker like ScorpioBroker is required for
storing smart device entity data of vehicle traffic cameras and pedestrian
traffic cameras in the project.

- IoT Agent JSON: A FIWARE IoT Agent like IoT Agent JSON is required for
smart device registration of vehicle traffic cameras and pedestrian traffic
cameras in the project.

- Apache Zookeeper: A cluster manager like Apache Zookeeper is required for
distributing messages and workloads to multiple pods of
reactive/asynchronous microservices like Apache Solr and the Smart Village
Platform.

- Apache Solr: An open source search engine like Apache Solr is required by
the Smart Village application API to serve up API stored objects as quickly
as possible.

- Smart Village Platform: The Smart Village Platform is used by researchers
to configure smart devices for improving traffic light configuration, and
running on reports on traffic simulations configured at intersections in the
world.

## Implementation Steps: Detail the steps you took to implement your solution.
#Include any challenges faced and how you overcame them.

- Clone the Smarta Byar Smart Village Java code, and it's dependent Java projects (smartabyar-smartvillage, smartvillage-platform, computate-vertx, computate-search, computate). 

- Use computate_project Ansible Playbook to compile and install each Java project into the user's home directory. 

- Use the computate code generation platform to index every Java class in every dependent Java project (smartabyar-smartvillage, smartvillage-platform, computate-vertx, computate-search, computate). 

- Clone the hundreds of edge device data related open source Smart Data Models that follow FIWARE standards for NGSI-LD Open IoT device data, and index information about them in the Solr search engine. 

- Run the Computate Code Watch Tool provided by the computate platform on the smartabyar-smartvillage project in a terminal in the Workbench that will generate massive amounts of code for us. 

- Create a new Java Class representing one of the hundreds of Smart Data Models, like PhotovoltaicMeasurement. 

- Analyze the output of the Computate Code Watch Tool and see how it predicts the right Smart Data Model code to generate. 

- The Computate Code Watch Tool and other provided tools will analyze the PhotovoltaicMeasurement Smart Data Model and will automatically generate OpenAPI specs, database table schemas, Java POJOs, Vert.x Reactive Java APIs, Handlebars HTML page templates, JavaScript page and API functions, NGSI-LD Context data, OpenShift Custom Resource Definitions, and Ansible Operator roles and playbooks to completely onboard new PhotovoltaicMeasurement devices into the data driven Smart Village Platform. 

## Key Features: List the key features of your solution that make it innovative
#and effective in solving the identified problem.

## Results: Share any quantitative or qualitative results achieved by your
#solution. This could include performance metrics, user feedback, or any other
#relevant measures of success.

## Lessons Learned: Reflect on the lessons learned during the hackathon.
#Identify areas for improvement or insights gained through the process.

## Future Recommendations: Provide suggestions for the potential future
#development or enhancement of your solution. Consider scalability, additional
#features, or broader applications.

## Acknowledgments: Acknowledge any team members, mentors, or external resources
#that contributed to the success of your project.

## Presentation Highlights: Summarize the key points you would highlight during
#a presentation of your hackathon project to ensure clarity and impact.
