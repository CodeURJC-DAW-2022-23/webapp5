package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}

