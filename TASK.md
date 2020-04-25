# The Gilded Rose Expands

As you may know, the Gilded Rose* is a small inn in a prominent city that buys and sells only the finest
items. The shopkeeper, Terry, is looking to expand by providing merchants in other cities with access to
the shop&#39;s inventory via a public HTTP-accessible API. The Gilded Rose would like to utilize a surge
pricing model like Uber to increase the price of items by 10% if they have been viewed more than 10
times in an hour.

## API requirements

- Retrieve the current inventory (i.e. list of items)
- Buy an item (user must be authenticated)

Here is the definition for an item:

```class Item
{
  public String Name {get; set;}
  public String Description {get; set;}
  public int Price {get; set;}
}
```

A couple questions to consider:

- How do we know if a user is authenticated?
- Is it always possible to buy an item?
Please model (and test!) accordingly. Using a real database is not required.

## Deliverables

1. A runnable system with instructions on how to build/run your application

    a. The application should be built using Java and the Spring framework

    b. The application should be able to be run from the command line without any
dependencies or database’s required to run your application. (Other than maven,
gradle and Java)

2. A system that can process the two API requests via HTTP
3. Appropriate tests (unit, integration etc)
4. A quick explanation of:

    a. Your application, how you set it up, how it was built, how you designed the surge pricing
and the type of architecture chosen.

    b. Choice of data format. Include one example of a request and response.
    
    c. What authentication mechanism was chosen, and why?

__* The Gilded Rose is a refactoring/TDD exercise created by Terry Hughes and Bobby Johnson. You can safely ignore it during the coding
exercise._
