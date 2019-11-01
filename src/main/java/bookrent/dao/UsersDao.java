package bookrent.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bookrent.persistence.UsersDTO;

public interface UsersDao {
	
	public List<UsersDTO> selectAll();
	public List<UsersDTO> selectByNameATel(@Param("u_name") String u_name, @Param("u_tel") String u_tel);
	public int insert(UsersDTO usersDto);
	public int update(UsersDTO usersDto);
	public String getMaxCode();
	public UsersDTO selectByCode(String u_code);

}
