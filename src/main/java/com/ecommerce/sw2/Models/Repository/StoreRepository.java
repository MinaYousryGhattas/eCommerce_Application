package com.ecommerce.sw2.Models.Repository;

import com.ecommerce.sw2.Models.Domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findOneByName(String name);
    Collection<Store> findAllBySuggested(Boolean check);
}
