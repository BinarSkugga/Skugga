# SkuggaHttps
## Description
SkuggaHttps is a lightweight HTTPS server hosted with Oracle's HTTPS server implementation. The goal of this project is to have a fast, clean and easy to understand annotation API to create REST servers and data APIs.

## Strict URL Overloading
URL overloading is a subject of opinion. SkuggaHttps doesn't allow for much overloading to keep things clear and easy to follow. You can overload an endpoint with a different number of path parameters but you can not have two endpoints with the same number of paramters of different types. For example:

*/api/inventory/electronic/{int}* **can** coexist with */api/inventory/electronic/{int}/{string}*

But...

*/api/inventory/electronic/{int}* **cannot** coexist with */api/inventory/electronic/{string}*

The main reason for this is both performance and clarity. All parameters in a URL is a string at its core. Having the same url with the same amount of parameters causes a conflict that needs to be resolved through a priority system. For example, an Integer is more specific than a String so you test for that first and only assume a String if the Integer fails. It all good and games until you want 10-15 different types of arguments and you need to go thought all those fails before getting a response. So yes, it is possible but no it will not be added.

## Questions
**Q: Is there a way to use HTTP if I don't need the whole...**

R: No. Only HTTPS using PKCS12 keystore is available. Might add other keystore in the future but not plain HTTP.

**Q: Can I use HTTP/2.0**

R: No, I still have no idea how to implement that with only the Java Standard Library. Please contact me if you know.

**Q: Is the server using compression ?**

R: Yes, the server uses GZIP when the browser specifies it as an accepted encoding in its request headers. It doesn't support br yet.
