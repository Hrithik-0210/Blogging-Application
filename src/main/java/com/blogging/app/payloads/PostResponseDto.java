package com.blogging.app.payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResponseDto {

	private List<PostDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
	 
}
