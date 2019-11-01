package bookrent.service;

import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import bookrent.config.DBConnection;
import bookrent.dao.BooksDao;
import bookrent.persistence.BooksDTO;

public class BooksService {

	Scanner scan = null;
	BooksDao booksDao;

	public BooksService() {
		// TODO Auto-generated constructor stub

		SqlSession sqlSession = DBConnection.getSqlSessionFactory().openSession(true);
		scan = new Scanner(System.in);
		this.booksDao = sqlSession.getMapper(BooksDao.class);
	}

	public void BooksMenu() {

		while (true) {

			System.out.println("=============================");
			System.out.println("도서 정보");
			System.out.println("============================");
			System.out.println("s:전체검색, sna:제목+저자로 검색, i:도서등록, u:도서수정, -q:종료");
			System.out.println("---------------------------------------------------------");
			System.out.print(">> ");
			String strC = scan.nextLine();
			if (strC.equalsIgnoreCase("-q"))
				break;

			if (strC.equalsIgnoreCase("s")) {
				List<BooksDTO> booksList = booksDao.selectAll();

				this.booksListAllPrint(booksList);
				continue;
			}

			else if (strC.equalsIgnoreCase("sna")) {
				System.out.print("도서명 입력 >> ");
				String strb_name = scan.nextLine();
				System.out.print("저자 입력 >> ");
				String strb_auther = scan.nextLine();

				List<BooksDTO> booksList = booksDao.selectByNameAAuther(strb_name, strb_auther); // 파라미터 2개 에러

				if (booksList == null || booksList.size() < 0) {
					System.out.println("해당 도서명&저자의 도서 정보 없음");
					continue;
				} else {

					this.booksListAllPrint(booksList);
					continue;

				}
			}

			else	if (strC.equalsIgnoreCase("i")) {
				String strCode = booksDao.getMaxCode();
				String codeval2 = strCode.substring(2, 6);
				int intb_code = Integer.valueOf(codeval2);
				String codevalF = String.format("%05d", intb_code + 1);
				String strb_code = "BK" + codevalF;

				System.out.println(strb_code + "의 입력 시작");
				System.out.print("도서명 입력 >> ");
				String strb_name = scan.nextLine();

				if (strb_name.trim().isEmpty()) {
					System.out.println("도서명은 반드시 입력");
					continue;
				}

				BooksDTO dto = booksDao.selectByName(strb_name);

				if (dto != null) {
					this.bookDtoPrint(dto);
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println("이미 같은 도서가 존재");
					continue;
				}

				System.out.print("저자 입력 >> ");
				String strb_auther = scan.nextLine();

				if (strb_auther.trim().isEmpty()) {
					System.out.println("저자는 반드시 입력");
					continue;
				}

				System.out.print("출판사 입력 >> ");
				String strb_comp = scan.nextLine();

				int intb_year = 0;
				int intb_iprice = 0;
				int intb_rprice = 0;
				try {
					System.out.print("구입연도 입력 >> ");
					String strb_year = scan.nextLine();
					if (strb_year.trim().isEmpty()) {
						System.out.println("구입연도는 반드시 입력");
						continue;
					}
					intb_year = Integer.valueOf(strb_year);

					System.out.print("구입가격 입력 >> ");
					String strb_iprice = scan.nextLine();
					intb_iprice = Integer.valueOf(strb_iprice);

					System.out.print("대여가격 입력 >> ");
					String strb_rprice = scan.nextLine();
					if (strb_rprice.trim().isEmpty()) {
						System.out.println("대여가격은 반드시 입력");
						continue;
					}

					intb_rprice = Integer.valueOf(strb_rprice);

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("구입연도, 구입가격, 대여가격을 올바르게 입력하시오");
					continue;
				}

				dto = BooksDTO.builder().b_auther(strb_auther).b_code(strb_code).b_comp(strb_comp).b_iprice(intb_iprice)
						.b_name(strb_name).b_rprice(intb_rprice).b_year(intb_year).build();

				int ret = booksDao.insert(dto);

				if (ret > 0) {
					System.out.println("등록 성공");
				} else {
					System.out.println("등록 실패");
				}

				continue;

			}
			
			
			else if(strC.equalsIgnoreCase("u")) {
				try {
					
				
				System.out.print("수정할 도서코드 입력 >> ");
				String strb_code = scan.nextLine();
				
				BooksDTO dto = booksDao.selectByCode(strb_code);
				
				if(dto == null) {
					System.out.println("해당 도서 존재하지 않음");
					continue;
				}
				
				this.bookDtoPrint(dto);
				
				System.out.println(strb_code + "의 수정 시작");
				System.out.print("도서명 입력 >> ");
				String strb_name = scan.nextLine();

				if (strb_name.trim().isEmpty()) {
					System.out.println("도서명은 반드시 입력");
					continue;
				}

				/* dto = booksDao.selectByName(strb_name);

				if (dto != null) {
					this.bookDtoPrint(dto);
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println("이미 같은 도서가 존재");
					continue;
				}
				 		*/
				System.out.print("저자 입력 >> ");
				String strb_auther = scan.nextLine();

				if (strb_auther.trim().isEmpty()) {
					System.out.println("저자는 반드시 입력");
					continue;
				}

				System.out.print("출판사 입력 >> ");
				String strb_comp = scan.nextLine();

				int intb_year = 0;
				int intb_iprice = 0;
				int intb_rprice = 0;
				try {
					System.out.print("구입연도 입력 >> ");
					String strb_year = scan.nextLine();
					if (strb_year.trim().isEmpty()) {
						System.out.println("구입연도는 반드시 입력");
						continue;
					}
					intb_year = Integer.valueOf(strb_year);

					System.out.print("구입가격 입력 >> ");
					String strb_iprice = scan.nextLine();
					intb_iprice = Integer.valueOf(strb_iprice);

					System.out.print("대여가격 입력 >> ");
					String strb_rprice = scan.nextLine();
					if (strb_rprice.trim().isEmpty()) {
						System.out.println("대여가격은 반드시 입력");
						continue;
					}

					intb_rprice = Integer.valueOf(strb_rprice);

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("구입연도, 구입가격, 대여가격을 올바르게 입력하시오");
					continue;
				}
				
				dto.setB_auther(strb_auther);
				dto.setB_comp(strb_comp);
				dto.setB_iprice(intb_iprice);
				dto.setB_name(strb_name);
				dto.setB_rprice(intb_rprice);
				dto.setB_year(intb_year);
				
				int ret = booksDao.update(dto);
				if(ret > 0) {
					System.out.println("업뎃 완료");
				}else {
					System.out.println("업뎃 실패");
				}
				
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				
				continue;
				
			}

		}

	}

	public void booksListAllPrint(List<BooksDTO> booksList) {

		System.out.println("도서코드\t도서명\t저자\t출판사\t구입연도\t구입가격\t대여가격");
		System.out.println("============================================================");
		for (BooksDTO dto : booksList) {

			System.out.print(dto.getB_code() + "\t");
			System.out.print(dto.getB_name() + "\t");
			System.out.print(dto.getB_auther() + "\t");
			System.out.print(dto.getB_comp() + "\t");
			System.out.print(dto.getB_year() + "\t");
			System.out.print(dto.getB_iprice() + "\t");
			System.out.print(dto.getB_rprice() + "\n");

		}

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

}
