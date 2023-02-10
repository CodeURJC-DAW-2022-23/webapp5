package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import app.model.Purchase;
import app.repository.PurchaseRepository;

@Service
public class PurchaseService {
    @Autowired
	private PurchaseRepository purchases;

	public void save(Purchase purchase) {
		purchases.save(purchase);
	}

	public Optional<Purchase> findById(long id) {
		return purchases.findById(id);
	}

	public void deleteById(long id) {
		purchases.deleteById(id);
	}
}
