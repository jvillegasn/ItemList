

import java.text.NumberFormat;
import java.util.Scanner;
//import java.lang.StringBuilder;

public class Item{
	private int regNum;
	private String name;
	private int quantity;
	private double price;
	private boolean sale;
	
	/*
	public Item(){
		this(0, "", 0, 0.0, false);
	}
	
	public Item(int regNum, String name,int quantity, double price, boolean sale){
		this.regNum = regNum;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.sale = sale;
	}
	*/
	
	public void setRegNum(int regNum) {
		this.regNum = regNum;
	}
	
	public int getRegNum() { return regNum; }
	
	public String getStringNum() { return Integer.toString(regNum); }
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName() { return name; }
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getPrice() { return price; }
	
	public String getStringPrice() { return Double.toString(price); }
	
	public void setQuantity(int quantity){
	}
	
	public int getQuantity() { return quantity; }
	
	public String getStringQuantity() { return Integer.toString(quantity); }
	
	
	public String getStringCurrency(double x) {
		NumberFormat str = NumberFormat.getCurrencyInstance();
        return str.format(x);
	} 
	
	public void  setSale(boolean sale) {
		this.sale = sale;
	}
	
	public boolean getSale() { return sale; }
}
