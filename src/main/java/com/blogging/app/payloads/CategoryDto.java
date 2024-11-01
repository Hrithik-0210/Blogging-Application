package com.blogging.app.payloads;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

	private int id;
	
	@NotEmpty(message = "please enter the title....")
	private String categoryTitle;
	@NotEmpty(message = "please enter the description")
	private String categoryDescription;
}
