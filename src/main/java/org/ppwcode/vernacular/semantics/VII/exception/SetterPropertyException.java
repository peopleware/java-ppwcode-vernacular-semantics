/*<license>
Copyright 2004 - 2016 by PeopleWare n.v.

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

package org.ppwcode.vernacular.semantics.VII.exception;


import org.ppwcode.vernacular.exception.IV.ApplicationException;

import static org.ppwcode.vernacular.semantics.VII.util.CloneHelpers.safeReference;


/**
 * <p>In many cases, a property exception is needed that reports the new value that was tried to set, and the
 *   old value of the property. These values can be used to generate sensible end-user messages of the form
 *   <em>Unable to change {propertyName} for {origin} from {propertyValue} to {vetoedValue}</em>.</p>
 * <p>This exception is a generalized version of a {@link PropertyException} that carries that information.
 *   It is a bore to create separate exceptions for each of those specific cases. It would be nice to use
 *   generics for the type of the property value, but generics are not possible for exceptions.</p>
 * <p>This exception can be used for simple properties of all kinds: simple properties of reference type, as
 *   well as simple properties of value types, both of mutable types and immutable types. When the values are
 *   put into the exception and when they are returned out of the exception, we try to clone them. We expect
 *   mutable value types to be cloneable, and immutable value types and reference types not to be cloneable.
 *   This way, the actual value is guarded from change.</p>
 * <p>This kind of exception cannot be used in a constructor.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@SuppressWarnings("WeakerAccess")
public class SetterPropertyException extends ValuePropertyException {

  /*<construction>*/
  //-------------------------------------------------------------------------

  /**
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     vetoedValue
   *            The value that was tried to set on property {@code propertyName},
   *            which is being vetoed by this exception.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_origin != null"),
      @Expression("_propertyName != null"),
      @Expression("hasProperty(_origin.class, _propertyName)"),
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == _origin"),
      @Expression("originType == _origin.class"),
      @Expression("propertyName == _propertyName"),
      @Expression("propertyValue == safeReference(_origin[_propertyName])"),
      @Expression("vetoedValue == safeReference(_vetoedValue)"),
      @Expression("value == _ value"),
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  */
  public SetterPropertyException(final Object origin,
                                 final String propertyName,
                                 final Object vetoedValue,
                                 final String message,
                                 final Throwable cause) {
    super(origin, propertyName, message, cause);
    $vetoedValue = safeReference(vetoedValue);
  }

  /*</construction>*/



  /*<property name="vetoed value">*/
  //------------------------------------------------------------------

  /**
   * The value that was tried to set on property {@link #getPropertyName()},
   * which was vetoed by this exception.
   */
  /*
  @Basic
  */
  public final Object getVetoedValue() {
    return safeReference($vetoedValue);
  }

  private Object $vetoedValue;

  /*</property>*/



  /*<section name="comparison">*/
  //------------------------------------------------------------------

  /**
   * Compare {@code other} to this: is other of the the exact same
   * type and does other have the exact same properties, including
   * {@link #getVetoedValue()}.
   *
   * @since VI
   */
  /*
  @MethodContract(
    post = @Expression("result ? other.vetoedValue == vetoedValue")
  )
  */
  @Override
  public boolean like(ApplicationException other) {
    return super.like(other) && eqn(((SetterPropertyException)other).getVetoedValue(), getVetoedValue());
  }

  /*</property>*/

}
