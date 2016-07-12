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
import org.ppwcode.vernacular.semantics.VII.exception.CompoundPropertyException;
import org.ppwcode.vernacular.semantics.VII.exception.PropertyException;

import java.util.*;

import static org.junit.Assert.*;


@SuppressWarnings({"UnusedParameters", "WeakerAccess"})
public class AbstractRousseauBeanTest {

  @SuppressWarnings({"WeakerAccess", "unused"})
  public static class AbstractRousseauBeanSTUB extends AbstractRousseauBean implements NumberOfProperties {

    public AbstractRousseauBeanSTUB(String property1, Date property2, Set<String> property3, int[] property4) {
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

  public static class AbstractRousseauBeanWILD extends AbstractRousseauBeanSTUB implements NumberOfProperties {

    public AbstractRousseauBeanWILD(String property1, Date property2, Set<String> property3, int[] property4) {
      super(property1, property2, property3, property4);
    }

    @Override
    public CompoundPropertyException wildExceptions() {
      CompoundPropertyException cpe = super.wildExceptions();
      cpe.addElementException(new PropertyException(this, "property1", null, null));
      cpe.addElementException(new PropertyException(this, "property1", null, null));
      cpe.addElementException(new PropertyException(this, "property2", null, null));
      cpe.addElementException(new PropertyException(this, "property2", null, null));
      cpe.addElementException(new PropertyException(this, "property3", null, null));
      cpe.addElementException(new PropertyException(this, "property3", null, null));
      return cpe;
    }

  }

  @SuppressWarnings("unused")
  public static class AbstractRousseauBeanNOPROPERTIES extends AbstractRousseauBean implements NumberOfProperties {

    public int nrOfProperties() {
      return 0;
    }

    public int nrOfSimpleProperties() {
      return nrOfProperties(); // only simple properties in this class
    }

  }


  private List<AbstractRousseauBean> subjects;

  @Before
  public void setUp() throws Exception {
    subjects = new ArrayList<>();
    AbstractRousseauBeanSTUB subject = new AbstractRousseauBeanSTUB(null, null, null, null);
    subjects.add(subject);
    Set<String> stringSet = new HashSet<>();
    stringSet.add("string 1");
    stringSet.add("string 2");
    stringSet.add(null);
    int[] intArray = {5, 6, 4, 8};
    subject = new AbstractRousseauBeanSTUB("PROPERTY 1", null, null, null);
    subjects.add(subject);
    subject = new AbstractRousseauBeanSTUB(null, new Date(), null, null);
    subjects.add(subject);
    subject = new AbstractRousseauBeanSTUB(null, null, stringSet, null);
    subjects.add(subject);
    subject = new AbstractRousseauBeanSTUB(null, null, null, intArray);
    subjects.add(subject);
    subject = new AbstractRousseauBeanSTUB("PROPERTY 1", new Date(), stringSet, intArray);
    subjects.add(subject);
    subject = new AbstractRousseauBeanWILD("PROPERTY 1", new Date(), stringSet, intArray);
    subjects.add(subject);
    subjects.addAll(RousseauBeanHelpersTest.someRousseauBeans());
  }

  @After
  public void tearDown() throws Exception {
    subjects = null;
  }

  public static void assertInvariants(RousseauBean subject) {
    // no own invariants
    RousseauBeanContract.assertInvariants(subject);
  }

  public static void testEquals(AbstractRousseauBean subject, Object other) {
    // execute
    boolean result = subject.equals(other);
    // validate
    RousseauBeanContract.contractEquals(subject, other, result);
    assertInvariants(subject);
  }

  @Test
  public void testEqualsObject() {
    for (AbstractRousseauBean subject : subjects) {
      testEquals(subject, null);
      testEquals(subject, subject);
      testEquals(subject, new Object());
      testEquals(subject, new AbstractRousseauBeanSTUB("An ARB stub string", null, null, null));
    }
  }

  public static void testHashCode(AbstractRousseauBean subject) {
    // execute
    int result = subject.hashCode();
    // validate
    RousseauBeanContract.contractHashCode(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testHashCode() {
    subjects.forEach(AbstractRousseauBeanTest::testHashCode);
  }

  public static void testToString(AbstractRousseauBean subject) {
    // execute
    String result = subject.toString();
    // validate
    RousseauBeanContract.contractToString(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testToString() {
    subjects.forEach(AbstractRousseauBeanTest::testToString);
  }

  @Test
  public void testClone() {
    for (AbstractRousseauBean subject : subjects) {
      try {
        subject.clone();
        fail();
      }
      catch (CloneNotSupportedException cnsExc) {
        assertInvariants(subject);
      }
    }
  }

  public static CompoundPropertyException testGetWildExceptions(AbstractRousseauBean subject) {
    // execute
    CompoundPropertyException result = subject.wildExceptions();
    // validate
    RousseauBeanContract.contractGetWildExceptions(subject, result);
    assertInvariants(subject);
    return result;
  }

  private static void contractPROTECTEDGetWildExceptions(AbstractRousseauBean subject, CompoundPropertyException result) {
    assertNull(result.getPropertyName());
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetWildExceptions() {
    for (AbstractRousseauBean subject : subjects) {
      CompoundPropertyException result = testGetWildExceptions(subject);
      if (subject.getClass() == AbstractRousseauBeanSTUB.class) {
        contractPROTECTEDGetWildExceptions(subject, result);
      }
    }
  }

  public static void testIsCivilized(AbstractRousseauBean subject) {
    // execute
    boolean result = subject.civilized();
    // validate
    RousseauBeanContract.contractIsCivilized(subject, result);
    assertInvariants(subject);
  }

  @Test
  public void testIsCivilized() {
    subjects.forEach(AbstractRousseauBeanTest::testIsCivilized);
  }

  public static void testCheckCivility(AbstractRousseauBean subject) {
    boolean OLDCivilized = subject.civilized();
    try {
      subject.checkCivility();
      RousseauBeanContract.contractPostCheckCivility(OLDCivilized, subject);
    }
    catch (CompoundPropertyException thrown) {
      RousseauBeanContract.contractExcCheckCivility(OLDCivilized, subject, thrown);
    }
    assertInvariants(subject);
  }

  @Test
  public void testCheckCivility() {
    subjects.forEach(AbstractRousseauBeanTest::testCheckCivility);
  }

  public static void testNormalize(AbstractRousseauBean subject) {
    // execute
    subject.normalize();
    // validate
    assertInvariants(subject);
  }

  @Test
  public void testNormalize() {
    subjects.forEach(AbstractRousseauBeanTest::testNormalize);
  }

}

