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

}
