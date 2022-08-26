package com.kubian.mode.dao;

import com.kubian.mode.Product;
import com.kubian.mode.Specs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ProductDao extends CrudRepository<Product, Integer> {

    @Query("select aaaa from Product as aaaa where id in( SELECT pid FROM Specs WHERE pid in ( SELECT id FROM Product) ) and bid=:bid and isClose=:isClose and status=:status and sid=:sIds")
    Page<Product> findByBidAndIsCloseAndStatus(Pageable pageable, @Param("bid") Long bid, @Param("isClose") Integer isClose, @Param("status") Integer status, @Param("sIds") Long sIds);

    Product findById(Long id);

    @Query(" from Product where name like :name")
    List<Product> findByNameLike(Pageable pageable, @Param("name") String name);

    @Query("select count(name) from Product where name like :name")
    Integer findByNameCount(@Param("name") String name);

    List<Product> findByBid(Long bid);

    Page<Product> findByBid(Long bid, Pageable pageable);

    @Query("from Product where name like :name and bid=:bid")
    List<Product> getProductByName(@Param("name") String name, @Param("bid") Long bid, Pageable pageable);

    @Query("select count(1)from Product where name like :name and bid=:bid")
    int getProductByName(@Param("name") String name, @Param("bid") Long bid);

    Page<Product> findByBidAndSlhc(Long bid, Integer slhc, Pageable pageable);

    Page<Product> findByBidAndSales(Long bid, Integer sales, Pageable pageable);

    Page<Product> findByBidAndSIds(Long bid, Long sIds, Pageable pageable);

    Page<Product> findByBidAndSId(Long bid, Long sId, Pageable pageable);

    List<Product> findBySIdsAndIsSuggest(Long sIds , Integer isSuggest);
}
