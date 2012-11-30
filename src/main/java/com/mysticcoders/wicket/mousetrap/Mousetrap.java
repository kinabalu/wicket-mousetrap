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
 * Binding to mousetrap.js
 * from: http://craig.is/killing/mice
 *
 * @author Andrew Lombardi
 */
public class Mousetrap extends Behavior {
    private static final long serialVersionUID = 1L;

    private Map<KeyBinding, AbstractDefaultAjaxBehavior> bindings = new HashMap<KeyBinding, AbstractDefaultAjaxBehavior>();
    private Map<KeyBinding, AbstractDefaultAjaxBehavior> globalBindings = new HashMap<KeyBinding, AbstractDefaultAjaxBehavior>();

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
     * ...
     *
     * @param component
     * @param response
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
     * ...
     *
     * @param keyBinding
     * @param behavior
     */
    public void addBind(KeyBinding keyBinding, AbstractDefaultAjaxBehavior behavior) {
        bindings.put(keyBinding, behavior);
    }

    /**
     * ...
     *
     * @param keyBinding
     * @param behavior
     */
    public void addGlobalBind(KeyBinding keyBinding, AbstractDefaultAjaxBehavior behavior) {
        globalBindings.put(keyBinding, behavior);
    }



}