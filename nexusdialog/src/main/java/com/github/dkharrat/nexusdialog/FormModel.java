package com.github.dkharrat.nexusdialog;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * <code>FormModel</code> is an abstract class that represents the backing data for a form. It provides a mechanism
 * for form elements to retrieve their values to display to the user and persist changes to the model upon changes.
 */
public abstract class FormModel {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * This method is called when a form element changes its value through user input or external changes. Subclasses
     * must implement this method to update the backing model.
     *
     * @param name      the field name to set the value for
     * @param newValue  the value to set
     */
    protected abstract void setBackingValue(String name, Object newValue);

    /**
     * This method is called whenever the form needs the current value for a specific field. Subclasses must implement
     * this method to provide the form access to the backing model.
     *
     * @param name      the field name to retrieve the value for
     * @return          the current value of the specified field
     */
    protected abstract Object getBackingValue(String name);

    /**
     * Returns the value for the specified field name.
     *
     * @param name  the field name
     * @return      the value currently set for the specified field name
     */
    public final Object getValue(String name) {
        return getBackingValue(name);
    }

    /**
     *
     * Sets a value for the specified field name. A property change notification is fired to registered listeners if
     * the field's value changed.
     *
     * @param name      the field name to set the value for
     * @param newValue  the value to set
     */
    public final void setValue(String name, Object newValue) {
        Object curValue = getBackingValue(name);
        if (!objectsEqual(curValue, newValue)) {
            setBackingValue(name, newValue);
            propertyChangeSupport.firePropertyChange(name, curValue, newValue);
        }
    }

    private static boolean objectsEqual(Object a, Object b) {
        return a == b || (a != null && a.equals(b));

    }

    /**
     * Subscribes {@code listener} to change notifications for all fields.
     *
     * @see PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Subscribes {@code listener} to change notifications for the specified field name.
     *
     * @see PropertyChangeSupport#addPropertyChangeListener(String, java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String fieldName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(fieldName, listener);
    }

    /**
     * Unsubscribes {@code listener} from change notifications for all fields.
     *
     * @see PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Unsubscribes {@code listener} from change notifications for the specified field name.
     *
     * @see PropertyChangeSupport#removePropertyChangeListener(String, java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String fieldName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(fieldName, listener);
    }
}
