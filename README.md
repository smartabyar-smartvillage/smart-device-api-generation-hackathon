

# Smart Device API Code Generation with AI/ML hackathon

## Written by Christopher Tate

- Red Hat Principal Software Engineer in Red Hat Research 

- Creator of the Smart Village Operator and Smart Village Platform 

- Architect of the Red Hat Social Innovation Program 

- Founder of the Smarta Byar Smart Village Community FIWARE Innovation Hub 

- Principal Software Engineer for the New England Research Cloud

## Problem Statement: Describe the specific problem or challenge you aimed to address during the hackathon using AI tools/technologies.

Because reactive/asynchronous APIs are more efficient and scalable, but are much
more complex to build by hand. We can use code generation to build extendable
APIs consistently, faster, and more secure with AI/ML Code Generation based on
the well established [computate open source
project](https://github.com/computate-org/computate). Increase Quarkus API and
web developer productivity with OpenAPI Code Generation based on code comments.
Easily deploy new edge device models and edge device models following open
source FIWARE smart data model schemas with AI/ML predictive code generation.

## Solution Overview: Provide a brief overview of your solution, emphasizing the innovative use of AI tools or technologies.

- For any of the hundreds of open source Smart Data Models provided by the
FIWARE Community, we will use AI/ML code generation technology to build, deploy,
and visualize a working Edge-to-Cloud solution for any Smart Data Model in less
than 10 minutes.

- The AI/ML solution generates all the code for any given Smart Data Model into
a working solution. The AI/ML solution will generate working Java classes with
intuitive asynchronous initialization code, Reactive Java APIs linked to an
OpenAPI spec, Handlebars HTML Templates, generated JavaScript, database
initialization scripts, NGSI-LD Entity data context and documentation, and
Ansible Operator Roles and Playbooks to register new smart devices to update
entity data through the whole event driven subscription process through the
FIWARE and Smart Village Platform.

- The generated Handlebars HTML templates and generated JavaScript build
powerful analytics dashboards for any new Smart Data Model generated to enable
full text search, autosuggest, filtering on any fields, grouped range data,
grouped facet counts, nested pivoting on fields, multiple sort fields, field
statistics, and JSON APIs.

- The AI/ML solution will also rebuild the OpenAPI Spec, update the database
schema in PostgreSQL, update the role-based access control defaults, register
new APIs in the Main Verticle, resolve all imports and dependencies from other
projects for an updated Smart Village Platform codebase that successfully
compiles, build new Ansible Operator Roles and Playbooks for registering new
Smart Data Model devices, updating the smart device entity data through the
event driven FIWARE and Smart Village subscription process through the Message
Broker, IoT Agent, Context Broker, NGSI-LD Smart Village Sync, and Smart Village
Platform, and view the new entity in the map in dashboard running directly from
the OpenShift AI workbench in development.

- This AI/ML solution reduces the vast amount of software development,
deployment, data import, and data visualization to only 6 minutes and 54
seconds, which would normally take months to develop, deploy, import, and
visualize.

## Technical Details: Outline the technical aspects of your solution, including the AI tools, algorithms, and technologies employed. Be specific about the key technical components.

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

- MongoDB: A MongoDB No-SQL Database is required for FIWARE IoT Agents to store
smart device registration data about each smart device in the project.

- RabbitMQ: A message broker like RabbitMQ can receive messages from smart
devices and forward them on to an IoT Agent to process the message.

- PostgreSQL: A relational SQL database like PostgreSQL is required by the
ScorpioBroker to store smart device context data for each smart device.

- ScorpioBroker: A FIWARE Context Broker like ScorpioBroker is required for
storing smart device entity data of vehicle traffic cameras and pedestrian
traffic cameras in the project.

- IoT Agent JSON: A FIWARE IoT Agent like IoT Agent JSON is required for smart
device registration of vehicle traffic cameras and pedestrian traffic cameras in
the project.

- Apache Zookeeper: A cluster manager like Apache Zookeeper is required for
distributing messages and workloads to multiple pods of reactive/asynchronous
microservices like Apache Solr and the Smart Village Platform.

- Apache Solr: An open source search engine like Apache Solr is required by the
Smart Village application API to serve up API stored objects as quickly as
possible.

- Smart Village Platform: The Smart Village Platform is used by researchers to
configure smart devices for improving traffic light configuration, and running
on reports on traffic simulations configured at intersections in the world.

### AI/ML Tools and Algorithms

- You can think of Apache Solr running in the same project as the OpenShift AI 
  workbench as the Model Server in this solution. 
- You can think of the model as an index of every detail of every Java Class, 
  Java Constructor, Java Method, and Java Field of every Java Project in the 
  Smarta Byar Smart Village project, Smart Village Platform, Computate Vertx 
  project, and Computate Search Project. Having your entire code base indexed 
  in a search engine allows you to generate code that resolves complicated 
  imports, links together foreign key relations including many-to-many, 
  one-to-many, and many-to-one foreign key relations. It also allows you to 
  rebuild code that registers all APIs automatically as plugins to the main 
  application. 
- The model is built with the [computate](https://github.com/computate-org/computate) 
  project on GitHub. A well established open source project written by myself 
  over the last 16 years to generate code as you save your code. It's 
  integrated with Solr search. A new way to write code to make persistence easier.
- The algorithm will watch any directory for changes recursively. When a Java 
  class is updated, the code will immediately be parsed with a fork of the 
  [open source QDox Java code parser library](https://github.com/computate-org/computate/blob/main/src/main/java/com/thoughtworks/qdox/model/impl/DefaultJavaClass.java), 
  then every detail of the Java Class is indexed in the Solr search engine, 
  the [computate project](https://github.com/computate-org/computate) will then query the indexed data in the search engine, 
  and generate intuitive asynchronous initialization code, Vert.x reactive APIs, 
  Handlebars HTML Templates, Java Page rendering code, and more. Imports are 
  automatically resolved through searching through the deep knowledge base of 
  dependent code projects in the Solr search engine model server. 

## Implementation Steps: Detail the steps you took to implement your solution. Include any challenges faced and how you overcame them.

- Clone the Smarta Byar Smart Village Java code, and it's dependent Java
projects (smartabyar-smartvillage, smartvillage-platform, computate-vertx,
computate-search, computate).

- Use computate_project Ansible Playbook to compile and install each Java
project into the user's home directory.

- Use the computate code generation platform to index every Java class in every
dependent Java project (smartabyar-smartvillage, smartvillage-platform,
computate-vertx, computate-search, computate).

- Clone the hundreds of edge device data related open source Smart Data Models
that follow FIWARE standards for NGSI-LD Open IoT device data, and index
information about them in the Solr search engine.

- Run the Computate Code Watch Tool provided by the computate platform on the
smartabyar-smartvillage project in a terminal in the Workbench that will
generate massive amounts of code for us.

- Create a new Java Class representing one of the hundreds of Smart Data Models,
like PhotovoltaicMeasurement, and note the Smart Data Model search keywords in
the Java Class comments.

- Analyze the output of the Computate Code Watch Tool and see how it predicts
the right Smart Data Model code to generate.

- Watch as the Computate Code Watch Tool identifies the right Smart Data Model
to generate, analyzes the PhotovoltaicMeasurement Smart Data Model automatically
generates the Java POJOs and complete Vert.x reactive API for each method
defined in the generated Java class.

- Run additional code generation that applies to the whole project:

  - the updated OpenAPI spec, 

  - updated database table schemas, 

  - Java POJOs with advanced initialization code in the order that
  initialization methods are defined,

  - Complete Vert.x Reactive Java APIs, 

  - Default role-based access control for the new Smart Data Model API, 

  - Register the new Smart Data Model API with the Main Verticle aligned with
  the updated OpenAPI Spec,

  - Handlebars HTML page templates, 

  - JavaScript page and API functions, 

  - Complete NGSI-LD Context data for all Smart Data Models defined in the
  project,

  - and complete Ansible Operator Custom Resource Definition schema, Ansible
  Roles, and Playbooks to automate deployment of the new Smart Data Models as
  actual smart devices with entity data in the FIWARE and Smart Village
  Platform.

- Run the SQL scripts to update the database schema with all the new generated
tables and fields.

- Deploy a new Service, Route, and NetworkPolicy to run the newly built code in
development directly from the OpenShift AI workbench and view the new dashboard
for new Smart Data Model immediately at the new Route.

## Key Features: List the key features of your solution that make it innovative and effective in solving the identified problem.

- See the thousands of lines of generated code build successfully with Maven.
The computate code generator can perfectly resolve all Java package imports
successfully between all dependent projects because the Solr search engine has
already been trained with every detail about every dependent project
(computate-search, computate-vertx, smartvillage-platform,
smartabyar-smartvillage projects).

- The generated Handlebars HTML templates and generated JavaScript build
powerful analytics dashboards for any new Smart Data Model generated to enable
full text search, autosuggest, filtering on any fields, grouped range data,
grouped facet counts, nested pivoting on fields, multiple sort fields, field
statistics, and JSON APIs.

- Every generated Java class, API class, and Handlebars template has an empty
parent class or template that allows you to override any of the generated code
that was implemented for you. It's already built to allow you to override or
improve any part of the generated code for complete flexibility.

- The newly generated Ansible Roles and Playbooks in the Ansible Operator allow
you to deploy multiple instances of the new Smart Data Model to the message
broker, IoT Agent, Context Broker, NGSI-LD Smart Village Sync, and Smart Village
Platform for a complete Edge-to-Cloud event driven solution.

## Results: Share any quantitative or qualitative results achieved by your solution. This could include performance metrics, user feedback, or any other relevant measures of success.

- In 6 minutes and 54 seconds, I tested out building a new Smart Data Model Java
class from scratch, replaced the Java Class I wrote with the AI/ML suggested
Java code for the Smart Data Model instead, rebuilt the OpenAPI Spec and other
project-wide changes, updated the database schema in PostgreSQL, updated the
RBAC and Main Verticle, compiled the new Smart Village Platform code, built new
Ansible Operator Roles and Playbooks for the new Smart Data Model, and deployed
a new Smart Device and entity data through the Message Broker, IoT Agent,
Context Broker, NGSI-LD Smart Village Sync, and Smart Village Platform, and view
the new entity in the map in dashboard.

- This AI/ML solution reduces the vast amount of software development,
deployment, data import, and data visualization of Smart Data Model data to only
6 minutes and 54 seconds, which would normally take months to develop, deploy,
import, and visualize.

## Lessons Learned: Reflect on the lessons learned during the hackathon. Identify areas for improvement or insights gained through the process.

- Currently the Smart Data Model code generation writes the suggested code in
the terminal output, but it could simplify copy and paste errors by overwriting
the original source file automatically. I hesitate to do this because it can be
confusing and frustrating to the user if their hard work is ever overwritten.
The [computate project](https://github.com/computate-org/computate) by default is built to generate code, but also provide an
empty parent class where you can override any generated method, but never
overwrite any code written by the developer. Overwriting the original source
code is against the established workflow of the [computate project](https://github.com/computate-org/computate), but rewriting
Smart Data Models could be an exception.

- Sometimes the VSCode Maven build integration interferes with running `mvn
clean install` in the terminal on the project. Occasionally I've had to remove
the target directory `rm -rf target/` and re-run `mvn clean install` to resolve
the issue. It might be best to update the settings of the VSCode workbench to
disable automatic Maven builds in VSCode.

## Future Recommendations: Provide suggestions for the potential future development or enhancement of your solution. Consider scalability, additional features, or broader applications.

- The hundreds of FIWARE Smart Data Models are only a sample of the kind of APIs
and dashboards that can be developed with the `computate` platform.

- Examples of previous data driven projects for social innovation that have been
developed with the same `computate` platform over many years include:

  - [Smarta Byar Smart Village
  Platform](https://www.smartabyarsmartvillage.org/) - Red Hat Global Social
  Innovation Program is partnering with Boston University and Smarta Byar in
  order to collaborate on creating a global and open research platform allowing
  researchers to study what social sustainability means by using a digital twin
  of Veberöd, Sweden as the test village, supported by Smarta Byar.

  - [Ratial Equity Report Cards](https://rerc.southerncoalition.org/) - The
  RERCs use public data to provide a snapshot of a community’s school-to-prison
  pipeline, including any racial disproportionalities that exist in the
  pipeline. There is a Report Card for each of the state’s 115 school districts
  and one for the state as a whole.

  - [Open Data Policing](https://www.opendatapolicingnc.com/) - Open Data
  Policing is a first-of-its-kind platform that aims to make real the
  recommendation of the President’s Task Force on 21st Century Policing to make
  stop, search, and use-of-force “data...publicly available to ensure
  transparency.” The site currently aggregates, visualizes, and publishes public
  records related to all known traffic stops to have occurred in North Carolina
  since 2002, in Maryland since 2013, and in Illinois since 2005.

  - See my [Websites page on computate.org](https://www.computate.org/websites)
  for more details.

## Acknowledgments: Acknowledge any team members, mentors, or external resources that contributed to the success of your project.

- Thanks to the [Red Hat Social Innovation
Program](https://www.redhat.com/en/about/social-innovation) for many
opportunities since 2018 to develop and deploy multiple innovative sites and
hackathons with the computate platform together with field experts in 
interesting domains like Ratial
Equity, Virus DNA Sequencing, Educational Software, Medical Imaging Software,
and more. See my [Hackathons page on
computate.org](https://www.computate.org/hackathons) for more information on the
collaborative work we have done with organizations around the world with AI/ML.

- Thanks to the [Red Hat Research team](https://research.redhat.com/) and [Red
Hat Collaboratory with Boston University](https://www.bu.edu/rhcollab/) for
funding the [Smarta Byar Smart Village
project](https://research.redhat.com/blog/research_project/creating-a-global-open-research-platform-to-better-understand-social-sustainability-using-data-from-a-real-life-smart-village/)
during 2022 and 2023, where we found that the FIWARE and Smart Village Platform
work beautifully together with the computate platform to build a working
Edge-to-Cloud solution with AI/ML.

- Thanks to the Red Hat B.U.I.L.D. community for collaborating on the upcoming
[Edge-to-Cloud Learning Experience with Shaw University and other Universities
in North
Carolina](https://research.redhat.com/blog/2023/12/04/hackathons-power-open-source-technology-and-innovative-research/)
February 29 2024. The [course we developed for the
hackathon](https://github.com/smartabyar-smartvillage/smartabyar-smartvillage-sandbox-course)
guides students through launching a free Red Hat OpenShift Developer Sandbox
environment, deploying the same FIWARE and Smart Village components as this
course, and teaches how to build a new Python microservice that animates
simulated traffic data on maps over time.

## Presentation Highlights: Summarize the key points you would highlight during a presentation of your hackathon project to ensure clarity and impact.

- In less than 10 minutes, Anyone can build a new Smart Data Model Java class
from scratch, replace the Java Class with the AI/ML suggested Java code for the
Smart Data Model instead, rebuild the OpenAPI Spec and other project-wide
changes, update the database schema in PostgreSQL, update the RBAC and Main
Verticle, compile the new Smart Village Platform code, build new Ansible
Operator Roles and Playbooks for the new Smart Data Model, and deploy a new
Smart Device and entity data through the Message Broker, IoT Agent, Context
Broker, NGSI-LD Smart Village Sync, and Smart Village Platform, and view the new
entity in the map in dashboard.

- This AI/ML solution reduces the vast amount of software development,
deployment, data import, and data visualization of Smart Data Model data to less
than 10 minutes, which would normally take months to develop, deploy, import,
and visualize.

## Try out the AI/ML Smart Device API Code Generation yourself

Try out the AI/ML Smart Device API Code Generation yourself in your own OpenShift 
cluster: 

- by deploying the [vscode-java ImageStream here](https://github.com/nerc-images/vscode-java/blob/main/cluster-scope/base/image.openshift.io/imagestreams/vscode-java/imagestream.yaml)
- Then follow the Jupyter notebooks in this course, starting with 
  [01-install-prerequisites.ipynb](01-install-prerequisites.ipynb). 

