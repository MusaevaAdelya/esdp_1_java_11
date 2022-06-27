package com.esdp.demo_esdp.repositories;

import com.esdp.demo_esdp.entity.Product;
import com.esdp.demo_esdp.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where lower(p.name) like  %:name% and p.status = :status ")
    Page<Product> getProductName(@Param("name") String name, @Param("status") ProductStatus status, Pageable pageable);

    @Query("select p from Product p where lower(p.category.name) like %:category% and p.status = :status")
    Page<Product> getProductCategory(@Param("category") String category, @Param("status") ProductStatus status, Pageable pageable);

    @Query("select p from Product p where p.price >= :from and p.price <= :before and p.status = :status")
    Page<Product> getProductPrice(@Param("from") Integer from, @Param("before") Integer before, @Param("status") ProductStatus status, Pageable pageable);

    @Query("select p from Product p where p.user.email = :email and p.status = :status")
    List<Product> getProductUser(@Param("email") String email, @Param("status") ProductStatus status);

    @Query("select p from Product p where p.status = :status order by p.dateAdd")
    Page<Product> getProducts(@Param("status") ProductStatus status, Pageable pageable);

    @Query("select p.user.email from Product p where p.id = :id ")
    String getPublicationUserEmail(@Param("id") Long id);

    @Query("select p from Product p where p.category = :categoryId order by p.dateAdd")
    Page<Product> findProductsByCategoryId(Long categoryId, Pageable pageable);


    @Transactional
    @Modifying
    @Query(value = "update products  set status = :status where id = :id", nativeQuery = true)
    void updateProductStatus(@Param("status") String status, @Param("id") Long id);


    @Query("select p from Product p where p.user.email = :email")
    List<Product> getProductsUser(@Param("email") String email);


    @Query("select p from Product p where p.status = :status")
    List<Product> getProductsStatus(@Param("status") ProductStatus status);


    @Query("select p from Product p where lower(p.name) like  %:name%")
    List<Product> getProductsName(@Param("name") String name);
}
