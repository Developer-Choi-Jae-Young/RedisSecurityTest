package com.backend.Server.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String email;
	private String name;
	private String addr;
	private int age;
	private String password;
	private String authority;
	private String refreshtoken;

	public List<String> getRoleList(){
        if(this.authority.length()>0){
            return Arrays.asList(this.authority.split(","));
        }

        return new ArrayList<>();
    }
}
