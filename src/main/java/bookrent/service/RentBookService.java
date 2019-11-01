package bookrent.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import bookrent.config.DBConnection;
import bookrent.dao.BooksDao;
import bookrent.dao.RentBookDao;
import bookrent.dao.UsersDao;
import bookrent.persistence.BooksDTO;
import bookrent.persistence.RentBookDTO;
import bookrent.persistence.UsersDTO;

public class RentBookService {

	Scanner scan = null;
	RentBookDao rentBookDao;
	BooksDao booksDao;
	UsersDao usersDao;

	public RentBookService() {
		// TODO Auto-generated constructor stub

		SqlSession sqlSession = DBConnection.getSqlSessionFactory().openSession(true);
		scan = new Scanner(System.in);
		this.rentBookDao = sqlSession.getMapper(RentBookDao.class);
		this.booksDao = sqlSession.getMapper(BooksDao.class);
		this.usersDao = sqlSession.getMapper(UsersDao.class);
	}

	public void RentBookMenu() {

		while (true) {

			int forEx = 0;

			System.out.println("==================");
			System.out.println("도서 대출 시스템");
			System.out.println("==================");
			System.out.println("s:대여명세출력 sn:도서명으로검색 i:등록 r:반납처리 d:삭제 -q:종료");
			System.out.println("----------------------------------");
			System.out.print(" >> ");
			String strC = scan.nextLine();

			if (strC.equalsIgnoreCase("-q"))
				break;

			if (strC.equalsIgnoreCase("s")) {
				List<RentBookDTO> rentbookList = rentBookDao.selectAll();

				this.RentListPrint(rentbookList);
				;
				continue;
			}
			
			else if(strC.equalsIgnoreCase("sn")) {
				System.out.print("도서명 입력 >> ");
				String strb_name = scan.nextLine();
				
			 BooksDTO dto	= booksDao.selectByName(strb_name);
			 
			 if(dto == null) {
				 System.out.println("도서 정보 없음");
			 }
			 
			 this.bookDtoPrint(dto);
			 
			 RentBookDTO rentBookDto = rentBookDao.selectByBCode(dto.getB_code());
			 
			 System.out.println("==============================================");
			 this.RentListPrint(rentBookDto);
				
				
				
			}

			else if (strC.equalsIgnoreCase("i")) {

				System.out.print(" 도서코드 입력 >> ");
				String strb_code = scan.nextLine();

				BooksDTO dto = booksDao.selectByCode(strb_code);

				if (dto == null) {
					System.out.println("해당 도서 존재하지 않음");
					continue;
				}

				List<RentBookDTO> rentbookList = rentBookDao.selectByRYN();
				
				if(rentbookList != null) {

				for (RentBookDTO rentBookDto : rentbookList) {

					String strGetB_code = rentBookDto.getRent_bcode();

					System.out.println(strGetB_code);

					if (strGetB_code.equals(strb_code)) {
						System.out.println("이미 대여 중인 도서임");
						forEx = 1;
						break;
					}

				}
				
				}

				
				if (forEx == 1) {
					continue;
				}

				this.bookDtoPrint(dto);

				System.out.print("회원코드 입력 >> ");
				String stru_code = scan.nextLine();

				UsersDTO usersDto = usersDao.selectByCode(stru_code);

				if (usersDto == null) {
					System.out.println("해당 회원 존재하지 않음");
					continue;
				}

				this.usersDtoPrint(usersDto);

				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String strDate = sdf.format(date);

				Calendar cal = Calendar.getInstance();

				cal.setTime(date);
				cal.add(Calendar.DATE, 14);

				String strReturnDate = sdf.format(cal.getTime());

				System.out.println(strDate);
				System.out.println(strReturnDate);

				String strReturnYN = "N";

				int intPoint = 0;

				RentBookDTO rentBookDto = new RentBookDTO();

				rentBookDto.setRent_bcode(strb_code);
				rentBookDto.setRent_date(strDate);
				rentBookDto.setRent_point(intPoint);
				rentBookDto.setRent_retur_yn(strReturnYN);
				rentBookDto.setRent_return_date(strReturnDate);
				rentBookDto.setRent_ucode(stru_code);

				int ret = rentBookDao.insert(rentBookDto);

				if (ret > 0) {
					System.out.println("등록 완료");
				} else {
					System.out.println("등록 실패");
				}

			}
			
			
			else if (strC.equalsIgnoreCase("r")) {
				System.out.print("반납처리할 도서 코드 >> ");
				String strb_code = scan.nextLine();
				
				RentBookDTO dto= rentBookDao.selectByBCode(strb_code);
				
				System.out.println(dto.toString());
				
				if(dto == null) {
					System.out.println("해당 도서 정보 없음");
					continue;
				}
				
				
				
				dto = rentBookDao.selectByCodeForR(dto.getRent_bcode());
				
				System.out.println(dto.toString());
				
				if(dto == null) {
					System.out.println("해당 도서는 이미 반납 처리 되었거나 정보에 없음");
					continue;
				}
				
				
				this.RentListPrint(dto);
				
			
				try {			
					
					System.out.println("반납 예정일: " + dto.getRent_return_date());		
					System.out.print("반납일자 입력 >> ");
					String strInDate = this.DateInput();
					
					
					
					int diff= strInDate.compareTo(dto.getRent_return_date());
					System.out.println(diff);
					
					
					int intPoint = 0;
					if(diff > 0) { // 1 이상은 지연
						System.out.println("지연 반납");
						
					}else {		// 0이거나 -1이면 정상
						System.out.println("정상 반납");
						intPoint = 5;
					}
					
					
					
					dto.setRent_retur_yn("Y");
					dto.setRent_point(intPoint);
					
					
					
					int ret = rentBookDao.update(dto);
					
					if(ret > 0) {
						System.out.println("반납 처리 완료");
					}else {
						System.out.println("반납 처리 실패");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("반납일 바르게 입력");
				}
				
				
			}
			
			else if(strC.equalsIgnoreCase("d")) {
				
				try {
				
					System.out.print("시퀀스 번호 입력 >> ");
					String strSEQ = scan.nextLine();
					long longSEQ = Integer.valueOf(strSEQ);
					
					RentBookDTO dto = rentBookDao.selectBySEQForDelete(longSEQ);
					
					if(dto == null) {
						System.out.println("대여 정보 없거나 해당 도서가 미반납 상태임");
						continue;
						
					}
					
					this.RentListPrint(dto);
					
					
					System.out.println("삭제:Y, 취소:N");
					System.out.print(">> ");
					strC = scan.nextLine();
					
					
					if(strC.equalsIgnoreCase("Y")) {
						int ret = rentBookDao.deleteBySEQ(longSEQ);
						if(ret > 0) {
							System.out.println("삭제 완료");
						}else {
							System.out.println("삭제 실패");
						}
						continue;
					}else if(strC.equalsIgnoreCase("N")){
						continue;
					}
					
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					System.out.println("커맨드 올바르게 입력하시오 ");
					continue;
				}
				
			}

		}

	}

	public void RentListPrint(List<RentBookDTO> rentBookList) {

		System.out.println("일련번호\t대출일\t반납예정일\t도서코드\t회원코드\t도서반납여부\t포인트");
		System.out.println("============================================================");
		for (RentBookDTO dto : rentBookList) {

			System.out.print(dto.getRent_seq() + "\t");
			System.out.print(dto.getRent_date() + "\t");
			System.out.print(dto.getRent_return_date() + "\t");
			System.out.print(dto.getRent_bcode() + "\t");
			System.out.print(dto.getRent_ucode() + "\t");
			System.out.print(dto.getRent_retur_yn() + "\t");
			System.out.print(dto.getRent_point() + "\n");

		}

	}
	
	public void RentListPrint(RentBookDTO dto) {

		System.out.println("일련번호\t대출일\t반납예정일\t도서코드\t회원코드\t도서반납여부\t포인트");
		System.out.println("============================================================");
		

			System.out.print(dto.getRent_seq() + "\t");
			System.out.print(dto.getRent_date() + "\t");
			System.out.print(dto.getRent_return_date() + "\t");
			System.out.print(dto.getRent_bcode() + "\t");
			System.out.print(dto.getRent_ucode() + "\t");
			System.out.print(dto.getRent_retur_yn() + "\t");
			System.out.print(dto.getRent_point() + "\n");

		

	}

	public void bookDtoPrint(RentBookDTO dto) {

		System.out.println("도서코드\t도서명\t저자\t출판사\t구입연도\t구입가격\t대여가격");
		System.out.println("============================================================");

		System.out.print(dto.getRent_seq() + "\t");
		System.out.print(dto.getRent_date() + "\t");
		System.out.print(dto.getRent_return_date() + "\t");
		System.out.print(dto.getRent_bcode() + "\t");
		System.out.print(dto.getRent_ucode() + "\t");
		System.out.print(dto.getRent_retur_yn() + "\t");
		System.out.print(dto.getRent_point() + "\n");

	}

	public void bookDtoPrint(BooksDTO dto) {

		System.out.println("도서코드\t도서명\t저자\t출판사\t구입연도\t구입가격\t대여가격");
		System.out.println("============================================================");

		System.out.print(dto.getB_code() + "\t");
		System.out.print(dto.getB_name() + "\t");
		System.out.print(dto.getB_auther() + "\t");
		System.out.print(dto.getB_comp() + "\t");
		System.out.print(dto.getB_year() + "\t");
		System.out.print(dto.getB_iprice() + "\t");
		System.out.print(dto.getB_rprice() + "\n");

	}

	public void usersDtoPrint(UsersDTO dto) {

		System.out.println("회원코드\t회원명\t전화번호\t주소");
		System.out.println("============================================================");

		System.out.print(dto.getU_code() + "\t");
		System.out.print(dto.getU_name() + "\t");
		System.out.print(dto.getU_tel() + "\t");
		System.out.print(dto.getU_addr() + "\n");

	}
	
	
	public String DateInput() {
		
		int intYear = 0;
		int intMonth = 0;
		int intDay = 0;
		while(true) {
			
			try {
				
				System.out.println("년/원/일을 차례대로 입력받음");
				System.out.println("-------------------------------");
				System.out.print("년(2010~2020) >> " );
				String strYear = scan.nextLine();
				
				intYear = Integer.valueOf(strYear);
				if(intYear < 2010 || intYear > 2020) {
					System.out.println("범위 안에서 올바르게 입력!");
					continue;
				}
				
				System.out.print("월 >> " );
				String strMonth = scan.nextLine();
				
				intMonth = Integer.valueOf(strMonth);
				
				if(intMonth < 1 || intMonth > 12) {
					System.out.println("범위 안에서 올바르게 입력!");
					continue;
				}
				
				System.out.print("일 >> " );
				String strDay = scan.nextLine();
				
			 intDay = Integer.valueOf(strDay);
				
				if(intDay < 1 || intDay > 31) {
					System.out.println("범위 안에서 올바르게 입력!");
					continue;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("날짜 올바르게 입력");
			}
			
			
			String strInDate = intYear +"-"+intMonth+"-"+intDay;
			
			return strInDate ;
		
		}
	}

}
