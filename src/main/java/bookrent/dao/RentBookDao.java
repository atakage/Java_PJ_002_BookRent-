package bookrent.dao;

import java.util.List;

import bookrent.persistence.RentBookDTO;

public interface RentBookDao {
	
	public List<RentBookDTO> selectAll();
	public RentBookDTO selectByBCode(String rent_bcode);
	public int insert(RentBookDTO dto);
	public List<RentBookDTO> selectByRYN();
	//public RentBookDTO searchByYNForDelete(String rent_retur_yn);
	public int update(RentBookDTO dto);
	public RentBookDTO selectBySEQForDelete(long rent_seq);
	public int deleteBySEQ(long rent_seq);
	public RentBookDTO selectByCodeForR(String rent_bcode);
}
