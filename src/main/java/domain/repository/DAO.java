package domain.repository;

import java.util.List;
import java.util.Map;

import exceptions.NegativeValueException;

public interface DAO<T> {
	
	List<T> getAll();
    
	void add(T t);
    
    T create(Map<String, String> tStr) throws NegativeValueException;
    
    void update(int position, T t);
    
    void delete(int position);

}
