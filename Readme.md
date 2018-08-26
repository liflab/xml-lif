An XML library for Java
=======================

This is a simple library for parsing and outputing XML strings. It also
provides a simple XPath 1.0 processor.

Build instructions
------------------

Download the source code or clone the repository. If you have Ant, you
can compile by simply typing:

    $ ant

at the command prompt. This will create the `xml-lif.jar` library,
which you can then include in your projects.

Usage
-----

### To parse a string

``` java
String my_string = "some XML...":
try {
  XmlElement elem = XmlElement.parse(my_string);
} catch (XmlParseException e) {
  // Do something
}
```

### To create an XML structure

``` java
XmlElement my_element = new XmlElement("foo");
XmlElement inside = new XmlElement("bar");
inside.addChild(new TextElement("Hello"));
my_element.addChild(inside);
```

### To query an object using an XPath expression

``` java
XPathExpression exp = XPathExpression.parse("foo/bar/text()");
try {
  Collection<XmlElement> result = XP.get(my_map, "b[0]");
} catch (XPathParseException e) {
  // Do something
}
```

With the element `my_element` created above, this would return the
`TextElement` with value "Hello".
