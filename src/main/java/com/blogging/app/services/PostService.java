package com.blogging.app.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.blogging.app.payloads.PostDto;
import com.blogging.app.payloads.PostResponseDto;

public interface PostService {

	PostDto createPost(Integer userId, Integer categoryId, PostDto postDto, MultipartFile imageFile) throws IOException;
	
	PostDto updatePost(PostDto postDto, Integer postId);
	
	PostResponseDto getAllPost(int pageNo, int pageSize, String sortBy,String sortDir);
	
	PostDto getPostById(Integer postId);
	
	void deletePost(Integer postId);
	
	PostResponseDto getPostByCategory(Integer categoryId, int pageNo, int pageSize, String sortBy,String sortDir);
	
	PostResponseDto getPostByUser(Integer userId,  int pageNo, int pageSize, String sortBy,String sortDir);
	
	PostResponseDto searchPost(String keyword,  int pageNo, int pageSize, String sortBy,String sortDir);
}
