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

package org.ppwcode.i18n_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.Properties.getPropertyType;
import static org.ppwcode.util.resourcebundle.ResourceBundles.findKeyInTypeProperties;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Support methods to work with properties files.
 *   There are general methods here, and some methods specific to a
 *   properties file strategy for i18n of bean type labels and bean property
 *   labels, and general bean related text.</p>
 * <p>In general, we look for the i18n strings in a properties file that has
 *   the same fully qualified name as the bean class whose labels we are
 *   interested in. If a match is not found, we search for the labels
 *   through the supertypes of the bean class.</p>
 * <p>{@link #i18nPropertyLabel(String, Class, boolean, ResourceBundleLoadStrategy)}
 *   returns the full or the short label of a JavaBean property, using a
 *   specific resource bundle load strategy, based on the <code>Class</code>
 *   of the JavaBean.
 *   {@link #i18nInstancePropertyLabel(String, Object, boolean, ResourceBundleLoadStrategy)}
 *   does the same, based on an instance of a JavaBean.<br/>
 *   {@link #i18nTypeLabel(Class, boolean, ResourceBundleLoadStrategy)} returns
 *   the full or short type label of a JavaBean, using a specific resource bundle
 *   load strategy, based on the <code>Class</code>
 *   of the JavaBean.</p>
 * <p>The other public methods are support methods to retrieve properties
 *   file entries.</p>
 *
 * @author Jan Dockx
 * @todo (jand): test code
 * @idea split the support and bean label properties in different classes;
 *       move the bean property label stuff to ppw-bean
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class I18nLabels {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Cannot instantiate this class. Only use static methods.
   */
  private I18nLabels() {
    // NOP
  }

  /*</construction>*/



  /**
   * <p>Token used in return values to signal properties that
   *  are not found.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String NOT_FOUND_TOKEN = "???"; //$NON-NLS-1$

  /**
   * <p>Token used in return values to separate type names
   *  from property names.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String PROPERTY_SEPARATOR_TOKEN = "#"; //$NON-NLS-1$

  private static final char DOT = '.';

  /**
   * <p>Return a label for a property with name <code>property</code>
   *   of a type <code>type</code>.</p>
   * <p>We look for a property with key
   *   <code>propertyName.<var>property</var></code> or
   *   <code>propertyName.short.<var>property</var></code>
   *   in a properties file with the name of <code>type</code>,
   *   and its supertypes. We search bottom up, and return the
   *   first match. If no match is found, we return the String
   *   <code>&quot;???&quot;<var>property</var>&quot;???&quot;</code>.
   *   If the short label does not exist in a given properties file,
   *   we look for the normal label, and vice versa, before
   *   we try the next type.</p>
   * <p>Property names can be nested, as described in
   *   <a href="http://jakarta.apache.org/commons/beanutils/commons-beanutils-1.7.0/docs/api/"><code>org.apache.commons.beanutils.PropertyUtilsBean</code></a>.
   *   This means that, if <code>property</code> contains '.' separators, we
   *   look up the type of the portion before the dot, and then call
   *   <code>findKeyInTypeProperties(<var>beforeDotType</var>, <var>propertyAfterDot</var>, shortLabel, strategy)</code>.
   *   If one of the properties in this path does not exist, we return <code>null</code>.</p>
   *
   * @param property
   *        The name of the property to return an i18n'ed label for.
   * @param type
   *        The type of bean that owns the property. This is the
   *        start of our lookup.
   * @param shortLabel
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *         (property == null)
   *          || (property.length() <= 0)
   *          || (type == null);
   *
   * @mudo better exception type
   */
  public static String i18nPropertyLabel(final String property, final Class<?> type, final boolean shortLabel, final ResourceBundleLoadStrategy strategy)
      throws IllegalArgumentException {
    if ((property == null) || (property.length() <= 0) || (type == null)) {
      throw new IllegalArgumentException("parameters must be effective");
    }
    int dotPosition = property.indexOf(DOT);
    if (dotPosition >= 0) {
      String preDot = property.substring(0, dotPosition);
      String postDot = property.substring(dotPosition + 1);
      try {
        Class<?> preType = getPropertyType(type, preDot);
        return i18nPropertyLabel(postDot, preType, shortLabel, strategy);
      }
      catch (NoSuchMethodException nsmExc) {
        // MUDO (jand) log warning
        return null;
      }
    }
    String result = findKeyInTypeProperties(type, i18nPropertyLabel_keys(property, shortLabel), strategy);
    return (result != null)
            ? result
            : keyNotFound(property + PROPERTY_SEPARATOR_TOKEN + type.getName());
  }

  /**
   * <p>Return a label for a property with name <code>property</code>
   *   of a type <code>instance.getClass()</code>.</p>
   *
   * @see #i18nPropertyLabel(String, Class, boolean, ResourceBundleLoadStrategy)
   *
   * @param property
   *        The name of the property to return an i18n'ed label for.
   * @param instance
   *        The object that owns the property. The class of this object
   *        is the start of our lookup.
   * @param shortLabel
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *         (property == null)
   *          || (property.length() <= 0)
   *          || (type == null);
   */
  public static String i18nInstancePropertyLabel(
      final String property,
      final Object instance,
      final boolean shortLabel,
      final ResourceBundleLoadStrategy strategy) {
    return i18nPropertyLabel(property,
                             instance.getClass(),
                             shortLabel,
                             strategy);
  }


  /**
   * <p>Prefix used in property files to discriminate property
   *  labels.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_PROPERTY_LABEL_KEY_PREFIX = "propertyName.";

  /**
   * <p>Prefix used in property files to discriminate short property
   *  labels.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_SHORT_PROPERTY_LABEL_KEY_PREFIX = I18N_PROPERTY_LABEL_KEY_PREFIX + "short.";

  /**
   * @pre property != null;
   * @pre property.length() > 0;
   */
  private static String[] i18nPropertyLabel_keys(final String property, final boolean shortLabel) {
    assert property != null;
    assert property.length() > 0;
    String key = I18N_PROPERTY_LABEL_KEY_PREFIX + property;
    String shortKey = I18N_SHORT_PROPERTY_LABEL_KEY_PREFIX + property;
    return shortLabel ? new String[] {shortKey, key} : new String[] {key, shortKey};
  }

  /**
   * @pre key != null;
   * @pre key.length() > 0;
   * @pre type != null;
   */
  private static String keyNotFound(final String key) {
    assert key != null;
    assert key.length() > 0;
    return NOT_FOUND_TOKEN
            + key
            + NOT_FOUND_TOKEN;
  }

  /**
   * <p>Key used in property files to discriminate the type
   *  label.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_TYPE_LABEL_KEY = "type"; //$NON-NLS-1$

  /**
   * <p>Key used in property files to discriminate the
   *  plural type label.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_PLURAL_TYPE_LABEL_KEY =
    I18N_TYPE_LABEL_KEY + ".plural"; //$NON-NLS-1$

  private static String[] I18N_TYPE_LABEL_KEYS =
    new String[] {I18N_TYPE_LABEL_KEY};

  private static String[] I18N_PLURAL_TYPE_LABEL_KEYS =
    new String[] {I18N_PLURAL_TYPE_LABEL_KEY};

  /**
   * <p>Return a label for a type <code>type</code>.</p>
   * <p>We look for a property with key
   *   <code>type</code> or <code>type.plural</code>
   *   in a properties file with the name of <code>type</code>,
   *   and its supertypes. We search bottom up, and return the
   *   first match. If no match is found, we return the String
   *   <code>&quot;???&quot;<var>className</var>&quot;???&quot;</code>.</p>
   *
   * @param type
   *        The type of bean that owns the property. This is the
   *        start of our lookup.
   * @param plural
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *          type == null;
   */
  public static String i18nTypeLabel(final Class<?> type, final boolean plural, final ResourceBundleLoadStrategy strategy) {
    if (type == null) {
      throw new IllegalArgumentException("type must be effective"); //$NON-NLS-1$
    }
    String result = findKeyInTypeProperties(type,
                                            (plural
                                              ? I18N_PLURAL_TYPE_LABEL_KEYS
                                              : I18N_TYPE_LABEL_KEYS),
                                            strategy);
    return (result != null) ? result : keyNotFound(type.getName());
  }


}
