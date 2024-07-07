package models.transactions;

import java.time.LocalDate;

import controllers.transactions.CreateTransactionsController;
import models.schemas.OrderProduct;
import models.schemas.Transaction;
import models.schemas.User;

public class CreateTransactionsModel {

    private CreateTransactionsController controller;
    

	public CreateTransactionsModel(CreateTransactionsController createtransactionscontroller) {
        this.controller = createtransactionscontroller;
    }
    
    public boolean saveTransaction(Transaction transaction,User loggedInUser) {
    	return this.controller.getDBManager().query.createTransaction(transaction,  loggedInUser);
    }

}
