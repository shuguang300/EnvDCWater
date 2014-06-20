package com.env.dcwater.daoservice;

import java.util.List;

public interface DataBaseService <T>{
	
	public void insert (T t);
	
	public void delete (T t);

	public void update (T t);
	
	public T query (int id);
	
	public List<T> queryAll();
	
	
	
}
