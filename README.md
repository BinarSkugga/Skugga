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
method is that the first argument of the method need to ALWAYS be the body. Following arguments can be followed by
path params.

## Add filters
To add filters, you'll need to first tell the server where you store your filter classes. This package can be 
specified in "http.properties" with the key "server.package.filter".

## Add models
To add models, you'll need to first tell the server where you store your model classes. This package can be specified
 in "http.properties" with the key "server.package.model".

## Add roles
(TO BE DONE)

## Link your data
(TO BE DONE)
