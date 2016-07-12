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

package org.ppwcode.vernacular.semantics.VII.bean;


import java.beans.PropertyDescriptor;
import java.util.*;

import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.ppwcode.vernacular.exception.IV.util.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.semantics.VII.util.PropertyHelpers.propertyValue;


/**
 * An implementation enforcing the rules defined in {@link SemanticBean}.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractSemanticBean implements SemanticBean {

  @Override
  public final boolean equals(final Object other) {
    return super.equals(other);
  }

  @Override
  public final int hashCode() {
    return super.hashCode();
  }

  /**
   * Because this method is final, it is impossible to make a subtype
   * Cloneable successfully.
   */
  /*
  @MethodContract(
    post = @Expression("false"),
    exc  = @Throw(type = CloneNotSupportedException.class, cond = @Expression("true"))
  )
  */
  @Override
  protected final Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException("semantic objects may never be cloned");
  }

  @Override
  public final String toString() {
    StringBuilder result = new StringBuilder();
    HashSet<AbstractSemanticBean> cycleDetector = new HashSet<>();
    appendToString(result, cycleDetector);
    return result.toString();
  }

  private void appendToString(StringBuilder sb, HashSet<AbstractSemanticBean> cycleDetector) {
    preArgumentNotNull(sb);
    preArgumentNotNull(cycleDetector);
    if (cycleDetector.contains(this)) {
      sb.append("see --> ");
      appendSimpleToString(this, sb);
    }
    else {
      cycleDetector.add(this);
      appendSimpleToString(this, sb);
      propertiesString(sb, cycleDetector);
    }
  }

  private static void appendSimpleToString(AbstractSemanticBean asb, StringBuilder sb) {
    sb.append(asb.getClass().getCanonicalName());
    sb.append("@");
    sb.append(Integer.toHexString(asb.hashCode()));
  }

  private void propertiesString(StringBuilder sb, HashSet<AbstractSemanticBean> cycleDetector) {
    preArgumentNotNull(sb);
    preArgumentNotNull(cycleDetector);
    sb.append("[");
    Iterator<String> iterator = propertyNamesForToString().iterator();
    while (iterator.hasNext()) {
      String propertyName = iterator.next();
      sb.append(propertyName);
      sb.append(" = ");
      Object pValue = propertyValue(this, propertyName);
      if (pValue == null) {
        sb.append("null");
      }
      else if (! (pValue instanceof AbstractSemanticBean)) {
        sb.append(pValue.toString());
      }
      else {
        AbstractSemanticBean asb = (AbstractSemanticBean)pValue;
        asb.appendToString(sb, cycleDetector);
      }
      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");
  }

  /**
   * A list of property names, used to list the
   * <code><var>propertyName</var> = <var>propertyValue</var></code> pairs
   * in {@link #toString()}, in the order of the returned array.
   *
   * The default implementation returns the names of all properties
   * that are not of a type that is a subtype of {@link Collection}
   * or {@link Map}, or an array, and that is not the {@link #getClass()} method.
   * The order is indeterminate.
   */
  protected Set<String> propertyNamesForToString() {
    PropertyDescriptor[] pds = getPropertyDescriptors(this);
    Set<String> result = new HashSet<>(pds.length);
    for (int i = 0; i < pds.length; i++) {
      Class<?> propertyType = pds[i].getPropertyType();
      if (! Collection.class.isAssignableFrom(propertyType) &&
          ! Map.class.isAssignableFrom(propertyType) &&
          ! propertyType.isArray() &&
          ! pds[i].getName().equals("class")) {
        result.add(pds[i].getName());
      }
    }
    return result;
  }

  /**
   * Convenience method for generating the toString of to-many associations.
   */
  /*
  @MethodContract(pre = @Expression("c != null"), post = @Expression("true"))
  */
  protected static String collectionString(final Collection<?> c) {
    assert c != null;
    StringBuilder result = new StringBuilder("{");
    Iterator<?> iter = c.iterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      result.append(item.getClass().getName());
      result.append("@");
      result.append(Integer.toHexString(item.hashCode()));
      if (iter.hasNext()) {
        result.append(", ");
      }
    }
    result.append("}");
    return result.toString();
  }

}
