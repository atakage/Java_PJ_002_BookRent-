package bookrent.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bookrent.persistence.BooksDTO;

public interface BooksDao {
	
	public List<BooksDTO> selectAll();			// 출력할 때 대여테이블에서 대여여부까지 출력
	public List<BooksDTO> selectByNameAAuther(@Param("b_name") String b_name, @Param("b_auther") String b_auther);
	public int insert(BooksDTO booksDto);		//등록시 코드중복X,
	public int update(BooksDTO booksDto);
	public String getMaxCode();
	public BooksDTO selectByName(String b_name);
	public BooksDTO selectByCode(String b_code);

}
