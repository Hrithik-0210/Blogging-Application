package com.blogging.app.controllers;




import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogging.app.payloads.ApiResponse;
import com.blogging.app.payloads.PostDto;
import com.blogging.app.payloads.PostResponseDto;
import com.blogging.app.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	@Autowired
	private PostService postService;

	//create post
	@PostMapping("/createPost/user/{userId}/category/{categoryId}")
	public ResponseEntity<PostDto> createPost(@PathVariable("userId")Integer userId, @PathVariable("categoryId") Integer categoryId,@RequestPart("post")  PostDto postDto, @RequestPart(name = "photo") MultipartFile imageFile) throws IOException{
		PostDto createdPost = this.postService.createPost(userId, categoryId, postDto, imageFile);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
	}
	
	//update post
	@PutMapping("/updatePost/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable("postId") Integer postId){
		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
	}
	
	//get all posts
	@GetMapping("/getPosts")
	public ResponseEntity<PostResponseDto> getPosts(@RequestParam(name = "pageNo")int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "sortBy") String sortBy, @RequestParam(name = "sortDir") String sortDir){
		PostResponseDto allPost =  this.postService.getAllPost(pageNo, pageSize, sortBy,sortDir);
		return new ResponseEntity<PostResponseDto>(allPost, HttpStatus.OK);
	}
	
	//get post by id
	@GetMapping("/getPost/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable("id") Integer postId){
		PostDto post =  this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(post, HttpStatus.OK);
	}
	
	//delete post by id
	@DeleteMapping("/deletePost/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Integer postId){
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post Deleted Succesfully ...", true), HttpStatus.OK);
	}
	
	//get post by category
	@GetMapping("/getPost/category/{id}")
	public ResponseEntity<PostResponseDto> getPostByCategory(@PathVariable("id") Integer categoryId,@RequestParam(name = "pageNo")int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "sortBy") String sortBy, @RequestParam(name = "sortDir") String sortDir){
		PostResponseDto postByCategory =  this.postService.getPostByCategory(categoryId, pageNo,pageSize, sortBy, sortDir);
	return new ResponseEntity<PostResponseDto>(postByCategory, HttpStatus.OK);
	}
	
	
	@GetMapping("/getPost/user/{id}")
	public ResponseEntity<PostResponseDto> getPost(@PathVariable("id") Integer userId,@RequestParam(name = "pageNo")int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "sortBy") String sortBy, @RequestParam(name = "sortDir") String sortDir){
		PostResponseDto postDtos =  this.postService.getPostByUser(userId,pageNo,pageSize,sortBy, sortDir);
		return new ResponseEntity<PostResponseDto>(postDtos,HttpStatus.OK);
	}
	
	@GetMapping("/getPost/keyword/{key}")
	public ResponseEntity<PostResponseDto> getPostByKeywords(@PathVariable("key") String keyword,@RequestParam(name = "pageNo")int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "sortBy") String sortBy, @RequestParam(name = "sortDir") String sortDir){
		PostResponseDto posts =  this.postService.searchPost(keyword,pageNo,pageSize,sortBy, sortDir);
		return new ResponseEntity<PostResponseDto>(posts,HttpStatus.OK);
	}
	
	
}
