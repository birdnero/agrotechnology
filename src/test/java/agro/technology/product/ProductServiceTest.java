package agro.technology.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import agro.technology.Product.Product;
import agro.technology.Product.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(args = "--test")
public class ProductServiceTest {
    String word = "Apple";
    @Autowired
    ProductService productService;

    @BeforeEach
    void preSetUp() {}

    @AfterEach
    void postSetUp() {}

    @Test
    // create simple product
    void productCreate01() {
        int amount = 100;
        Product product = productService.create(word, amount);

        assertEquals(product.getName(), word);
        assertEquals(product.getAmount(), amount);
    }

    @Test
    // create null product
    void productCreate02() {
        String word = null;
        String word2 = "";
        int amount = 100;
        Product product = productService.create(word, amount);
        assertEquals(product, null);

        product = productService.create(word2, amount);
        assertEquals(product, null);
    }

    @Test
    // create product with від'ємною amount
    void productCreate03() {
        int amount = -100;
        Product product = productService.create(word, amount);

        assertEquals(product.getAmount(), 0);
    }

    @Test
    // тут був тест який дублює перший тому я замінив його на це щоб лишилася нумерація ))
    void product04() {
        //*  its just an empty test :) it automatically passed yyyuuupppiii
    }

    @Test
    // simply add amount
    void productAdd05() {
        int amount = 100;
        Product product = productService.create(word, 0);

        productService.add(product, amount);
        assertEquals(product.getAmount(), amount);
    }

    @Test
    // add від'ємне amount
    void productAdd06() {
        int amount = 100;
        Product product = productService.create(word, amount);

        productService.add(product, -amount);
        assertEquals(product.getAmount(), amount);
    }

    @Test
    // add amount to null
    void productAdd07() {
        assertDoesNotThrow(() -> productService.add(null, -100));
    }

    @Test
    // simply substract amount
    void productSubstract08() {
        int amount = 100, substract = 40;
        Product product = productService.create(word, amount);

        productService.substract(product, substract);
        assertEquals(product.getAmount(), amount - substract);
    }

    @Test
    // substract amвід'ємне amountount
    void productSubstract09() {
        int amount = 100, substract = -40;
        Product product = productService.create(word, amount);

        assertEquals(productService.substract(product, substract), false);
        assertEquals(product.getAmount(), amount);
    }

    @Test
    // substract amount from null
    void productSubstract10() {
        int substract = 40;
        assertEquals(productService.substract(null, substract), false);
    }

    @Test
    // substract more than amount of product
    void productSubstract11() {
        int amount = 100, substract = 101;
        Product product = productService.create(word, amount);

        assertEquals(productService.substract(product, substract), false);
        assertEquals(product.getAmount(), amount);
    }

    @Test
    // substract exatly the same amount
    void productSubstract12() {
        int amount = 100;
        Product product = productService.create(word, amount);

        assertEquals(productService.substract(product, amount), true);
        assertEquals(product.getAmount(), 0);
    }

    @Test
    // simply update amount
    void productUpdate13() {
        int amount = 100;
        Product product = productService.create(word, amount);

        assertEquals(productService.updateAmount(product, init -> init + amount), true);
        assertEquals(product.getAmount(), 2 * amount);
    }

    @Test
    // update amount to null
    void productUpdate14() {
        int amount = 100;
        Product product = productService.create(word, amount);

        assertEquals(productService.updateAmount(product, init -> null), false);
    }

    @Test
    // update amount to minus
    void productUpdate15() {
        int amount = 100;
        Product product = productService.create(word, 0);

        assertEquals(productService.updateAmount(product, i -> - amount), false);
        assertEquals(product.getAmount(), 0);
    }

    @Test
    // update amount for null
    void productUpdate16() {
        assertEquals(productService.updateAmount(null, i -> i), false);
    }

    @Test
    // simply divide product
    void productDivide17() {
        int amount = 100;
        int part = 40;
        Product product = productService.create(word, amount);
        Product product2 = productService.divideProduct(product, part);

        assertEquals(product.getAmount(), amount - part);
        assertEquals(product2.getAmount(), part);
    }

    @Test
    // divide product with від'ємним значенням
    void productDivide18() {
        int amount = 100;
        int part = -1;
        Product product = productService.create(word, amount);
        Product product2 = productService.divideProduct(product, part);

        assertEquals(product.getAmount(), amount);
        assertEquals(product2.getAmount(), 0);
    }

    @Test
    // divide product with exatly the same amount
    void productDivide19() {
        int amount = 100;
        Product product = productService.create(word, amount);
        Product product2 = productService.divideProduct(product, amount);

        assertEquals(product.getAmount(), 0);
        assertEquals(product2.getAmount(), amount);
    }

    @Test
    // divide product with bigger amount
    void productDivide20() {
        int amount = 100;
        int part = 101;
        Product product = productService.create(word, amount);
        Product product2 = productService.divideProduct(product, part);

        assertEquals(product.getAmount(), amount);
        assertEquals(product2.getAmount(), 0);
    }

    @Test
    // divide null product
    void productDivide21() {
        Product product2 = productService.divideProduct(null, 100);

        assertEquals(product2, null);
    }

    // також там є метод getAllProducts, але його практично неможливо тестувати оскільки він зчитує дані з бд
}