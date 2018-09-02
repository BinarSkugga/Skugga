# SkuggaHttps

## Create and start your server
First off all, you'll need to get your configuration file. You can do that using the HttpConfigProvider class that fetches the "http.properties" file in your resources.
``` java
PropertiesConfiguration config = HttpConfigProvider.get();
```
PropertiesConfiguration provides an easy interface to interacts with .properties files in your resource folder. Using this configuration you can easily create and start your server.
``` java
HttpServer server = new HttpServer(config.getString("server.ip"), config.getInt("server.port"), 
  new CustomExchangeHandler());
server.setThreadPoolSize(config.getInt("server.threads"));
server.start();
```

In this code we do a couple of things. First we create the server using our property "server.ip" and "server.port". Next we provide a HttpExchangeHandler to the server. This class is created by you and describes what id types you are using as well as how to transform object into json. It would looks like this for a MongoDB database using the BSON type ObjectId.
``` java
public class CustomExchangeHandler extends AbstractHttpExchangeHandler<ObjectId> {
	@Override
	public HttpJsonHandler getJsonHandler() {
		return new MoshiJsonHandler();
	}

	@Override
	public ObjectId createID(String s) {
		return new ObjectId(s);
	}
}
```

## Add controllers

## Add filters

## Add models

## Add roles

## Link your data
