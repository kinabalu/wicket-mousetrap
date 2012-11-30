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
    private Map<KeyBinding, AbstractDefaultAjaxBehavior> defaultBindings = new HashMap<KeyBinding, AbstractDefaultAjaxBehavior>();
    private Map<KeyBinding, AbstractDefaultAjaxBehavior> defaultGlobalBindings = new HashMap<KeyBinding, AbstractDefaultAjaxBehavior>();

    private static final int BIND = 0;
    private static final int BIND_GLOBAL = 1;
    private static final int BIND_DEFAULT = 2;
    private static final int BIND_DEFAULT_GLOBAL = 3;

    /**
     * Convenience method for returning a mousetrap binding call
     *
     * @param type are we global, a default, or a regular bind
     * @param bindings list of bindings
     * @return Mousetrap bindings
     */
    private StringBuffer getMousetrapBinds(int type, Map<KeyBinding, AbstractDefaultAjaxBehavior> bindings) {
        StringBuffer mousetrapBinds = new StringBuffer();
        for (Map.Entry<KeyBinding, AbstractDefaultAjaxBehavior> entry : bindings.entrySet()) {
            mousetrapBinds.append("Mousetrap.").append(type == BIND_GLOBAL || type == BIND_DEFAULT_GLOBAL ? "bindGlobal" : "bind").append("(")
                    .append(entry.getKey())
                    .append(", function(e) { ");
            if(type == BIND_DEFAULT || type == BIND_DEFAULT_GLOBAL) {
                mousetrapBinds.append("console.log(e); if (e.preventDefault) {e.preventDefault();} else {e.returnValue = false;}");
            }
            mousetrapBinds.append(entry.getValue().getCallbackScript())
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

            StringBuffer mousetrapBinds = getMousetrapBinds(BIND, bindings);
            response.render(OnDomReadyHeaderItem.forScript(mousetrapBinds));
        }

        if (globalBindings.size() > 0) {
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Mousetrap.class, "mousetrap-global.min.js")));

            StringBuffer mousetrapBinds = getMousetrapBinds(BIND_GLOBAL, globalBindings);
            response.render(OnDomReadyHeaderItem.forScript(mousetrapBinds));
        }

        if (defaultBindings.size() > 0) {
            StringBuffer mousetrapBinds = getMousetrapBinds(BIND_DEFAULT, defaultBindings);
            response.render(OnDomReadyHeaderItem.forScript(mousetrapBinds));
        }

        if (defaultGlobalBindings.size() > 0) {
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Mousetrap.class, "mousetrap-global.min.js")));

            StringBuffer mousetrapBinds = getMousetrapBinds(BIND_DEFAULT_GLOBAL, defaultGlobalBindings);
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

    /**
     * Adds a default key binding to Mousetrap for given behavior
     *
     * - this will fire and override any default in the browser
     *
     * @param keyBinding keys to bind
     * @param behavior behavior to execute upon binding being fired
     */
    public void addDefaultBind(KeyBinding keyBinding, AbstractDefaultAjaxBehavior behavior) {
        defaultBindings.put(keyBinding, behavior);
    }

    /**
     * Adds a default global key binding to Mousetrap for given behavior
     *
     * - this will fire wherever your focus is, including text fields, any form element
     * - and it will fire and override any default in the browser
     *
     * @param keyBinding keys to bind
     * @param behavior behavior to execute upon binding being fired
     */
    public void addDefaultGlobalBind(KeyBinding keyBinding, AbstractDefaultAjaxBehavior behavior) {
        defaultGlobalBindings.put(keyBinding, behavior);
    }



}