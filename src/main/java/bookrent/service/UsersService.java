package bookrent.service;

import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import bookrent.config.DBConnection;
import bookrent.dao.UsersDao;
import bookrent.persistence.BooksDTO;
import bookrent.persistence.UsersDTO;

public class UsersService {

	Scanner scan = null;
	UsersDao usersDao;

	public UsersService() {
		// TODO Auto-generated constructor stub
		SqlSession sqlSession = DBConnection.getSqlSessionFactory().openSession(true);
		scan = new Scanner(System.in);
		this.usersDao = sqlSession.getMapper(UsersDao.class);

	}

	public void UsersMenu() {

		while (true) {
			System.out.println("=============================");
			System.out.println("회원 정보");
			System.out.println("============================");
			System.out.println("s:전체검색, snt:이름+전화번호로 검색, i:회원등록, u:회원수정, -q:종료");
			System.out.println("---------------------------------------------------------");
			System.out.print(">> ");
			String strC = scan.nextLine();
			if (strC.equalsIgnoreCase("-q"))
				break;

			else if (strC.equalsIgnoreCase("s")) {
				List<UsersDTO> usersList = usersDao.selectAll();

				this.usersListAllPrint(usersList);
				continue;
			}

			else if (strC.equalsIgnoreCase("snt")) {
				System.out.print("회원명 입력 >> ");
				String stru_name = scan.nextLine();

				System.out.print("전화번호 입력 >> ");
				String stru_tel = scan.nextLine();

				List<UsersDTO> usersList = usersDao.selectByNameATel(stru_name, stru_tel); // 파라미터 2개 에러

				if (usersList == null || usersList.size() < 0) {
					System.out.println("해당 회원명&번호의 정보 없음");
					continue;
				} else {

					this.usersListAllPrint(usersList);
					;
					continue;

				}

			}

			else if (strC.equalsIgnoreCase("i")) {
				String strCode = usersDao.getMaxCode();
				String codeval2 = strCode.substring(1, 6);

				System.out.println(codeval2);

				int intu_code = Integer.valueOf(codeval2);
				String codevalF = String.format("%05d", intu_code + 1);
				String stru_code = "S" + codevalF;

				System.out.println(stru_code + "의 입력 시작");
				System.out.print("회원명 입력 >> ");
				String stru_name = scan.nextLine();

				if (stru_name.trim().isEmpty()) {
					System.out.println("회원명은 반드시 입력");
					continue;
				}
				/*
				 * BooksDTO dto = usersDao.selectByName(stru_name);
				 * 
				 * if (dto != null) { this.bookDtoPrint(dto);
				 * System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
				 * System.out.println("이미 같은 도서가 존재"); continue; }
				 * 
				 */

				System.out.print("전화번호 입력 >> ");
				String stru_tel = scan.nextLine();

				System.out.print("주소 입력 >> ");
				String stru_addr = scan.nextLine();

				UsersDTO dto = UsersDTO.builder().u_addr(stru_addr).u_code(stru_code).u_name(stru_name).u_tel(stru_tel)
						.build();
				int ret = usersDao.insert(dto);

				if (ret > 0) {
					System.out.println("등록 성공");
				} else {
					System.out.println("등록 실패");
				}

				continue;

			}

			else if (strC.equalsIgnoreCase("u")) {
				
				try {
					
				

				System.out.print("수정할 회원코드 입력 >> ");
				String stru_code = scan.nextLine();

				UsersDTO dto = usersDao.selectByCode(stru_code);

				if (dto == null) {
					System.out.println("해당 회원 존재하지 않음");
					continue;
				}

				this.usersDtoPrint(dto);
				;

				System.out.println(stru_code + "의 수정 시작");
				System.out.print("회원명 입력 >> ");
				String stru_name = scan.nextLine();

				if (stru_name.trim().isEmpty()) {
					System.out.println("회원명은 반드시 입력");
					continue;
				}

				System.out.print("전화번호 입력 >> ");
				String stru_tel = scan.nextLine();

				System.out.print("주소 입력 >> ");
				String stru_addr = scan.nextLine();

				dto.setU_addr(stru_addr);
				dto.setU_name(stru_name);
				dto.setU_tel(stru_tel);

				int ret = usersDao.update(dto);

				if (ret > 0) {
					System.out.println("수정 성공");
				} else {
					System.out.println("수정 실패");
				}

				continue;

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
				
				
				
			}
		}

		
	}

	public void usersListAllPrint(List<UsersDTO> usersList) {

		System.out.println("회원코드\t회원명\t전화번호\t주소");
		System.out.println("============================================================");
		for (UsersDTO dto : usersList) {

			System.out.print(dto.getU_code() + "\t");
			System.out.print(dto.getU_name() + "\t");
			System.out.print(dto.getU_tel() + "\t");
			System.out.print(dto.getU_addr() + "\n");

		}

	}

	public void usersDtoPrint(UsersDTO dto) {

		System.out.println("회원코드\t회원명\t전화번호\t주소");
		System.out.println("============================================================");

		System.out.print(dto.getU_code() + "\t");
		System.out.print(dto.getU_name() + "\t");
		System.out.print(dto.getU_tel() + "\t");
		System.out.print(dto.getU_addr() + "\n");

	}

}
