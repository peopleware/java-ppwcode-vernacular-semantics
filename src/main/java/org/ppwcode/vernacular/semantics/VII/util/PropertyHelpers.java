/*<license>
Copyright 2004 - 2016 by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.ppwcode.vernacular.semantics.VII.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

import static org.ppwcode.vernacular.exception.IV.util.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception.IV.util.ProgrammingErrorHelpers.unexpectedException;


/**
 * <p>Utility methods for property reflection (a.k.a. introspection). Use these methods if you are
 *   interested in the result of reflection, and not in a particular reason why some reflective
 *   inspection might have failed. Other sources are methods available in {@link java.beans} and
 *   <a href="http://commons.apache.org/beanutils/" target="extern">Apache Jakarta Commons BeanUtils</a>,
 *   but for the methods here, the ppwcode exception vernacular is applied.</p>
 * <p>Programmatic property names used in this class can be nested, as described in
 *   <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
 *   target="external">Apache Jakarta Commons BeanUtils PropertyUtils</a></p>
 *
 * <p>Note: this is a quick unfinished revival of ppwcode util reflect.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class PropertyHelpers {

  /**
   * <p>Grep pattern to find dot.</p> <p><code>DOT_PATTERN = &quot;{@value}&quot;</code></p>
   */
  public final static String DOT_PATTERN = "\\.";

  private PropertyHelpers() {
    // no instances possible
  }
//
//  /**
//   * The empty String.
//   */
//  public static final String EMPTY = "";
//
//  /**
//   * The word before the first dot in {@code nestedPropertyName}.
//   */
//  @MethodContract(
//    pre  = @Expression("nestedPropertyName != null"),
//    post = @Expression("_nestedPropertyName.indexOf(DOT) >= 0 ? " +
//                        "_nestedPropertyName.substring(0, _nestedPropertyName.indexOf(DOT)) : " +
//                        "_nestedPropertyName")
//  )
//  public static String carNestedPropertyName(String nestedPropertyName) {
//    return nestedPropertyName.split(DOT_PATTERN)[0];
//  }
//
//  /**
//   * The string after the first dot in {@code nestedPropertyName}.
//   */
//  @MethodContract(
//    pre  = @Expression("nestedPropertyName != null"),
//    post = @Expression("_nestedPropertyName.indexOf(DOT) >= 0 ? " +
//                        "_nestedPropertyName.substring(_nestedPropertyName.indexOf(DOT) + 1, _nestedPropertyName.length()) : " +
//                        "EMPTY")
//  )
//  public static String cdrNestedPropertyName(String nestedPropertyName) {
//    int firstDot = nestedPropertyName.indexOf(TypeName.DOT);
//    if (firstDot < 0) {
//      return EMPTY;
//    }
//    else {
//      return nestedPropertyName.substring(firstDot + 1, nestedPropertyName.length());
//    }
//  }
//
  /**
   * Returns the {@link PropertyDescriptor} of the property with simple name <code>propertyName</code> of
   * <code>type</code>. If no property descriptor is found, {@code null} is returned. Other problems are
   * considered a programming error.
   * This method is kept private not to confuse users. It is introduced to tackle the big overlap in code between
   * several methods.
   *
   * @param     type
   *            The bean type to get the property descriptor of
   * @param     propertyName
   *            The simple programmatic name of the property we want the descriptor for
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("hasProperty(_type, _propertyName)")
    },
    post = {
      @Expression("PropertyUtils.getPropertyDescriptors(_type).contains(result)"),
      @Expression("result.name == _propertyName")
    }
  )
  */
  private static PropertyDescriptor simplePropertyDescriptorHelper(final Class<?> type, final String propertyName) {
    assert preArgumentNotNull(type, "clazz");
    PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(type);
      // entries in the array are never null
    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
      assert propertyDescriptor != null;
      if (propertyDescriptor.getName().equals(propertyName)) {
        return propertyDescriptor;
      }
    }
    // not found
    return null;
  }

  /**
   * Returns the {@link PropertyDescriptor} of the property with simple name <code>propertyName</code> of
   * <code>type</code>. If no property descriptor is found, this is considered a programming error.
   *
   * @param     type
   *            The bean type to get the property descriptor of
   * @param     propertyName
   *            The simple programmatic name of the property we want the descriptor for
   *
   * <p>Note: This method was deprecated in version V of ppwcode util reflect, and now no longer is.
   *   The reason for its existence is that {@link PropertyUtils} does not feature a method to retrieve a
   *   {@link PropertyDescriptor} based on a {@link Class} argument.
   * <p>Note: renamed from {@code getPropertyDescriptor()}</p>
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("hasProperty(_type, _propertyName)")
    },
    post = {
      @Expression("result != null"),
      @Expression("PropertyUtils.getPropertyDescriptors(_type).contains(result)"),
      @Expression("result.name == _propertyName")
    }
  )
  */
  public static PropertyDescriptor simplePropertyDescriptor(final Class<?> type, final String propertyName) {
    assert preArgumentNotNull(type, "clazz");
    PropertyDescriptor result = simplePropertyDescriptorHelper(type, propertyName);
    if (result == null) {
      // not found: precondition violation
      throw new AssertionError("no property descriptor found with name \"" + propertyName + "\" in type " + type.getName());
    }
    return result;
  }

//  /**
//   * Returns the {@link PropertyDescriptor} of the property with name {@code propertyName} of
//   * <code>type</code>. {@code propertyName} can have a nested property name syntax
//   * (&quot;{@code propertyName1.propertyName2.propertyName3...}&quot;).
//   * In navigating the nested properties, the static type of each consecutive property is used
//   * to look for the next property.
//   * If no property descriptor is found, for any reason, this is considered a programming error.
//   *
//   * @param     type
//   *            The starting bean type to get the property descriptor of
//   * @param     propertyName
//   *            The programmatic name of the property we want the descriptor for. May be a nested name.
//   *
//   * @note This method was deprecated in version V, and now no longer is. The reason for its existence is that
//   *       {@link PropertyUtils} does not feature a method to retrieve a {@link PropertyDescriptor} based on a
//   *       {@link Class} argument.
//   * @note renamed from {@code getPropertyDescriptor()}
//   *
//   * @idea extend to use BeanUtils mapped notation for properties
//   */
//  @MethodContract(
//    pre  = {
//      @Expression("_type != null"),
//      @Expression("_propertyName != null"),
//      @Expression("_propertyName != EMPTY"),
//      @Expression("hasProperty(_type, _propertyName)")
//    },
//    post = {
//      @Expression("result != null"),
//      @Expression("_propertyName.contains(DOT) ? " +
//                    "propertyDescriptor(simplePropertyDescriptor(type, carNestedPropertyName(propertyName).getPropertyType()), " +
//                      "carNestedPropertyName(propertyName)) :" +
//                    "simplePropertyDescriptor(type, propertyName")
//    }
//  )
//  public static PropertyDescriptor propertyDescriptor(final Class<?> type, final String propertyName) {
//    assert preArgumentNotNull(type, "type");
//    assert preArgumentNotEmpty(propertyName, "propertyName");
//    String[] propertyNames = propertyName.split(DOT_PATTERN);
//    Class<?> currentType = type;
//    PropertyDescriptor pd = null;
//    for (String pn : propertyNames) {
//      pd = simplePropertyDescriptor(currentType, pn);
//      assert pd != null;
//      currentType = pd.getPropertyType();
//    }
//    if (pd == null) {
//      throw new AssertionError("propertyName should not be empty");
//    }
//    return pd;
//  }

//  /**
//   * Return the type of the property with name <code>propertyName</code>
//   * of type <code>type</code>. {@code propertyName} can have a nested property name syntax
//   * (&quot;{@code propertyName1.propertyName2.propertyName3...}&quot;).
//   * In navigating the nested properties, the static type of each consecutive property is used
//   * to look for the next property.
//   * If no property with the given name is found for any reason, this is considered a programming error.
//   *
//   * @param     type
//   *            The bean type to get the property descriptor of
//   * @param     propertyName
//   *            The programmatic name of the property we want the type for
//   *
//   * @note renamed from {@code getPropertyType()}
//   */
//  @MethodContract(
//    pre  = {
//      @Expression("_type != null"),
//      @Expression("hasProperty(_type, _propertyName)")
//    },
//    post = @Expression("propertyDescriptor(_type, _propertyName).propertyType")
//  )
//  public static Class<?> propertyType(final Class<?> type, final String propertyName) {
//    assert preArgumentNotNull(type, "clazz");
//    return propertyDescriptor(type, propertyName).getPropertyType();
//  }
//
//  /**
//   * Return true if {@code type} has a property with simple name {@code propertyName}.
//   *
//   * @param     type
//   *            The bean type to get the property descriptor of
//   * @param     propertyName
//   *            The simple programmatic name of the property we want to know exists
//   */
//  @MethodContract(
//    pre  = @Expression("_type != null"),
//    post = @Expression("exists(PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(_type)) {" +
//                         "pd.name == _propertyName" +
//                       "}")
//  )
//  public static boolean hasSimpleProperty(final Class<?> type, final String propertyName) {
//    assert preArgumentNotNull(type, "clazz");
//    return simplePropertyDescriptorHelper(type, propertyName) != null;
//  }

  /**
   * <p>Return true if {@code type} has a property with name {@code propertyName}.
   *   {@code propertyName} can have a nested property name syntax
   *   (&quot;{@code propertyName1.propertyName2.propertyName3...}&quot;).
   *   In navigating the nested properties, the static type of each consecutive property is used
   *   to look for the next property.</p>
   *
   * @param     type
   *            The starting bean type to get the property descriptor of
   * @param     propertyName
   *            The programmatic name of the property we want to know exists. May be a nested name.
   */
  /*
  @MethodContract(
    pre  = @Expression("_type != null"),
    post = @Expression("_propertyName.contains(DOT) ? " +
                         "hasSimpleProperty(type, carNestedPropertyName(propertyName)) && " +
                           "hasProperty(simplePropertyDescriptor(type, carNestedPropertyName(propertyName)).getPropertyType(), " +
                           "cdrNestedPropertyName(propertyName)) : " +
                         "hasSimpleProperty(type, propertyName")
  )
  */
  public static boolean hasProperty(final Class<?> type, final String propertyName) {
    assert preArgumentNotNull(type, "type");
    String[] propertyNames = propertyName.split(DOT_PATTERN);
    Class<?> currentType = type;
    PropertyDescriptor pd;
    for (String pn : propertyNames) {
      pd = simplePropertyDescriptorHelper(currentType, pn);
      if (pd == null) {
        return false;
      }
      else {
        currentType = pd.getPropertyType();
      }
    }
    // if we get here, the last pd was not null either
    return true;
  }
//
//  /**
//   * Returns the {@link PropertyEditor} of the property with name <code>propertyName</code> of type <code>type</code>,
//   * using the property editor class set in the property descriptor of the property, using its default constructor,
//   * or the {@link PropertyEditorManager} if that is lacking (which it normally is). {@code propertyName} can have a
//   * nested property name syntax (&quot;{@code propertyName1.propertyName2.propertyName3...}&quot;). In navigating
//   * the nested properties, the static type of each consecutive property is used to look for the next property.
//   * If no property with the given name is found for any reason, or no property editor is found, this is considered
//   * a programming error.
//   *
//   * @param     type
//   *            The bean type to get the property editor of
//   * @param     propertyName
//   *            The programmatic name of the property we want the editor for
//   */
//  @MethodContract(
//    pre  = {
//      @Expression("_type != null"),
//      @Expression("_propertyName != null"),
//      @Expression("_propertyName != EMPTY"),
//      @Expression("hasProperty(_type, _propertyName)"),
//      @Expression("hasPropertyEditor(_type, _propertyName)")
//    },
//    post = {
//      @Expression("result != null"),
//      @Expression("result instanceof (propertyDescriptor(_type, _propertyName).getPropertyEditorClass() != null ? " +
//                                    "propertyDescriptor(_type, _propertyName).getPropertyEditorClass() : " +
//                                    "PropertyEditorManager.findEditor(propertyDescriptor(_type, _propertyName).getPropertyType()))")
//    }
//  )
//  public static PropertyEditor propertyEditor(final Class<?> type, final String propertyName) {
//    assert preArgumentNotNull(type, "type");
//    assert preArgumentNotEmpty(propertyName, "propertyName");
//    PropertyEditor result = null;
//    PropertyDescriptor descriptor = propertyDescriptor(type, propertyName);
//    Class<?> editorClass = descriptor.getPropertyEditorClass();
//    if (editorClass != null) {
//      try {
//        @SuppressWarnings("unchecked")
//        Class<? extends PropertyEditor> safeEditorClass = (Class<? extends PropertyEditor>)editorClass;
//        result = newInstance(safeEditorClass);
//      }
//      catch (ClassCastException e) {
//        unexpectedException(e, "using editor class " + editorClass + " set in property descriptor for property with " +
//                            "name " + propertyName + " in type " + type.getName());
//      }
//    }
//    else {
//      result = PropertyEditorManager.findEditor(descriptor.getPropertyType());
//      if (result == null) {
//        // not found: precondition violation
//        throw new AssertionError("no property editor found for property with name \"" + propertyName + "\" in type " + type.getName());
//      }
//    }
//    return result;
//  }

  // DONE UNTIL HERE
//
  /**
   * Returns the method object of the inspector of the property with
   * name <code>propertyName</code> of <code>beanClass</code>. If such a
   * property or such an inspector does not exist, an exception is thrown.
   * This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods.
   *
   * // TODO check that this method only finds implemented methods, thus not methods in interfaces or abstract methods, and fix.
   *
   * @param     beanClass
   *            The bean class to get the property read method of
   * @param     propertyName
   *            The programmatic name of the property we want to read
   */
  /*
  @MethodContract(
    pre  = @Expression("beanClass != null"),
    post = {
      @Expression("result != null"),
      @Expression("getPropertyDescriptor(beanClass, propertyName).readMethod")
    },
    exc  = {
      @Throw(type = IntrospectionException.class, cond = @Expression(value = "true", description = "Cannot get the BeanInfo of <beanClass.")),
      @Throw(type = NoSuchMethodException.class, cond = @Expression(value = "getPropertyDescriptor(beanClass, propertyName).readMethod == null"))
    }
  )
  */
  public static Method propertyReadMethod(final Class<?> beanClass, final String propertyName) {
    assert beanClass != null;
    Method inspector = simplePropertyDescriptor(beanClass, propertyName).getReadMethod();
        // this can be null for an read-protected property
    if (inspector == null) {
      throw new AssertionError("No read method for property " + propertyName);
    }
    return inspector;
  }

//  /**
//   * Check whether <code>beanClass</code> has a no-arguments getter for
//   * <code>propertyName</code>. This method only finds implemented methods,
//   * thus not methods in interfaces or abstract methods.
//   *
//   * @todo check that This method only finds implemented methods,
//   * thus not methods in interfaces or abstract methods, and fix.
//   *
//   * @param     beanClass
//   *            The bean class to get the property read method of
//   * @param     propertyName
//   *            The programmatic name of the property we want to read
//   */
//  @MethodContract(
//    pre  = @Expression("beanClass != null"),
//    post = @Expression("getPropertyDescriptor(beanClass, propertyName).readMethod != null"),
//    exc  = @Throw(type = IntrospectionException.class, cond = @Expression(value = "true", description = "Cannot get the BeanInfo of <beanClass."))
//  )
//  public static boolean hasPropertyReadMethod(final Class<?> beanClass, final String propertyName)
//      throws IntrospectionException {
//    assert beanClass != null;
//    return propertyDescriptor(beanClass, propertyName).getReadMethod() != null;
//  }
//
//  /**
//   * Returns the method object of the mutator of the property with
//   * name <code>propertyName</code> of <code>beanClass</code>. If such a
//   * property or such an mutator does not exist, an exception is thrown.
//   *
//   * @param     beanClass
//   *            The bean class to get the property write method of
//   * @param     propertyName
//   *            The programmatic name of the property we want to write
//   */
//  @MethodContract(
//    pre  = @Expression("beanClass != null"),
//    post = {
//      @Expression("result != null"),
//      @Expression("getPropertyDescriptor(beanClass, propertyName).writeMethod")
//    },
//    exc  = {
//      @Throw(type = IntrospectionException.class, cond = @Expression(value = "true", description = "Cannot get the BeanInfo of <beanClass.")),
//      @Throw(type = NoSuchMethodException.class, cond = @Expression(value = "getPropertyDescriptor(beanClass, propertyName).writeMethod == null"))
//    }
//  )
//  public static Method propertyWriteMethod(final Class<?> beanClass, final String propertyName) {
//    assert beanClass != null;
//    Method mutator = propertyDescriptor(beanClass, propertyName).getWriteMethod();
//        // this can be null for a write-protected property
//    if (mutator == null) {
//      throw new AssertionError("No write method for property " + propertyName);
//    }
//    return mutator;
//  }

  /**
   * The value of property {@code propertyName} of {@code bean}.
   */
  public static <_Value_> _Value_ propertyValue(final Object bean, final String propertyName) {
    Method inspector;
    inspector = propertyReadMethod(bean.getClass(), propertyName); // MUDO should use dynamic method; use method from PropertyUtils?
    assert inspector != null;
    try {
      @SuppressWarnings("unchecked") _Value_ result = (_Value_)inspector.invoke(bean);
      return result;
    }
    catch (IllegalArgumentException iaExc) {
      unexpectedException(iaExc, "there are no arguments, and the implicit argument is not null and of the correct type");
    }
    catch (NullPointerException npExc) {
      unexpectedException(npExc, "implicit argument is not null");
    }
    catch (IllegalAccessException | InvocationTargetException | ExceptionInInitializerError iaExc) {
      unexpectedException(iaExc);
    }
    catch (ClassCastException ccExc) {
      unexpectedException(ccExc, "retrieve value not of expected type");
    }
    return null;
  }

  /**
   * The value of property {@code propertyName} of {@code bean}.
   */
  public static <_Value_> _Value_ propertyValue(final Object bean, final String propertyName, Class<_Value_> valueType) {
    Method inspector;
    inspector = propertyReadMethod(bean.getClass(), propertyName); // MUDO should use dynamic method; use method from PropertyUtils?
    assert inspector != null;
    try {
      return valueType.cast(inspector.invoke(bean));
    }
    catch (IllegalArgumentException iaExc) {
      unexpectedException(iaExc, "there are no arguments, and the implicit argument is not null and of the correct type");
    }
    catch (NullPointerException npExc) {
      unexpectedException(npExc, "implicit argument is not null");
    }
    catch (IllegalAccessException | InvocationTargetException | ExceptionInInitializerError iaExc) {
      unexpectedException(iaExc);
    }
    catch (ClassCastException ccExc) {
      unexpectedException(ccExc, "retrieve value not of expected type");
    }
    return null;
  }

//  /**
//   * Set the property {@code propertyName} of {@code bean} to {@code value}.
//   * Anything that goes wrong is considered a programming error.
//   */
//  public static void setPropertyValue(Object bean, String propertyName, Object value) {
//    try {
//      robustSetPropertyValue(bean, propertyName, value);
//    }
//    catch (Exception exc) {
//      unexpectedException(exc);
//    }
//  }
//
//  /**
//   * Set the property {@code propertyName} of {@code bean} to {@code value}.
//   * Anything that goes wrong is considered a programming error, except
//   * exception types listed as parameter.
//   *
//   * {@code expectedExceptionType} must be subtypes of {@link Exception}.
//   * This is not enforced with generics, because then we get a warning when we use
//   * the method, sadly.
//   */
//  public static void robustSetPropertyValue(Object bean, String propertyName, Object value, Class<?>... expectedExceptionType) throws Exception {
//    assert pre(! Arrays.asList(expectedExceptionType).contains(null));
//    assert pre(preAssert(expectedExceptionType));
//    try {
//      PropertyUtils.setProperty(bean, propertyName, value);
//    }
//    catch (InvocationTargetException exc) {
//      for (Class<?> eet : expectedExceptionType) {
//        @SuppressWarnings("unchecked")
//        Exception expectedExc = huntFor(exc, (Class<? extends Exception>)eet);
//        if (expectedExc != null) {
//          throw expectedExc;
//        }
//      }
//      unexpectedException(exc);
//    }
//    catch (IllegalAccessException exc) {
//      unexpectedException(exc);
//    }
//    catch (NoSuchMethodException exc) {
//      unexpectedException(exc);
//    }
//  }
//
//  private static boolean preAssert(Class<?>[] expectedExceptionType) {
//    for (Class<?> el : expectedExceptionType) {
//      if (! Exception.class.isAssignableFrom(el)) {
//        return false;
//      }
//    }
//    return true;
//  }

}
