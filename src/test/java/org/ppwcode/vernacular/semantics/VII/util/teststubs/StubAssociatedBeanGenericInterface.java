/*<license>
Copyright 2004 - $Date: 2008-10-14 16:33:42 +0200 (Tue, 14 Oct 2008) $ by PeopleWare n.v..

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


import java.util.Set;


public interface StubAssociatedBeanGenericInterface<_I_ extends Number> {

  String getProperty1();

  void setProperty1(String property1);

  StubAssociatedBeanGenericInterface<_I_> getProperty2();

  void setProperty2(StubAssociatedBeanGenericInterface<_I_> property2);

  _I_ getProperty3();

  void setProperty3(_I_ property3);

  StubAssociatedBeanGenericA getProperty4();

  void setProperty4(StubAssociatedBeanGenericA property4);

  StubAssociatedBeanB getProperty5();

  void setProperty5(StubAssociatedBeanB property5);

  Set<StubAssociatedBeanGenericA> getPropertyA();

  void addPropertyA(StubAssociatedBeanGenericA srb);

}

