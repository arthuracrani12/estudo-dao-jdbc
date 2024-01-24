package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department obj = new Department(1, "Books");
		System.out.println(obj);

		Seller seller = new Seller(25, "arthur", "arthuracrani12@gmail.com", new Date(), 3000.0, obj);
		
		//uma forma de fazer uma injeção de dependencia sem conhecer a implementação apenas a interface
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println(seller);
	}

}
