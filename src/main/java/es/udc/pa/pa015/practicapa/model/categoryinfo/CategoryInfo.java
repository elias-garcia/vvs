package es.udc.pa.pa015.practicapa.model.categoryinfo;

import org.hibernate.annotations.BatchSize;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Immutable
@BatchSize(size = 5)
public class CategoryInfo {

  private Long categoryId;
  private String categoryName;

  public CategoryInfo() {

  }

  /**
   * This is the category info constructor.
   * @param categoryName
   *          Category name
   */
  public CategoryInfo(String categoryName) {

    /**
     * NOTE: "categoryId" *must* be left as "null" since its value is
     * automatically generated.
     */

    this.categoryName = categoryName;
  }

  @Id
  @SequenceGenerator( // It only takes effect for
      name = "CategoryInfoIdGenerator", // databases providing identifier
      sequenceName = "CategoryInfoSeq") // generators.
  @GeneratedValue(strategy = GenerationType.AUTO, 
                                generator = "CategoryInfoIdGenerator")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  @Override
  public String toString() {
    return "CategoryInfo [categoryId=" + categoryId + ", categoryName="
        + categoryName + "]";
  }
}
