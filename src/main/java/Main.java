//import com.github.javafaker.Faker;
//import lombok.SneakyThrows;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import ru.geekbrains.java4.lesson6.db.dao.CategoriesMapper;
//import ru.geekbrains.java4.lesson6.db.dao.ProductsMapper;
//import ru.geekbrains.java4.lesson6.db.model.Categories;
//import ru.geekbrains.java4.lesson6.db.model.CategoriesExample;
//import ru.geekbrains.java4.lesson6.db.model.Products;
//import ru.geekbrains.java4.lesson6.db.model.ProductsExample;
//
//import java.io.InputStream;
//
//public class Main {
//    static Faker faker = new Faker();
//
//    @SneakyThrows
//    public static void main(String[] args) {
//
//
//
//
//
//        long categoriesCount = categoriesMapper.countByExample(new CategoriesExample());
//        System.out.println(categoriesCount);
//
//        ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
//        Products product2816 = productsMapper.selectByPrimaryKey(2815L);
//        productsMapper.deleteByPrimaryKey(product2816.getId());

//        Products product = new Products();
//        product.setTitle("updated");
//        product.setId(2900L);
//
//        ProductsExample example = new ProductsExample();
//        example.createCriteria().andIdEqualTo(2900L);
//        productsMapper.updateByExample(product, example);
//
//
//        Categories newCategory = new Categories();
//        newCategory.setTitle(faker.artist().name());
//        newCategory.setId(6);
//        categoriesMapper.insert(newCategory);
//
//        productsMapper.insert(new Products(null,"Chair",897,4L));
//    }
//}
