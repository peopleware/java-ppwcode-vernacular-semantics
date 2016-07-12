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

package org.ppwcode.vernacular.semantics.VII.exception;


import org.apache.commons.beanutils.PropertyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.ppwcode.vernacular.exception.IV.ApplicationException.DEFAULT_MESSAGE_KEY;
import static org.ppwcode.vernacular.semantics.VII.util.PropertyHelpers.hasProperty;


@SuppressWarnings({"WeakerAccess", "Duplicates", "FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"})
public class ValuePropertyExceptionTest {

  public final static String EMPTY = "";

  private Set<PropertyExceptionTest.OriginStub> origins;
  private Set<String> propertyNames; // not empty
  private Set<Object> propertyValues;
  private Set<String> messages; // not empty
  private Set<Throwable> throwables;
  public Set<ValuePropertyException> subjects;

  private Set<Object> origins2;
  private Set<Class<?>> originTypes2;
  private Set<String> propertyNames2; // not empty
  private Set<String> messages2; // not empty
  private Set<Throwable> throwables2;

  @Before
  public void setUp() throws Exception {
//    originMock = new OriginStub();
    messages = new HashSet<>();
    messages.add(null);
    messages.add("stub message");
    propertyNames = new HashSet<>();
    // null not allowed as property name
    propertyNames.add("stubProperty");
    propertyValues = new HashSet<>();
    propertyValues.add(null);
    propertyValues.add(new Object());
    propertyValues.add("");
    propertyValues.add(new Date());
    throwables = new HashSet<>();
    throwables.add(null);
    throwables.add(new Throwable());
    origins = new HashSet<>();
    for (Object propertyValue : propertyValues) {
      PropertyExceptionTest.OriginStub origin = new PropertyExceptionTest.OriginStub();
      origin.setStubProperty(propertyValue);
      origins.add(origin);
    }
    subjects = createSubjects();
    origins2 = new HashSet<>(origins);
    origins2.add(null);
    origins2.add(new Object());
    originTypes2 = new HashSet<>();
    originTypes2.add(PropertyExceptionTest.OriginStub.class);
    originTypes2.add(null);
    originTypes2.add(Object.class);
    originTypes2.add(PropertyException.class);
    propertyNames2 = new HashSet<>(propertyNames);
    propertyNames2.add(EMPTY);
    propertyNames2.add("not a property");
    messages2 = new HashSet<>(messages);
    messages2.add(EMPTY);
    messages2.add("another message");
    throwables2 = new HashSet<>(throwables);
    throwables2.add(new Exception());
  }

  private Set<ValuePropertyException> createSubjects() {
    Set<ValuePropertyException> result = new HashSet<>();
    for (String message : messages) {
      for (String propertyName : propertyNames) {
        for (Object origin : origins) {
          result.addAll(throwables.stream().map(t
                  -> new ValuePropertyException(origin, propertyName, message, t)).collect(Collectors.toList()));
        }
      }
    }
    return result;
  }

  @After
  public void tearDown() throws Exception {
    origins = null;
    messages = null;
    propertyNames = null;
    propertyValues = null;
    throwables = null;
    subjects = null;
    origins2 = null;
    propertyNames2 = null;
    messages2 = null;
  }

  public static void assertTypeInvariants(ValuePropertyException subject) {
    PropertyExceptionTest.assertTypeInvariants(subject);
    assertNotNull(subject.getPropertyName());
    assertTrue(subject.getOrigin() != null || subject.getPropertyValue() == null);
  }

  private void testValuePropertyExceptionObjectStringObjectStringThrowable(final Object origin,
                                                                      final String propertyName,
                                                                      final String message,
                                                                      final Throwable cause)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    // execute
    ValuePropertyException subject = new ValuePropertyException(origin, propertyName, message, cause);
    // validate
    assertEquals(origin, subject.getOrigin());
    assertEquals(origin.getClass(), subject.getOriginType());
    assertEquals(propertyName, subject.getPropertyName());
    assertEquals(PropertyUtils.getProperty(origin, propertyName), subject.getPropertyValue());
    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
    assertEquals(cause, subject.getCause());
    PropertyExceptionTest.assertTypeInvariants(subject);
    assertTypeInvariants(subject);
  }

  @Test
  public void testValuePropertyExceptionObjectStringObjectStringThrowable()
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    for (Object origin : origins) {
      for (String message : messages) {
        for (String propertyName : propertyNames) {
          for (Throwable t : throwables) {
            testValuePropertyExceptionObjectStringObjectStringThrowable(origin, propertyName, message, t);
          }
        }
      }
    }
  }

//  private void testValuePropertyExceptionObjectBooleanStringObjectStringThrowable(final Object origin,
//                                                                                   final boolean inOriginInitialization,
//                                                                                   final String propertyName,
//                                                                                   final String message,
//                                                                                   final Throwable cause)
//      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//    // execute
//    ValuePropertyException subject = new ValuePropertyException(origin, inOriginInitialization, propertyName, message, cause);
//    // validate
//    assertTrue(inOriginInitialization ? subject.getOrigin() == null : subject.getOrigin() == origin);
//    assertEquals(origin.getClass(), subject.getOriginType());
//    assertEquals(propertyName, subject.getPropertyName());
//    assertEquals(inOriginInitialization ? null : PropertyUtils.getProperty(origin, propertyName),
//                 subject.getPropertyValue());
//    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
//    assertEquals(cause, subject.getCause());
//    PropertyExceptionTest.assertTypeInvariants(subject);
//    assertTypeInvariants(subject);
//  }
//
//  @Test
//  public void testValuePropertyExceptionObjectBooleanStringObjectStringThrowable()
//      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//    for (boolean inOriginInitialization : booleans) {
//      for (Object origin : origins) {
//        for (String message : messages) {
//          for (String propertyName : propertyNames) {
//            for (Throwable t : throwables) {
//              testValuePropertyExceptionObjectBooleanStringObjectStringThrowable(origin, inOriginInitialization, propertyName, message, t);
//            }
//          }
//        }
//      }
//    }
//  }

//  private void testValuePropertyExceptionClassOfQStringObjectStringThrowable(final Class<?> originType,
//                                                                              final String propertyName,
//                                                                              final String message,
//                                                                              final Throwable cause) {
//    // execute
//    ValuePropertyException subject = new ValuePropertyException(originType, propertyName, message, cause);
//    // validate
//    assertNull(subject.getOrigin());
//    assertEquals(originType, subject.getOriginType());
//    assertEquals(propertyName, subject.getPropertyName());
//    assertNull(subject.getPropertyValue());
//    assertEquals(message == null ? DEFAULT_MESSAGE_KEY : message, subject.getMessage());
//    assertEquals(cause, subject.getCause());
//    PropertyExceptionTest.assertTypeInvariants(subject);
//    assertTypeInvariants(subject);
//  }

//  Method no longer exists
//  @Test
//  public void testValuePropertyExceptionClassOfQStringObjectStringThrowable() {
//    for (String message : messages) {
//      for (String propertyName : propertyNames) {
//        for (Throwable t : throwables) {
//          testValuePropertyExceptionClassOfQStringObjectStringThrowable(OriginStub.class, propertyName, message, t);
//        }
//      }
//    }
//  }

  public static void testLike(ValuePropertyException subject, PropertyException other) {
    // execute
    boolean result = subject.like(other);
    // validate
    PropertyExceptionTest.testLike(subject, other);
    assertTrue(!result || eqn(subject.getPropertyValue(), ((ValuePropertyException) other).getPropertyValue()));
    assertTypeInvariants(subject);
  }

  private static boolean eqn(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }

  @Test
  public void testLike() {
    for (ValuePropertyException subject : subjects) {
      for (Object origin : origins2) {
        if (origin != null) {
          propertyNames2.stream().filter(propertyName
                  -> (propertyName == null) || hasProperty(origin.getClass(), propertyName)).forEach(propertyName
                  -> messages2.stream().filter(message -> (message == null) || (!message.equals(""))).forEach(message
                  -> {
                        for (Throwable cause : throwables2) {
                          testLike(subject, new ValuePropertyException(origin, propertyName, message, cause));
                          testLike(subject, new PropertyException(origin, propertyName, message, cause));
                        }
                  }));
        }
        testLike(subject, null);
      }
    }
  }

}

