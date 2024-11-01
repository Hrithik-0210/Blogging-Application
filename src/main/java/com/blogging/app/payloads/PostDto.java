package com.blogging.app.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

	private int id;
	private String title;
	private String content;
	private String imageName;
	private byte[] imageData;
	private Date addedDate;
	private CategoryDto category;
	private UserDto user;
	private Set<CommentDto> comment;
}
