# A simple Wicket component to render a Vue app

This simple Wicket component allows you to easily render a Vue 3 app in Java. This 
component is implemented in about just 130 lines of code (including Java, HTML and js), so
there is no risk in using it.

## How to use
Suppose that you want a Vue app on an HTML page like below:
```html
    <script src="https://.../vue.js"></script>
    <div id="app1"></div>
    <script>
    var app = Vue.createApp({
        data() {
            return {a: 4, b: "Hi"};
        },
        template: `<span @click='m1()'>{{a}} {{b}}<span>`,
        methods {
            m1() {
                console.log(this.a);
            }
        },
    });
    app.mount('#app1')
    </script>
```
And you want to generate this from a Wicket page. To do that, you
can use the WicketVueApp component provided by this project:
```java
import com.ttdev.WicketVueApp;

public class GetStartedDemo extends WebPage {
  public GetStartedDemo() {
    HashMap<String, Object> state = new HashMap<>();
    state.put("a", 4);
    state.put("b", "Hi");
    WicketVueApp vwa=new WicketVueApp("wva", 
        new Model(state), 
        "<span @click='m1()'>{{a}} {{b}}<span>",
        "m1() {console.log(this.a);}");
    add(vwa);
  }
}
```
The corresponding HTML markup can be just a `<div>`:
```html
<html>
<body>
<div wicket:id="wva"></div>
</body>
</html>
```
The WicketVueApp component will generate the desired HTML and js code inside the `<div>` automatically.
Now, run your Wicket webapp as usual and you will see the Vue app working on your Wicket page.

## Handling Vue events in Wicket

In the example above, suppose that you want to handle the click on the server side (Wicket), all
you need to do is to call a js method named "cb" (standing for "call back") as shown below:
```java
public class GetStartedDemo extends WebPage {
  public GetStartedDemo() {
    HashMap<String, Object> state = new HashMap<>();
    state.put("a", 4);
    state.put("b", "Hi");
    WicketVueApp vwa=new WicketVueApp("wva", 
        new Model(state), 
        "<span @click='cb()'>{{a}} {{b}}<span>");
    add(vwa);
  }
}
```
This cb method will use Wicket's ajax mechanism to send the request to the Wicket side, where
you can handle it by overriding the OnVueEvent method:
```java
public class GetStartedDemo extends WebPage {
  public GetStartedDemo() {
    HashMap<String, Object> state = new HashMap<>();
    state.put("a", 4);
    state.put("b", "Hi");
    WicketVueApp vwa=new WicketVueApp("wva", 
        new Model(state), 
        "<span @click='cb()'>{{a}} {{b}}<span>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("b", state.get("b")+"!");
      }
    };
    add(vwa);
  }
}
```
The cb method will automatically send the current state of the Vue app back to the WicketVueApp component
on server, which will use the data to update itself own state, before calling its onVueEvent method. This
way you will have access to the latest state of the Vue app in the browser.

In this method you can further modify the state, which will be sent back to the browser automatically 
to refresh the Vue app. Here, in this example, the "b" variable's value will have an exclamation mark 
added to it.

Note that the AjaxRequestTarget one of the parameters, you can add other ajax Wicket components to the
target to have them refreshed.

## Further functionality
The above sums up the core functionality. To further fine-tune its behaviour, please take a look at
the various [examples](https://github.com/freemant2000/WicketVueApp/tree/master/wicket-vue-app-examples/src/main/java/com/ttdev).