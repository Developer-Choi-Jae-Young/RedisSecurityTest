package com.backend.Server.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private String Email;
	private String Name;
	private String Addr;
	private int Age;
	private String Password;
	private String Authority;
	
	
	public static com.backend.Server.Entity.User toEntity(User user_dto)
	{
		return com.backend.Server.Entity.User.builder().email(user_dto.getEmail()).name(user_dto.getName()).
				addr(user_dto.getAddr()).age(user_dto.getAge()).password(user_dto.getPassword()).
				authority(user_dto.getAuthority()).build();
	}
}
