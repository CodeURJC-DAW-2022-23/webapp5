package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.Purchase;
import app.repository.PurchaseRepository;

@Service
public class PurchaseService {
    @Autowired
	private PurchaseRepository purchases;

	public void save(Purchase purchase) {

		purchases.save(purchase);
	}
}
