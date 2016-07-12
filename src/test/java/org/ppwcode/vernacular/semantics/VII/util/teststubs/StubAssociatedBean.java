/*<license>
Copyright 2004 - $Date: 2008-10-05 20:33:16 +0200 (Sun, 05 Oct 2008) $ by PeopleWare n.v..

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

package org.ppwcode.vernacular.semantics.VII.util.teststubs;


import java.util.HashSet;
import java.util.Set;


public class StubAssociatedBean {

  private String $property1;

  private StubAssociatedBean $property2;

  private int $property3;

  private StubAssociatedBeanA $property4;

  private StubAssociatedBeanB $property5;

  private Set<StubAssociatedBeanA> $propertyA = new HashSet<StubAssociatedBeanA>();


  public final String getProperty1() {
    return $property1;
  }


  public final void setProperty1(String property1) {
    $property1 = property1;
  }


  public final StubAssociatedBean getProperty2() {
    return $property2;
  }


  public final void setProperty2(StubAssociatedBean property2) {
    $property2 = property2;
  }


  public final int getProperty3() {
    return $property3;
  }


  public final void setProperty3(int property3) {
    $property3 = property3;
  }


  public final StubAssociatedBeanA getProperty4() {
    return $property4;
  }


  public final void setProperty4(StubAssociatedBeanA property4) {
    $property4 = property4;
  }


  public final StubAssociatedBeanB getProperty5() {
    return $property5;
  }


  public final void setProperty5(StubAssociatedBeanB property5) {
    $property5 = property5;
  }

  public final Set<StubAssociatedBeanA> getPropertyA() {
    return $propertyA;
  }

  public final void addPropertyA(StubAssociatedBeanA srb) {
    $propertyA.add(srb);
  }

}

