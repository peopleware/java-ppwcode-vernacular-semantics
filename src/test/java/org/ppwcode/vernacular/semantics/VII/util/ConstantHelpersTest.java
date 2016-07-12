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


import org.junit.Test;
import org.ppwcode.vernacular.semantics.VII.util.teststubs.AbstractSubSubStubClass;
import org.ppwcode.vernacular.semantics.VII.util.teststubs.CloneableStubClassA;
import org.ppwcode.vernacular.semantics.VII.util.teststubs.CloneableStubClassB;
import org.ppwcode.vernacular.semantics.VII.util.teststubs.StubInterfaceGamma;

import static org.junit.Assert.*;
import static org.ppwcode.vernacular.semantics.VII.util.ConstantHelpers.constant;
import static org.ppwcode.vernacular.semantics.VII.util.ConstantHelpers.isConstant;
import static org.ppwcode.vernacular.semantics.VII.util.teststubs.CloneableStubClassA.STUB_PUBLIC_CONSTANT;


public class ConstantHelpersTest {

  @Test
  public void testConstant_Class_StringA1() {
    String result = constant(CloneableStubClassA.class, "STUB_PUBLIC_CONSTANT");
    assertEquals(STUB_PUBLIC_CONSTANT, result);
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringA2() {
    constant(CloneableStubClassA.class, "STUB_PRIVATE_CONSTANT");
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringA3() {
    constant(CloneableStubClassA.class, "STUB_PACKAGE_CONSTANT");
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringA4() {
    constant(CloneableStubClassA.class, "STUB_PROTECTED_CONSTANT");
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringA5() {
    constant(CloneableStubClassA.class, "STUB_NON_FINAL_CONSTANT");
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringA8() {
    constant(CloneableStubClassA.class, "A DUMMY STRING");
  }

  @Test
  public void testConstant_Class_StringB1() {
    String result = constant(CloneableStubClassB.class, "STUB_PUBLIC_CONSTANT");
    assertEquals(STUB_PUBLIC_CONSTANT, result);
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringB2() {
    constant(CloneableStubClassB.class, "STUB_PRIVATE_CONSTANT");
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringB3() {
    constant(CloneableStubClassB.class, "STUB_PACKAGE_CONSTANT");
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringB4() {
    constant(CloneableStubClassB.class, "STUB_PROTECTED_CONSTANT");
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringB5() {
    constant(CloneableStubClassB.class, "STUB_NON_FINAL_CONSTANT");
  }

  @Test(expected = AssertionError.class)
  public void testConstant_Class_StringB8() {
    constant(CloneableStubClassA.class, "A DUMMY STRING");
  }

  @Test
  public void testConstant_Class_String1() {
    String result = constant(AbstractSubSubStubClass.class, "STUB_CONSTANT");
    assertEquals(AbstractSubSubStubClass.STUB_CONSTANT, result);
  }

  @Test
  public void testConstant_Class_String2() {
    String result = constant(AbstractSubSubStubClass.class, "STUB_CONSTANT_GAMMA");
    assertEquals(StubInterfaceGamma.STUB_CONSTANT_GAMMA, result);
  }

  @Test
  public void testConstant_Class_String3() {
    String result = constant(StubInterfaceGamma.class, "STUB_CONSTANT_GAMMA");
    assertEquals(StubInterfaceGamma.STUB_CONSTANT_GAMMA, result);
  }

  @Test
  public void testIsConstant_Class_StringA1() {
    boolean result = isConstant(CloneableStubClassA.class, "STUB_PUBLIC_CONSTANT");
    assertTrue(result);
  }

  @Test
  public void testIsConstant_Class_StringA2() {
    boolean result = isConstant(CloneableStubClassA.class, "STUB_PRIVATE_CONSTANT");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringA3() {
    boolean result = isConstant(CloneableStubClassA.class, "STUB_PACKAGE_CONSTANT");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringA4() {
    boolean result = isConstant(CloneableStubClassA.class, "STUB_PROTECTED_CONSTANT");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringA5() {
    boolean result = isConstant(CloneableStubClassA.class, "STUB_NON_FINAL_CONSTANT");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringA8() {
    boolean result = isConstant(CloneableStubClassA.class, "A DUMMY STRING");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringB1() {
    boolean result = isConstant(CloneableStubClassB.class, "STUB_PUBLIC_CONSTANT");
    assertTrue(result);
  }

  @Test
  public void testIsConstant_Class_StringB2() {
    boolean result = isConstant(CloneableStubClassB.class, "STUB_PRIVATE_CONSTANT");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringB3() {
    boolean result = isConstant(CloneableStubClassB.class, "STUB_PACKAGE_CONSTANT");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringB4() {
    boolean result = isConstant(CloneableStubClassB.class, "STUB_PROTECTED_CONSTANT");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringB5() {
    boolean result = isConstant(CloneableStubClassB.class, "STUB_NON_FINAL_CONSTANT");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_StringB8() {
    boolean result = isConstant(CloneableStubClassA.class, "A DUMMY STRING");
    assertFalse(result);
  }

  @Test
  public void testIsConstant_Class_String1() {
    boolean result = isConstant(AbstractSubSubStubClass.class, "STUB_CONSTANT");
    assertTrue(result);
  }

  @Test
  public void testIsConstant_Class_String2() {
    boolean result = isConstant(AbstractSubSubStubClass.class, "STUB_CONSTANT_GAMMA");
    assertTrue(result);
  }

  @Test
  public void testIsConstant_Class_String3() {
    boolean result = isConstant(StubInterfaceGamma.class, "STUB_CONSTANT_GAMMA");
    assertTrue(result);
  }

}

