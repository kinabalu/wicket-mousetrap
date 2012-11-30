wicket-mousetrap
================

A small wicket 6 library to utilize mousetrap.js [http://craig.is/killing/mice].  For an example of how this works
you can see a [demo](http://mysticpaste.com/).

Here's an example of usage pulled from the [Mystic Pastebin](http://github.com/kinabalu/mysticpaste)

```java
final AbstractDefaultAjaxBehavior historyNav = new AbstractDefaultAjaxBehavior() {
    @Override
    protected void respond(AjaxRequestTarget target) {
        throw new RestartResponseException(HistoryPage.class);
    }
};
add(historyNav);

mousetrap.addBind(new KeyBinding().addKeyCombo("n"), historyNav);
```

Library supports the standard key bindings, along with global bindings (works inside form fields as well):

regular binding:
```java
mousetrap.addBind(new KeyBinding().addKeyCombo("ctrl+enter"), myAjaxBehavior);
```

global binding:
```java
mousetrap.addGlobalBind(new KeyBinding().addKeyCombo("ctrl+enter"), myAjaxBehavior);
```

See the javascript page for examples of more usage [http://craig.is/killing/mice].


If you'd like to use our maven repo add the following to your Maven configuration files:
```xml
<repositories>
    ...
    <repository>
        <id>mystic-mvn-repo</id>
        <name>Mystic Github Maven Repository</name>
        <url>https://raw.github.com/kinabalu/mystic-mvn-repo/master/snapshots</url>
    </repository>
</repositories>

...

<dependencies>
    ...
    <dependency>
        <groupId>com.mysticcoders</groupId>
        <artifactId>wicket-mousetrap</artifactId>
        <version>0.1-SNAPSHOT</version>
    </dependency>
    ...
```