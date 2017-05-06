package it.diepet.spring.tx.context.test.app;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.diepet.spring.tx.context.test.app.error.ApplicationRuntimeException;
import it.diepet.spring.tx.context.test.app.model.Product;
import it.diepet.spring.tx.context.test.app.service.ProductService;
import it.diepet.spring.tx.context.test.util.StringCollector;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/test-spring-tx-context-application-context.xml")
public class TxContextAppTest {

	@Autowired
	private ProductService productService;

	@Before
	public void init() {
		StringCollector.reset();
	}

	@Test
	public void testPersistence() {
		Product product = new Product();
		product.setId(9L);
		product.setCode("99999");
		product.setDescription("Apple");
		productService.add(product);
		List<Product> productList = productService.findAll();

		Assert.assertNotNull(productList);
		Assert.assertEquals(4, productList.size());

		List<String> stringList = StringCollector.getList();
		Assert.assertNotNull(stringList);
		Assert.assertEquals(8, stringList.size());
		Assert.assertEquals("productService.add()", stringList.get(0));
		Assert.assertTrue(stringList.get(1).startsWith("Transaction Context ID: "));
		Assert.assertEquals("Operation: add", stringList.get(2));
		Assert.assertEquals("Added product to list: Apple", stringList.get(3));
		Assert.assertEquals("Product set size 1", stringList.get(4));
		Assert.assertEquals("productService.findAll()", stringList.get(5));
		Assert.assertTrue(stringList.get(6).startsWith("Transaction Context ID: "));
		Assert.assertEquals("Operation: findAll", stringList.get(7));
	}

	@Test
	public void testAddTwoProducts() {
		Product product1 = new Product();
		product1.setId(98L);
		product1.setCode("999998");
		product1.setDescription("Pear");

		Product product2 = new Product();
		product2.setId(99L);
		product2.setCode("999999");
		product2.setDescription("Lemon");

		productService.addTwoProducts(product1, product2);
		List<String> stringList = StringCollector.getList();
		Assert.assertNotNull(stringList);
		Assert.assertEquals(6, stringList.size());
		Assert.assertEquals("productService.addTwoProducts()", stringList.get(0));
		Assert.assertTrue(stringList.get(1).startsWith("Transaction Context ID: "));
		Assert.assertEquals("Operation: addTwoProducts", stringList.get(2));
		Assert.assertEquals("Added product to list: Pear", stringList.get(3));
		Assert.assertEquals("Added product to list: Lemon", stringList.get(4));
		Assert.assertEquals("Product set size 2", stringList.get(5));

	}

	@Test
	public void testRemoveAttribute() {
		productService.addProductToContextAndRemoveIt();
		List<String> stringList = StringCollector.getList();
		Assert.assertNotNull(stringList);
		Assert.assertEquals(3, stringList.size());
		Assert.assertEquals("productService.addProductToContextAndRemoveIt()", stringList.get(0));
		Assert.assertTrue(stringList.get(1).startsWith("Transaction Context ID: "));
		Assert.assertEquals("Operation: null", stringList.get(2));
	}

	@Test
	public void testNullContextInANotTransactionalMethod() {
		productService.retrieveContextInANotTransactionalMethod();
		List<String> stringList = StringCollector.getList();
		Assert.assertNotNull(stringList);
		Assert.assertEquals(8, stringList.size());
		Assert.assertEquals("productService.retrieveContextInANotTransactionalMethod()", stringList.get(0));
		Assert.assertEquals("Transaction Context ID: ", stringList.get(1));
		Assert.assertEquals("Operation: null", stringList.get(2));
		Assert.assertEquals("Product List: null", stringList.get(3));
		Assert.assertEquals("Product Set: null", stringList.get(4));
		Assert.assertEquals("Operation: null", stringList.get(5));
		Assert.assertEquals("Product List: null", stringList.get(6));
		Assert.assertEquals("Product Set: null", stringList.get(7));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetListAttributeToWrongAttributeName() {
		productService.populateContextListAttributeWrong();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetSetAttributeToWrongAttributeName() {
		productService.populateContextSetAttributeWrong();
	}

	@Test
	public void testTransactionalMethodLaunchingRuntimeException() {
		try {
			productService.launchRuntimeException();
		} catch (ApplicationRuntimeException e) {
			List<String> stringList = StringCollector.getList();
			Assert.assertNotNull(stringList);
			Assert.assertEquals(1, stringList.size());
			Assert.assertEquals("productService.launchRuntimeException()", stringList.get(0));
			return;
		}
		Assert.fail("ApplicationRuntimeException not thrown");

	}

	@Test
	public void testActiveNotActive() {
		productService.launchTransactionalMethod();
		productService.launchNotTransactionalMethod();
		List<String> stringList = StringCollector.getList();
		Assert.assertNotNull(stringList);
		Assert.assertEquals(4, stringList.size());
		Assert.assertEquals("Transaction Active: true", stringList.get(0));
		Assert.assertTrue(stringList.get(1).startsWith("Transaction Context ID: "));
		Assert.assertEquals("Operation: launchTransactionalMethod", stringList.get(2));
		Assert.assertEquals("Transaction Active: false", stringList.get(3));
	}

	@Test
	public void testRequiresNewMethod() {
		productService.launchRequiresNewMethod();
		List<String> stringList = StringCollector.getList();
		Assert.assertNotNull(stringList);
		Assert.assertEquals(6, stringList.size());
		Assert.assertEquals("productService.launchRequiresNewMethod()", stringList.get(0));
		Assert.assertEquals("warehouseService.executeRequiresNewMethod()", stringList.get(1));
		Assert.assertTrue(stringList.get(2).startsWith("Transaction Context ID: "));
		Assert.assertEquals("Operation: executeRequiresNewMethod", stringList.get(3));
		Assert.assertTrue(stringList.get(4).startsWith("Transaction Context ID: "));
		Assert.assertEquals("Operation: launchRequiresNewMethod", stringList.get(5));
	}

	@Test
	public void testMethodFailingWhenCommits() {
		try {
			productService.launchMethodFailingWhenCommits();
		} catch (RuntimeException e) {
			List<String> stringList = StringCollector.getList();
			Assert.assertNotNull(stringList);
			Assert.assertEquals(3, stringList.size());
			Assert.assertEquals("productService.launchMethodFailingWhenCommits()", stringList.get(0));
			Assert.assertEquals("warehouseService.launchCheckedException()", stringList.get(1));
			Assert.assertEquals("Thrown ApplicationException", stringList.get(2));
			return;
		}
		Assert.fail("RuntimeException not thrown");
	}
}
