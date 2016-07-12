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
import org.ppwcode.vernacular.semantics.VII.bean.stubs.NumberOfProperties;

import java.util.*;

import static org.junit.Assert.fail;


@SuppressWarnings("WeakerAccess")
public class AbstractSemanticBeanTest {

  @SuppressWarnings("unused")
  public static class AbstractSemanticBeanSTUB extends AbstractSemanticBean implements NumberOfProperties {

    public AbstractSemanticBeanSTUB(String property1, Date property2, Set<String> property3, int[] property4) {
      $property1 = property1;
      $property2 = property2;
      $property3 = property3;
      $property4 = property4;
    }

    public final String getProperty1() {
      return $property1;
    }

    public final void setProperty1(String property1) {
      $property1 = property1;
    }

    private String $property1;


    public final Date getProperty2() {
      return $property2;
    }

    public final void setProperty2(Date property2) {
      $property2 = property2;
    }

    private Date $property2;

    public final Set<String> getProperty3() {
      return $property3;
    }

    public final void setProperty3(Set<String> property3) {
      $property3 = property3;
    }

    private Set<String> $property3;

    public final int[] getProperty4() {
      return $property4;
    }

    public final void setProperty4(int[] property4) {
      $property4 = property4;
    }

    private int[] $property4;


    public int nrOfProperties() {
      return 4;
    }

    public int nrOfSimpleProperties() {
      return nrOfProperties() - 2; // property 3 is a set, property 4 is an array
    }

  }

  @SuppressWarnings("unused")
  public static class AbstractSemanticBeanNOPROPERTIES extends AbstractSemanticBean implements NumberOfProperties {

    public int nrOfProperties() {
      return 0;
    }

    public int nrOfSimpleProperties() {
      return nrOfProperties(); // only simple properties in this class
    }

  }


  private List<AbstractSemanticBean> subjects;

  @Before
  public void setUp() throws Exception {
    subjects = new ArrayList<>();
    AbstractSemanticBeanSTUB subject = new AbstractSemanticBeanSTUB(null, null, null, null);
    subjects.add(subject);
    Set<String> stringSet = new HashSet<>();
    stringSet.add("string 1");
    stringSet.add("string 2");
    stringSet.add(null);
    int[] intArray = {5, 6, 4, 8};
    subject = new AbstractSemanticBeanSTUB("PROPERTY 1", null, null, null);
    subjects.add(subject);
    subject = new AbstractSemanticBeanSTUB(null, new Date(), null, null);
    subjects.add(subject);
    subject = new AbstractSemanticBeanSTUB(null, null, stringSet, null);
    subjects.add(subject);
    subject = new AbstractSemanticBeanSTUB(null, null, null, intArray);
    subjects.add(subject);
    subject = new AbstractSemanticBeanSTUB("PROPERTY 1", new Date(), stringSet, intArray);
    subjects.add(subject);
    subjects.addAll(RousseauBeanHelpersTest.someRousseauBeans());
  }

  @After
  public void tearDown() throws Exception {
    subjects = null;
  }

  public static void assertInvariants(SemanticBean subject) {
    // no own invariants
    SemanticBeanContract.assertInvariants(subject);
  }

  public static void testEquals(AbstractSemanticBean subject, Object other) {
    // execute
    boolean result = subject.equals(other);
    // validate
    SemanticBeanContract.contractEquals(subject, other, result);
    assertInvariants(subject);
  }

  @Test
  public void testEqualsObject() {
    for (AbstractSemanticBean subject : subjects) {
      testEquals(subject, null);
      testEquals(subject, subject);
      testEquals(subject, new Object());
      testEquals(subject, new AbstractSemanticBeanSTUB("a asb stub string", null, null, null));
    }
  }

  public static void testHashCode(AbstractSemanticBean subject) {
    // execute
    int result = subject.hashCode();
    // validate
    SemanticBeanContract.contractHashCode(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testHashCode() {
    subjects.forEach(AbstractSemanticBeanTest::testHashCode);
  }

  public static void testToString(AbstractSemanticBean subject) {
    // execute
    String result = subject.toString();
    System.out.println(result);
    // validate
    SemanticBeanContract.contractToString(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testToString() {
    subjects.forEach(AbstractSemanticBeanTest::testToString);
  }

  @Test
  public void testClone() {
    for (AbstractSemanticBean subject : subjects) {
      try {
        subject.clone();
        fail();
      }
      catch (CloneNotSupportedException cnsExc) {
        // expected
        assertInvariants(subject);
      }
    }
  }

}

