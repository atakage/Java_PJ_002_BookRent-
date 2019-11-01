package bookrent.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UsersDTO {
	
	private String u_code;//	varchar2(6 byte)
	private String u_name;//	nvarchar2(125 char)
	private String u_tel;//	nvarchar2(125 char)
	private String u_addr;//	nvarchar2(125 char)

}
