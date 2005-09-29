package org.geworkbench.bison.annotation;

import org.geworkbench.bison.datastructure.bioobjects.DSBioObject;
import org.geworkbench.bison.datastructure.complex.panels.DSPanel;
import org.geworkbench.bison.datastructure.biocollections.DSDataSet;

import java.util.List;

/**
 * This defines the contract for a context of <i>annotations</i>, <i>labels</i> and <i>classifications</i> for a
 * {@link DSDataSet}. The terms are defined below.
 * <h3>Annotations</h3>
 * An annotation is a (type, value) pair applied to an item. For example, a phenotype could be annotated with
 * (Gender, Female). For each annotation type, an item can only have one annotation value.
 * <h3>Labels</h3>
 * A label is a single-valued tag that is applied to an item. For example, a phenotype could be labelled as "Diabetic".
 * An item can hold any number of labels. All labels have an <i>activation status</i>. This provides a convenient way
 * to consider a subset of items for display or analysis.
 * <p>
 * Special labels called <i>Criterion Labels</i> can be created. These
 * labels are automatically applied to the items of the {@link DSDataSet} that satisfy the associated
 * {@link DSCriterion criterion}. The criterion is a true/false function of the annotations for that item. Changes to
 * the annotation values of the items will result in automatic membership changes to criterion labels.
 * <h3>Classes</h3>
 * A class is like a label, but each item can only hold one class. Additionally, if an item is not
 * explicitly classified, it will automatically hold the <i>default class</i> (usually <i>Control</i>).
 * Classes are not applied directly to items, but indirectly via labels. Changes to label membership will
 * result in automatic changes in classification.
 * <p>
 * Classes are prioritized in the order they are created. It is possible for an item to hold labels of
 * conflicting classification. In this case, the item will be assigned to the highest-priority class associated with
 * its labels.
 *
 * @author John Watkinson
 */
public interface DSAnnotationContext<T extends DSBioObject> extends DSAnnotationSource<T> {

    /**
     * Retrieves the name of this context.
     */
    public String getName();

    /**
     * Sets the name of this context.
     * When the context is used in conjunction with a {@link DSAnnotationContextManager}, do not use this method.
     * Use {@link DSAnnotationContextManager#renameContext(DSDataSet, String, String)} instead. Otherwise, the
     * {@link DSAnnotationContextManager} will lose track of this context.
     */
    public void setName(String newName);

    /**
     * Retrieves the underlying data set on which this context is based.
     */
    public DSDataSet<T> getDataSet();

    //// ANNOTATIONS

    /**
     * Adds an annotation type to this context.
     * This method is a convenience as {@link #annotateItem} will implicitly add the annotation type specified if
     * it has not already been added.
     * @return <tt>true</tt> if the annotation type did not already exist, <tt>false</tt> otherwise.
     */
    public boolean addAnnotationType(DSAnnotationType annotationType);

    /**
     * Removes an annotation type, and all annotations associated with that type.
     * @param annotationType the type to remove.
     * @return <tt>true</tt> if the annotation existed before removal, <tt>false</tt> if it did not.
     */
    public boolean removeAnnotationType(DSAnnotationType annotationType);

    /**
     * Retrieves the number of annotation types in use in this context.
     */
    public int getNumberOfAnnotationTypes();

    /**
     * Retrieves an annotation type by index.
     */
    public DSAnnotationType getAnnotationType(int index);

    /**
     * Applies an annotation to an item.
     * @param item the item to annotate.
     * @param annotationType the type of the annotation. If the annotation type has not yet been added to this context,
     * this method will implicitly add it.
     * @param value the value of the annotation.
     */
    public <Q> void annotateItem(T item, DSAnnotationType<Q> annotationType, Q value);

    /**
     * Removes an annotation for an item.
     * @param item the item from which to remove the annotation.
     * @param annotationType the type of the annotation to remove.
     * @return <tt>true</tt> if there was an annotation to remove, <tt>false</tt> otherwise.
     */
    public boolean removeAnnotationFromItem(T item, DSAnnotationType annotationType);

    /**
     * Retrieves the annotation types for all annotations that are applied to an item.
     * @param item the item for which to retrieve the annotation types.
     * @return the annotation types. Modifying this array has no effect on the annotations.
     */
    public DSAnnotationType[] getAnnotationTypesForItem(T item);

    //// LABELS

    /**
     * Adds a label to this context.
     * This method is a convenience as {@link #labelItem} will implicitly add the label specified if
     * it has not already been added.
     * @return <tt>true</tt> if the label did not already exist, <tt>false</tt> otherwise.
     */
    public boolean addLabel(Object label);

    /**
     * Adds a criterion label to this context.
     * @param label the label.
     * @param criterion the associated criterion.
     * @return <tt>true</tt> if the label did not already exist, <tt>false</tt> otherwise.
     */
    public boolean addCriterionLabel(Object label, DSCriterion<T> criterion);

    /**
     * Removes a label from this context.
     * @param label the label to remove.
     * @return <tt>true</tt> if the label existed prior to removal, <tt>false</tt> otherwise.
     */
    public boolean removeLabel(Object label);

    /**
     * Indicates if the specified label is a criterion label, or a simple label.
     * @param label the label in question.
     * @return <tt>true</tt> if the label has an associated criterion, <tt>false</tt> otherwise.
     */
    public boolean isCriterionLabel(Object label);

    /**
     * Retrieves the criterion for a given criterion label.
     * @param label the label.
     * @return the criterion for that label, or <tt>null</tt> if the label is not a criterion label.
     */
    public DSCriterion<T> getCriterionForLabel(Object label);

    /**
     * Retrieves the number of labels in this context.
     */
    public int getNumberOfLabels();

    /**
     * Retrieves a label by index.
     */
    public Object getLabel(int index);

    /**
     * Sets the active status flag for a label.
     * @param label the label whose active status should be modified.
     * @param active the value of the active status.
     */
    public void setLabelActive(Object label, boolean active);

    /**
     * Sets a label's active status to <tt>true</tt>.
     */
    public void activateLabel(Object label);

    /**
     * Sets a label's active status to <tt>false</tt>.
     */
    public void deactivateLabel(Object label);

    /**
     * Retrieves the active status value of a label.
     */
    public boolean isLabelActive(Object label);

    /**
     * Applies a label to an item.
     * @param item the item to label.
     * @param label the label to apply. If the label does exist in this context, it will be implicitly added by this
     * method.
     * @return <tt>true</tt> if the item did not already hold the label, <tt>false</tt> otherwise.
     */
    public boolean labelItem(T item, Object label);

    /**
     * Retrieves all items that are associated with at least one active label.
     * @return an anonymous panel consisting of a subpanel for each active label. Each subpanel contains the items
     * associated with the appropriate label.
     */
    public DSPanel<T> getActiveItems();

    /**
     * Retrieves the items for a label.
     * @param label the label for which to retrieve items.
     * @return a panel with the same name as the label, containing the appropriate items.
     */
    public DSPanel<T> getItemsWithLabel(Object label);

    /**
     * Returns <tt>true</tt> if the item has the specified label, <tt>false</tt> otherwise.
     */
    public boolean hasLabel(T item, Object label);

    /**
     * Retrieves all items that hold at least one of the specified labels.
     * @param labels the labels for which to retrieve items.
     * @return an anonymous panel consisting of a subpanel for each specified label. Each subpanel contains the items
     * associated with the appropriate label.
     */
    public DSPanel<T> getItemsWithAnyLabel(Object... labels);

    /**
     * Retrieves those items that hold all of the specified labels.
     * @param labels the required labels.
     * @return an anonymous panel containing the appropriate items.
     */
    public DSPanel<T> getItemsWithAllLabels(Object... labels);

    /**
     * Retrieves the labels for a given item.
     * @param item the item in question.
     * @return the labels for the item. Modifying this array has no effect on the labelling of the item.
     */
    public Object[] getLabelsForItem(T item);

    /**
     * Removes a label from an item.
     * @param item the item from which to remove the label.
     * @param label the label to remove.
     * @return <tt>true</tt> if the item held the label prior to removal, <tt>false</tt> otherwise.
     */
    public boolean removeLabelFromItem(T item, Object label);

    //// CLASSES

    /**
     * Adds a class to this context.
     * This method is a convenience as {@link #assignClassToLabel(Object, Object)} or {@link #setDefaultClass} will implicitly add
     * the class specified if it has not already been added.
     * @return <tt>true</tt> if the class did not already exist, <tt>false</tt> otherwise.
     */
    public boolean addClass(Object clazz);

    /**
     * Removes a class from this context. All items that were under this class
     * shift to the next highest priority class that can be applied to them. If there is no next
     * highest priority class, then the item will be assigned to the default class.
     * <p>
     * If the class to be removed is the current default class, then the lowest priority
     * class will become the new default class.
     * <p>
     * If the class to be removed is the only class, then there will no longer be default
     * class.
     * @param clazz the class to remove.
     * @return <tt>true</tt> if the class existed before removal, false otherwise.
     */
    public boolean removeClass(Object clazz);

    /**
     * Retrieves the number of classes in this context.
     */
    public int getNumberOfClasses();

    /**
     * Retrieves a class by index.
     * The index is also the priority of the class (lower index means higher priority).
     */
    public Object getClass(int index);

    /**
     * Retrieves the default class.
     * @return the default class, or <tt>null</tt> if there are no classes in this context.
     */
    public Object getDefaultClass();

    /**
     * Sets the default class.
     * If the class does not exist, it will be added.
     * @param clazz the new default class.
     * @return <tt>true</tt> if the class was not already the default class, <tt>false</tt> otherwise.
     */
    public boolean setDefaultClass(Object clazz);

    /**
     * Assigns a class to a label.
     * All items in that label are assigned to this clazz unless the items are already in a higher-priority class.
     * @param label the label to classify.
     * @param clazz the class.
     * @return <tt>true</tt> if the label was not already assigned to this label, <tt>false</tt> otherwise.
     */
    public boolean assignClassToLabel(Object label, Object clazz);

    /**
     * Retrieves the class assigned to a label.
     * @param label the label in question.
     * @return the assigned class, which will be the default class if not explicitly classified.
     */
    public Object getClassForLabel(Object label);

    /**
     * Retrieves the class for a given item.
     * @param item the item in question.
     * @return the assigned class, which will be the default class if not explicitly classified.
     */
    public Object getClassForItem(T item);

    /**
     * Retrieves all the labels that have been assigned to a class. If the class specified is the default class,
     * then all labels will be returned that were not explicitly classified.
     * @param clazz the class in question.
     * @return the labels assigned to the class. Modifying this array has no effect on classification.
     */
    public Object[] getLabelsForClass(Object clazz);

    /**
     * Removes a class from a label, restoring its classification to the default class.
     * @param label the label from which the class should be removed.
     * @return the old classification held by this label, or <tt>null</tt> if no explicit class was assigned to this
     * label.
     */
    public Object removeClassFromLabel(Object label);

    /**
     * Retrieves all items for the a class. Note that each item will only appear once
     * in the resulting panel, even if it holds two or more labels, each of which is associated with the given class.
     * @param clazz the class for which to retrieve items.
     * @return a panel with the same name as the class, containing the items in the class.
     */
    public DSPanel<T> getItemsForClass(Object clazz);

}
