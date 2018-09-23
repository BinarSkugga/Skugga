# SkuggaHttps

## Create and start your server
First off all, you'll need to get your configuration file. You can do that using the HttpConfigProvider 
class that fetches the "http.properties" file in your resources.
``` java
PropertiesConfiguration config = HttpConfigProvider.get();
```

PropertiesConfiguration provides an easy interface to interacts with .properties files in your resource 
folder. Using this configuration you can easily create and start your server.
``` java
HttpServer server = new HttpServer(new CustomExchangeHandler());
server.start();
```

In this code we do a couple of things. First we create the server using our property "server.ip" and "server.port". 
Next we provide a HttpExchangeHandler to the server. This class is created by you and describes what id types you 
are using as well as how to transform object into json. It would look like this for a MongoDB database using the 
BSON type ObjectId. The IdentityRepository method returns null here meaning this API is 'auth-less'. Note that this will
be implemented later in the README.
``` java
public class CustomExchangeHandler extends AbstractHttpExchangeHandler<ObjectId> {
	@Override
	public HttpJsonHandler getJsonHandler() {
		return new MoshiJsonHandler();
	}
	
	@Override
	public <Q, T extends Identifiable> DataRepository<Q, ObjectId, T> getIdentityRepository() {
		return null;
	}

	@Override
	public ObjectId createID(String s) {
		return new ObjectId(s);
	}
}
```

## Add controllers
To add controllers, you'll need to first tell the server where you store your controller classes. This package can be 
specified in "http.properties" with the key "server.package.controller". You can then simply create a new class in
this package annotated with Controller and extending AbstractController.
``` java
@Controller("meta")
public class MetaController extends AbstractController {

}
```
> The meta controller is included with all SkuggaHttps servers and can be disabled by setting 'server.controller.meta' 
to false.

The server will build urls using the root in your configuration. You can set the property 'server.root' to start 
every url with a prefix. This prefix is followed by the value of the controller annotation. So in this case with a 
root of /api/ the url would be '/api/meta'.

### GET
``` java
@Get
public Response ping() {
	return Response.ok();
}
```
Here is a method with the Get annotation. When no value is specified to the Get or Post annotation, the name of 
the method is automatically use to map a url. So to ping, you would go to '/api/meta/ping'. If the method name 
contains an underscore, it is replaced with a forward slash.

> To expose an endpoint that contains an underscore, you need to set the value for the Get or Post annotation.

``` java
@Get("get/{string}/{id}")
public SuperCoolEntity get(String category, ObjectId id) {
	...
	return superCoolEntity;
}
```
Using path parameters is pretty simple. There are 3 types of parameters: id, int and string. String and int needs
a parameter with the String or int type respectively. As for id, it needs a parameter that match the return type 
of createID in your HttpExchangeHandler. Note that the parameters in the url are in the same order as the method's
parameters, this is mandatory for the mapping to work.

> SkuggaHttps doesn't / won't support query parameters.

### POST
``` java
@Post
public Response create(CoolDTO dto) {
	return Response.ok();
}
```
Next, you can see the Post method. The post annotation works the same as the get. The only difference with a post
method is that the first argument of the method need to ALWAYS be the body. Following arguments can be path parameters.

## Add filters
To add filters, you'll need to first tell the server where you store your filter classes. This package can be 
specified in "http.properties" with the key "server.package.filter".
``` java
@Filter(0)
public class CustomFilter extends PreFilter {
	@Override
	public boolean apply(HttpSession httpSession) {
		return true;
	}
}
```
Filters are classes that are executed before and after your endpoint (PreFilter and PostFilter interfaces). These classes
are used internally to allow access to clients and validate input but you can more of them to complexify your routine.
When creating your own filters, it is important to specify an order value of 0 or higher. Negative values are used 
for internal filters and can change in future versions. The apply method takes an HttpSession and returns a boolean.
The return value indicates if this filter needs to be the last in the chain. Returning false will prevent any further
filter in the chain from executing.

The HttpSession is a wrapper object that contains every information that might (or not) be useful to your filter.
Note that certain values might be unavailable like the Response in a PreFilter and the body for a GET endpoint.

## Add models
To add models, you'll need to first tell the server where you store your model classes. This package can be specified
 in "http.properties" with the key "server.package.model". This is more for your convenience when linking the data.
 The internals do not actually use this property.

## Link your data
Linking your data needs you to implements 3 classes: a connector, an initializer and a repository.
### Data Connector
A connector is a class that allows you to... connect to your data. It creates a connection and returns a query builder.
Query builders are different from one data backend to another so you might need to create a wrapper to have all the
objects needed. Here is an example for a MongoDB backend.
``` java
public class MongoConnector extends DataConnector<Datastore> {

	@Override
	protected Datastore create() {
		PropertiesConfiguration config = HttpConfigProvider.get();
		Morphia morphia = new Morphia();

		// Get all models using the Reflections package and the models property.
		Reflections reflections = new Reflections(config.getString("server.package.model").get());
		Set<Class> models = reflections.getTypesAnnotatedWith(Entity.class).stream()
			.collect(Collectors.toSet());
		
		// Datastore is the Query Builder for MongoDB / Morphia
		Datastore store = morphia.map(models)
			.createDatastore(new MongoClient(), config.getString("server.database").get());
		store.ensureIndexes();

		return store;
	}

}
```

### Data Repository
The data repository is the main meat of your data implementation. It tells SkuggaHttps how to perform CRUD actions
on your data layer. The DataRepository class takes 3 type parameters, the first one is the Query object obtained via
your query builder in the connector, the second is the type used as a primary id and the last type is the type of 
object this repository is going to manipulate. The last argument must implement Identifiable which allows the 
repository to do comparison and search on ids.
> You don't want to implement all this for each model so I would advise to create a factory method that takes the
> model class as argument.

Since MongoDB is single threaded my query builder is static here. You might also see that the initializer and
connector logic are handle by you in the constructor. This is because the connector can return a wrapper and the
initializer might require some pre-initialization. In this, this is the standard and expected setup for these two
classes.

``` java
public class MongoRepository<T extends Identifiable> extends DataRepository<Query<T>, ObjectId, T> {

	private static Datastore store = null;

	public MongoRepository(Class<T> clazz) {
		super(clazz);

		if(store == null) {
			MongoConnector connector = new MongoConnector();
			store = connector.create();
			connector.initialize(new MongoInitializer());
		}
	}

	@Override
	protected T byId(ObjectId id) {}

	@Override
	public T id(String id) {}

	@Override
	public T single(Function<Query<T>, T> filter) {}

	@Override
	public List<T> list(Function<Query<T>, List<T>> filter) {}

	@Override
	public T create(T entity) {}

	@Override
	public List<T> createAll(Iterable<T> entities) {}

	@Override
	protected T doUpdate(T entity) {}

	@Override
	public List<T> updateAll(Iterable<T> entities) {}

	@Override
	public boolean delete(T entity) {}

	@Override
	public boolean deleteAll(Iterable<T> entities) {}

}
```

### Data Initializer
The data initializer is a class that gets executed every time you start the server. This is mostly useful in
development cycles where you might want to truncate your tables and fill them with trusted values.
``` java
public class MongoInitializer implements DataInitializer {

	@Override
	public void initialize() {
		// Create repository and entities here.
	}

}
```

## Authentication & Access
(TO BE DONE)
