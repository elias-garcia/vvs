package es.udc.pa.pa015.practicapa.test.model.categoryinfo;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class CategoryInfoDaoUnitTest {

	private final String EXISTENT_CATEGORY_NAME = "Baloncesto";
	private final long NON_EXISTENT_CATEGORY_ID = -1;

	CategoryInfo categoryInfo;

	@Autowired
	private CategoryInfoDao categoryInfoDao;

	@Autowired
	private SessionFactory sessionFactory;

	private CategoryInfo newCategory() {
		return new CategoryInfo(EXISTENT_CATEGORY_NAME);
	}

	private void initializeCategory() {
		categoryInfo = new CategoryInfo(EXISTENT_CATEGORY_NAME);
		sessionFactory.getCurrentSession().saveOrUpdate(categoryInfo);
	}

	/**
	 * PR-UN-000 (GenericDaosave(E entity) no devuelve el objeto después de
	 * hacerlo persistente, por lo tanto la única manera de comprobar si el dato
	 * es persistente es usar el método GenericDaosave.find(PK id))
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test
	public void testSaveAndFindGenericDao() throws InstanceNotFoundException {
		/* Setup */
		CategoryInfo categoryInfo = newCategory();
		CategoryInfo foundCategoryInfo;
		/* Call */
		categoryInfoDao.save(categoryInfo);
		foundCategoryInfo = categoryInfoDao.find(categoryInfo.getCategoryId());
		/* Assertion */
		assertEquals(categoryInfo, foundCategoryInfo);
	}

	/**
	 * PR-UN-001
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testFindAndSaveGenericDaoWithANonExistentCategoryId() throws InstanceNotFoundException {
		/* Setup */
		CategoryInfo categoryInfo = newCategory();
		/* Call */
		categoryInfoDao.save(categoryInfo);
		categoryInfoDao.find(NON_EXISTENT_CATEGORY_ID);
		/* Assertion */
		/* InstanceNotFoundException expected */
	}

	/**
	 * PR-UN-002
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test
	public void testRemoveAndSaveAndFindGenericDao() throws InstanceNotFoundException {
		/* Setup */
		boolean exceptionCaptured = false;
		CategoryInfo categoryInfo = newCategory();
		/* Call */
		categoryInfoDao.save(categoryInfo);
		categoryInfoDao.remove(categoryInfo.getCategoryId());
		try {
			categoryInfoDao.find(categoryInfo.getCategoryId());
		} catch (InstanceNotFoundException e) {
			exceptionCaptured = true;
		}
		/* Assertion */
		assertTrue(exceptionCaptured);
	}

	/**
	 * PR-UN-003
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveAndSaveAndFindGenericDaoWithANonExistentCategoryId() throws InstanceNotFoundException {
		/* Setup */
		CategoryInfo categoryInfo = newCategory();
		/* Call */
		categoryInfoDao.save(categoryInfo);
		categoryInfoDao.remove(NON_EXISTENT_CATEGORY_ID);
		/* Assertion */
		/* InstanceNotFoundException expected */
	}

	/**
	 * PR-UN-004
	 * 
	 * @throws InstanceNotFoundException
	 */
	@Test
	public void testSaveUddateDataGenericDao() throws InstanceNotFoundException {
		/* Setup */
		CategoryInfo categoryInfo = newCategory();
		CategoryInfo foundCategoryInfo;
		String newName = "Tenis";
		/* Call */
		categoryInfoDao.save(categoryInfo);
		categoryInfo.setCategoryName(newName);
		categoryInfoDao.save(categoryInfo);
		foundCategoryInfo = categoryInfoDao.find(categoryInfo.getCategoryId());
		/* Assertion */
		assertEquals(foundCategoryInfo.getCategoryName(), newName);
	}

	/**
	 * PR-UN-005
	 */
	@Test
	public void testFindAllCategoriesWithSavedCategorys() {
		/* Setup */
		initializeCategory();
		/* Call */
		List<CategoryInfo> list = categoryInfoDao.findAllCategories();
		/* Assertion */
		assertEquals(categoryInfo, list.get(0));
	}

	/**
	 * PR-UN-006
	 */
	@Test
	public void testFindAllCategoriesWithoutSavedCategorys() {
		/* Setup */

		/* Call */
		List<CategoryInfo> list = categoryInfoDao.findAllCategories();
		/* Assertion */
		assertTrue(list.isEmpty());
	}

}
