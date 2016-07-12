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


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.ppwcode.vernacular.exception.IV.util.ProgrammingErrorHelpers.*;


/**
 * <p>Utility methods for constant reflection. Use these methods if you are interested in the result of reflection,
 *   and not in a particular reason why some reflective inspection might have failed. The ppwcode exception vernacular
 *   is applied.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class ConstantHelpers {

  private ConstantHelpers() {
    // NOP
  }

  /**
   * Returns the value of the constant (public final static) {@code constantName} in class {@code type}.
   * If that constant doesn't exist, or something else goes wrong, this is considered a programming error.
   * We can work only with public class variables, and we look for these in type {@code type} and super types.
   *
   * @param     type
   *            The type to look in for the constant.
   * @param     constantName
   *            The name of the constant whose value to return.
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("isConstant(_type, _constantName)")
    },
    post = {
      @Expression("_type.getField(_constantName).get(null)")
    }
  )
  */
  public static <_ConstantValue_> _ConstantValue_ constant(final Class<?> type, final String constantName) {
    preArgumentNotNull(type);
    pre(isConstant(type, constantName));
    try {
      Field field = type.getField(constantName); // NoSuchFieldException, NullPointerException, SecurityException
      @SuppressWarnings("unchecked") _ConstantValue_ result =
          (_ConstantValue_)field.get(null); // IllegalAccessException, IllegalArgumentException,
                                            // NullPointerException; cannot happen
                                            // ExceptionInInitializerError
      return result;
      // not checking for static; why should we?
    }
    catch (NullPointerException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException | ExceptionInInitializerError npExc) {
      unexpectedException(npExc);
    }
    return null; // make compiler happy
  }

  /**
   * Is {@code constantName} a constant in {@code type} or one of its super types?
   * This means a variable with that name must exist that is static, final and public.
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_type != null"),
      @Expression("_constantName != null"),
      @Expression("_constantName != EMPTY")
    },
    post = {
      @Expression("exists (Field f : type.fields) {" +
                    "f.name == _constantName && Modifier.isFinal(f.modifiers) && Modifier.isStatic(f.modifiers)" +
                  "}")
    }
  )
  */
  public static boolean isConstant(Class<?> type, String constantName) {
    preArgumentNotNull(type);
    preArgumentNotEmpty(constantName);
    boolean result = false;
    try {
      Field field = type.getField(constantName); // result is public for sure
      int fMods = field.getModifiers();
      result = Modifier.isFinal(fMods) && Modifier.isStatic(fMods);
    }
    catch (NullPointerException npExc) {
      unexpectedException(npExc, "constantName nor type or null here");
    }
    catch (SecurityException exc) {
      unexpectedException(exc);
    }
    catch (NoSuchFieldException exc) {
      result = false;
    }
    return result;
  }

}
