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
import org.ppwcode.vernacular.exception.IV.SemanticException;

import static org.ppwcode.vernacular.semantics.VII.util.PropertyHelpers.hasProperty;

/**
 * <p>{@code PropertyExceptions} are exceptions that carry with them information about the property for which they
 *   occurred. They are usually thrown by a property setter during validation. If the property name is {@code null},
 *   it means that the exception could not be attributed to a specific property of {@link #getOrigin()}.
 *   <em>The <code>origin</code> should not be <code>null</code></em>, except when the exception is thrown during
 *   construction of an object, that could not be completed. In that case, the type should be filled out. Carrying
 *   the reference to the object would expose an incompletely initialized object, as the exception signals a failure
 *   to complete the initialization. <em>The <code>originType</code> should not be <code>null</code></em>.
 *   A {@code PropertyException} reports on an issue with <em>one</em> object. If there is a need to communicate
 *   an issue over more than one issue, use a {@link SemanticException}. Specific property exception subtypes will
 *   make these advises binding in most cases.</p>
 * <p>Note: Throwables cannot have generic parameters. Otherwise we would have used this instead of
 *  {@link #getOriginType()}.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@SuppressWarnings("WeakerAccess")
public class PropertyException extends SemanticException {

  /*<construction>*/
  //-------------------------------------------------------------------------

  /**
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
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
      @Expression("_propertyName != null ? hasProperty(_origin.class, _propertyName)"),
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == _origin"),
      @Expression("originType == _origin.class"),
      @Expression("propertyName == _propertyName"),
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  */
  public PropertyException(Object origin, String propertyName, String message, Throwable cause) {
    super(message, cause);
    assert origin != null;
    assert ! (origin instanceof Class);
    assert (propertyName == null) || hasProperty(origin.getClass(), propertyName);
    $origin = origin;
    $originType = origin.getClass();
    $propertyName = propertyName;
  }

  /**
   * @param     originType
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   *
   * @since IV
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_originType != null"),
      @Expression("_propertyName != null ? hasProperty(_originType, _propertyName)"),
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("origin == null"),
      @Expression("originType == _originType"),
      @Expression("propertyName == _propertyName"),
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  */
  public PropertyException(Class<?> originType, final String propertyName, String message, Throwable cause) {
    super(message, cause);
    assert originType != null;
    assert (propertyName == null) || hasProperty(originType, propertyName);
    assert (message == null) || (!message.equals(""));
    $originType = originType;
    $propertyName = propertyName;
  }

  /*</construction>*/



  /*<property name="originType">*/
  //------------------------------------------------------------------

  /**
   * The type of the bean that has thrown this exception.
   */
  /*
  @Basic(invars = @Expression("originType != null"))
  */
  public final Class<?> getOriginType() {
    return $originType;
  }

  private Class<?> $originType;

  /*</property>*/



  /*<property name="origin">*/
  //------------------------------------------------------------------

  /**
   * The bean that has thrown this exception.
   */
  /*
  @Basic(
    invars = {
      @Expression("origin != null ? originType == origin.class")
    }
  )
  */
  public final Object getOrigin() {
    return $origin;
  }

  private Object $origin;

  /*</property>*/



  /*<property name="propertyName">*/
  //------------------------------------------------------------------

  /**
   * The name of the property for which this
   */
  /*
  @Basic(invars = @Expression("propertyName != null ? hasProperty(originType, propertyName)"))
  */
  public final String getPropertyName() {
    return $propertyName;
  }

  private String $propertyName;

  /*</property>*/



  /*<section name="comparison">*/
  //------------------------------------------------------------------

  @Override
  /*
  @MethodContract(
    post = @Expression("result ? (origin == _other.origin) && " +
                                "(originType == _other.originType) && (propertyName == _other.propertyName)")
  )
  */
  public boolean like(ApplicationException other) {
    return super.like(other) &&
           (((PropertyException)other).getOrigin() == getOrigin()) &&
           (((PropertyException)other).getOriginType() == getOriginType()) &&
           eqn(((PropertyException)other).getPropertyName(), getPropertyName());
  }

  /*</section>*/

}
