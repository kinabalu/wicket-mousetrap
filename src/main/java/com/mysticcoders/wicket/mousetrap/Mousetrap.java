package com.mysticcoders.wicket.mousetrap;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Binding for mousetrap.js
 *
 * from: http://craig.is/killing/mice
 *
 * @author Andrew Lombardi
 */
public class Mousetrap extends Behavior {
    private static final long serialVersionUID = 1L;

    private Map<KeyBinding, AbstractDefaultAjaxBehavior> bindings = new HashMap<KeyBinding, AbstractDefaultAjaxBehavior>();
    private Map<KeyBinding, AbstractDefaultAjaxBehavior> globalBindings = new HashMap<KeyBinding, AbstractDefaultAjaxBehavior>();

    /**
     * Convenience method for returning a mousetrap binding call
     *
     * @param global are we going to bind this globally?
     * @param bindings list of bindings
     * @return Mousetrap bindings
     */
    private StringBuffer getMousetrapBinds(boolean global, Map<KeyBinding, AbstractDefaultAjaxBehavior> bindings) {
        StringBuffer mousetrapBinds = new StringBuffer();
        for (Map.Entry<KeyBinding, AbstractDefaultAjaxBehavior> entry : bindings.entrySet()) {
            mousetrapBinds.append("Mousetrap.").append(global ? "bindGlobal" : "bind").append("(")
                    .append(entry.getKey())
                    .append(", function(e) { ")
                    .append(entry.getValue().getCallbackScript())
                    .append(" }");
            if(entry.getKey().getEventType()!=null) {
                mousetrapBinds.append(", '")
                        .append(entry.getKey().getEventType())
                        .append("'");
            }
            mousetrapBinds.append(");\n");
        }

        return mousetrapBinds;
    }

    /**
     * Render to the web response whatever the component wants to contribute to the head section.
     *
     * @param component component this behavior is attached to
     * @param response Response object
     */
    public void renderHead(final Component component, IHeaderResponse response) {
        super.renderHead(component, response);

        if (bindings.size() > 0) {
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Mousetrap.class, "mousetrap.min.js")));

            StringBuffer mousetrapBinds = getMousetrapBinds(false, bindings);
            response.render(OnDomReadyHeaderItem.forScript(mousetrapBinds));
        }

        if (globalBindings.size() > 0) {
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Mousetrap.class, "mousetrap-global.min.js")));

            StringBuffer mousetrapBinds = getMousetrapBinds(true, globalBindings);
            response.render(OnDomReadyHeaderItem.forScript(mousetrapBinds));
        }

    }

    /**
     * Adds a key binding to Mousetrap for given behavior
     *
     * @param keyBinding keys to bind
     * @param behavior behavior to execute upon binding being fired
     */
    public void addBind(KeyBinding keyBinding, AbstractDefaultAjaxBehavior behavior) {
        bindings.put(keyBinding, behavior);
    }

    /**
     * Adds a global key binding to Mousetrap for given behavior
     *
     * - this will fire wherever your focus is, including text fields, any form element
     *
     * @param keyBinding keys to bind
     * @param behavior behavior to execute upon binding being fired
     */
    public void addGlobalBind(KeyBinding keyBinding, AbstractDefaultAjaxBehavior behavior) {
        globalBindings.put(keyBinding, behavior);
    }



}