package com.blogging.app.payloads;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

	List<UserDto> users;
	private int pageNo;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
}
