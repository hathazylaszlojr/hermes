# Hermes

Hermes is the god of trade, merchants and commerce.
The project itself aims to be the god of online trading.

# Technical description

The project currently consist of a simple REST API, which allows adding and updating products, and creating new orders.

Being just at the start, there are a lot of potential improvements that can be done, found in the below section.

# Run

The project is managed using Gradle.
After checking out the project, it can be easily run using: 

`./gradlew bootRun` (in case `gradlew` does not have execute permission: `chmod +x ./gradlew`)

By default, will use the port 8080.

Swagger API Documentation can be found at: `http://localhost:8080/swagger-ui.html`

Alternatively, the following `curl` commands can be used:

`curl -XPOST -H "content-type: application/json" -d"{\"name\": \"product 1\", \"price\": 22.1}" localhost:8080/api/v1/products`

`curl -XGET localhost:8080/api/v1/products`

`curl -XPOST -H "content-type: application/json" -d"{\"email\": \"mymail@example.com\", \"productSkus\": [1]}" localhost:8080/api/v1/orders`


By default, data is persisted only to in-memory H2 db.
To change persistence to disk, the datasource related lines in `application.yml` should be uncommented.

# Further improvements:

There are several areas that can be further developed (not in the order of importance):
- Cucumber could be used for BDD, closing the gap between business requirements and development team
- Current end to end tests cover only from the controller, but they should go even beyond that, making proper HTTP calls
(almost like integration tests).
- DTOs should be used in more cases.
Currently it is being used only for placing an order, but they should be used whenever an entity is being exposed to the outer world.
MapStruct offers a quite good mapping functionality to achieve this.
- Unit tests were replaced with end to end tests as they cover all the 3 existing layers, controller, service and repository
- JavaDoc should be added to public methods
- Whenever returning a list, pagination should be used.
- Logging can be improved further.
It could be simplified using Aspects.
- Email address field could be changed to InternetAddress type, so it would have more validation.
- Currently order date it just a timestamp; it could be a timezoned date.
- Product entity stores also the price at the moment, however this prevents from having products with the same SKU but different price.
Was kept like this for simplicity, but there should be another entity (like ProductBatch), which would then store the 
quantity, price and other relevant metadata of the products 
- HATEOAS support
