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

package org.ppwcode.vernacular.semantics.VII.bean;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.exception.IV.CompoundSemanticException;
import org.ppwcode.vernacular.semantics.VII.bean.stubs.StubRousseauBean;
import org.ppwcode.vernacular.semantics.VII.bean.stubs.StubRousseauBeanA;
import org.ppwcode.vernacular.semantics.VII.bean.stubs.StubRousseauBeanB;
import org.ppwcode.vernacular.semantics.VII.exception.PropertyException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.beanutils.PropertyUtils.getProperty;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.junit.Assert.*;
import static org.ppwcode.vernacular.exception.IV.util.ProgrammingErrorHelpers.preArgumentNotNull;
import static org.ppwcode.vernacular.exception.IV.util.ProgrammingErrorHelpers.unexpectedException;
import static org.ppwcode.vernacular.semantics.VII.bean.RousseauBeanHelpers.*;
import static org.ppwcode.vernacular.semantics.VII.util.PropertyHelpers.propertyValue;


@SuppressWarnings({"UnnecessaryLocalVariable", "ConstantConditions", "WeakerAccess"})
public class RousseauBeanHelpersTest {

  private Set<? extends RousseauBean> $rousseauBeans;

  @Before
  public void setUp() throws Exception {
    Set<? extends RousseauBean> rousseauBeans = someRousseauBeans();
    $rousseauBeans = rousseauBeans;
  }

  public static Set<StubRousseauBean> someRousseauBeans() {
    Set<StubRousseauBean> rousseauBeans = new HashSet<>();

    StubRousseauBean srb = new StubRousseauBean();
    rousseauBeans.add(srb);

    StubRousseauBean srb1 = new StubRousseauBean();
    srb = new StubRousseauBean();
    srb.setProperty2(srb1);
    StubRousseauBeanA srbA = new StubRousseauBeanA();
    srb.setProperty4(srbA);
    StubRousseauBeanB srbB = new StubRousseauBeanB();
    srb.setProperty5(srbB);
    rousseauBeans.add(srb);

    srb1 = new StubRousseauBean();
    srb = new StubRousseauBean();
    srb.setProperty2(srb1);
    srbA = new StubRousseauBeanA();
    srbA.setProperty2(srb1);
    srbA.setProperty4(srbA);
    StubRousseauBeanB srbB5 = new StubRousseauBeanB();
    srbA.setProperty5(srbB5);
    srbA.setProperty6(new StubRousseauBean());
    srbA.setProperty7(new StubRousseauBeanA());
    srbA.setPropertyLoop(srb);
    srb.setProperty4(srbA);
    srbB = new StubRousseauBeanB();
    srb.setProperty5(srbB);
    rousseauBeans.add(srb);

    srb = new StubRousseauBean();
    for (int i = 0; i < 5; i++) {
      StubRousseauBeanA srbAQ = new StubRousseauBeanA();
      srbAQ.setProperty6(srb);
      StubRousseauBeanB srbBQ = new StubRousseauBeanB();
      srbBQ.setProperty2(srb);
      srbBQ.setProperty8(srbAQ);
      srbAQ.setProperty5(srbBQ);
      srb.addPropertyA(srbAQ);
    }
    rousseauBeans.add(srb);

    return rousseauBeans;
  }

  @After
  public void tearDown() throws Exception {
    $rousseauBeans = null;
  }

  public void testDirectUpstreamRousseauBeans(RousseauBean rb) {
    Set<RousseauBean> result = directUpstreamRousseauBeans(rb);
    assertNotNull(result);
    for (PropertyDescriptor pd : getPropertyDescriptors(rb)) {
      if (RousseauBean.class.isAssignableFrom(pd.getPropertyType()) && propertyValue(rb, pd.getName()) != null) {
        //noinspection SuspiciousMethodCalls
        assertTrue(result.contains(propertyValue(rb, pd.getName())));
      }
    }
    for (RousseauBean rousseauBean : result) {
      boolean found = false;
      for (PropertyDescriptor pd : getPropertyDescriptors(rb)) {
        if (RousseauBean.class.isAssignableFrom(pd.getPropertyType()) && propertyValue(rb, pd.getName()) == rousseauBean) {
          found = true;
          break;
        }
      }
      if (! found) {
        fail();
      }
    }
  }

  @Test
  public void testDirectUpstreamRousseauBeans() {
    $rousseauBeans.forEach(this::testDirectUpstreamRousseauBeans);
  }

  public void testUpstreamRousseauBeans(RousseauBean rb) {
    Set<RousseauBean> result = upstreamRousseauBeans(rb);
    assertNotNull(result);
    Set<RousseauBean> expected = new HashSet<>();
    expected.add(rb);
    Set<RousseauBean> duRbs = directUpstreamRousseauBeans(rb);
    expected.addAll(duRbs);
    for (RousseauBean rbr : duRbs) {
      expected.addAll(upstreamRousseauBeans(rbr));
    }
    assertEquals(expected, result);
  }

  @Test
  public void testUpstreamRousseauBeans() {
    $rousseauBeans.forEach(this::testUpstreamRousseauBeans);
  }

  public void testNormalizeAndCheckCivilityOnUpstreamRousseauBeans(RousseauBean rb) {
    CompoundSemanticException result = normalizeAndCheckCivilityOnUpstreamRousseauBeans(rb);
    assertNotNull(result);
    Set<PropertyException> expected = new HashSet<>();
    for (RousseauBean rbr : upstreamRousseauBeans(rb)) {
      assertTrue(((StubRousseauBean)rbr).normalized);
      expected.addAll(((StubRousseauBean)rbr).wildExceptions.getElementExceptions());
    }
    assertEquals(expected, result.getElementExceptions());
  }

  @Test
  public void testNormalizeAndCheckCivilityOnUpstreamRousseauBeans() {
    $rousseauBeans.forEach(this::testNormalizeAndCheckCivilityOnUpstreamRousseauBeans);
  }

  public void testNormalize(Set<? extends RousseauBean> rbs) {
    normalize(rbs);
    for (RousseauBean rb : rbs) {
      assertTrue(((StubRousseauBean)rb).normalized);
    }
  }

  @Test
  public void testNormalize() {
    for (RousseauBean rb : $rousseauBeans) {
      testNormalize(associatedRousseauBeans(rb));
    }
  }

  public void testWildExceptions(Set<? extends RousseauBean> rbs) {
    CompoundSemanticException result = wildExceptions(rbs);
    assertNotNull(result);
    Set<PropertyException> expected = new HashSet<>();
    for (RousseauBean rb : rbs) {
      expected.addAll(((StubRousseauBean)rb).wildExceptions.getElementExceptions());
    }
    assertEquals(expected, result.getElementExceptions());
  }

  @Test
  public void testWildExceptions() {
    for (RousseauBean rb : $rousseauBeans) {
      testWildExceptions(associatedRousseauBeans(rb));
    }
  }

  /*
   * code below comes from ppwcode-util-reflection org.ppwcode.util.reflect_I.AssociationHelpers
   * r3064
   * There is also test code there for the methods below (although they are slightly change - made more easy)
   */

  /**
   * The direct associated objects of type {@code associatedType} starting from {@code bean}.
   * These are the beans that are properties of {@code bean}, directly (to-one) or via
   * a collection (to-many).
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_bean != null"),
      @Expression("_associatedType != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("for (PropertyDescriptor pd : getPropertyDescriptors(_bean)) {" +
                    "associatedType.isAssignableFrom(pd.propertyType) && propertyValue(_bean, pd.name) != null ? " +
                      "result.contains(propertyValue(_bean, pd.name))" +
                  "}"), // MUDO add toMany
      @Expression("for (_T_ t : result) {" +
                    "exists (PropertyDescriptor pd : getPropertyDescriptors(_bean)) {" +
                      "t == propertyValue(_bean, pd.name)" +
                    "}" +
                  "}") // MUDO add toMany
    }
  )
  */
  private static Set<RousseauBean> directAssociatedRousseauBeans(RousseauBean bean) {
    assert preArgumentNotNull(bean, "bean");
    Set<RousseauBean> result = new HashSet<>();
    PropertyDescriptor[] pds = getPropertyDescriptors(bean);
    for (PropertyDescriptor pd : pds) {
      if (RousseauBean.class.isAssignableFrom(pd.getPropertyType())) {
        RousseauBean candidate = propertyValue(bean, pd.getName(), RousseauBean.class);
        if (candidate != null) {
          result.add(candidate);
        }
      } else if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
        // get the elements, and if they are RousseauBeans, add them
        Set<?> many = null;
        try {
          @SuppressWarnings("unchecked")
          Set<?> anyMany = (Set<?>) getProperty(bean, pd.getName());
          many = anyMany;
        } catch (InvocationTargetException exc) {
          /* in a special case, to avoid problems with deserialized objects of a non-initialized
             JPA toMany, we deal with a NullPointerException as if it is a null property */
          if (exc.getCause() instanceof NullPointerException) {
            assert many == null;
            // NOP
          } else {
            unexpectedException(exc);
          }
        } catch (IllegalAccessException | NoSuchMethodException exc) {
          unexpectedException(exc);
        }
        if (many != null) {
          result.addAll(many.stream().filter(object
                  -> object != null && object instanceof RousseauBean).map(object
                  -> (RousseauBean) object).collect(Collectors.toList()));
        }
      }
    }
    return result;
  }

  /**
   * All objects of type {@code associatedType} that are reachable from {@code bean}.
   * These are the beans that are simple or multiple properties of {@code bean}. This is applied
   * recursively. {@code bean} itself is also part of the set.
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_bean != null"),
      @Expression("_associatedType != null")
    },
    post = {
      @Expression("result != null"),
      @Expression("{_bean} U directAssociatedRousseauBeans(_bean) U " +
                   "union (_T_ t : directAssociatedRousseauBeans(_bean)) {associatedRousseauBeans(t)}")
    }
  )
  */
  private static Set<RousseauBean> associatedRousseauBeans(RousseauBean bean) {
    assert preArgumentNotNull(bean, "bean");
    LinkedList<RousseauBean> agenda = new LinkedList<>();
    agenda.add(bean);
    int i = 0;
    while (i < agenda.size()) {
      RousseauBean current = agenda.get(i);
      directAssociatedRousseauBeans(current).stream().filter(t -> !agenda.contains(t)).forEach(agenda::add);
      i++;
    }
    return new HashSet<>(agenda);
  }


}

