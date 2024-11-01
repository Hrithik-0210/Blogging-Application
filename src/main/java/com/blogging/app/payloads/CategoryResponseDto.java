package com.blogging.app.payloads;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryResponseDto {
	List<CategoryDto> categories;
	private int pageNo;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
}
