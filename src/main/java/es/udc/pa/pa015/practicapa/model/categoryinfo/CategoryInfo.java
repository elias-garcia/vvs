package es.udc.pa.pa015.practicapa.model.categoryinfo;

import org.hibernate.annotations.BatchSize;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Category info class.
 */
@Entity
@Immutable
@BatchSize(size = 5)
public class CategoryInfo {

  /** CategoryId. */
  private Long categoryId;

  /** Category name. */
  private String categoryName;

  /** Blank constructor. */
  public CategoryInfo() {

  }

  /**
   * This is the category info constructor.
   * @param categoryNameParam
   *          Category name
   */
  public CategoryInfo(final String categoryNameParam) {

    /**
     * NOTE: "categoryId" *must* be left as "null" since its value is
     * automatically generated.
     */

    this.categoryName = categoryNameParam;
  }

  /**
   * Get category id.
   * @return category id
   */
  @Id
  @SequenceGenerator(// It only takes effect for
      name = "CategoryInfoIdGenerator", // databases providing identifier
      sequenceName = "CategoryInfoSeq") // generators.
  @GeneratedValue(strategy = GenerationType.AUTO,
                                    generator = "CategoryInfoIdGenerator")
  public final Long getCategoryId() {
    return categoryId;
  }

  /**
   * Set category id.
   * @param categoryIdParam
   *                  category id
   */
  public final void setCategoryId(final Long categoryIdParam) {
    this.categoryId = categoryIdParam;
  }

  /**
   * Get category name.
   * @return category name
   */
  public String getCategoryName() {
    return categoryName;
  }

  /**
   * Set category name.
   * @param categoryNameParam
   *                category name
   */
  public void setCategoryName(final String categoryNameParam) {
    this.categoryName = categoryNameParam;
  }

  /**
   * Transform category to string.
   */
  @Override
  public final String toString() {
    return "CategoryInfo [categoryId=" + categoryId + ", categoryName="
        + categoryName + "]";
  }
}
