/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.vernacular.semantics_VI.bean;


import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppwcode.util.reflect_I.AssociationHelpers.associatedBeans;
import static org.ppwcode.util.reflect_I.PropertyHelpers.propertyValue;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.directUpstreamRousseauBeans;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.normalize;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.normalizeAndCheckCivilityOnUpstreamRousseauBeans;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.upstreamRousseauBeans;
import static org.ppwcode.vernacular.semantics_VI.bean.RousseauBeanHelpers.wildExceptions;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBean;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBeanA;
import org.ppwcode.vernacular.semantics_VI.bean.stubs.StubRousseauBeanB;
import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;
import org.ppwcode.vernacular.semantics_VI.exception.PropertyException;


public class RousseauBeanHelpersTest {

  private Set<? extends RousseauBean> $rousseauBeans;

  @Before
  public void setUp() throws Exception {
    Set<? extends RousseauBean> rousseauBeans = someRousseauBeans();
    $rousseauBeans = rousseauBeans;
  }

  public static Set<StubRousseauBean> someRousseauBeans() {
    Set<StubRousseauBean> rousseauBeans = new HashSet<StubRousseauBean>();

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
    for (RousseauBean rb : $rousseauBeans) {
      testDirectUpstreamRousseauBeans(rb);
    }
  }

  public void testUpstreamRousseauBeans(RousseauBean rb) {
    Set<RousseauBean> result = upstreamRousseauBeans(rb);
    assertNotNull(result);
    Set<RousseauBean> expected = new HashSet<RousseauBean>();
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
    for (RousseauBean rb : $rousseauBeans) {
      testUpstreamRousseauBeans(rb);
    }
  }

  public void testNormalizeAndCheckCivilityOnUpstreamRousseauBeans(RousseauBean rb) {
    CompoundPropertyException result = normalizeAndCheckCivilityOnUpstreamRousseauBeans(rb);
    assertNotNull(result);
    Set<PropertyException> expected = new HashSet<PropertyException>();
    for (RousseauBean rbr : upstreamRousseauBeans(rb)) {
      assertTrue(((StubRousseauBean)rbr).normalized);
      expected.addAll(((StubRousseauBean)rbr).wildExceptions.getAllElementExceptions());
    }
    assertEquals(expected, result.getAllElementExceptions());
  }

  @Test
  public void testNormalizeAndCheckCivilityOnUpstreamRousseauBeans() {
    for (RousseauBean rb : $rousseauBeans) {
      testNormalizeAndCheckCivilityOnUpstreamRousseauBeans(rb);
    }
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
      testNormalize(associatedBeans(rb, RousseauBean.class));
    }
  }

  public void testWildExceptions(Set<? extends RousseauBean> rbs) {
    CompoundPropertyException result = wildExceptions(rbs);
    assertNotNull(result);
    Set<PropertyException> expected = new HashSet<PropertyException>();
    for (RousseauBean rb : rbs) {
      expected.addAll(((StubRousseauBean)rb).wildExceptions.getAllElementExceptions());
    }
    assertEquals(expected, result.getAllElementExceptions());
  }

  @Test
  public void testWildExceptions() {
    for (RousseauBean rb : $rousseauBeans) {
      testWildExceptions(associatedBeans(rb, RousseauBean.class));
    }
  }


//  @Test
//  public void tt() {
//    PropertyDescriptor pd = PropertyHelpers.propertyDescriptor(StubRousseauBeanA.class, "propertyA");
//    System.out.println(pd);
//    System.out.println(pd.getPropertyType());
//    System.out.println(pd.getPropertyType().getTypeParameters());
//    System.out.println(pd.getPropertyType().getTypeParameters().length);
//    System.out.println(pd.getPropertyType().getTypeParameters()[0]);
//    System.out.println(pd.getPropertyType().getTypeParameters()[0].getName());
//    System.out.println(pd.getPropertyType().getTypeParameters()[0].getGenericDeclaration());
//    System.out.println(pd.getPropertyType().getTypeParameters()[0].getBounds());
//    System.out.println(pd.getPropertyType().getTypeParameters()[0].getBounds().length);
//    System.out.println(pd.getPropertyType().getTypeParameters()[0].getBounds()[0]);
//
//    System.out.println();
//
//    StubRousseauBean srb = new StubRousseauBean();
//    for (int i = 0; i < 5; i++) {
//      StubRousseauBeanA srbAQ = new StubRousseauBeanA();
//      srbAQ.setProperty6(srb);
//      StubRousseauBeanB srbBQ = new StubRousseauBeanB();
//      srbBQ.setProperty2(srb);
//      srbBQ.setProperty8(srbAQ);
//      srbAQ.setProperty5(srbBQ);
//      srb.addPropertyA(srbAQ);
//    }
//    System.out.println(srb.getPropertyA());
//    System.out.println(srb.getPropertyA().getClass());
//    System.out.println(srb.getPropertyA().getClass().getGenericInterfaces());
//    System.out.println(srb.getPropertyA().getClass().getGenericInterfaces().length);
//    for (int i = 0; i < srb.getPropertyA().getClass().getGenericInterfaces().length; i++) {
//      System.out.println(srb.getPropertyA().getClass().getGenericInterfaces()[i]);
//    }
//    System.out.println(srb.getPropertyA().getClass().getGenericSuperclass());
//    System.out.println(srb.getPropertyA().getClass().getTypeParameters());
//    System.out.println(srb.getPropertyA().getClass().getTypeParameters().length);
//    System.out.println(srb.getPropertyA().getClass().getTypeParameters()[0]);
//  }

}

