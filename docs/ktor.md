# hikaku - ktor

## Feature Support

Please refer to the list of [all features](features.md). To check the feature support for this converter.

### Path and HttpMethod

+ Simple path using http method function
  + _Example:_ `get("/todos") { }`

+ Simple path using route function with nested http method lambda:
  + _Example:_ `route("/todos") { get { } }`

+ Simple path using route function with nested method function:
  + _Example:_ `route("/todos") { method(Get) { } }`

+ Simple path containing both path and http method
  + _Example:_ `route("/todos", Get) { }`

+ Simple path using method function with nested route function
  + _Example:_ `method(Get) { route("/todos") { } }`

---- 

+ Nested path using http method function
  + _Example:_ `get("/todo/list") { }`  

+ Nested path using route function with nested http method lambda:
  + _Example:_ `route("/todo/list") { get { } }`
  
+ Nested path using route function with nested method function:
  + _Example:_ `route("/todo/list") { method(Get) { } }`

+ Nested path containing both path and http method
  + _Example:_ `route("/todo/list", Get) { }`

----

+ Nested path split into route function containing http method function
  + _Example:_ `route("/todo") { get("/list") { } }`

+ Nested path split into two nested route functions and a http method lambda
  + _Example:_ `route("/todo") { route("/list") { get { } } }`

+ Nested path split into two nested route functions and a method function
  + _Example:_ `route("/todo") { route("/list") { method(Get) { } } }`

+ Nested path split into two nested route functions. Inner route function containing the http method
  + _Example:_ `route("/todo") { route("/list", Get) { } }`

+ Nested path split into nested route functions with individual method functions
  + _Example:_ `route("/todo") { method(Post, {}) route("/list") { method(Get, {}) } }`

### Path Parameter

+ Required path parameter for each path definition as described above.
  + _Example:_ `get("/todos/{id}") { }`

+ Optional path parameter for each path definition as described above. 
  + _Example:_ `get("/todos/{id?}") { }`
  + Currently only Interpreted as `/todos/{id}` whereas it should be interpreted as both `/todos/{id}` AND `/todos`

## Currently not supported

+ Nested path using method function with nested route function
  + _Example:_ `method(Get) { route("/todo/list") { } }`

+ Nested path split into two nested route functions within a method function
  + _Example:_ `method(Get) { route("/todo") { route("/list") { } } }`

+ Nested path inherits http method from parent and adds individual http method using method function
  + _Example:_
```
val routing = Routing(mockk()).apply {
    route("/todo") {
        method(HttpMethod.Post) {
            handle {
                println("POST /todo")
            }
        }

        route("/list") {
            handle {
                println ("POST /todo/list")
            }
            method(HttpMethod.Get) {
                handle {
                    println("GET /todo/list")
                }
            }
        }
    }
}
```
*or*
```
val routing = Routing(mockk()).apply {
    route("/todo") {
        post {
            println("POST /todo")
        }
        route("/list") {
            handle {
                println("POST /todo/list")
            }
            get {
                println("GET /todo/list")
            }
        }
    }
}
```
*to be:*
```
val specification = setOf(
        Endpoint("/todo", POST),
        Endpoint("/todo/list", POST),
        Endpoint("/todo/list", GET)
)
```

+ Optional path parameter
  + _Example:_ `get("/todos/{id?}") { }`

+ Wildcard path parameter
  + _Example:_ `get("/todos/*") { }`

+ Tailcard path parameter
  + _Example:_ `get("/todos/{...}") { }`

+ Captured tailcard path parameter
  + _Example:_ `get("/todos/{param...}`

## Usage

Instantiate `KtorConverter` passing your instance of `io.ktor.routing.Routing`.